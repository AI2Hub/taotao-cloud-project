package com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.common.aop;

import cn.bootx.platform.common.core.util.ValidationUtil;
import cn.bootx.platform.daxpay.exception.pay.PayFailureException;
import cn.bootx.platform.daxpay.param.PaymentCommonParam;
import com.taotao.cloud.payment.biz.daxpay.single.service.annotation.PaymentApi;
import com.taotao.cloud.payment.biz.daxpay.single.service.core.payment.common.service.PaymentSignService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

/**
 * 支付签名切面, 用于对支付参数进行校验和签名
 * 执行顺序: 过滤器 -> 拦截器 -> 切面 -> 方法
 * @author xxm
 * @since 2023/12/24
 */
@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentVerifySignAop {
    private final PaymentSignService paymentSignService;

    @Before("@annotation(paymentApi)")
    public void beforeMethod(JoinPoint joinPoint, PaymentApi paymentApi) {
        Object[] args = joinPoint.getArgs();
        if (args.length == 0){
            throw new PayFailureException("支付方法至少有一个参数，并且需要签名支付参数需要放在第一位");
        }
        Object param = args[0];
        if (param instanceof PaymentCommonParam){
            // 参数校验
            ValidationUtil.validateParam(param);
            // 验签
            paymentSignService.verifySign((PaymentCommonParam) param);
        } else {
            throw new PayFailureException("支付参数需要继承PayCommonParam");
        }
    }

}
