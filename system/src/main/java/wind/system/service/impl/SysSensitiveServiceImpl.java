package wind.system.service.impl;

import org.springframework.stereotype.Service;
import wind.common.core.service.SensitiveService;
import wind.common.helper.LoginHelper;

/**
 * 脱敏服务
 * 默认管理员不过滤
 * 需自行根据业务重写实现
 *
 * @author Yu Gaoheng
 * @version 3.6.0
 */
@Service
public class SysSensitiveServiceImpl implements SensitiveService {

    /**
     * 是否脱敏
     */
    @Override
    public boolean isSensitive() {
        return !LoginHelper.isAdmin();
    }

}
