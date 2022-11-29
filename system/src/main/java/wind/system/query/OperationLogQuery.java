package wind.system.query;

import lombok.Data;

/**
 * 操作日志
 *
 * @author Yu Gaoheng
 */
@Data
public class OperationLogQuery {

    /**
     * 操作模块
     */
    private String title;

    /**
     * 操作人员
     */
    private String name;

    /**
     * 操作时间范围
     */
    private String[] operationTimeRange;

}
