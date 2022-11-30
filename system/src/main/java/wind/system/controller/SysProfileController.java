package wind.system.controller;

import cn.dev33.satoken.secure.BCrypt;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wind.common.annotation.Log;
import wind.common.constant.UserConstants;
import wind.common.core.controller.BaseController;
import wind.common.core.domain.Result;
import wind.common.enums.BusinessType;
import wind.common.helper.LoginHelper;
import wind.system.entity.SysUser;
import wind.system.service.UserService;

import java.util.Map;

/**
 * 个人信息 业务处理
 *
 * @author Yu Gaoheng
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/user")
public class SysProfileController extends BaseController {

    private final UserService userService;

    /**
     * 个人信息
     */
    @GetMapping("/profile")
    public Result<Map<String, Object>> profile() {
        SysUser user = userService.selectUserById(getUserId());
        Map<String, Object> ajax = MapUtil.newHashMap();
        ajax.put("user", user);
        ajax.put("roleGroup", userService.selectUserRoleGroup(user.getUsername()));
        return Result.ok(ajax);
    }

    /**
     * 修改用户
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updateProfile")
    public Result<Void> updateProfile(@RequestBody SysUser user) {
        if (StrUtil.isNotEmpty(user.getMobile())
                && UserConstants.NOT_UNIQUE.equals(userService.checkPhoneUnique(user))) {
            return Result.fail("修改用户'" + user.getUsername() + "'失败，手机号码已存在");
        }
        if (StrUtil.isNotEmpty(user.getEmail())
                && UserConstants.NOT_UNIQUE.equals(userService.checkEmailUnique(user))) {
            return Result.fail("修改用户'" + user.getUsername() + "'失败，邮箱账号已存在");
        }
        user.setId(getUserId());
        user.setUsername(null);
        user.setPassword(null);
        user.setAvatar(null);
        if (userService.updateUserProfile(user) > 0) {
            return Result.ok();
        }
        return Result.fail("修改个人信息异常，请联系管理员");
    }

    /**
     * 重置密码
     */
    @Log(title = "个人信息", businessType = BusinessType.UPDATE)
    @PutMapping("/updatePwd")
    public Result<Void> updatePwd(@RequestBody Map<String, Object> params) {
        String newPassword = params.get("newPassword").toString();
        String oldPassword = params.get("oldPassword").toString();
        SysUser user = userService.selectUserById(LoginHelper.getUserId());
        String username = user.getUsername();
        String password = user.getPassword();
        if (!BCrypt.checkpw(oldPassword, password)) {
            return Result.fail("修改密码失败，旧密码错误");
        }
        if (newPassword.equals(oldPassword)) {
            return Result.fail("新密码不能与旧密码相同");
        }

        if (userService.resetUserPwd(username, BCrypt.hashpw(newPassword)) > 0) {
            return Result.ok();
        }
        return Result.fail("修改密码异常，请联系管理员");
    }

}
