package wind.system.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import wind.common.config.WindConfig;
import wind.system.service.ConfigService;

/**
 * 初始化 system 模块对应业务数据
 *
 * @author Yu Gaoheng
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SystemApplicationRunner implements ApplicationRunner {

    private final WindConfig windConfig;
    private final ConfigService configService;

    @Override
    public void run(ApplicationArguments args) {
        if (windConfig.isCacheLazy()) {
            return;
        }
        configService.loadingConfigCache();
        log.info("加载参数缓存数据成功");
    }

}
