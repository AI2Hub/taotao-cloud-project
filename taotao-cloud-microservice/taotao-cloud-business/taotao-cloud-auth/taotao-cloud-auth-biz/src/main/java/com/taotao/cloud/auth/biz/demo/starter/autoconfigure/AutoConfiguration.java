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

package com.taotao.cloud.auth.biz.demo.starter.autoconfigure;

import cn.herodotus.dante.authentication.processor.HerodotusAccountStatusChangeService;
import cn.herodotus.engine.oauth2.compliance.definition.AccountStatusChangeService;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Description: OAuth Starter 自动注入配置
 *
 * @author : gengwei.zheng
 * @date : 2022/2/17 13:43
 */
@Configuration(proxyBeanMethods = false)
public class AutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(AutoConfiguration.class);

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- Starter [Herodotus OAuth Starter] Auto Configure.");
    }

    @Bean
    public AccountStatusChangeService accountStatusChangeService() {
        HerodotusAccountStatusChangeService herodotusAccountStatusChangeService =
                new HerodotusAccountStatusChangeService();
        log.trace("[Herodotus] |- Bean [Account Status Change Service] Auto Configure.");
        return herodotusAccountStatusChangeService;
    }
}
