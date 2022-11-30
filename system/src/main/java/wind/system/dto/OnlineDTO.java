package wind.system.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 当前在线会话
 *
 * @author Yu Gaoheng
 */

@Data
@NoArgsConstructor
public class OnlineDTO {

    private static final long serialVersionUID = 1L;

    /**
     * 会话编号
     */
    private String token;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 登录IP地址
     */
    private String ip;

    /**
     * 登录地址
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
     * 登录时间
     */
    private String loginTime;

}
