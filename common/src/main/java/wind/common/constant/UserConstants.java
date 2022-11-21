package wind.common.constant;

/**
 * 用户常量信息
 *
 * @author Yu Gaoheng
 */
public interface UserConstants {

    /**
     * 平台内系统用户的唯一标志
     */
    String SYS_USER = "SYS_USER";

    /**
     * 正常状态
     */
    String NORMAL = "0";

    /**
     * 异常状态
     */
    String EXCEPTION = "1";

    /**
     * 用户正常状态
     */
    String USER_NORMAL = "0";

    /**
     * 用户封禁状态
     */
    String USER_DISABLE = "1";

    /**
     * 角色封禁状态
     */
    String ROLE_DISABLE = "1";

    /**
     * 部门正常状态
     */
    Integer DEPT_NORMAL = 1;

    /**
     * 部门停用状态
     */
    Integer DEPT_DISABLE = 2;

    /**
     * 字典正常状态
     */
    Integer DICT_NORMAL = 1;

    /**
     * 是否为系统默认（是）
     */
    Integer YES = 1;

    /**
     * 是否菜单外链（是）
     */
    String YES_FRAME = "0";

    /**
     * 是否菜单外链（否）
     */
    String NO_FRAME = "1";

    /**
     * 菜单正常状态
     */
    String MENU_NORMAL = "0";

    /**
     * 菜单停用状态
     */
    String MENU_DISABLE = "1";

    /**
     * 菜单类型（目录）
     */
    Integer TYPE_DIR = 1;

    /**
     * 菜单类型（菜单）
     */
    Integer TYPE_MENU = 2;

    /**
     * 菜单类型（按钮）
     */
    Integer TYPE_BUTTON = 3;

    /**
     * Layout组件标识
     */
    String LAYOUT = "Layout";

    /**
     * ParentView组件标识
     */
    String PARENT_VIEW = "ParentView";

    /**
     * InnerLink组件标识
     */
    String INNER_LINK = "InnerLink";

    /**
     * 校验返回结果码
     */
    String UNIQUE = "0";
    String NOT_UNIQUE = "1";

    /**
     * 用户名长度限制
     */
    int USERNAME_MIN_LENGTH = 2;
    int USERNAME_MAX_LENGTH = 20;

    /**
     * 密码长度限制
     */
    int PASSWORD_MIN_LENGTH = 5;
    int PASSWORD_MAX_LENGTH = 20;

    /**
     * 管理员ID
     */
    Integer ADMIN_ID = 1;

}
