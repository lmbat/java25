package com.kaisheng.tms.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统账户登录日志实体类
 * @author mh
 * @date 2018/4/12
 */
public class AccountLoginLog implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 账户登录的IP地址
     */
    private String loginIp;

    /**
     * 账户登录时间
     */
    private Date loginTime;

    private Integer accountId;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Integer getAccountId() {
        return accountId;
    }

    public void setAccountId(Integer accountId) {
        this.accountId = accountId;
    }

    @Override
    public String toString() {
        return "AccountLoginLog{" +
                "id=" + id +
                ", loginIp='" + loginIp + '\'' +
                ", loginTime=" + loginTime +
                ", accountId=" + accountId +
                '}';
    }

}