package wind.common.core.service;

import org.springframework.scheduling.annotation.Async;
import wind.common.core.domain.dto.OperationLogDTO;

/**
 * 通用 操作日志
 *
 * @author Yu Gaoheng
 */
public interface OperationLogService {

    /**
     * 新增操作日志
     *
     * @param operationLogDTO 操作日志
     */
    @Async
    void recordOperation(OperationLogDTO operationLogDTO);
}
