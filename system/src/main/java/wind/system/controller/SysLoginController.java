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
import wind.common.core.domain.Result;
import wind.common.core.domain.model.LoginBody;
import wind.common.helper.LoginHelper;
import wind.system.entity.SysUser;
import wind.system.service.LoginService;
import wind.system.service.PermissionService;
import wind.system.service.UserService;

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

    private final LoginService loginService;
    private final UserService userService;
    private final PermissionService permissionService;

    /**
     * 登录方法
     *
     * @param loginBody 登录信息
     * @return 结果
     */
    @Anonymous
    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginBody loginBody) {
        Map<String, Object> ajax = MapUtil.newHashMap();
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return Result.ok(ajax);
    }

    /**
     * 退出登录
     */
    @Anonymous
    @PostMapping("/logout")
    public Result<Void> logout() {
        loginService.logout();
        return Result.ok("退出成功");
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public Result<SysUser> getInfo() {
        SysUser user = userService.selectUserById(LoginHelper.getUserId());
        user.setPermissions(permissionService.getMenuPermission(user));
        user.setRole(user.isAdmin() ? "admin" : "user");
        return Result.ok(user);
    }

}
