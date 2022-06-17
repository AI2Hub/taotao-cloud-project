package com.taotao.cloud.auth.biz.idserver.service;

import com.taotao.cloud.auth.biz.idserver.entity.Permission;
import com.taotao.cloud.auth.biz.idserver.entity.Role;
import com.taotao.cloud.auth.biz.idserver.entity.UserInfo;
import com.taotao.cloud.auth.biz.idserver.entity.dto.RoleDTO;
import com.taotao.cloud.auth.biz.idserver.entity.dto.RolePermissionDTO;
import com.taotao.cloud.auth.biz.idserver.enumate.Enabled;
import com.taotao.cloud.auth.biz.idserver.exception.BindingException;
import com.taotao.cloud.auth.biz.idserver.exception.NotFoundException;
import com.taotao.cloud.auth.biz.idserver.mapstruct.RoleMapper;
import com.taotao.cloud.auth.biz.idserver.repository.PermissionRepository;
import com.taotao.cloud.auth.biz.idserver.repository.RoleRepository;
import com.taotao.cloud.auth.biz.idserver.repository.UserInfoRepository;
import com.taotao.cloud.auth.biz.idserver.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Jpa role service.
 *
 * @since 1.0.0
 */
@Service
@AllArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class JpaRoleService implements RoleService {
    private static final String ROOT_ROLE_NAME = "id_server";
    private final RoleMapper roleMapper;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final PermissionRepository permissionRepository;
    private final UserInfoRepository userInfoRepository;

    @Override
    public Role save(Role role) {
        roleNameCheck(role);
        role.setEnabled(Boolean.valueOf(Enabled.ENABLE.val()));
        return roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        roleNameCheck(role);
        Role flush = roleRepository.findById(role.getRoleId()).orElseThrow(NotFoundException::new);
        roleMapper.mergeAndIgnorePermissions(role, flush);
        this.roleRepository.flush();
        return flush;
    }

    @Override
    public Role findByRoleId(String roleId) {
        return roleRepository.findById(roleId).orElseThrow(NotFoundException::new);
    }

    @Override
    public void deleteById(String roleId) {
        if (userRoleRepository.countByRoleId(roleId) > 0) {
            throw new BindingException("有用户持有该角色，删除前需要解除关系");
        }
        roleRepository.deleteById(roleId);
    }

    @Override
    public void bindPermissions(RolePermissionDTO rolePermissionDTO) {
        Role role = roleRepository.findById(rolePermissionDTO.getRoleId())
                .orElseThrow(NotFoundException::new);
        Set<Permission> permissions = permissionRepository.findAllByPermissionIdIn(rolePermissionDTO.getPermissionIds());
        role.setPermissions(permissions);
        this.roleRepository.flush();
    }

    @Override
    public Page<Role> page(Integer page, Integer limit) {
        page = Math.max(page - 1, 0);
        return roleRepository.findAll(PageRequest.of(page, limit, Sort.sort(Role.class)
                .by(Role::getCreateTime).descending()));
    }

    @Override
    public List<RoleDTO> roleTreeData(String userId) {
        Optional<UserInfo> userInfoOptional = userInfoRepository.findById(userId);

        if (!userInfoOptional.isPresent()) {
            return Collections.emptyList();
        }

        UserInfo userInfo = userInfoOptional.get();
        Set<Role> authorities = userInfo.getAuthorities();
        List<Role> all = roleRepository.findAll();
        Stream<RoleDTO> unChecked = all.stream()
                .filter((role -> !authorities.contains(role)))
                .map(roleMapper::toUnCheckedDTO);
        RoleDTO rootRole = new RoleDTO();
        rootRole.setRoleId("1");
        rootRole.setParentId("0");
        rootRole.setRoleContent("请选择角色");
        Stream<RoleDTO> concat = Stream.concat(unChecked,
                authorities.stream().map(roleMapper::toCheckedDTO));
        return Stream.concat(concat, Stream.of(rootRole))
                .collect(Collectors.toList());
    }


    private static void roleNameCheck(Role role) {
        String roleName = role.getRoleName();
        if (OidcScopes.OPENID.equals(roleName) || ROOT_ROLE_NAME.equals(roleName)) {
            throw new BindingException(roleName + "是Id Server关键字");
        }
    }

}
