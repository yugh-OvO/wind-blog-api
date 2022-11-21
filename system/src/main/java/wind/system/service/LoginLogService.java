package wind.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.TableDataInfo;
import wind.common.utils.StringUtils;
import wind.system.domain.SysLoginLog;
import wind.system.mapper.SysLoginLogMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 登录日志
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Service
public class LoginLogService {

    private final SysLoginLogMapper model;

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息
     * @param args     列表
     */
    @Async
    public void record(final String username, final String status, final String message,
                       HttpServletRequest request, final Object... args) {
//        final UserAgent userAgent = UserAgentUtil.parse(request.getHeader("User-Agent"));
//        final String ip = ServletUtils.getClientIp(request);
//
//        String address = AddressUtils.getRealAddressByIp(ip);
//        StringBuilder s = new StringBuilder();
//        s.append(getBlock(ip));
//        s.append(address);
//        s.append(getBlock(username));
//        s.append(getBlock(status));
//        s.append(getBlock(message));
//        // 打印信息到日志
//        log.info(s.toString(), args);
//        // 获取客户端操作系统
//        String os = userAgent.getOs().getName();
//        // 获取客户端浏览器
//        String browser = userAgent.getBrowser().getName();
//        // 封装对象
//        SysLoginLog loginLog = new SysLoginLog();
//        loginLog.setUserName(username);
//        loginLog.setIpaddr(ip);
//        loginLog.setLoginLocation(address);
//        loginLog.setBrowser(browser);
//        loginLog.setOs(os);
//        loginLog.setMsg(message);
//        // 日志状态
//        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
//            loginLog.setStatus(Constants.SUCCESS);
//        } else if (Constants.LOGIN_FAIL.equals(status)) {
//            loginLog.setStatus(Constants.FAIL);
//        }
//        // 插入数据
//        insertLoginLog(loginLog);
    }

    /**
     * 查询系统登录日志集合
     *
     * @param loginLog  访问日志对象
     * @param pageQuery 分页对象
     * @return 登录记录集合
     */
    public TableDataInfo<SysLoginLog> list(SysLoginLog loginLog, PageQuery pageQuery) {
        Map<String, Object> params = loginLog.getParams();
        LambdaQueryWrapper<SysLoginLog> lqw = new LambdaQueryWrapper<SysLoginLog>()
            .like(StringUtils.isNotBlank(loginLog.getIpaddr()), SysLoginLog::getIpaddr, loginLog.getIpaddr())
            .eq(StringUtils.isNotBlank(loginLog.getStatus()), SysLoginLog::getStatus, loginLog.getStatus())
            .like(StringUtils.isNotBlank(loginLog.getUserName()), SysLoginLog::getUserName, loginLog.getUserName())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysLoginLog::getLoginTime, params.get("beginTime"), params.get("endTime"));
        if (StringUtils.isBlank(pageQuery.getOrderByColumn())) {
            pageQuery.setOrderByColumn("info_id");
            pageQuery.setIsAsc("desc");
        }
        Page<SysLoginLog> page = model.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

}
