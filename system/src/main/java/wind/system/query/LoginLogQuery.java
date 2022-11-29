package wind.system.query;

import lombok.Data;

/**
 * 操作日志
 *
 * @author Yu Gaoheng
 */
@Data
public class LoginLogQuery {

    /**
     * 登录时间范围
     */
    private String[] loginTimeRange;

}
