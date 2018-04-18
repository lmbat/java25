package com.kaisheng.tms.service.impl;

import com.google.common.collect.Collections2;
import com.google.common.collect.Lists;
import com.kaisheng.tms.entity.*;
import com.kaisheng.tms.exception.ServiceException;
import com.kaisheng.tms.mapper.AccountRolesMapper;
import com.kaisheng.tms.mapper.PermissionMapper;
import com.kaisheng.tms.mapper.RolesMapper;
import com.kaisheng.tms.mapper.RolesPermissionMapper;
import com.kaisheng.tms.service.RolesPermissionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 角色,权限的业务类
 * @author mh
 * @date 2018/4/13
 */
@Service
public class RolesPermissionServiceImpl implements RolesPermissionService {

    public static final Logger logger = LoggerFactory.getLogger(RolesPermissionServiceImpl.class);

    @Autowired
    private PermissionMapper permissionMapper;
    @Autowired
    private RolesPermissionMapper rolesPermissionMapper;
    @Autowired
    private RolesMapper rolesMapper;
    @Autowired
    private AccountRolesMapper accountRolesMapper;

    /**
     * 查询所有权限
     * @date 2018/4/13
     * @param
     * @return java.util.List<com.kaisheng.tms.entity.Permission>
     */
    @Override
    public List<Permission> findAllPermission() {
        PermissionExample permissionExample = new PermissionExample();
        List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);
        List<Permission> resultList = new ArrayList<>();
        treeList(permissionList,resultList,0);
        return resultList;
    }

    private void treeList(List<Permission> sourceList, List<Permission> endList, int parentId) {
        List<Permission> tempList = Lists.newArrayList(Collections2.filter(sourceList, permission -> permission.getParentId().equals(parentId)));

        for(Permission permission : tempList) {
            endList.add(permission);
            treeList(sourceList,endList,permission.getId());
        }
    }

    /**
     * 保存权限
     * @date 2018/4/13
     * @param permission
     * @return void
     */
    @Override
    public void savePermission(Permission permission) {
        permission.setCreateTime(new Date());
        permissionMapper.insertSelective(permission);
        logger.info("添加成功 {}", permission);
    }

    /**
     * 根据权限类型查询权限列表
     * @date 2018/4/16
     * @param permissionType
     * @return java.util.List<com.kaisheng.tms.entity.Permission>
     */
    @Override
    public List<Permission> findPermissionByPermissionType(String permissionType) {
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andPermissionTypeEqualTo(permissionType);
        return permissionMapper.selectByExample(permissionExample);
    }

    /**
     * 编辑权限
     * @date 2018/4/15
     * @param permission
     * @return void
     */
    @Override
    public void updatePermissionById(Permission permission) {
        permission.setUpdateTime(new Date());
        permissionMapper.updateByPrimaryKeySelective(permission);
        logger.info("修改权限 {}", permission);
    }

    /**
     * ID查询
     * @date 2018/4/15
     * @param id
     * @return com.kaisheng.tms.entity.Permission
     */
    @Override
    public Permission findById(Integer id) {
        return permissionMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据ID删除权限
     * @date 2018/4/15
     * @param id
     * @return void
     */
    @Override
    public void delPermissionById(Integer id) throws ServiceException{
        PermissionExample permissionExample = new PermissionExample();
        permissionExample.createCriteria().andParentIdEqualTo(id);

        List<Permission> permissionList = permissionMapper.selectByExample(permissionExample);
        if(permissionList != null && !permissionList.isEmpty()) {
            throw new ServiceException("权限有子节点,无法删除");
        }

        RolesPermissionExample rolesPermissionExample = new RolesPermissionExample();
        rolesPermissionExample.createCriteria().andPermissionIdEqualTo(id);

        List<RolesPermissionKey> rolesPermissionKeyList = rolesPermissionMapper.selectByExample(rolesPermissionExample);
        if(rolesPermissionKeyList != null && !rolesPermissionKeyList.isEmpty()) {
            throw new ServiceException("权限有角色使用,无法删除");
        }
        Permission permission = permissionMapper.selectByPrimaryKey(id);
        permissionMapper.deleteByPrimaryKey(id);
        logger.info("删除权限 {}",permission);
    }

    /**
     * 新增角色
     * @date 2018/4/15
     * @param roles, permissionId
     * @return void
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveRoles(Roles roles, Integer[] permissionId) {
        roles.setCreateTime(new Date());
        rolesMapper.insertSelective(roles);

        for(Integer perId : permissionId) {
            RolesPermissionKey rolesPermissionKey = new RolesPermissionKey();
            rolesPermissionKey.setPermissionId(perId);
            rolesPermissionKey.setRolesId(roles.getId());

            rolesPermissionMapper.insert(rolesPermissionKey);
        }
        logger.info("保存角色 {}",roles);
    }

    /**
     * 根据ID删除角色
     * @param id
     * @return void
     * @date 2018/4/16
     */
    @Override
    public void delRolesById(Integer id) throws ServiceException {
        AccountRolesExample accountRolesExample = new AccountRolesExample();
        accountRolesExample.createCriteria().andAccountIdEqualTo(id);

        List<AccountRolesKey> accountRolesKeys = accountRolesMapper.selectByExample(accountRolesExample);
        if(accountRolesKeys != null && !accountRolesKeys.isEmpty()) {
            throw new ServiceException("该角色存在账号引用关系,无法删除");
        }

        RolesPermissionExample rolesPermissionExample = new RolesPermissionExample();
        rolesPermissionExample.createCriteria().andPermissionIdEqualTo(id);
        rolesPermissionMapper.deleteByExample(rolesPermissionExample);

        Roles roles = rolesMapper.selectByPrimaryKey(id);
        rolesMapper.deleteByPrimaryKey(id);
        logger.info("删除角色 {}", roles);
    }

    /**
     * 查询所有角色
     * @return java.util.List<com.kaisheng.tms.entity.Roles>
     * @date 2018/4/16
     */
    @Override
    public List<Roles> findAllRoles() {
        RolesExample rolesExample = new RolesExample();
        return rolesMapper.selectByExample(rolesExample);
    }

    /**
     * 根据角色ID查询角色对象及其拥有的权限
     * @param id
     * @return com.kaisheng.tms.entity.Roles
     * @date 2018/4/16
     */
    @Override
    public Roles findRolesWithPermissionById(Integer id) {
        return rolesMapper.findByIdWithPermission(id);
    }

    /**
     * 修改角色
     * @param roles
     * @param permissionId
     * @return void
     * @date 2018/4/16
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateRoles(Roles roles, Integer[] permissionId) {

        RolesPermissionExample rolesPermissionExample = new RolesPermissionExample();
        rolesPermissionExample.createCriteria().andRolesIdEqualTo(roles.getId());

        rolesPermissionMapper.deleteByExample(rolesPermissionExample);
        for(Integer perId : permissionId) {
            RolesPermissionKey rolesPermissionKey = new RolesPermissionKey();
            rolesPermissionKey.setRolesId(roles.getId());
            rolesPermissionKey.setPermissionId(perId);
            rolesPermissionMapper.insert(rolesPermissionKey);
        }
        rolesMapper.updateByPrimaryKeySelective(roles);

        logger.info("修改角色 {}",roles);
    }

    /**
     * 根据账号Id查询拥有的角色集合
     * @param id
     * @return java.util.List<com.kaisheng.tms.entity.Roles>
     * @date 2018/4/16
     */
    @Override
    public List<Roles> findRolesByAccountId(Integer id) {
        return rolesMapper.findRolesByAccountId(id);
    }

    /**
     * 查询所有的角色并加载角色拥有的权限列表
     * @return java.lang.Object
     * @date 2018/4/17
     */
    @Override
    public List<Roles> findAllRolesWithPermission() {
        return rolesMapper.findAllWithPermission();
    }

    /**
     * 根据角色ID查询权限
     * @param rolesId
     * @return java.util.List<com.kaisheng.tms.entity.Permission>
     * @date 2018/4/18
     */
    @Override
    public List<Permission> findAllPermissionByRolesId(Integer rolesId) {
        return permissionMapper.findAllPermissionByRolesId(rolesId);
    }

}
