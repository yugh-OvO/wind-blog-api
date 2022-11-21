package wind.common.enums;

/**
 * 用户状态
 *
 * @author Yu Gaoheng
 */
public enum UserStatus {
    /**
     * 正常
     */
    DISABLE(2, "停用");

    private final Integer code;
    private final String info;

    UserStatus(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

    public Integer getCode() {
        return code;
    }

    public String getInfo() {
        return info;
    }
}
