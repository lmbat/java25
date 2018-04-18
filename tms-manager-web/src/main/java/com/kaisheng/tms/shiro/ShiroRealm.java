package com.kaisheng.tms.shiro;

import com.kaisheng.tms.entity.Account;
import com.kaisheng.tms.entity.AccountLoginLog;
import com.kaisheng.tms.entity.Permission;
import com.kaisheng.tms.entity.Roles;
import com.kaisheng.tms.service.AccountService;
import com.kaisheng.tms.service.RolesPermissionService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

public class ShiroRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(ShiroRealm.class);

    @Autowired
    private AccountService accountService;
    @Autowired
    private RolesPermissionService rolesPermissionService;

    /**
     * 判断角色权限
     * @date 2018/4/17
     * @param principalCollection
     * @return org.apache.shiro.authz.AuthorizationInfo
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        Account account = (Account) principalCollection.getPrimaryPrincipal();

        List<Roles> rolesList = rolesPermissionService.findRolesByAccountId(account.getId());

        List<Permission> permissionList = new ArrayList<>();

        for(Roles roles : rolesList) {
            List<Permission> rolesPermissionList = rolesPermissionService.findAllPermissionByRolesId(roles.getId());
            permissionList.addAll(rolesPermissionList);
        }

        Set<String> rolesNameSet = new HashSet<>();
        for(Roles roles : rolesList) {
            rolesNameSet.add(roles.getRolesCode());
        }

        Set<String> permissionNameSet = new HashSet<>();
        for(Permission permission : permissionList) {
            permissionNameSet.add(permission.getPermissionCode());
        }

        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.setRoles(rolesNameSet);
        simpleAuthorizationInfo.setStringPermissions(permissionNameSet);

        return simpleAuthorizationInfo;
    }

    /**
     * 判断登录
     * @date 2018/4/17
     * @param authenticationToken
     * @return org.apache.shiro.authc.AuthenticationInfo
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken usernamePasswordToken = (UsernamePasswordToken) authenticationToken;
        String accountMobile = usernamePasswordToken.getUsername();

        if(accountMobile != null) {
            Account account =  accountService.findByMobile(accountMobile);
            if(account == null) {
                throw new UnknownAccountException("找不到账号:" + account);
            } else {
                if(Account.STATE_NORMAL.equals(account.getAccountState())) {
                    logger.info("登录成功 {} {}", account, usernamePasswordToken.getHost());

                    AccountLoginLog accountLoginLog = new AccountLoginLog();
                    accountLoginLog.setAccountId(account.getId());
                    accountLoginLog.setLoginTime(new Date());
                    accountLoginLog.setLoginIp(usernamePasswordToken.getHost());

                    accountService.saveAccountLogin(accountLoginLog);

                    return new SimpleAuthenticationInfo(account, account.getAccountPassword(), getName());
                } else {
                    throw new LockedAccountException("账号被禁用或锁定:" + account.getAccountState());
                }
            }
        }
        return null;
    }

}
