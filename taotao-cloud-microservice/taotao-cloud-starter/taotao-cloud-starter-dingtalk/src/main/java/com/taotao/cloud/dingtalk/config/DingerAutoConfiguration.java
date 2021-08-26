/*
 * Copyright ©2015-2021 Jaemon. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.dingtalk.config;

import com.taotao.cloud.dingtalk.core.DingerRobot;
import com.taotao.cloud.dingtalk.core.entity.DingerProperties;
import com.taotao.cloud.dingtalk.core.session.DingerSessionFactory;
import com.taotao.cloud.dingtalk.core.spring.DingerSessionFactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * DingerAutoConfiguration
 *
 * @author Jaemon
 * @since 1.2
 */
@Configuration
@EnableConfigurationProperties(DingerProperties.class)
@AutoConfigureAfter(DingerConfiguration.class)
public class DingerAutoConfiguration implements InitializingBean {

	private final DingerProperties properties;
	private final DingerRobot dingerRobot;
	private final ResourceLoader resourceLoader;

	public DingerAutoConfiguration(
		DingerProperties dingerProperties,
		DingerRobot dingerRobot,
		ResourceLoader resourceLoader
	) {
		this.properties = dingerProperties;
		this.dingerRobot = dingerRobot;
		this.resourceLoader = resourceLoader;
	}

	@Bean
	@ConditionalOnMissingBean
	public DingerSessionFactory dingerSessionFactory() throws Exception {
		DingerSessionFactoryBean factory = new DingerSessionFactoryBean();

		factory.setConfiguration(
			com.taotao.cloud.dingtalk.core.session.Configuration.of(properties, dingerRobot)
		);

		return factory.getObject();
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		checkConfigFileExists();
	}

	private void checkConfigFileExists() {

		if (
			StringUtils.hasText(this.properties.getDingerLocations())
		) {

			Resource resource = this.resourceLoader.getResource(
				this.properties.getDingerLocations());

			Assert.state(resource.exists(), "Cannot find config location: " + resource
				+ " (please add config file or check your Dinger configuration)");
		}
	}
}
