package wind.common.core.service;

/**
 * 通用 参数配置服务
 *
 * @author Yu Gaoheng
 */
public interface ConfigService {

    /**
     * 根据参数 key 获取参数值
     *
     * @param code 参数 key
     * @return 参数值
     */
    String getValue(String code);

}
