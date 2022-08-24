package com.taotao.cloud.retry.aop;

import com.taotao.cloud.retry.annotation.GuavaRetrying;
import com.taotao.cloud.retry.listener.RetryLogListener;
import com.taotao.cloud.retry.strategy.SpinBlockStrategy;
import io.github.itning.retry.Attempt;
import io.github.itning.retry.Retryer;
import io.github.itning.retry.RetryerBuilder;
import io.github.itning.retry.listener.RetryListener;
import io.github.itning.retry.strategy.stop.StopStrategies;
import io.github.itning.retry.strategy.wait.WaitStrategies;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

@Aspect
@Service
public class GuavaRetryingAspect {

	@Around(value = "@annotation(com.taotao.cloud.retry.annotation.GuavaRetrying)")
	public Object monitorAround(ProceedingJoinPoint pjp) throws Throwable {
		Method method;
		if (pjp.getSignature() instanceof MethodSignature signature) {
			method = signature.getMethod();
		} else {
			return null;
		}

		GuavaRetrying annotation = method.getDeclaredAnnotation(GuavaRetrying.class);
		//重试时间，重试次数
		if (annotation.duration() <= 0 && annotation.attemptNumber() <= 1) {
			return pjp.proceed();
		}

		RetryerBuilder<Object> builder = RetryerBuilder.newBuilder();

		//重试次数
		if (annotation.attemptNumber() > 0) {
			builder.withStopStrategy(StopStrategies.stopAfterAttempt(annotation.attemptNumber()));
		}
		//退出策略
		if (annotation.duration() > 0) {
			builder.withStopStrategy(StopStrategies.stopAfterDelay(annotation.duration(), TimeUnit.MILLISECONDS));
		}

		//重试间间隔等待策略
		//等待策略：每次请求间隔1s
		//fixedWait 固定X秒后重试
		//noWait不等时间直接重试
		//incrementingWait 第一个参数为第一次重试时间，后面会通过设置间隔递增秒数重试
		//randomWait 随机等待设置范围内的时间重试
		if (annotation.waitStrategySleepTime() > 0) {
			builder.withWaitStrategy(WaitStrategies.fixedWait(annotation.waitStrategySleepTime(), TimeUnit.SECONDS));
		}

		//默认的阻塞策略：线程睡眠
		//.withBlockStrategy(BlockStrategies.threadSleepStrategy())
		//自定义阻塞策略：自旋锁
		builder.withBlockStrategy(new SpinBlockStrategy());

		//自定义重试监听器
		builder.withRetryListener(new RetryLogListener());

		//停止重试的策略
		if (annotation.exceptionClass().length > 0) {
			for (Class<? extends Throwable> retryThrowable : annotation.exceptionClass()) {
				if (retryThrowable != null && Throwable.class.isAssignableFrom(retryThrowable)) {
					builder.retryIfExceptionOfType(retryThrowable);
				}
			}
		}

		return builder
			.build()
			.call(() -> {
				try {
					return pjp.proceed();
				} catch (Throwable throwable) {
					throw new Exception(throwable);
				}
			});
	}


	public void guavaRetry(){
		Retryer<Boolean> retryer = RetryerBuilder.<Boolean>newBuilder()
			// 异常重试
			.retryIfExceptionOfType(IOException.class)
			// 根据结果重试
			.retryIfResult(res-> !res)
			// 设置等待间隔时间
			.withWaitStrategy(WaitStrategies.exponentialWait(100, 5, TimeUnit.MINUTES))
			// 设置最大重试次数
			.withStopStrategy(StopStrategies.stopAfterAttempt(3))
			.withRetryListener(new RetryListener() {
				@Override
				public <V> void onRetry(Attempt<V> attempt) {
					System.out.println(attempt.getAttemptNumber());
				}
			})
			.build();

		try {
			retryer.call(() -> {
				System.out.println("sdlf");
				return false;
			});
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
