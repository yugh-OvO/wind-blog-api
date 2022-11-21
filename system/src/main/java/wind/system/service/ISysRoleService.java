package wind.system.service;

import wind.common.core.domain.OptionEntity;
import wind.common.core.domain.PageQuery;
import wind.common.core.page.TableDataInfo;
import wind.system.domain.SysRole;

import java.util.List;
import java.util.Set;

/**
 * 角色业务层
 *
 * @author Yu Gaoheng
 */
public interface ISysRoleService {


    /**
     * 根据条件分页查询角色数据
     *
     * @param role      角色信息
     * @param pageQuery 分页对象
     * @return 角色数据集合信息
     */
    TableDataInfo<SysRole> selectPageRoleList(SysRole role, PageQuery pageQuery);

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<SysRole> selectRolesByUserId(Integer userId);

    /**
     * 根据用户ID查询角色权限
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    Set<String> selectRolePermissionByUserId(Integer userId);

    /**
     * 查询所有角色
     *
     * @return 角色列表
     */
    List<SysRole> selectRoleAll();

    /**
     * 通过角色ID查询角色
     *
     * @param roleId 角色ID
     * @return 角色对象信息
     */
    SysRole selectRoleById(Integer roleId);

    /**
     * 校验角色名称是否唯一
     *
     * @param role 角色信息
     * @return 结果
     */
    String checkRoleNameUnique(SysRole role);

    /**
     * 校验角色是否允许操作
     *
     * @param role 角色信息
     */
    void checkRoleAllowed(SysRole role);

    /**
     * 通过角色ID查询角色使用数量
     *
     * @param roleId 角色ID
     * @return 结果
     */
    long countUserRoleByRoleId(Integer roleId);

    /**
     * 新增保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int insertRole(SysRole role);

    /**
     * 修改保存角色信息
     *
     * @param role 角色信息
     * @return 结果
     */
    int updateRole(SysRole role);

    /**
     * 修改角色状态
     *
     * @param role 角色信息
     * @return 结果
     */
    int updateRoleStatus(SysRole role);

    /**
     * 批量删除角色信息
     *
     * @param roleIds 需要删除的角色ID
     * @return 结果
     */
    int deleteRoleByIds(Integer[] roleIds);

    /**
     * 查询角色选项
     *
     * @return 角色选项数据
     */
    List<OptionEntity> getOptions();

}


