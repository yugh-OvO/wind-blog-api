package wind.system.service.impl;

import cn.hutool.core.util.ObjectUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CachePut;
import org.springframework.stereotype.Service;
import wind.common.constant.CacheNames;
import wind.common.core.domain.PageQuery;
import wind.common.core.domain.entity.SysDictData;
import wind.common.core.page.TableDataInfo;
import wind.common.exception.ServiceException;
import wind.common.utils.StringUtils;
import wind.common.utils.redis.CacheUtils;
import wind.system.mapper.SysDictDataMapper;
import wind.system.service.ISysDictDataService;

import java.util.List;

/**
 * 字典 业务层处理
 *
 * @author Yu Gaoheng
 */
@RequiredArgsConstructor
@Service
public class SysDictDataServiceImpl implements ISysDictDataService {

    private final SysDictDataMapper baseMapper;

    @Override
    public TableDataInfo<SysDictData> selectPageDictDataList(SysDictData dictData, PageQuery pageQuery) {
        LambdaQueryWrapper<SysDictData> lqw = new LambdaQueryWrapper<SysDictData>()
            .eq(StringUtils.isNotBlank(dictData.getType()), SysDictData::getType, dictData.getType())
            .like(StringUtils.isNotBlank(dictData.getLabel()), SysDictData::getLabel, dictData.getLabel())
            .eq(ObjectUtil.isNotNull(dictData.getStatus()), SysDictData::getStatus, dictData.getStatus())
            .orderByAsc(SysDictData::getSort);
        Page<SysDictData> page = baseMapper.selectPage(pageQuery.build(), lqw);
        return TableDataInfo.build(page);
    }

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictData 字典数据信息
     * @return 字典数据集合信息
     */
    @Override
    public List<SysDictData> selectDictDataList(SysDictData dictData) {
        return baseMapper.selectList(new LambdaQueryWrapper<SysDictData>()
            .eq(StringUtils.isNotBlank(dictData.getType()), SysDictData::getType, dictData.getType())
            .like(StringUtils.isNotBlank(dictData.getLabel()), SysDictData::getLabel, dictData.getLabel())
            .eq(ObjectUtil.isNotNull(dictData.getStatus()), SysDictData::getStatus, dictData.getStatus())
            .orderByAsc(SysDictData::getSort));
    }

    /**
     * 根据字典类型和字典键值查询字典数据信息
     *
     * @param dictType  字典类型
     * @param dictValue 字典键值
     * @return 字典标签
     */
    @Override
    public String selectDictLabel(String dictType, String dictValue) {
        return baseMapper.selectOne(new LambdaQueryWrapper<SysDictData>()
            .select(SysDictData::getLabel)
            .eq(SysDictData::getType, dictType)
            .eq(SysDictData::getValue, dictValue))
            .getLabel();
    }

    /**
     * 根据字典数据ID查询信息
     *
     * @param dictCode 字典数据ID
     * @return 字典数据
     */
    @Override
    public SysDictData selectDictDataById(Long dictCode) {
        return baseMapper.selectById(dictCode);
    }

    /**
     * 批量删除字典数据信息
     *
     * @param dictCodes 需要删除的字典数据ID
     */
    @Override
    public void deleteDictDataByIds(Long[] dictCodes) {
        for (Long dictCode : dictCodes) {
            SysDictData data = selectDictDataById(dictCode);
            baseMapper.deleteById(dictCode);
            CacheUtils.evict(CacheNames.SYS_DICT, data.getType());
        }
    }

    /**
     * 新增保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#data.type")
    @Override
    public List<SysDictData> insertDictData(SysDictData data) {
        int row = baseMapper.insert(data);
        if (row > 0) {
            return baseMapper.selectDictDataByType(data.getType());
        }
        throw new ServiceException("操作失败");
    }

    /**
     * 修改保存字典数据信息
     *
     * @param data 字典数据信息
     * @return 结果
     */
    @CachePut(cacheNames = CacheNames.SYS_DICT, key = "#data.type")
    @Override
    public List<SysDictData> updateDictData(SysDictData data) {
        int row = baseMapper.updateById(data);
        if (row > 0) {
            return baseMapper.selectDictDataByType(data.getType());
        }
        throw new ServiceException("操作失败");
    }

}
