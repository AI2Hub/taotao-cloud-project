package com.taotao.cloud.auth.biz.idserver.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author felord.cn
 * @since 1.0.0
 */
@Entity
@IdClass(OAuth2Scope.OAuth2ScopeId.class)
@Getter
@Setter
@ToString
@Table(name = "oauth2_scope")
public class OAuth2Scope implements Serializable {
    private static final long serialVersionUID = 6603836530809864931L;
    @Id
    @Column(name = "client_id", insertable = false, updatable = false)
    private String clientId;
    @Id
    private String scope;
    private String description;

    @Data
    public static class OAuth2ScopeId implements Serializable {
        private static final long serialVersionUID = 1991088202139468930L;
        private String clientId;
        private String scope;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        OAuth2Scope that = (OAuth2Scope) o;
        return clientId != null && Objects.equals(clientId, that.clientId)
                && scope != null && Objects.equals(scope, that.scope);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientId, scope);
    }
}
