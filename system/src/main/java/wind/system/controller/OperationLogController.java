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
import wind.system.entity.OperationLog;
import wind.system.query.OperationLogQuery;
import wind.system.service.OperationLogService;

/**
 * 操作日志记录
 *
 * @author Yu Gaoheng
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/operationLog")
public class OperationLogController extends BaseController {

    private final OperationLogService service;

    /**
     * 获取操作日志记录列表
     */
    @SaCheckPermission("operationLogList")
    @GetMapping("/list")
    public Result<Paging<OperationLog>> list(OperationLogQuery operationLog, PageQuery pageQuery) {
        return Result.ok(service.list(operationLog, pageQuery));
    }

}
