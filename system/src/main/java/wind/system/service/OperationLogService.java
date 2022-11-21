package wind.system.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import wind.common.constant.Constants;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.TableDataInfo;
import wind.common.utils.StringUtils;
import wind.system.domain.SysOperationLog;
import wind.system.mapper.SysOperationLogMapper;

/**
 * 操作日志 服务层
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Service
public class OperationLogService {

    private final SysOperationLogMapper model;

    public TableDataInfo<SysOperationLog> list(SysOperationLog req, PageQuery pageQuery) {
        LambdaQueryWrapper<SysOperationLog> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotEmpty(req.getTitle()), SysOperationLog::getTitle, req.getTitle());
        lqw.like(StringUtils.isNotEmpty(req.getName()), SysOperationLog::getName, req.getName());
        String[] operationTime = req.getOperationTimeRange();
        if (operationTime != null && operationTime.length == Constants.TIME_RANGE_LENGTH) {
            lqw.between(SysOperationLog::getOperationTime, operationTime[0], operationTime[1] + " 23:59:59");
        }
        lqw.orderByDesc(SysOperationLog::getId);
        Page<SysOperationLog> result = model.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

}
