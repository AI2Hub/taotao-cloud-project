/*
 * Copyright (c) 2020-2030 ZHENGGENGWEI(码匠君)<herodotus@aliyun.com>
 *
 * Dante Cloud licensed under the Apache License, Version 2.0 (the "License");
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
 * Dante Cloud 采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改 Dante Cloud 源码头部的版权声明。
 * 3.请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://gitee.com/dromara/dante-cloud
 * 5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/dromara/dante-cloud
 * 6.若您的项目无法满足以上几点，可申请商业授权
 */

package com.taotao.cloud.auth.biz.dante.uaa.configuration;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import com.taotao.cloud.auth.biz.dante.authentication.customizer.HerodotusJwtTokenCustomizer;
import com.taotao.cloud.auth.biz.dante.authentication.customizer.HerodotusOpaqueTokenCustomizer;
import com.taotao.cloud.auth.biz.dante.authentication.form.OAuth2FormLoginUrlConfigurer;
import com.taotao.cloud.auth.biz.dante.authentication.oidc.HerodotusOidcUserInfoMapper;
import com.taotao.cloud.auth.biz.dante.authentication.properties.OAuth2AuthenticationProperties;
import com.taotao.cloud.auth.biz.dante.authentication.provider.*;
import com.taotao.cloud.auth.biz.dante.authentication.response.DefaultOAuth2AuthenticationEventPublisher;
import com.taotao.cloud.auth.biz.dante.authentication.response.HerodotusAuthenticationFailureHandler;
import com.taotao.cloud.auth.biz.dante.authentication.response.HerodotusAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.dante.authentication.response.OAuth2DeviceVerificationAuthenticationSuccessHandler;
import com.taotao.cloud.auth.biz.dante.authentication.utils.OAuth2ConfigurerUtils;
import com.taotao.cloud.auth.biz.dante.authorization.customizer.HerodotusTokenStrategyConfigurer;
import com.taotao.cloud.auth.biz.dante.authorization.properties.OAuth2AuthorizationProperties;
import com.taotao.cloud.auth.biz.dante.core.definition.service.ClientDetailsService;
import com.taotao.cloud.auth.biz.dante.core.enums.Certificate;
import com.taotao.cloud.auth.biz.dante.uaa.other.BaseConstants;
import com.taotao.cloud.auth.biz.dante.uaa.other.EndpointProperties;
import com.taotao.cloud.auth.biz.dante.uaa.other.HttpCryptoProcessor;
import com.taotao.cloud.captcha.support.core.processor.CaptchaRendererFactory;
import com.taotao.cloud.common.utils.io.ResourceUtils;
import jakarta.annotation.PostConstruct;
import org.apache.commons.lang3.ArrayUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.Resource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenGenerator;
import org.springframework.security.oauth2.server.authorization.web.authentication.*;
import org.springframework.security.rsa.crypto.KeyStoreKeyFactory;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.AuthenticationConverter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.Arrays;
import java.util.UUID;

/**
 * <p>Description: 认证服务器配置 </p>
 * <p>
 * 1. 权限核心处理 {@link org.springframework.security.web.access.intercept.FilterSecurityInterceptor}
 * 2. 默认的权限判断 {@link org.springframework.security.access.vote.AffirmativeBased}
 * 3. 模式决策 {@link org.springframework.security.authentication.ProviderManager}
 *
 * @author : gengwei.zheng
 * @date : 2022/2/12 20:57
 */
@Configuration(proxyBeanMethods = false)
public class AuthorizationServerConfiguration {

	private static final Logger log = LoggerFactory.getLogger(AuthorizationServerConfiguration.class);


	@PostConstruct
	public void postConstruct() {
		log.debug("[Herodotus] |- SDK [OAuth2 Authorization Server] Auto Configure.");
	}

	@Bean
	@Order(Ordered.HIGHEST_PRECEDENCE)
	public SecurityFilterChain authorizationServerSecurityFilterChain(
		HttpSecurity httpSecurity,
		ClientDetailsService clientDetailsService,
		UserDetailsService userDetailsService,
		PasswordEncoder passwordEncoder,
		HttpCryptoProcessor httpCryptoProcessor,
		CaptchaRendererFactory captchaRendererFactory,
		OAuth2AuthenticationProperties authenticationProperties,
		HerodotusTokenStrategyConfigurer herodotusTokenStrategyConfigurer,
		OAuth2FormLoginUrlConfigurer formLoginUrlConfigurer
	) throws Exception {

		log.debug("[Herodotus] |- Core [Authorization Server Security Filter Chain] Auto Configure.");

		OAuth2AuthorizationServerConfigurer authorizationServerConfigurer = new OAuth2AuthorizationServerConfigurer();
		httpSecurity.apply(authorizationServerConfigurer);

		HerodotusAuthenticationFailureHandler failureHandler = new HerodotusAuthenticationFailureHandler();
		authorizationServerConfigurer.clientAuthentication(endpoint ->
			endpoint.errorResponseHandler(failureHandler)
		);

		authorizationServerConfigurer.authorizationEndpoint(endpoint -> {
			endpoint.errorResponseHandler(failureHandler);
			endpoint.consentPage(BaseConstants.CUSTOM_AUTHORIZATION_CONSENT_URI);
		});

		authorizationServerConfigurer.deviceAuthorizationEndpoint(endpoint -> {
			endpoint.errorResponseHandler(failureHandler);
			endpoint.verificationUri(BaseConstants.CUSTOM_DEVICE_ACTIVATION_URI);
		});

		authorizationServerConfigurer.deviceVerificationEndpoint(endpoint -> {
			endpoint.errorResponseHandler(failureHandler);
			endpoint.consentPage(BaseConstants.CUSTOM_AUTHORIZATION_CONSENT_URI);
			endpoint.deviceVerificationResponseHandler(new OAuth2DeviceVerificationAuthenticationSuccessHandler());
		});

		authorizationServerConfigurer.tokenEndpoint(endpoint -> {
			AuthenticationConverter authenticationConverter = new DelegatingAuthenticationConverter(
				Arrays.asList(
					new OAuth2AuthorizationCodeAuthenticationConverter(),
					new OAuth2RefreshTokenAuthenticationConverter(),
					new OAuth2ClientCredentialsAuthenticationConverter(),
					new OAuth2DeviceCodeAuthenticationConverter(),
					new OAuth2ResourceOwnerPasswordAuthenticationConverter(httpCryptoProcessor),
					new OAuth2SocialCredentialsAuthenticationConverter(httpCryptoProcessor)));
			endpoint.accessTokenRequestConverter(authenticationConverter);
			endpoint.errorResponseHandler(failureHandler);
			endpoint.accessTokenResponseHandler(new HerodotusAuthenticationSuccessHandler(httpCryptoProcessor));
		});

		authorizationServerConfigurer.tokenIntrospectionEndpoint(endpoint ->
			endpoint.errorResponseHandler(failureHandler));

		authorizationServerConfigurer.tokenRevocationEndpoint(endpoint -> endpoint.errorResponseHandler(failureHandler));

		authorizationServerConfigurer.oidc(oidc -> {
			oidc.clientRegistrationEndpoint(Customizer.withDefaults());
			oidc.userInfoEndpoint(userInfo -> userInfo
				.userInfoMapper(new HerodotusOidcUserInfoMapper()));
		});

		SessionRegistry sessionRegistry = OAuth2ConfigurerUtils.getOptionalBean(httpSecurity, SessionRegistry.class);

		// 使用自定义的 AuthenticationProvider 替换已有 AuthenticationProvider
		authorizationServerConfigurer.withObjectPostProcessor(new ObjectPostProcessor<AuthenticationProvider>() {
			@Override
			public <O extends AuthenticationProvider> O postProcess(O object) {
				OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);

				if (org.springframework.security.oauth2.server.authorization.authentication.OAuth2AuthorizationCodeAuthenticationProvider.class.isAssignableFrom(object.getClass())) {
					OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
					OAuth2AuthorizationCodeAuthenticationProvider provider = new OAuth2AuthorizationCodeAuthenticationProvider(authorizationService, tokenGenerator);
					provider.setSessionRegistry(sessionRegistry);
					log.debug("[Herodotus] |- Custom OAuth2AuthorizationCodeAuthenticationProvider is in effect!");
					return (O) provider;
				}

				if (org.springframework.security.oauth2.server.authorization.authentication.OAuth2ClientCredentialsAuthenticationProvider.class.isAssignableFrom(object.getClass())) {
					OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);
					OAuth2ClientCredentialsAuthenticationProvider provider = new OAuth2ClientCredentialsAuthenticationProvider(authorizationService, tokenGenerator, clientDetailsService);
					log.debug("[Herodotus] |- Custom OAuth2ClientCredentialsAuthenticationProvider is in effect!");
					return (O) provider;
				}
				return object;
			}
		});

		RequestMatcher endpointsMatcher = authorizationServerConfigurer.getEndpointsMatcher();
		// 仅拦截 OAuth2 Authorization Server 的相关 endpoint
		httpSecurity.securityMatcher(endpointsMatcher)
			// 开启请求认证
			.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
			// 禁用对 OAuth2 Authorization Server 相关 endpoint 的 CSRF 防御
			.csrf(csrf -> csrf.ignoringRequestMatchers(endpointsMatcher))
			.oauth2ResourceServer(herodotusTokenStrategyConfigurer::from);

		// 这里增加 DefaultAuthenticationEventPublisher 配置，是为了解决 ProviderManager 在初次使用时，外部定义DefaultAuthenticationEventPublisher 不会注入问题
		// 外部注入DefaultAuthenticationEventPublisher是标准配置方法，两处都保留是为了保险，还需要深入研究才能决定去掉哪个。
		AuthenticationManagerBuilder authenticationManagerBuilder = httpSecurity.getSharedObject(AuthenticationManagerBuilder.class);
		ApplicationContext applicationContext = httpSecurity.getSharedObject(ApplicationContext.class);
		authenticationManagerBuilder.authenticationEventPublisher(new DefaultOAuth2AuthenticationEventPublisher(applicationContext));

		// build() 方法会让以上所有的配置生效
		SecurityFilterChain securityFilterChain = httpSecurity
			.formLogin(formLoginUrlConfigurer::from)
			.sessionManagement(Customizer.withDefaults())
			.addFilterBefore(new MultiTenantFilter(), AuthorizationFilter.class)
			.build();

		// 增加新的、自定义 OAuth2 Granter
		OAuth2AuthorizationService authorizationService = OAuth2ConfigurerUtils.getAuthorizationService(httpSecurity);
		OAuth2TokenGenerator<? extends OAuth2Token> tokenGenerator = OAuth2ConfigurerUtils.getTokenGenerator(httpSecurity);

		OAuth2ResourceOwnerPasswordAuthenticationProvider resourceOwnerPasswordAuthenticationProvider =
			new OAuth2ResourceOwnerPasswordAuthenticationProvider(authorizationService, tokenGenerator, userDetailsService, authenticationProperties);
		resourceOwnerPasswordAuthenticationProvider.setPasswordEncoder(passwordEncoder);
		resourceOwnerPasswordAuthenticationProvider.setSessionRegistry(sessionRegistry);
		httpSecurity.authenticationProvider(resourceOwnerPasswordAuthenticationProvider);

		OAuth2SocialCredentialsAuthenticationProvider socialCredentialsAuthenticationProvider =
			new OAuth2SocialCredentialsAuthenticationProvider(authorizationService, tokenGenerator, userDetailsService, authenticationProperties);
		socialCredentialsAuthenticationProvider.setSessionRegistry(sessionRegistry);
		httpSecurity.authenticationProvider(socialCredentialsAuthenticationProvider);

		return securityFilterChain;
	}

	@Bean
	public JWKSource<SecurityContext> jwkSource(OAuth2AuthorizationProperties authorizationProperties) throws NoSuchAlgorithmException {

		OAuth2AuthorizationProperties.Jwk jwk = authorizationProperties.getJwk();

		KeyPair keyPair = null;
		if (jwk.getCertificate() == Certificate.CUSTOM) {
			try {
				Resource[] resource = ResourceUtils.getResources(jwk.getJksKeyStore());
				if (ArrayUtils.isNotEmpty(resource)) {
					KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(resource[0], jwk.getJksStorePassword().toCharArray());
					keyPair = keyStoreKeyFactory.getKeyPair(jwk.getJksKeyAlias(), jwk.getJksKeyPassword().toCharArray());
				}
			} catch (IOException e) {
				log.error("[Herodotus] |- Read custom certificate under resource folder error!", e);
			}

		} else {
			KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
			keyPairGenerator.initialize(2048);
			keyPair = keyPairGenerator.generateKeyPair();
		}

		RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
		RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
		RSAKey rsaKey = new RSAKey.Builder(publicKey)
			.privateKey(privateKey)
			.keyID(UUID.randomUUID().toString())
			.build();
		JWKSet jwkSet = new JWKSet(rsaKey);
		return (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
	}

	/**
	 * jwt 解码
	 */
	@Bean
	public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
		return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
	}

	@Bean
	public OAuth2TokenCustomizer<JwtEncodingContext> jwtTokenCustomizer() {
		HerodotusJwtTokenCustomizer herodotusJwtTokenCustomizer = new HerodotusJwtTokenCustomizer();
		log.trace("[Herodotus] |- Bean [OAuth2 Jwt Token Customizer] Auto Configure.");
		return herodotusJwtTokenCustomizer;
	}

	@Bean
	public OAuth2TokenCustomizer<OAuth2TokenClaimsContext> opaqueTokenCustomizer() {
		HerodotusOpaqueTokenCustomizer herodotusOpaqueTokenCustomizer = new HerodotusOpaqueTokenCustomizer();
		log.trace("[Herodotus] |- Bean [OAuth2 Opaque Token Customizer] Auto Configure.");
		return herodotusOpaqueTokenCustomizer;
	}

	@Bean
	public AuthorizationServerSettings authorizationServerSettings(EndpointProperties endpointProperties) {
		return AuthorizationServerSettings.builder()
			.issuer(endpointProperties.getIssuerUri())
			.authorizationEndpoint(endpointProperties.getAuthorizationEndpoint())
			.deviceAuthorizationEndpoint(endpointProperties.getDeviceAuthorizationEndpoint())
			.deviceVerificationEndpoint(endpointProperties.getDeviceVerificationEndpoint())
			.tokenEndpoint(endpointProperties.getAccessTokenEndpoint())
			.tokenIntrospectionEndpoint(endpointProperties.getTokenIntrospectionEndpoint())
			.tokenRevocationEndpoint(endpointProperties.getTokenRevocationEndpoint())
			.jwkSetEndpoint(endpointProperties.getJwkSetEndpoint())
			.oidcLogoutEndpoint(endpointProperties.getOidcLogoutEndpoint())
			.oidcUserInfoEndpoint(endpointProperties.getOidcUserInfoEndpoint())
			.oidcClientRegistrationEndpoint(endpointProperties.getOidcClientRegistrationEndpoint())
			.build();
	}
}
