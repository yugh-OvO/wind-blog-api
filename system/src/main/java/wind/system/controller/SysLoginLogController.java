package wind.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wind.common.core.controller.BaseController;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.TableDataInfo;
import wind.system.domain.SysLoginLog;
import wind.system.service.LoginLogService;

/**
 * 系统访问记录
 *
 * @author Yu Gaoheng
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/monitor/loginLog")
public class SysLoginLogController extends BaseController {

    private final LoginLogService service;

    /**
     * 获取系统访问记录列表
     */
    @SaCheckPermission("monitor:loginLog:list")
    @GetMapping("/list")
    public TableDataInfo<SysLoginLog> list(SysLoginLog loginLog, PageQuery pageQuery) {
        return service.list(loginLog, pageQuery);
    }

}
