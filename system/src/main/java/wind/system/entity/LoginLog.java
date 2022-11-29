package wind.system.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;
import wind.common.core.domain.BusinessEntity;

import java.util.Date;

/**
 * 系统访问记录表 sys_login_log
 *
 * @author Yu Gaoheng
 */

@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_login_log")
public class LoginLog extends BusinessEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名
     */
    private String username;

    /**
     * 登录状态(1-成功，2-失败)
     */
    private Integer status;

    /**
     * 登录IP地址
     */
    private String ip;

    /**
     * 登录地点
     */
    private String location;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 提示消息
     */
    private String message;

    /**
     * 访问时间
     */
    private Date loginTime;


}
