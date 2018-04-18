package com.kaisheng.tms.shiro;

import com.kaisheng.tms.entity.Permission;
import com.kaisheng.tms.service.RolesPermissionService;
import org.apache.shiro.config.Ini;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**  
 *  动态定义权限和URL关系
 * @date 2018/4/18
 * @param 
 * @return 
 */
public class CustomerFilterChainDefinition_method1 implements FactoryBean<Ini.Section> {

    @Autowired
    private RolesPermissionService rolesPermissionService;

    private String filterChainDefinitions;

    public void setFilterChainDefinitions(String filterChainDefinitions) {
        this.filterChainDefinitions = filterChainDefinitions;
    }

    @Override
    public Ini.Section getObject() throws Exception {
        Ini ini = new Ini();
        ini.load(filterChainDefinitions);

        List<Permission> permissionList = rolesPermissionService.findAllPermission();
        Ini.Section section = ini.get(Ini.DEFAULT_SECTION_NAME);
        for(Permission permission : permissionList) {
            section.put(permission.getUrl(), "perms[" + permission.getPermissionCode() + "]");
        }
        section.put("/**", "user");

        return section;
    }

    @Override
    public Class<?> getObjectType() {
        return Ini.Section.class;
    }

    @Override
    public boolean isSingleton() {
        return true;
    }
}
