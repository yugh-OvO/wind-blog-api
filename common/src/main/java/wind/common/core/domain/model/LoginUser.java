package wind.common.core.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import wind.common.core.domain.dto.RoleDTO;
import wind.common.helper.LoginHelper;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 登录用户身份权限
 *
 * @author Yu Gaoheng
 */

@Data
@NoArgsConstructor
public class LoginUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    private Integer userId;

    /**
     * 部门ID
     */
    private Integer departmentId;

    /**
     * 部门名
     */
    private String departmentName;

    /**
     * 用户唯一标识
     */
    private String token;

    /**
     * 用户类型
     */
    private String userType;

    /**
     * 登录时间
     */
    private Long loginTime;

    /**
     * 过期时间
     */
    private Long expireTime;

    /**
     * 登录IP地址
     */
    private String ipaddr;

    /**
     * 登录地点
     */
    private String loginLocation;

    /**
     * 浏览器类型
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 菜单权限
     */
    private Set<String> menuPermission;

    /**
     * 角色权限
     */
    private Set<String> rolePermission;

    /**
     * 用户名
     */
    private String username;

    /**
     * 角色对象
     */
    private List<RoleDTO> roles;

    /**
     * 数据权限 当前角色ID
     */
    private Long roleId;

    /**
     * 获取登录id
     */
    public String getLoginId() {
        if (userId == null) {
            throw new IllegalArgumentException("用户ID不能为空");
        }
        return LoginHelper.JOIN_CODE + userId;
    }

}
