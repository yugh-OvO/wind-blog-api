package wind.system.service;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wind.common.constant.Constants;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.Paging;
import wind.common.utils.ip.AddressUtils;
import wind.system.dto.OperationLogDTO;
import wind.system.entity.OperationLog;
import wind.system.mapper.OperationLogMapper;
import wind.system.query.OperationLogQuery;

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

    public Paging<OperationLog> list(OperationLogQuery req, PageQuery pageQuery) {
        LambdaQueryWrapper<OperationLog> lqw = Wrappers.lambdaQuery();
        lqw.like(StrUtil.isNotEmpty(req.getTitle()), OperationLog::getTitle, req.getTitle());
        lqw.like(StrUtil.isNotEmpty(req.getName()), OperationLog::getName, req.getName());
        String[] operationTime = req.getOperationTimeRange();
        if (operationTime != null && operationTime.length == Constants.TIME_RANGE_LENGTH) {
            lqw.between(OperationLog::getOperationTime, operationTime[0], operationTime[1] + " 23:59:59");
        }
        lqw.orderByDesc(OperationLog::getId);
        Page<OperationLog> result = model.selectVoPage(pageQuery.build(), lqw);
        return Paging.build(result);
    }

    @Async
    public void recordOperation(final OperationLogDTO operationLogDto) {
        OperationLog operationLog = BeanUtil.toBean(operationLogDto, OperationLog.class);
        // 远程查询操作地点
        operationLog.setLocation(AddressUtils.getRealAddressByIp(operationLog.getIp()));
        operationLog.setOperationTime(new Date());
        model.insert(operationLog);
    }

}
