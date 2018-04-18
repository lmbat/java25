package com.kaisheng.tms.shiro;

import com.kaisheng.tms.entity.Permission;
import com.kaisheng.tms.service.RolesPermissionService;
import org.apache.shiro.config.Ini;
import org.apache.shiro.web.filter.mgt.DefaultFilterChainManager;
import org.apache.shiro.web.filter.mgt.PathMatchingFilterChainResolver;
import org.apache.shiro.web.servlet.AbstractShiroFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;

/**  
 *  动态定义权限和URL关系
 * @author Administrator
 * @date 2018/4/18
 * @param 
 * @return 
 */
public class CustomerFilterChainDefinition {

    Logger logger = LoggerFactory.getLogger(CustomerFilterChainDefinition.class);

    @Autowired
    private RolesPermissionService rolesPermissionService;

    private String filterChainDefinitions;
    private AbstractShiroFilter shiroFilter;


    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    public void setShiroFilter(AbstractShiroFilter shiroFilter) {
        this.shiroFilter = shiroFilter;
    }

    @PostConstruct
    public synchronized void init() {
        logger.info("初始化URL权限");
        getFilterChainManager().getFilterChains().clear();
        load();
        logger.info("初始化URL权限结束");
    }

    public synchronized void updateUrlPermission() {
        logger.info("刷新URL权限");
        getFilterChainManager().getFilterChains().clear();
        load();
        logger.info("刷新URL权限结束");
    }

    public synchronized void load() {
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);

        List<Permission> permissionList = rolesPermissionService.findAllPermission();
        Ini.Section section = ini.get(Ini.DEFAULT_SECTION_NAME);

        for(Permission permission : permissionList) {
            section.put(permission.getUrl(),"perms["+permission.getPermissionCode()+"]");
        }
        section.put("/**","user");

        DefaultFilterChainManager defaultFilterChainManager = getFilterChainManager();
        for(Map.Entry<String,String> entry : section.entrySet()) {
            defaultFilterChainManager.createChain(entry.getKey(),entry.getValue());
        }
    }

    private DefaultFilterChainManager getFilterChainManager() {
        PathMatchingFilterChainResolver pathMatchingFilterChainResolver = (PathMatchingFilterChainResolver) shiroFilter.getFilterChainResolver();
        DefaultFilterChainManager defaultFilterChainManager = (DefaultFilterChainManager) pathMatchingFilterChainResolver.getFilterChainManager();
        return defaultFilterChainManager;
    }

}
