package wind.common.core.controller;

import cn.hutool.core.util.StrUtil;
import wind.common.core.domain.Result;
import wind.common.core.domain.model.LoginUser;
import wind.common.helper.LoginHelper;

/**
 * web层通用数据处理
 *
 * @author Yu Gaoheng
 */
public class BaseController {

    /**
     * 响应返回结果
     *
     * @param rows 影响行数
     * @return 操作结果
     */
    protected Result<Void> toAjax(int rows) {
        return rows > 0 ? Result.ok() : Result.fail();
    }

    /**
     * 响应返回结果
     *
     * @param result 结果
     * @return 操作结果
     */
    protected Result<Void> toAjax(boolean result) {
        return result ? Result.ok() : Result.fail();
    }

    /**
     * 页面跳转
     */
    public String redirect(String url) {
        return StrUtil.format("redirect:{}", url);
    }

    /**
     * 获取用户缓存信息
     */
    public LoginUser getLoginUser() {
        return LoginHelper.getLoginUser();
    }

    /**
     * 获取登录用户id
     */
    public Integer getUserId() {
        return LoginHelper.getUserId();
    }

    /**
     * 获取登录部门id
     */
    public Integer getDepartmentId() {
        return LoginHelper.getDepartmentId();
    }

    /**
     * 获取登录用户名
     */
    public String getUsername() {
        return LoginHelper.getUsername();
    }
}
