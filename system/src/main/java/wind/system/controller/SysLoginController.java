package wind.system.controller;

import cn.hutool.core.map.MapUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import wind.common.annotation.Anonymous;
import wind.common.constant.Constants;
import wind.common.core.domain.Res;
import wind.common.core.domain.model.LoginBody;
import wind.common.helper.LoginHelper;
import wind.system.domain.SysUser;
import wind.system.service.ISysUserService;
import wind.system.service.SysLoginService;
import wind.system.service.SysPermissionService;

import java.util.Map;

/**
 * 登录验证
 *
 * @author Yu Gaoheng
 */
@Validated
@RequiredArgsConstructor
@RestController
public class SysLoginController {

    private final SysLoginService loginService;
    private final ISysUserService userService;
    private final SysPermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @Anonymous
    @PostMapping("/login")
    public Res<Map<String, Object>> login(@Validated @RequestBody LoginBody loginBody) {
        Map<String, Object> ajax = MapUtil.newHashMap();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
            loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return Res.ok(ajax);
    }

    /**
     * 退出登录
     */
    @Anonymous
    @PostMapping("/logout")
    public Res<Void> logout() {
        loginService.logout();
        return Res.ok("退出成功");
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public Res<SysUser> getInfo() {
        SysUser user = userService.selectUserById(LoginHelper.getUserId());
        user.setPermissions(permissionService.getMenuPermission(user));
        user.setRole(user.isAdmin() ? "admin" : "user");
        return Res.ok(user);
    }

}
