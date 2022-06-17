package com.taotao.cloud.auth.biz.idserver.repository;

import com.taotao.cloud.auth.biz.idserver.entity.UserInfo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The interface User info repository.
 */
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, String> {

    /**
     * Find by username user info.
     *
     * @param username the username
     * @return the user info
     */
    @EntityGraph("userinfo.userDetails")
    Optional<UserInfo> findByUsername(String username);
}
