package wind.system.service;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.dynamic.datasource.annotation.DS;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import wind.common.constant.CacheNames;
import wind.common.constant.UserConstants;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.Paging;
import wind.common.exception.ServiceException;
import wind.common.utils.StringUtils;
import wind.common.utils.redis.CacheUtils;
import wind.system.entity.SysConfig;
import wind.system.mapper.SysConfigMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 参数配置 服务层实现
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Service
public class ConfigService {

    private final SysConfigMapper baseMapper;

    public Paging<SysConfig> selectPageConfigList(SysConfig config, PageQuery pageQuery) {
        Map<String, Object> params = config.getParams();
        LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<SysConfig>()
                .like(StringUtils.isNotBlank(config.getName()), SysConfig::getName, config.getName())
                .eq(ObjectUtil.isNotNull(config.getType()), SysConfig::getType, config.getType())
                .like(StringUtils.isNotBlank(config.getCode()), SysConfig::getCode, config.getCode())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        SysConfig::getCreateTime, params.get("beginTime"), params.get("endTime"));
        Page<SysConfig> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return Paging.build(page);
    }

    /**
     * 查询参数配置信息
     *
     * @param configId 参数配置ID
     * @return 参数配置信息
     */
    @DS("master")
    public SysConfig selectConfigById(Long configId) {
        return baseMapper.selectById(configId);
    }

    /**
     * 根据键名查询参数配置信息
     *
     * @param code 参数key
     * @return 参数键值
     */
    @Cacheable(cacheNames = CacheNames.SYS_CONFIG, key = "#code")
    public String selectConfigByCode(String code) {
        SysConfig retConfig = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>()
                .eq(SysConfig::getCode, code));
        if (ObjectUtil.isNotNull(retConfig)) {
            return retConfig.getValue();
        }
        return StringUtils.EMPTY;
    }

    /**
     * 获取验证码开关
     *
     * @return true开启，false关闭
     */
    public boolean selectCaptchaEnabled() {
        String captchaEnabled = selectConfigByCode("sys.account.captchaEnabled");
        if (StrUtil.isEmpty(captchaEnabled)) {
            return true;
        }
        return Convert.toBool(captchaEnabled);
    }

    /**
     * 查询参数配置列表
     *
     * @param config 参数配置信息
     * @return 参数配置集合
     */
    public List<SysConfig> selectConfigList(SysConfig config) {
        Map<String, Object> params = config.getParams();
        LambdaQueryWrapper<SysConfig> lqw = new LambdaQueryWrapper<SysConfig>()
                .like(StringUtils.isNotBlank(config.getName()), SysConfig::getName, config.getName())
                .eq(ObjectUtil.isNotNull(config.getType()), SysConfig::getType, config.getType())
                .like(StringUtils.isNotBlank(config.getCode()), SysConfig::getCode, config.getCode())
                .between(params.get("beginTime") != null && params.get("endTime") != null,
                        SysConfig::getCreateTime, params.get("beginTime"), params.get("endTime"));
        return baseMapper.selectList(lqw);
    }

    /**
     * 新增参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_CONFIG, key = "#config.code")
    public String insertConfig(SysConfig config) {
        int row = baseMapper.insert(config);
        if (row > 0) {
            return config.getValue();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改参数配置
     *
     * @param config 参数配置信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_CONFIG, key = "#config.code")
    public String updateConfig(SysConfig config) {
        int row;
        if (config.getId() != null) {
            row = baseMapper.updateById(config);
        } else {
            row = baseMapper.update(config, new LambdaQueryWrapper<SysConfig>()
                    .eq(SysConfig::getCode, config.getCode()));
        }
        if (row > 0) {
            return config.getValue();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 批量删除参数信息
     *
     * @param configIds 需要删除的参数ID
     */
    public void deleteConfigByIds(Long[] configIds) {
        for (Long configId : configIds) {
            SysConfig config = selectConfigById(configId);
            if (NumberUtil.equals(UserConstants.YES, config.getType())) {
                throw new ServiceException(String.format("内置参数【%1$s】不能删除 ", config.getCode()));
            }
            CacheUtils.evict(CacheNames.SYS_CONFIG, config.getCode());
        }
        baseMapper.deleteBatchIds(Arrays.asList(configIds));
    }

    /**
     * 加载参数缓存数据
     */
    public void loadingConfigCache() {
        List<SysConfig> configsList = selectConfigList(new SysConfig());
        configsList.forEach(config ->
                CacheUtils.put(CacheNames.SYS_CONFIG, config.getCode(), config.getValue()));
    }

    /**
     * 清空参数缓存数据
     */
    public void clearConfigCache() {
        CacheUtils.clear(CacheNames.SYS_CONFIG);
    }

    /**
     * 重置参数缓存数据
     */
    public void resetConfigCache() {
        clearConfigCache();
        loadingConfigCache();
    }

    /**
     * 校验参数键名是否唯一
     *
     * @param config 参数配置信息
     * @return 结果
     */
    public String checkConfigCodeUnique(SysConfig config) {
        Long configId = ObjectUtil.isNull(config.getId()) ? -1L : config.getId();
        SysConfig info = baseMapper.selectOne(new LambdaQueryWrapper<SysConfig>().eq(SysConfig::getCode, config.getCode()));
        if (ObjectUtil.isNotNull(info) && info.getId().longValue() != configId.longValue()) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据参数 key 获取参数值
     *
     * @param code 参数 key
     * @return 参数值
     */
    public String getValue(String code) {
        return selectConfigByCode(code);
    }

}
