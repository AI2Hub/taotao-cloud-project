package com.taotao.cloud.auth.biz.idserver.repository;

import com.taotao.cloud.auth.biz.idserver.entity.SystemSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * The System settings repository.
 * @since 1.0.0
 */
@Repository
public interface SystemSettingsRepository extends JpaRepository<SystemSettings, String> {
}
