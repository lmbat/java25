package com.kaisheng.tms.service.impl;

import com.kaisheng.tms.entity.*;
import com.kaisheng.tms.exception.ServiceException;
import com.kaisheng.tms.mapper.AccountLoginLogMapper;
import com.kaisheng.tms.mapper.AccountMapper;
import com.kaisheng.tms.mapper.AccountRolesMapper;
import com.kaisheng.tms.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 系统账号的业务类
 * @author mh
 * @date 2018/4/12
 */
@Service
public class AccountServiceImpl implements AccountService{

    private static final Logger logger = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private AccountMapper accountMapper;

    @Autowired
    private AccountLoginLogMapper accountLoginLogMapper;
    @Autowired
    private AccountRolesMapper accountRolesMapper;

    /**
     * @param accountMobile   手机号码
     * @param accountPassword 密码
     * @param requestIp       IP地址
     * @return com.kaisheng.tms.entity.Account 若登录成功,返回Account对象
     * @throws ServiceException 若登录失败,通过异常抛出具体的错误原因
     * @date 2018/4/12
     */
    @Override
    public Account login(String accountMobile, String accountPassword, String requestIp) throws ServiceException {
        AccountExample accountExample = new AccountExample();
        accountExample.createCriteria().andAccountMobileEqualTo(accountMobile);

        List<Account> accountList = accountMapper.selectByExample(accountExample);
        Account account = null;
        if(accountList != null && !accountList.isEmpty()) {
            account = accountList.get(0);
            System.out.println(account);
            System.out.println(DigestUtils.md5Hex(accountPassword));
            if(account.getAccountPassword().equals(DigestUtils.md5Hex(accountPassword))) {
                if(Account.STATE_NORMAL.equals(account.getAccountState())) {
                    AccountLoginLog loginLog = new AccountLoginLog();
                    loginLog.setAccountId(account.getId());
                    loginLog.setLoginIp(requestIp);
                    loginLog.setLoginTime(new Date());

                    accountLoginLogMapper.insertSelective(loginLog);
                    logger.info("{} 登录系统", account);
                    return account;
                } else if(Account.STATE_DISABLE.equals(account.getAccountState())) {
                    throw new ServiceException("账号被禁用");
                } else if(Account.STATE_LOCKED.equals(account.getAccountState())){
                    throw new ServiceException("账号被锁定");
                } else {
                    throw new ServiceException("状态未确定");
                }
            }else {
                throw new ServiceException("账号或密码错误");
            }
        } else {
            throw new ServiceException("账号或密码错误");
        }

    }

    /**
     * 根据URl传来查询参数,查询所有账号及对应的角色列表
     * @param requestParam
     * @return java.util.List<com.kaisheng.tms.entity.Account>
     * @date 2018/4/16
     */
    @Override
    public List<Account> findAllAccountWWithRolesByQueryParam(Map<String, Object> requestParam) {
        return null;
    }

    /**
     * 新增账号
     * @param account
     * @param rolesIds
     * @return void
     * @date 2018/4/16
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void saveAccount(Account account, Integer[] rolesIds) {
        account.setCreateTime(new Date());
        String password;
        if(account.getAccountMobile().length() <= 6) {
            password = account.getAccountMobile();
        } else {
            password = account.getAccountMobile().substring(6);
        }
        password = DigestUtils.md5Hex(password);

        account.setAccountPassword(password);

        account.setAccountState(Account.STATE_NORMAL);
        accountMapper.insertSelective(account);

        for(Integer roleId : rolesIds) {
            AccountRolesKey accountRolesKey = new AccountRolesKey();
            accountRolesKey.setAccountId(account.getId());
            accountRolesKey.setRolesId(roleId);

            accountRolesMapper.insert(accountRolesKey);
        }
    }

    /**
     * 根据主键查询账号
     * @param id
     * @return com.kaisheng.tms.entity.Account
     * @date 2018/4/16
     */
    @Override
    public Account findById(Integer id) {
        return accountMapper.selectByPrimaryKey(id);
    }

    /**
     * 修改账号
     * @param account
     * @param rolesIds
     * @return void
     * @date 2018/4/16
     */
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public void updateAccount(Account account, Integer[] rolesIds) {
        account.setUpdateTime(new Date());
        accountMapper.updateByPrimaryKeySelective(account);

        AccountRolesExample accountRolesExample = new AccountRolesExample();
        accountRolesExample.createCriteria().andAccountIdEqualTo(account.getId());
        accountRolesMapper.deleteByExample(accountRolesExample);

        if(rolesIds != null) {
            for (Integer rolesId : rolesIds) {
                AccountRolesKey accountRolesKey = new AccountRolesKey();
                accountRolesKey.setRolesId(rolesId);
                accountRolesKey.setAccountId(account.getId());
                accountRolesMapper.insertSelective(accountRolesKey);
            }
        }

        logger.info("修改账号 {}",account);
    }

    /**
     * 查询所有账号并加载对应的角色列表
     * @return java.util.List<com.kaisheng.tms.entity.Account>
     * @date 2018/4/16
     */
    @Override
    public List<Account> findAllAccountWithRoles() {
        return accountMapper.findAllWithRoles();
    }

}
