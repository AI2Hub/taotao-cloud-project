/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
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
 *
 * Eurynome Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Eurynome Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/herodotus/eurynome-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.herodotus.eurynome.authentication.configuration;

import com.taotao.cloud.auth.biz.herodotus.authorization.authorization.OAuth2FormLoginConfigurer;
import com.taotao.cloud.auth.biz.herodotus.authorization.properties.OAuth2UiProperties;
import com.taotao.cloud.auth.biz.herodotus.converter.HerodotusJwtAuthenticationConverter;
import com.taotao.cloud.auth.biz.herodotus.core.definition.strategy.StrategyAuthorityDetailsService;
import com.taotao.cloud.auth.biz.herodotus.core.processor.HerodotusSecurityConfigureHandler;
import com.taotao.cloud.auth.biz.herodotus.core.response.HerodotusAccessDeniedHandler;
import com.taotao.cloud.auth.biz.herodotus.core.response.HerodotusAuthenticationEntryPoint;
import com.taotao.cloud.auth.biz.herodotus.eurynome.authentication.service.HerodotusAuthorityDetailsService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.autoconfigure.security.servlet.EndpointRequest;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.annotation.PostConstruct;

/**
 * <p>Description: 默认安全配置 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/2/12 20:53
 */
@EnableWebSecurity
public class DefaultSecurityConfiguration {

    private static final Logger log = LoggerFactory.getLogger(DefaultSecurityConfiguration.class);

    private final HerodotusSecurityConfigureHandler herodotusSecurityConfigureHandler;
    private final JwtDecoder jwtDecoder;
    private final OAuth2UiProperties uiProperties;
    private final CaptchaRendererFactory captchaRendererFactory;

    @Autowired
    public DefaultSecurityConfiguration(HerodotusSecurityConfigureHandler herodotusSecurityConfigureHandler, JwtDecoder jwtDecoder, OAuth2UiProperties uiProperties, CaptchaRendererFactory captchaRendererFactory) {
        this.herodotusSecurityConfigureHandler = herodotusSecurityConfigureHandler;
        this.jwtDecoder = jwtDecoder;
        this.uiProperties = uiProperties;
        this.captchaRendererFactory = captchaRendererFactory;
    }

    @PostConstruct
    public void postConstruct() {
        log.debug("[Herodotus] |- SDK [OAuth2 Default Security] Auto Configure.");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity httpSecurity, UserDetailsService userDetailsService) throws Exception {

        // 禁用CSRF 开启跨域
        httpSecurity.csrf().disable().cors();

        // @formatter:off
        httpSecurity.authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers(herodotusSecurityConfigureHandler.getPermitAllArray()).permitAll()
                        .antMatchers(herodotusSecurityConfigureHandler.getStaticResourceArray()).permitAll()
                        .requestMatchers(EndpointRequest.toAnyEndpoint()).permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> {
                            form.loginPage(uiProperties.getLoginPageUrl()).permitAll()
                                    .usernameParameter(uiProperties.getUsernameParameter())
                                    .passwordParameter(uiProperties.getPasswordParameter());
                            if (StringUtils.isNotBlank(uiProperties.getFailureForwardUrl())) {
                                form.failureForwardUrl(uiProperties.getFailureForwardUrl());
                            }
                            if (StringUtils.isNotBlank(uiProperties.getSuccessForwardUrl())) {
                                form.successForwardUrl(uiProperties.getSuccessForwardUrl());
                            }
                        }
                )
                .exceptionHandling()
                .authenticationEntryPoint(new HerodotusAuthenticationEntryPoint())
                .accessDeniedHandler(new HerodotusAccessDeniedHandler())
                .and()
                .oauth2ResourceServer(configurer -> configurer
                        .jwt(jwt -> jwt
                                .decoder(jwtDecoder).jwtAuthenticationConverter(new HerodotusJwtAuthenticationConverter()))
                        .accessDeniedHandler(new HerodotusAccessDeniedHandler())
                        .authenticationEntryPoint(new HerodotusAuthenticationEntryPoint()))
                .apply(new OAuth2FormLoginConfigurer(userDetailsService, uiProperties, captchaRendererFactory));
        // @formatter:on
        return httpSecurity.build();
    }

    @Bean
    @ConditionalOnMissingBean
    UserDetailsService userDetailsService(SysUserService sysUserService) {
        HerodotusUserDetailsService herodotusUserDetailsService = new HerodotusUserDetailsService(sysUserService);
        log.debug("[Herodotus] |- Bean [Herodotus User Details Service] Auto Configure.");
        return herodotusUserDetailsService;
    }

    @Bean
    @ConditionalOnMissingBean
    StrategyAuthorityDetailsService strategyAuthorityDetailsService(SysAuthorityService sysAuthorityService) {
        HerodotusAuthorityDetailsService herodotusAuthorityDetailsService = new HerodotusAuthorityDetailsService(sysAuthorityService);
        log.trace("[Herodotus] |- Bean [Herodotus Authority Details Service] Auto Configure.");
        return herodotusAuthorityDetailsService;
    }
}
