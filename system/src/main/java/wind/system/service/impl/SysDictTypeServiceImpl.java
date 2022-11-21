package wind.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wind.common.constant.CacheNames;
import wind.common.constant.UserConstants;
import wind.common.core.domain.PageQuery;
import wind.common.core.domain.entity.SysDictData;
import wind.common.core.domain.entity.SysDictType;
import wind.common.core.page.TableDataInfo;
import wind.common.core.service.DictService;
import wind.common.exception.ServiceException;
import wind.common.utils.StreamUtils;
import wind.common.utils.StringUtils;
import wind.common.utils.redis.CacheUtils;
import wind.system.mapper.SysDictDataMapper;
import wind.system.mapper.SysDictTypeMapper;
import wind.system.service.ISysDictTypeService;

import java.util.*;

/**
 * 字典 业务层处理
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Service
public class SysDictTypeServiceImpl implements ISysDictTypeService, DictService {

    private final SysDictTypeMapper baseMapper;
    private final SysDictDataMapper dictDataMapper;

    @Override
    public TableDataInfo<SysDictType> selectPageDictTypeList(SysDictType dictType, PageQuery pageQuery) {
        Map<String, Object> params = dictType.getParams();
        LambdaQueryWrapper<SysDictType> lqw = new LambdaQueryWrapper<SysDictType>()
            .like(StringUtils.isNotBlank(dictType.getName()), SysDictType::getName, dictType.getName())
            .eq(ObjectUtil.isNotNull(dictType.getStatus()), SysDictType::getStatus, dictType.getStatus())
            .like(StringUtils.isNotBlank(dictType.getType()), SysDictType::getType, dictType.getType())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysDictType::getCreateTime, params.get("beginTime"), params.get("endTime"));
        Page<SysDictType> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询字典类型
     *
     * @param dictType 字典类型信息
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeList(SysDictType dictType) {
        Map<String, Object> params = dictType.getParams();
        return baseMapper.selectList(new LambdaQueryWrapper<SysDictType>()
            .like(StringUtils.isNotBlank(dictType.getName()), SysDictType::getName, dictType.getName())
            .eq(ObjectUtil.isNotNull(dictType.getStatus()), SysDictType::getStatus, dictType.getStatus())
            .like(StringUtils.isNotBlank(dictType.getType()), SysDictType::getType, dictType.getType())
            .between(params.get("beginTime") != null && params.get("endTime") != null,
                SysDictType::getCreateTime, params.get("beginTime"), params.get("endTime")));
    }

    /**
     * 根据所有字典类型
     *
     * @return 字典类型集合信息
     */
    @Override
    public List<SysDictType> selectDictTypeAll() {
        return baseMapper.selectList();
    }

    /**
     * 根据字典类型查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    @Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#dictType")
    @Override
    public List<SysDictData> selectDictDataByType(String dictType) {
        List<SysDictData> dictData = dictDataMapper.selectDictDataByType(dictType);
        if (CollUtil.isNotEmpty(dictData)) {
            return dictData;
        }
        return null;
    }

    /**
     * 根据字典类型ID查询信息
     *
     * @param dictId 字典类型ID
     * @return 字典类型
     */
    @Override
    public SysDictType selectDictTypeById(Long dictId) {
        return baseMapper.selectById(dictId);
    }

    /**
     * 根据字典类型查询信息
     *
     * @param dictType 字典类型
     * @return 字典类型
     */
    @Cacheable(cacheNames = CacheNames.SYS_DICT, key = "#type")
    @Override
    public SysDictType selectDictTypeByType(String dictType) {
        return baseMapper.selectById(new LambdaQueryWrapper<SysDictType>().eq(SysDictType::getType, dictType));
    }

    /**
     * 批量删除字典类型信息
     *
     * @param dictIds 需要删除的字典ID
     */
    @Override
    public void deleteDictTypeByIds(Long[] dictIds) {
        for (Long dictId : dictIds) {
            SysDictType dictType = selectDictTypeById(dictId);
            if (dictDataMapper.exists(new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getType, dictType.getType()))) {
                throw new ServiceException(String.format("%1$s已分配,不能删除", dictType.getName()));
            }
            CacheUtils.evict(CacheNames.SYS_DICT, dictType.getType());
        }
        baseMapper.deleteBatchIds(Arrays.asList(dictIds));
    }

    /**
     * 加载字典缓存数据
     */
    @Override
    public void loadingDictCache() {
        List<SysDictData> dictDataList = dictDataMapper.selectList(
            new LambdaQueryWrapper<SysDictData>().eq(SysDictData::getStatus, UserConstants.DICT_NORMAL));
        Map<String, List<SysDictData>> dictDataMap = StreamUtils.groupByKey(dictDataList, SysDictData::getType);
        dictDataMap.forEach((k, v) -> {
            List<SysDictData> dictList = StreamUtils.sorted(v, Comparator.comparing(SysDictData::getSort));
            CacheUtils.put(CacheNames.SYS_DICT, k, dictList);
        });
    }

    /**
     * 清空字典缓存数据
     */
    @Override
    public void clearDictCache() {
        CacheUtils.clear(CacheNames.SYS_DICT);
    }

    /**
     * 重置字典缓存数据
     */
    @Override
    public void resetDictCache() {
        clearDictCache();
        loadingDictCache();
    }

    /**
     * 新增保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#dict.type")
    @Override
    public List<SysDictData> insertDictType(SysDictType dict) {
        int row = baseMapper.insert(dict);
        if (row > 0) {
            return new ArrayList<>();
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改保存字典类型信息
     *
     * @param dict 字典类型信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#dict.type")
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SysDictData> updateDictType(SysDictType dict) {
        SysDictType oldDict = baseMapper.selectById(dict.getId());
        dictDataMapper.update(null, new LambdaUpdateWrapper<SysDictData>()
            .set(SysDictData::getType, dict.getType())
            .eq(SysDictData::getType, oldDict.getType()));
        int row = baseMapper.updateById(dict);
        if (row > 0) {
            CacheUtils.evict(CacheNames.SYS_DICT, oldDict.getType());
            return dictDataMapper.selectDictDataByType(dict.getType());
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 校验字典类型称是否唯一
     *
     * @param dict 字典类型
     * @return 结果
     */
    @Override
    public String checkDictTypeUnique(SysDictType dict) {
        boolean exist = baseMapper.exists(new LambdaQueryWrapper<SysDictType>()
            .eq(SysDictType::getType, dict.getType())
            .ne(ObjectUtil.isNotNull(dict.getId()), SysDictType::getId, dict.getId()));
        if (exist) {
            return UserConstants.NOT_UNIQUE;
        }
        return UserConstants.UNIQUE;
    }

    /**
     * 根据字典类型和字典值获取字典标签
     *
     * @param dictType  字典类型
     * @param dictValue 字典值
     * @param separator 分隔符
     * @return 字典标签
     */
    @Override
    public String getLabel(String dictType, String dictValue, String separator) {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> datas = selectDictDataByType(dictType);

        if (StringUtils.containsAny(dictValue, separator) && CollUtil.isNotEmpty(datas)) {
            for (SysDictData dict : datas) {
                for (String value : dictValue.split(separator)) {
                    if (value.equals(dict.getValue())) {
                        propertyString.append(dict.getLabel() + separator);
                        break;
                    }
                }
            }
        } else {
            for (SysDictData dict : datas) {
                if (dictValue.equals(dict.getValue())) {
                    return dict.getLabel();
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

    /**
     * 根据字典类型和字典标签获取字典值
     *
     * @param type      字典类型
     * @param label     字典标签
     * @param separator 分隔符
     * @return 字典值
     */
    @Override
    public String getValue(String type, String label, String separator) {
        StringBuilder propertyString = new StringBuilder();
        List<SysDictData> data = selectDictDataByType(type);

        if (StringUtils.containsAny(label, separator) && CollUtil.isNotEmpty(data)) {
            for (SysDictData dict : data) {
                for (String labelOne : label.split(separator)) {
                    if (labelOne.equals(dict.getLabel())) {
                        propertyString.append(dict.getValue() + separator);
                        break;
                    }
                }
            }
        } else {
            for (SysDictData dict : data) {
                if (label.equals(dict.getLabel())) {
                    return "123";
                }
            }
        }
        return StringUtils.stripEnd(propertyString.toString(), separator);
    }

}
