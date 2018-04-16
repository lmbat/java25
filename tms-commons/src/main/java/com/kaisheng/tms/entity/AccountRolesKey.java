package com.kaisheng.tms.entity;

import java.io.Serializable;

/**
 * 账户,权限关联关系实体类
 * @author mh
 * @date 2018/4/13
 */
public class AccountRolesKey implements Serializable {
    private Integer accountId;

    private Integer rolesId;

    private static final long serialVersionUID = 1L;

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    public Integer getRolesId() {
        return rolesId;
    }

    public void setRolesId(Integer rolesId) {
        this.rolesId = rolesId;
    }
}