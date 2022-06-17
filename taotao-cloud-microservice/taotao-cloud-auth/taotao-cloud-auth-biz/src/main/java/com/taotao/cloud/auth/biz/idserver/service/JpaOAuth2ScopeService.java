package com.taotao.cloud.auth.biz.idserver.service;

import com.taotao.cloud.auth.biz.idserver.entity.OAuth2Scope;
import com.taotao.cloud.auth.biz.idserver.repository.OAuth2ScopeRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * The Jpa OAuth2Scope service.
 *
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
public class JpaOAuth2ScopeService implements OAuth2ScopeService {
    private final OAuth2ScopeRepository oAuth2ScopeRepository;

    @Override
    public List<OAuth2Scope> findByClientIdAndScope(String clientId, Collection<String> scope) {
        return oAuth2ScopeRepository.findByClient(clientId, scope);
    }
}
