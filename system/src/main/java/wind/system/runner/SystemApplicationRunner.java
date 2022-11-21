package wind.system.runner;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import wind.common.config.WindConfig;
import wind.system.service.ISysConfigService;
import wind.system.service.ISysDictTypeService;

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
    private final ISysConfigService configService;
    private final ISysDictTypeService dictTypeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (windConfig.isCacheLazy()) {
            return;
        }
        configService.loadingConfigCache();
        log.info("加载参数缓存数据成功");
        dictTypeService.loadingDictCache();
        log.info("加载字典缓存数据成功");
    }

}
