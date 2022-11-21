package wind.system.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ArrayUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import wind.common.core.domain.PageQuery;
import wind.common.core.domain.dto.OperationLogDTO;
import wind.common.core.page.TableDataInfo;
import wind.common.core.service.OperationLogService;
import wind.common.utils.StringUtils;
import wind.common.utils.ip.AddressUtils;
import wind.system.domain.SysOperationLog;
import wind.system.mapper.SysOperationLogMapper;
import wind.system.service.ISysOperationLogService;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 操作日志 服务层处理
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Service
public class SysOperationLogServiceImpl implements ISysOperationLogService, OperationLogService {

    private final SysOperationLogMapper baseMapper;

    /**
     * 操作日志记录
     *
     * @param operationLogDTO 操作日志信息
     */
    @Async
    @Override
    public void recordOperation(final OperationLogDTO operationLogDTO) {
        SysOperationLog operationLog = BeanUtil.toBean(operationLogDTO, SysOperationLog.class);
        // 远程查询操作地点
        operationLog.setLocation(AddressUtils.getRealAddressByIp(operationLog.getIp()));
        insertOperationLog(operationLog);
    }

    @Override
    public TableDataInfo<SysOperationLog> selectPageOperationLogList(SysOperationLog operationLog, PageQuery pageQuery) {
        Map<String, Object> params = operationLog.getParams();
        LambdaQueryWrapper<SysOperationLog> lqw = new LambdaQueryWrapper<SysOperationLog>()
            .like(StringUtils.isNotBlank(operationLog.getTitle()), SysOperationLog::getTitle, operationLog.getTitle())
            .eq(operationLog.getBusinessType() != null && operationLog.getBusinessType() > 0,
                SysOperationLog::getBusinessType, operationLog.getBusinessType())
            .func(f -> {
                if (ArrayUtil.isNotEmpty(operationLog.getBusinessTypes())) {
                    f.in(SysOperationLog::getBusinessType, Arrays.asList(operationLog.getBusinessTypes()));
                }
            })
            .eq(operationLog.getStatus() != null,
                SysOperationLog::getStatus, operationLog.getStatus())
            .like(StringUtils.isNotBlank(operationLog.getName()), SysOperationLog::getName, operationLog.getName())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysOperationLog::getOperationTime, params.get("beginTime"), params.get("endTime"));
        if (StringUtils.isBlank(pageQuery.getOrderByColumn())) {
            pageQuery.setOrderByColumn("id");
            pageQuery.setIsAsc("desc");
        }
        Page<SysOperationLog> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 新增操作日志
     *
     * @param operationLog 操作日志对象
     */
    @Override
    public void insertOperationLog(SysOperationLog operationLog) {
        operationLog.setOperationTime(new Date());
        baseMapper.insert(operationLog);
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operationLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysOperationLog> selectOperationLogList(SysOperationLog operationLog) {
        Map<String, Object> params = operationLog.getParams();
        return baseMapper.selectList(new LambdaQueryWrapper<SysOperationLog>()
            .like(StringUtils.isNotBlank(operationLog.getTitle()), SysOperationLog::getTitle, operationLog.getTitle())
            .eq(operationLog.getBusinessType() != null && operationLog.getBusinessType() > 0,
                SysOperationLog::getBusinessType, operationLog.getBusinessType())
            .func(f -> {
                if (ArrayUtil.isNotEmpty(operationLog.getBusinessTypes())) {
                    f.in(SysOperationLog::getBusinessType, Arrays.asList(operationLog.getBusinessTypes()));
                }
            })
            .eq(operationLog.getStatus() != null && operationLog.getStatus() > 0,
                SysOperationLog::getStatus, operationLog.getStatus())
            .like(StringUtils.isNotBlank(operationLog.getName()), SysOperationLog::getName, operationLog.getName())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysOperationLog::getOperationTime, params.get("beginTime"), params.get("endTime"))
            .orderByDesc(SysOperationLog::getId));
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperationLogByIds(Long[] operIds) {
        return baseMapper.deleteBatchIds(Arrays.asList(operIds));
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysOperationLog selectOperationLogById(Long operId) {
        return baseMapper.selectById(operId);
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperationLog() {
        baseMapper.delete(new LambdaQueryWrapper<>());
    }
}
