package wind.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wind.common.core.controller.BaseController;
import wind.common.core.domain.PageQuery;
import wind.common.core.domain.Result;
import wind.common.core.page.Paging;
import wind.system.entity.LoginLog;
import wind.system.query.LoginLogQuery;
import wind.system.service.LoginLogService;

/**
 * 操作日志记录
 *
 * @author Yu Gaoheng
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/loginLog")
public class LoginLogController extends BaseController {

    private final LoginLogService service;

    /**
     * 获取操作日志记录列表
     */
    @SaCheckPermission("loginLogList")
    @GetMapping("/list")
    public Result<Paging<LoginLog>> list(LoginLogQuery query, PageQuery pageQuery) {
        return Result.ok(service.list(query, pageQuery));
    }

}
