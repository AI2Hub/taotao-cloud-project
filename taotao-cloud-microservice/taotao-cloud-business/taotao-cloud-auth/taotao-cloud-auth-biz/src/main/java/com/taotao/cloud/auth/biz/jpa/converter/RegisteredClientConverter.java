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

package com.taotao.cloud.auth.biz.jpa.converter;

import com.taotao.cloud.auth.biz.jpa.entity.RegisteredClientDetails;
import com.taotao.cloud.security.springsecurity.core.utils.OAuth2AuthorizationUtils;
import java.util.Set;
import org.dromara.hutool.core.date.DateUtil;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.util.StringUtils;

/**
 * <p>Description: 转换为 RegisteredClient 转换器定义</p>
 *
 *
 * @date : 2023/5/21 20:36
 */
public interface RegisteredClientConverter<S extends RegisteredClientDetails> extends Converter<S, RegisteredClient> {

    Set<String> getScopes(S details);

    ClientSettings getClientSettings(S details);

    TokenSettings getTokenSettings(S details);

    @Override
    default RegisteredClient convert(S details) {
        Set<String> clientScopes = getScopes(details);
        ClientSettings clientSettings = getClientSettings(details);

		//客服端设置 设置用户需要确认授权
		//ClientSettings build = ClientSettings.builder().requireAuthorizationConsent(false).build();

		TokenSettings tokenSettings = getTokenSettings(details);

        Set<String> clientAuthenticationMethods =
                StringUtils.commaDelimitedListToSet(details.getClientAuthenticationMethods());
        Set<String> authorizationGrantTypes = StringUtils.commaDelimitedListToSet(details.getAuthorizationGrantTypes());
        Set<String> redirectUris = StringUtils.commaDelimitedListToSet(details.getRedirectUris());
        Set<String> postLogoutRedirectUris = StringUtils.commaDelimitedListToSet(details.getPostLogoutRedirectUris());

        return RegisteredClient.withId(details.getId())
                .clientId(details.getClientId())
                .clientIdIssuedAt(DateUtil.toInstant(details.getClientIdIssuedAt()))
                .clientSecret(details.getClientSecret())
                .clientSecretExpiresAt(DateUtil.toInstant(details.getClientSecretExpiresAt()))
                .clientName(details.getId())
                .clientAuthenticationMethods(authenticationMethods ->
                        clientAuthenticationMethods.forEach(authenticationMethod -> authenticationMethods.add(
                                OAuth2AuthorizationUtils.resolveClientAuthenticationMethod(authenticationMethod))))
                .authorizationGrantTypes((grantTypes) -> authorizationGrantTypes.forEach(
                        grantType -> grantTypes.add(OAuth2AuthorizationUtils.resolveAuthorizationGrantType(grantType))))
                .redirectUris((uris) -> uris.addAll(redirectUris))
                .postLogoutRedirectUris((uris) -> uris.addAll(postLogoutRedirectUris))
                .scopes((scopes) -> scopes.addAll(clientScopes))
                .clientSettings(clientSettings)
                .tokenSettings(tokenSettings)
                .build();
    }
}
