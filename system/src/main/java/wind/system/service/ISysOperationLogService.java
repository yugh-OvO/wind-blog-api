package wind.system.service;

import wind.common.core.domain.PageQuery;
import wind.common.core.page.TableDataInfo;
import wind.system.domain.SysOperationLog;

import java.util.List;

/**
 * 操作日志 服务层
 *
 * @author Yu Gaoheng
 */
public interface ISysOperationLogService {

    /**
     * 查询操作日志列表
     *
     * @param operationLog 操作日志信息
     * @param pageQuery    分页对象
     * @return 操作日志集合
     */
    TableDataInfo<SysOperationLog> selectPageOperationLogList(SysOperationLog operationLog, PageQuery pageQuery);

    /**
     * 新增操作日志
     *
     * @param operationLog 操作日志对象
     */
    void insertOperationLog(SysOperationLog operationLog);

    /**
     * 查询系统操作日志集合
     *
     * @param operationLog 操作日志对象
     * @return 操作日志集合
     */
    List<SysOperationLog> selectOperationLogList(SysOperationLog operationLog);

    /**
     * 批量删除系统操作日志
     *
     * @param operationIds 需要删除的操作日志ID
     * @return 结果
     */
    int deleteOperationLogByIds(Long[] operationIds);

    /**
     * 查询操作日志详细
     *
     * @param operationId 操作ID
     * @return 操作日志对象
     */
    SysOperationLog selectOperationLogById(Long operationId);

    /**
     * 清空操作日志
     */
    void cleanOperationLog();
}
