package wind.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wind.common.annotation.Log;
import wind.common.constant.UserConstants;
import wind.common.core.controller.BaseController;
import wind.common.core.domain.Res;
import wind.common.enums.BusinessType;
import wind.system.domain.SysMenu;
import wind.system.service.ISysMenuService;

import java.util.List;
import java.util.Map;

/**
 * 菜单信息
 *
 * @author Yu Gaoheng
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/menu")
public class SysMenuController extends BaseController {

    private final ISysMenuService menuService;

    /**
     * 根据菜单编号获取详细信息
     *
     * @param menuId 菜单ID
     */
    @SaCheckPermission("menuList")
    @GetMapping(value = "/{menuId}")
    public Res<SysMenu> getInfo(@PathVariable Integer menuId) {
        return Res.ok(menuService.selectMenuById(menuId));
    }

    /**
     * 获取菜单下拉树列表
     */
    @GetMapping("/options")
    public Res<List<Tree<Integer>>> options(SysMenu menu) {
        List<SysMenu> menus = menuService.selectMenuList(menu, getUserId());
        return Res.ok(menuService.buildMenuTreeOptions(menus));
    }

    /**
     * 加载对应角色菜单列表树
     *
     * @param roleId 角色ID
     */
    @GetMapping(value = "/roleMenuTreeselect/{roleId}")
    public Res<Map<String, Object>> roleMenuTreeselect(@PathVariable("roleId") Integer roleId) {
        List<SysMenu> menus = menuService.selectMenuList(getUserId());
        Map<String, Object> ajax = MapUtil.newHashMap();
        ajax.put("checkedKeys", menuService.selectMenuListByRoleId(roleId));
        ajax.put("menus", menuService.buildMenuTreeOptions(menus));
        return Res.ok(ajax);
    }

    /**
     * 新增菜单
     */
    @SaCheckPermission("menuList")
    @Log(title = "菜单管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Res<Void> add(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return Res.fail("新增菜单'" + menu.getName() + "'失败，菜单名称已存在");
        }
        return toAjax(menuService.insertMenu(menu));
    }

    /**
     * 修改菜单
     */
    @SaCheckPermission("menuList")
    @Log(title = "菜单管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Res<Void> edit(@Validated @RequestBody SysMenu menu) {
        if (UserConstants.NOT_UNIQUE.equals(menuService.checkMenuNameUnique(menu))) {
            return Res.fail("修改菜单'" + menu.getName() + "'失败，菜单名称已存在");
        } else if (menu.getId().equals(menu.getParentId())) {
            return Res.fail("修改菜单'" + menu.getName() + "'失败，上级菜单不能选择自己");
        }
        return toAjax(menuService.updateMenu(menu));
    }

    /**
     * 删除菜单
     *
     * @param menuId 菜单ID
     */
    @SaCheckPermission("menuList")
    @Log(title = "菜单管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{menuId}")
    public Res<Void> remove(@PathVariable("menuId") Integer menuId) {
        if (menuService.hasChildByMenuId(menuId)) {
            return Res.fail("存在子菜单,不允许删除");
        }
        if (menuService.checkMenuExistRole(menuId)) {
            return Res.fail("菜单已分配,不允许删除");
        }
        return toAjax(menuService.deleteMenuById(menuId));
    }
}
