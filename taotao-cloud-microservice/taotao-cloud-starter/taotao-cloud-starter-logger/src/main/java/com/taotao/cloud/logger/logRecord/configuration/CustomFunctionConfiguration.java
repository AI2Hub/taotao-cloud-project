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
package com.taotao.cloud.logger.logRecord.configuration;

import com.taotao.cloud.logger.logRecord.function.CustomFunctionRegistrar;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 自定义函数配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-26 14:49:11
 */
@AutoConfiguration
public class CustomFunctionConfiguration {

	/**
	 * 注册商
	 *
	 * @return {@link CustomFunctionRegistrar }
	 * @since 2022-04-26 14:49:11
	 */
	@Bean
    public CustomFunctionRegistrar registrar() {
        return new CustomFunctionRegistrar();
    }
}
