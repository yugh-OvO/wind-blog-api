package wind.system.mapper;

import wind.common.core.mapper.BaseMapperPlus;
import wind.system.domain.SysRole;

import java.util.List;

/**
 * 角色表 数据层
 *
 * @author Yu Gaoheng
 */
public interface SysRoleMapper extends BaseMapperPlus<SysRoleMapper, SysRole, SysRole> {

    /**
     * 根据用户ID查询角色
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolePermissionByUserId(Integer userId);

}
