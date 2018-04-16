package com.kaisheng.tms.controller;

import com.google.common.collect.Maps;
import com.kaisheng.tms.controller.exception.NotFoundException;
import com.kaisheng.tms.dto.ResponseBean;
import com.kaisheng.tms.entity.Permission;
import com.kaisheng.tms.entity.Roles;
import com.kaisheng.tms.exception.ServiceException;
import com.kaisheng.tms.service.RolesPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Map;


/**
 * 角色管理控制器
 * @author mh
 * @date 2018/4/15
 */
@Controller
@RequestMapping("/manage/roles")
public class RolesController {

    @Autowired
    private RolesPermissionService rolesPermissionService;

    /**
     * 角色主页
     * @date 2018/4/15
     * @param
     * @return java.lang.String
     */
    @GetMapping
    public String home() {
        return "manage/roles/home";
    }

    /**
     * 新增角色
     * @date 2018/4/15
     * @param model
     * @return java.lang.String
     */
    @GetMapping("/new")
    public String newRoles(Model model) {
        model.addAttribute("permissionList", rolesPermissionService.findAllPermission());
        return "/manage/roles/new";
    }

    @PostMapping("/new")
    public String newRoles(Roles roles, RedirectAttributes redirectAttributes,
                           Integer[] permissionId) {
        rolesPermissionService.saveRoles(roles, permissionId);
        redirectAttributes.addFlashAttribute("message", "新增成功");
        return "redirect:/manage/roles";
    }

    /**
     * 根据ID删除角色
     * @date 2018/4/16
     * @param id
     * @return com.kaisheng.tms.dto.ResponseBean
     */
    @GetMapping("/{id:\\d+}/del")
    public @ResponseBody ResponseBean delRoles(@PathVariable Integer id) {
        try {
            rolesPermissionService.delRolesById(id);
            return ResponseBean.success();
        } catch (ServiceException e) {
            return ResponseBean.error(e.getMessage());
        }
    }

    /**
     * 修改编辑角色
     * @date 2018/4/16
     * @param id, model
     * @return java.lang.String
     */
    @GetMapping("/{id:\\d+}/edit")
    public String editRoles(@PathVariable Integer id, Model model) {
        Roles roles = rolesPermissionService.findRolesWithPermissionById(id);

        if(roles == null) {
            throw new NotFoundException();
        }

        List<Permission> permissionList = rolesPermissionService.findAllPermission();

        Map<Permission, Boolean> map = checkdPermissionList(roles.getPermissionList(),permissionList);

        model.addAttribute("roles",roles);
        model.addAttribute("permissionMap",map);
        return "manage/roles/edit";
    }

    private Map<Permission,Boolean> checkdPermissionList(List<Permission> rolesPermissionList, List<Permission> permissionList) {

        Map<Permission,Boolean> resultMap = Maps.newLinkedHashMap();

        for(Permission permission : permissionList) {
            boolean flag = false;
            for(Permission rolesPermission : rolesPermissionList) {
                if(permission.getId().equals(rolesPermission.getId())) {
                    flag = true;
                    break;
                }
            }
            resultMap.put(permission,flag);
        }
        return resultMap;
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editRoles(Roles roles,Integer[] permissionId,
                            RedirectAttributes redirectAttributes) {
        rolesPermissionService.updateRoles(roles,permissionId);

        redirectAttributes.addFlashAttribute("message","角色修改成功");
        return "redirect:/manage/roles";
    }

}
