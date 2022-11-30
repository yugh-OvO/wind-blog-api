package wind.system.controller;

import cn.dev33.satoken.annotation.SaCheckPermission;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import wind.common.annotation.Log;
import wind.common.constant.UserConstants;
import wind.common.core.controller.BaseController;
import wind.common.core.domain.PageQuery;
import wind.common.core.domain.Result;
import wind.common.core.page.Paging;
import wind.common.enums.BusinessType;
import wind.system.entity.SysConfig;
import wind.system.service.ConfigService;

/**
 * 参数配置 信息操作处理
 *
 * @author Yu Gaoheng
 */
@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("/system/config")
public class ConfigController extends BaseController {

    private final ConfigService configService;

    /**
     * 获取参数配置列表
     */
    @SaCheckPermission("configList")
    @GetMapping("/list")
    public Result<Paging<SysConfig>> list(SysConfig config, PageQuery pageQuery) {
        return Result.ok(configService.selectPageConfigList(config, pageQuery));
    }

    /**
     * 根据参数编号获取详细信息
     *
     * @param configId 参数ID
     */
    @SaCheckPermission("configList")
    @GetMapping(value = "/{configId}")
    public Result<SysConfig> getInfo(@PathVariable Long configId) {
        return Result.ok(configService.selectConfigById(configId));
    }

    /**
     * 根据参数键名查询参数值
     *
     * @param code 参数Key
     */
    @GetMapping(value = "/code/{code}")
    public Result<Void> getCode(@PathVariable String code) {
        return Result.ok(configService.selectConfigByCode(code));
    }

    /**
     * 新增参数配置
     */
    @SaCheckPermission("configList")
    @Log(title = "参数管理", businessType = BusinessType.INSERT)
    @PostMapping
    public Result<Void> add(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigCodeUnique(config))) {
            return Result.fail("新增参数'" + config.getName() + "'失败，参数键名已存在");
        }
        configService.insertConfig(config);
        return Result.ok();
    }

    /**
     * 修改参数配置
     */
    @SaCheckPermission("configList")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public Result<Void> edit(@Validated @RequestBody SysConfig config) {
        if (UserConstants.NOT_UNIQUE.equals(configService.checkConfigCodeUnique(config))) {
            return Result.fail("修改参数'" + config.getName() + "'失败，参数键名已存在");
        }
        configService.updateConfig(config);
        return Result.ok();
    }

    /**
     * 根据参数键名修改参数配置
     */
    @SaCheckPermission("configList")
    @Log(title = "参数管理", businessType = BusinessType.UPDATE)
    @PutMapping("/updateByKey")
    public Result<Void> updateByKey(@RequestBody SysConfig config) {
        configService.updateConfig(config);
        return Result.ok();
    }

    /**
     * 删除参数配置
     *
     * @param configIds 参数ID串
     */
    @SaCheckPermission("configList")
    @Log(title = "参数管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{configIds}")
    public Result<Void> remove(@PathVariable Long[] configIds) {
        configService.deleteConfigByIds(configIds);
        return Result.ok();
    }

    /**
     * 刷新参数缓存
     */
    @SaCheckPermission("configList")
    @Log(title = "参数管理", businessType = BusinessType.CLEAN)
    @DeleteMapping("/flushCache")
    public Result<Void> flushCache() {
        configService.resetConfigCache();
        return Result.ok();
    }
}
