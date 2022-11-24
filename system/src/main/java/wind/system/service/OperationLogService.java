package wind.system.service;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wind.common.constant.Constants;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.TableDataInfo;
import wind.common.utils.StringUtils;
import wind.common.utils.ip.AddressUtils;
import wind.system.dto.OperationLogDto;
import wind.system.entity.OperationLog;
import wind.system.mapper.OperationLogMapper;
import wind.system.queryDto.OperationLogQueryDto;

import java.util.Date;

/**
 * 操作日志 服务层
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Service
public class OperationLogService {

    private final OperationLogMapper model;

    public TableDataInfo<OperationLog> list(OperationLogQueryDto req, PageQuery pageQuery) {
        LambdaQueryWrapper<OperationLog> lqw = Wrappers.lambdaQuery();
        lqw.like(StringUtils.isNotEmpty(req.getTitle()), OperationLog::getTitle, req.getTitle());
        lqw.like(StringUtils.isNotEmpty(req.getName()), OperationLog::getName, req.getName());
        String[] operationTime = req.getOperationTimeRange();
        if (operationTime != null && operationTime.length == Constants.TIME_RANGE_LENGTH) {
            lqw.between(OperationLog::getOperationTime, operationTime[0], operationTime[1] + " 23:59:59");
        }
        lqw.orderByDesc(OperationLog::getId);
        Page<OperationLog> result = model.selectVoPage(pageQuery.build(), lqw);
        return TableDataInfo.build(result);
    }

    @Async
    public void recordOperation(final OperationLogDto operationLogDto) {
        OperationLog operationLog = BeanUtil.toBean(operationLogDto, OperationLog.class);
        // 远程查询操作地点
        operationLog.setLocation(AddressUtils.getRealAddressByIp(operationLog.getIp()));
        operationLog.setOperationTime(new Date());
        model.insert(operationLog);
    }

}
