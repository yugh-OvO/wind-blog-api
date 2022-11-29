package wind.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录类型
 *
 * @author Yu Gaoheng
 */
@Getter
@AllArgsConstructor
public enum LoginType {
 
    /**
     * 密码登录
     */
    PASSWORD("尝试登录次数过多", "用户名或密码错误"),

    /**
     * 短信登录
     */
    SMS("sms.code.retry.limit.exceed", "sms.code.retry.limit.count"),

    /**
     * 小程序登录
     */
    XCX("", "");

    /**
     * 登录重试超出限制提示
     */
    final String retryLimitExceed;

    /**
     * 登录重试限制计数提示
     */
    final String retryLimitCount;
}
