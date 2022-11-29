package wind.system.service;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import wind.common.constant.CacheConstants;
import wind.common.constant.Constants;
import wind.common.core.domain.dto.RoleDTO;
import wind.common.core.domain.model.LoginUser;
import wind.common.enums.DeviceType;
import wind.common.enums.LoginType;
import wind.common.enums.UserStatus;
import wind.common.exception.user.CaptchaException;
import wind.common.exception.user.CaptchaExpireException;
import wind.common.exception.user.UserException;
import wind.common.helper.LoginHelper;
import wind.common.utils.ServletUtils;
import wind.common.utils.StringUtils;
import wind.common.utils.redis.RedisUtils;
import wind.system.entity.SysUser;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;
import java.util.List;
import java.util.function.Supplier;

/**
 * 登录校验方法
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class LoginService {

    private final UserService userService;
    private final ConfigService configService;
    private final LoginLogService loginLogService;
    private final PermissionService permissionService;

    @Value("${user.password.maxRetryCount}")
    private Integer maxRetryCount;

    @Value("${user.password.lockTime}")
    private Integer lockTime;

    /**
     * 登录验证
     *
     * @param username 用户名
     * @param password 密码
     * @param code     验证码
     * @param uuid     唯一标识
     * @return 结果
     */
    public String login(String username, String password, String code, String uuid) {
        HttpServletRequest request = ServletUtils.getRequest();
        boolean captchaEnabled = configService.selectCaptchaEnabled();
        // 验证码开关
        if (captchaEnabled) {
            validateCaptcha(username, code, uuid, request);
        }
        SysUser user = loadUserByUsername(username);
        checkLogin(LoginType.PASSWORD, username, () -> !BCrypt.checkpw(password, user.getPassword()));
        // 此处可根据登录用户的数据不同 自行创建 loginUser
        LoginUser loginUser = buildLoginUser(user);
        // 生成token
        LoginHelper.loginByDevice(loginUser, DeviceType.PC);

        loginLogService.record(username, Constants.LOGIN_SUCCESS, "登录成功", request);
        recordLoginInfo(user.getId(), username);
        return StpUtil.getTokenValue();
    }

    /**
     * 退出登录
     */
    public void logout() {
        try {
            String username = LoginHelper.getUsername();
            StpUtil.logout();
            loginLogService.record(username, Constants.LOGOUT, "退出成功", ServletUtils.getRequest());
        } catch (NotLoginException e) {
        }
    }

    /**
     * 校验短信验证码
     */
    private boolean validateSmsCode(String phonenumber, String smsCode, HttpServletRequest request) {
        String code = RedisUtils.getCacheObject(CacheConstants.CAPTCHA_CODE_KEY + phonenumber);
        if (StringUtils.isBlank(code)) {
            loginLogService.record(phonenumber, Constants.LOGIN_FAIL, "验证码已失效", request);
            throw new CaptchaExpireException();
        }
        return code.equals(smsCode);
    }

    /**
     * 校验验证码
     *
     * @param username 用户名
     * @param code     验证码
     * @param uuid     唯一标识
     */
    public void validateCaptcha(String username, String code, String uuid, HttpServletRequest request) {
        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.defaultString(uuid, "");
        String captcha = RedisUtils.getCacheObject(verifyKey);
        RedisUtils.deleteObject(verifyKey);
        if (captcha == null) {
            loginLogService.record(username, Constants.LOGIN_FAIL, "验证码已失效", request);
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            loginLogService.record(username, Constants.LOGIN_FAIL, "验证码错误", request);
            throw new CaptchaException();
        }
    }

    private SysUser loadUserByUsername(String username) {
        SysUser user = userService.selectUserByUsername(username);
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", username);
            throw new UserException("user.not.exists", username);
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", username);
            throw new UserException("user.blocked", username);
        }
        return user;
    }

    private SysUser loadUserByOpenid(String openid) {
        // 使用 openid 查询绑定用户 如未绑定用户 则根据业务自行处理 例如 创建默认用户
        SysUser user = new SysUser();
        if (ObjectUtil.isNull(user)) {
            log.info("登录用户：{} 不存在.", openid);
            // todo 用户不存在 业务逻辑自行实现
        } else if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            log.info("登录用户：{} 已被停用.", openid);
            // todo 用户已被停用 业务逻辑自行实现
        }
        return user;
    }

    /**
     * 构建登录用户
     */
    private LoginUser buildLoginUser(SysUser user) {
        LoginUser loginUser = new LoginUser();
        loginUser.setUserId(user.getId());
        loginUser.setUsername(user.getUsername());
        loginUser.setMenuPermission(permissionService.getMenuPermission(user));
        loginUser.setRolePermission(permissionService.getRolePermission(user));
        List<RoleDTO> roles = BeanUtil.copyToList(user.getRoles(), RoleDTO.class);
        loginUser.setRoles(roles);
        return loginUser;
    }

    /**
     * 记录登录信息
     *
     * @param userId 用户ID
     */
    public void recordLoginInfo(Integer userId, String username) {
        SysUser sysUser = new SysUser();
        sysUser.setId(userId);
        sysUser.setLoginIp(ServletUtils.getClientIp());
        sysUser.setLoginTime(DateUtil.date());
        sysUser.setUpdateBy(username);
        userService.updateUserProfile(sysUser);
    }

    /**
     * 登录校验
     */
    private void checkLogin(LoginType loginType, String username, Supplier<Boolean> supplier) {
        HttpServletRequest request = ServletUtils.getRequest();
        String errorKey = CacheConstants.PWD_ERR_CNT_KEY + username;
        String loginFail = Constants.LOGIN_FAIL;

        // 获取用户登录错误次数(可自定义限制策略 例如: key + username + ip)
        Integer errorNumber = RedisUtils.getCacheObject(errorKey);
        // 锁定时间内登录 则踢出
        if (ObjectUtil.isNotNull(errorNumber) && errorNumber.equals(maxRetryCount)) {
            loginLogService.record(username, loginFail, "登录尝试次数过多，账号已锁定", request);
            throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
        }

        if (supplier.get()) {
            // 是否第一次
            errorNumber = ObjectUtil.isNull(errorNumber) ? 1 : errorNumber + 1;
            // 达到规定错误次数 则锁定登录
            if (errorNumber.equals(maxRetryCount)) {
                RedisUtils.setCacheObject(errorKey, errorNumber, Duration.ofMinutes(lockTime));
                loginLogService.record(username, loginFail, "登录尝试次数过多，账号已锁定", request);
                throw new UserException(loginType.getRetryLimitExceed(), maxRetryCount, lockTime);
            } else {
                // 未达到规定错误次数 则递增
                RedisUtils.setCacheObject(errorKey, errorNumber);
                loginLogService.record(username, loginFail, "账号或密码错误", request);
                throw new UserException(loginType.getRetryLimitCount(), errorNumber);
            }
        }

        // 登录成功 清空错误次数
        RedisUtils.deleteObject(errorKey);
    }
}
