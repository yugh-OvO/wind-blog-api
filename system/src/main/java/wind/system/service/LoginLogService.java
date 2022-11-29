package wind.system.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.http.useragent.UserAgent;
import cn.hutool.http.useragent.UserAgentUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wind.common.constant.Constants;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.Paging;
import wind.common.utils.ServletUtils;
import wind.common.utils.StringUtils;
import wind.common.utils.ip.AddressUtils;
import wind.system.entity.LoginLog;
import wind.system.mapper.LoginLogMapper;
import wind.system.query.LoginLogQuery;

import javax.servlet.http.HttpServletRequest;

/**
 * 登录日志
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Service
public class LoginLogService {

    private final LoginLogMapper model;

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     */
    @Async
    public void record(final String username, final String status, final String message,
                       HttpServletRequest request) {
        final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
        final String ip = ServletUtils.getClientIP(request);

        String address = AddressUtils.getRealAddressByIp(ip);
        StringBuilder s = new StringBuilder();
        s.append(getBlock(ip));
        s.append(address);
        s.append(getBlock(username));
        s.append(getBlock(status));
        s.append(getBlock(message));
        // 获取客户端操作系统
        String os = userAgent.getOs().getName();
        // 获取客户端浏览器
        String browser = userAgent.getBrowser().getName();
        // 封装对象
        LoginLog loginLog = new LoginLog();
        loginLog.setUsername(username);
        loginLog.setIp(ip);
        loginLog.setLocation(address);
        loginLog.setBrowser(browser);
        loginLog.setOs(os);
        loginLog.setMessage(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            loginLog.setStatus(Constants.SUCCESS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            loginLog.setStatus(Constants.FAIL);
        }
        loginLog.setLoginTime(DateUtil.date());
        // 插入数据
        model.insert(loginLog);
    }

    private String getBlock(Object msg) {
        if (msg == null) {
            msg = "";
        }
        return "[" + msg + "]";
    }

    /**
     * 查询系统登录日志集合
     *
     * @param query     访问日志对象
     * @param pageQuery 分页对象
     * @return 登录记录集合
     */
    public Paging<LoginLog> list(LoginLogQuery query, PageQuery pageQuery) {
        LambdaQueryWrapper<LoginLog> lqw = Wrappers.lambdaQuery();
        String[] operationTime = query.getLoginTimeRange();
        if (operationTime != null && operationTime.length == Constants.TIME_RANGE_LENGTH) {
            lqw.between(LoginLog::getLoginTime, operationTime[0], operationTime[1] + " 23:59:59");
        }
        lqw.orderByDesc(LoginLog::getId);
        Page<LoginLog> result = model.selectVoPage(pageQuery.build(), lqw);
        return Paging.build(result);
    }

}
