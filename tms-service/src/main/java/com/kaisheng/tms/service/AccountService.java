package com.kaisheng.tms.service;

import com.kaisheng.tms.entity.Account;
import com.kaisheng.tms.exception.ServiceException;

import java.util.List;
import java.util.Map;

/**   
 * 系统账号的业务类
 * @author mh  
 * @date 2018/4/12
 */ 
public interface AccountService {

   /**
    * 系统登录
    * @date 2018/4/12
    * @param accountMobile 手机号码
    * @param accountPassword 密码
    * @param requestIp  IP地址
    * @return com.kaisheng.tms.entity.Account 若登录成功,返回Account对象
    * @throws ServiceException 若登录失败,通过异常抛出具体的错误原因
    */
    Account login(String accountMobile, String accountPassword, String requestIp) throws ServiceException;


    /**  
     * 根据URl传来查询参数,查询所有账号及对应的角色列表
     * @date 2018/4/16
     * @param requestParam
     * @return java.util.List<com.kaisheng.tms.entity.Account>
     */
    List<Account> findAllAccountWWithRolesByQueryParam(Map<String, Object> requestParam);

    /**
     * 新增账号
     * @date 2018/4/16
     * @param account, rolesIds
     * @return void
     */
    void saveAccount(Account account, Integer[] rolesIds);

    /**
     * 根据主键查询账号
     * @date 2018/4/16
     * @param
     * @return com.kaisheng.tms.entity.Account
     */
    Account findById(Integer id);

    /**
     * 修改账号
     * @date 2018/4/16
     * @param account, rolesIds
     * @return void
     */
    void updateAccount(Account account, Integer[] rolesIds);

    /**
     * 查询所有账号并加载对应的角色列表
     * @date 2018/4/16
     * @param
     * @return java.util.List<com.kaisheng.tms.entity.Account>
     */
    List<Account> findAllAccountWithRoles();
}
