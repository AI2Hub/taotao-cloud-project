package com.taotao.cloud.auth.biz.idserver.endpoint.system;

import com.taotao.cloud.auth.biz.idserver.advice.BaseController;
import com.taotao.cloud.auth.biz.idserver.advice.Rest;
import com.taotao.cloud.auth.biz.idserver.advice.RestBody;
import com.taotao.cloud.auth.biz.idserver.entity.Role;
import com.taotao.cloud.auth.biz.idserver.entity.dto.PermissionDTO;
import com.taotao.cloud.auth.biz.idserver.entity.dto.RolePermissionDTO;
import com.taotao.cloud.auth.biz.idserver.service.PermissionService;
import com.taotao.cloud.auth.biz.idserver.service.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * The type Role controller.
 *
 * @author felord.cn
 * @since 1.0.0
 */
@Controller
@AllArgsConstructor
public class RoleController extends BaseController {

    private final RoleService roleService;
    private final PermissionService permissionService;

    /**
     * Main string.
     *
     * @return the string
     */
    @GetMapping("/system/role/main")
    public String main() {
        return "/system/role/main";
    }

    /**
     * Add string.
     *
     * @return the string
     */
    @GetMapping("/system/role/add")
    @PreAuthorize("hasPermission('role','add')")
    public String add() {
        return "/system/role/add";
    }

    /**
     * Add rest.
     *
     * @param role the role
     * @return the rest
     */
    @PostMapping("/system/role/add")
    @ResponseBody
    @PreAuthorize("hasPermission('role','add')")
    public Rest<?> add(@RequestBody Role role) {
        roleService.save(role);
        return RestBody.ok("操作成功");
    }

    /**
     * Page page.
     *
     * @param page  the page
     * @param limit the limit
     * @return the page
     */
    @GetMapping("/system/role/data")
    @ResponseBody
    @PreAuthorize("hasPermission('role','list')")
    public Page<Role> page(@RequestParam Integer page, @RequestParam Integer limit) {
        return roleService.page(page, limit);
    }

    /**
     * Bind permission string.
     *
     * @param model  the model
     * @param roleId the role id
     * @return the string
     */
    @GetMapping("/system/role/permission/{roleId}")
    @PreAuthorize("hasPermission('role','permission')")
    public String bindPermission(Model model, @PathVariable String roleId) {
        //       [[${roleId}]]
        model.addAttribute("roleId", roleId);
        return "/system/role/permission";
    }

    /**
     * Edit string.
     *
     * @param model  the model
     * @param roleId the role id
     * @return the string
     */
    @GetMapping("/system/role/edit/{roleId}")
    @PreAuthorize("hasPermission('role','update')")
    public String edit(Model model, @PathVariable String roleId) {
        Role role = roleService.findByRoleId(roleId);
        model.addAttribute("role", role);
        return "/system/role/edit";
    }

    /**
     * Edit rest.
     *
     * @param role the role
     * @return the rest
     */
    @PostMapping("/system/role/edit")
    @ResponseBody
    @PreAuthorize("hasPermission('role','update')")
    public Rest<?> edit(@RequestBody Role role) {
        roleService.update(role);
        return RestBody.ok("操作成功");
    }

    /**
     * Page list.
     *
     * @param roleId the role id
     * @return the list
     */
    @GetMapping("/system/role/permissions/{roleId}")
    @ResponseBody
    @PreAuthorize("hasPermission('role','permission')")
    public List<PermissionDTO> permissions(@PathVariable String roleId) {
        return permissionService.permissionTreeData(roleId);
    }

    /**
     * Save role permissions rest.
     *
     * @param rolePermissionDTO the role permission dto
     * @return the rest
     */
    @PostMapping("/system/role/save/permissions")
    @ResponseBody
    @PreAuthorize("hasPermission('role','permission')")
    public Rest<?> saveRolePermissions(@RequestBody RolePermissionDTO rolePermissionDTO) {
        this.roleService.bindPermissions(rolePermissionDTO);
        return RestBody.ok("操作成功");
    }

    /**
     * Remove rest.
     *
     * @param roleId the role id
     * @return the rest
     */
    @PostMapping("/system/role/remove/{roleId}")
    @ResponseBody
    @PreAuthorize("hasPermission('role','remove')")
    public Rest<?> remove(@PathVariable String roleId) {
        roleService.deleteById(roleId);
        return RestBody.ok("操作成功");
    }
}
