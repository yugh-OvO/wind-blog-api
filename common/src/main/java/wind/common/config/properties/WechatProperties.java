package wind.common.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * xss过滤 配置属性
 *
 * @author Yu Gaoheng
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat")
public class WechatProperties {

    private String appId;

    private String appSecret;

    private String loginAccessTokenUrl;

    private String loginUserInfoUrl;

    private String loginAuthBaseUrl;

    private String redirectUri;

    private String scope;

}
