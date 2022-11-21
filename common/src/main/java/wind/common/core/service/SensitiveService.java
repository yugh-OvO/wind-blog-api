package wind.common.core.service;

/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 *
 * @author Yu Gaoheng
 * @version 3.6.0
 */
public interface SensitiveService {

    /**
     * 脱敏
     *
     * @return 脱敏后的对象
     */
    boolean isSensitive();

}
