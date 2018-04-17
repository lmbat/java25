package com.kaisheng.tms.service;

import com.kaisheng.tms.entity.Permission;
import com.kaisheng.tms.entity.Roles;
import com.kaisheng.tms.exception.ServiceException;

import java.util.List;

/**
 * 角色,权限的业务类
 * @author mh
 * @date 2018/4/13
 */
public interface RolesPermissionService {

    /**
     * 查询所有权限
     * @date 2018/4/13
     * @param
     * @return java.util.List<com.kaisheng.tms.entity.Permission>
     */
    List<Permission> findAllPermission();

    /**
     * 保存权限
     * @date 2018/4/13
     * @param permission
     * @return void
     */
    void savePermission(Permission permission);

    /**
     * 根据权限类型查询权限列表
     * @date 2018/4/13
     * @param permissionType
     * @return java.util.List<com.kaisheng.tms.entity.Permission>
     */
    List<Permission> findPermissionByPermissionType(String permissionType);

    /**
     * 编辑权限
     * @date 2018/4/15
     * @param permission
     * @return void
     */
    void updatePermissionById(Permission permission);

    /**
     * ID查询
     * @date 2018/4/15
     * @param id
     * @return com.kaisheng.tms.entity.Permission
     */
    Permission findById(Integer id);

    /**
     * 根据ID删除权限
     * @date 2018/4/15
     * @param id
     * @return void
     */
    void delPermissionById(Integer id) throws ServiceException;

    /**
     * 新增角色
     * @date 2018/4/15
     * @param roles, permissionId
     * @return void
     */
    void saveRoles(Roles roles, Integer[] permissionId);

    /**
     * 根据ID删除角色
     * @date 2018/4/16
     * @param id
     * @return void
     */
    void delRolesById(Integer id) throws ServiceException;

    /**
     * 查询所有角色
     * @date 2018/4/16
     * @param
     * @return java.util.List<com.kaisheng.tms.entity.Roles>
     */
    List<Roles> findAllRoles();

    /**
     * 根据角色ID查询角色对象及其拥有的权限
     * @date 2018/4/16
     * @param id
     * @return com.kaisheng.tms.entity.Roles
     */
    Roles findRolesWithPermissionById(Integer id);

    /**
     * 修改角色
     * @date 2018/4/16
     * @param roles, permissionId
     * @return void
     */
    void updateRoles(Roles roles, Integer[] permissionId);

    /**
     * 根据账号Id查询拥有的角色集合
     * @date 2018/4/16
     * @param id
     * @return java.util.List<com.kaisheng.tms.entity.Roles>
     */
    List<Roles> findRolesByAccountId(Integer id);

    /**
     * 查询所有的角色并加载角色拥有的权限列表
     * @date 2018/4/17
     * @param
     * @return java.lang.Object
     */
    List<Roles> findAllRolesWithPermission();


}
