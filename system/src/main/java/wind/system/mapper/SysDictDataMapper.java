package wind.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import wind.common.constant.UserConstants;
import wind.common.core.domain.entity.SysDictData;
import wind.common.core.mapper.BaseMapperPlus;

import java.util.List;

/**
 * 字典表 数据层
 *
 * @author Yu Gaoheng
 */
public interface SysDictDataMapper extends BaseMapperPlus<SysDictDataMapper, SysDictData, SysDictData> {

    /**
     * 根据条件分页查询字典数据
     *
     * @param dictType 字典类型
     * @return 字典数据集合信息
     */
    default List<SysDictData> selectDictDataByType(String dictType) {
        return selectList(
            new LambdaQueryWrapper<SysDictData>()
                .eq(SysDictData::getStatus, UserConstants.DICT_NORMAL)
                .eq(SysDictData::getType, dictType)
                .orderByAsc(SysDictData::getSort));
    }

}
