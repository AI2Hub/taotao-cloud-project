/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.quartz.configuration;

import com.taotao.cloud.common.utils.context.ContextUtils;
import com.taotao.cloud.quartz.event.DefaultQuartzEventListener;
import com.taotao.cloud.quartz.event.RedisQuartzEventListener;
import com.taotao.cloud.quartz.listener.QuartzListenerRegister;
import com.taotao.cloud.quartz.utils.QuartzManager;
import com.taotao.cloud.redis.repository.RedisRepository;
import org.jetbrains.annotations.NotNull;
import org.quartz.Scheduler;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;

/**
 * 定时任务配置
 *
 * @author shuigedeng
 * @version 2022.03
 * @since 2022-03-25 15:01:01
 */
@AutoConfiguration
public class QuartzJobConfiguration implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ContextUtils.setApplicationContext((ConfigurableApplicationContext) applicationContext);
	}

	/**
	 * 解决Job中注入Spring Bean为null的问题
	 */
	@Component("quartzJobFactory")
	public static class QuartzJobFactory extends SpringBeanJobFactory {

		private final AutowireCapableBeanFactory capableBeanFactory;

		public QuartzJobFactory(AutowireCapableBeanFactory capableBeanFactory) {
			this.capableBeanFactory = capableBeanFactory;
		}

		@NotNull
		@Override
		protected Object createJobInstance(@NotNull TriggerFiredBundle bundle) throws Exception {
			//调用父类的方法
			Object jobInstance = super.createJobInstance(bundle);
			capableBeanFactory.autowireBean(jobInstance);
			return jobInstance;
		}
	}

	/**
	 * 注入scheduler到spring
	 */
	@Bean(name = "scheduler")
	public Scheduler scheduler(SchedulerFactoryBean schedulerFactoryBean) throws Exception {
		Scheduler scheduler = schedulerFactoryBean.getScheduler();
		// scheduler.getListenerManager().addJobListener(new QuartzJobListener());

		// 添加JobListener, 精确匹配JobKey
		// KeyMatcher<JobKey> keyMatcher = KeyMatcher.keyEquals(JobKey.jobKey("helloJob", "group1"));
		// scheduler.getListenerManager().addJobListener(new HelloJobListener(), keyMatcher);

		scheduler.start();
		return scheduler;
	}

	@Bean
	public QuartzManager quartzManage() {
		return new QuartzManager();
	}

	@Bean
	public QuartzListenerRegister quartzListenerRegister(){
		return new QuartzListenerRegister();
	}

	@Bean
	@ConditionalOnBean(RedisRepository.class)
	public RedisQuartzEventListener redisQuartzLogEventListener(){
		return new RedisQuartzEventListener();
	}
	@Bean
	@ConditionalOnMissingBean
	public DefaultQuartzEventListener defaultQuartzLogEventListener(){
		return new DefaultQuartzEventListener();
	}
}
