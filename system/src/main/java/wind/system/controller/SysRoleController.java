package wind.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wind.common.annotation.Log;
import wind.common.constant.UserConstants;
import wind.common.core.controller.BaseController;
import wind.common.core.domain.OptionEntity;
import wind.common.core.domain.PageQuery;
import wind.common.core.domain.Res;
import wind.common.core.domain.model.LoginUser;
import wind.common.core.page.TableDataInfo;
import wind.common.enums.BusinessType;
import wind.common.helper.LoginHelper;
import wind.system.domain.SysRole;
import wind.system.domain.SysUser;
import wind.system.service.ISysMenuService;
import wind.system.service.ISysRoleService;
import wind.system.service.ISysUserService;
import wind.system.service.SysPermissionService;

import java.util.List;

/**
 * 角色信息
 *
 * @author Yu Gaoheng
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/role")
public class SysRoleController extends BaseController {

    private final ISysRoleService roleService;
    private final ISysUserService userService;
    private final SysPermissionService permissionService;
    private final ISysMenuService menuService;

    /**
     * 获取角色信息列表
     */
    @SaCheckPermission("roleList")
    @GetMapping("/list")
    public Res<TableDataInfo<SysRole>> list(SysRole role, PageQuery pageQuery) {
        return Res.ok(roleService.selectPageRoleList(role, pageQuery));
    }

    @GetMapping("/options")
    public Res<List<OptionEntity>> getOptions() {
        return Res.ok(roleService.getOptions());
    }

    /**
     * 根据角色编号获取详细信息
     *
     * @param roleId 角色ID
     */
    @SaCheckPermission("roleList")
    @GetMapping(value = "/{roleId}")
    public Res<SysRole> getInfo(@PathVariable Integer roleId) {
        SysRole role = roleService.selectRoleById(roleId);
        role.setMenuIds(menuService.selectMenuListByRoleId(roleId).stream().toArray(Integer[]::new));
        return Res.ok(role);
    }

    /**
     * 新增角色
     */
    @SaCheckPermission("roleList")
    @Log(title = "角色管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Res<Void> add(@Validated @RequestBody SysRole role) {
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return Res.fail("新增角色'" + role.getName() + "'失败，角色名称已存在");
        }
        return toAjax(roleService.insertRole(role));
    }

    /**
     * 修改保存角色
     */
    @SaCheckPermission("roleList")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Res<Void> edit(@Validated @RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        if (UserConstants.NOT_UNIQUE.equals(roleService.checkRoleNameUnique(role))) {
            return Res.fail("修改角色'" + role.getName() + "'失败，角色名称已存在");
        }

        if (roleService.updateRole(role) > 0) {
            // 更新缓存用户权限
            LoginUser loginUser = getLoginUser();
            SysUser sysUser = userService.selectUserById(loginUser.getUserId());
            if (ObjectUtil.isNotNull(sysUser) && !sysUser.isAdmin()) {
                loginUser.setMenuPermission(permissionService.getMenuPermission(sysUser));
                LoginHelper.setLoginUser(loginUser);
            }
            return Res.ok();
        }
        return Res.fail("修改角色'" + role.getName() + "'失败，请联系管理员");
    }

    /**
     * 状态修改
     */
    @SaCheckPermission("roleList")
    @Log(title = "角色管理", businessType = BusinessType.UPDATE)
    @PutMapping("/changeStatus")
    public Res<Void> changeStatus(@RequestBody SysRole role) {
        roleService.checkRoleAllowed(role);
        return toAjax(roleService.updateRoleStatus(role));
    }

    /**
     * 删除角色
     *
     * @param roleIds 角色ID串
     */
    @SaCheckPermission("roleList")
    @Log(title = "角色管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{roleIds}")
    public Res<Void> remove(@PathVariable Integer[] roleIds) {
        return toAjax(roleService.deleteRoleByIds(roleIds));
    }

}
