package com.taotao.cloud.auth.biz.idserver.repository;

import com.taotao.cloud.auth.biz.idserver.entity.OAuth2GrantType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author felord.cn
 * @since 1.0.0
 */
@Repository
public interface OAuth2GrantTypeRepository extends JpaRepository<OAuth2GrantType, String> {
}
