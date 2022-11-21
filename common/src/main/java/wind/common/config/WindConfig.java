package wind.common.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 *
 * @author Yu Gaoheng
 */

@Data
@Component
@ConfigurationProperties(prefix = "wind")
public class WindConfig {

    /**
     * 获取地址开关
     */
    @Getter
    private static boolean addressEnabled;
    /**
     * 项目名称
     */
    private String name;
    /**
     * 版本
     */
    private String version;
    /**
     * 缓存懒加载
     */
    private boolean cacheLazy;

    public void setAddressEnabled(boolean addressEnabled) {
        WindConfig.addressEnabled = addressEnabled;
    }

}
