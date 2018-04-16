package com.kaisheng.tms.controller;

import com.kaisheng.tms.dto.ResponseBean;
import com.kaisheng.tms.entity.Permission;
import com.kaisheng.tms.entity.RolesPermissionKey;
import com.kaisheng.tms.exception.ServiceException;
import com.kaisheng.tms.service.RolesPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

/**
 * 权限管理控制器
 * @author mh
 * @date 2018/4/13
 */
@Controller
@RequestMapping("/manage/permission")
public class PermissionController {

    @Autowired
    private RolesPermissionService rolesPermissionService;

    /**
     * 权限主页面
     * @date 2018/4/13
     * @param model
     * @return java.lang.String
     */
    @GetMapping
    public String home(Model model) {
        List<Permission> permissionList = rolesPermissionService.findAllPermission();
        model.addAttribute("permissionList", permissionList);
        return "/manage/permission/home";
    }

    /**
     * 新增权限
     * @date 2018/4/13
     * @param model
     * @return java.lang.String
     */
    @GetMapping("/new")
    public String newPermission(Model model) {
        List<Permission> permissionList = rolesPermissionService.findPermissionByPermissionType(Permission.MENU_TYPE);
        model.addAttribute("permissionList", permissionList);
        return "/manage/permission/new";
    }

    @PostMapping("/new")
    public String newPermission(Permission permission, RedirectAttributes redirectAttributes) {
        rolesPermissionService.savePermission(permission);
        redirectAttributes.addFlashAttribute("message", "保存成功");
        return "redirect:/manage/permission";
    }

    /**
     * 修改编辑权限
     * @date 2018/4/15
     * @param id, model
     * @return java.lang.String
     */
    @GetMapping("/{id:\\d+}/edit")
    public String editPermission(@PathVariable Integer id, Model model) {
        Permission permission = rolesPermissionService.findById(id);
        if (permission == null){
            throw new ServiceException();
        }
        model.addAttribute("permission", permission);
        return "/manage/permission/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String editPerssion(Permission permission,
                               RedirectAttributes redirectAttributes) {

        rolesPermissionService.updatePermissionById(permission);
        return "redirect:/manage/permission";
    }

    /**
     *  删除权限
     * @date 2018/4/15
     * @param id
     * @return com.kaisheng.tms.dto.ResponseBean
     */
    @GetMapping("/{id:\\d+}/del")
    public @ResponseBody ResponseBean deletePermission(@PathVariable Integer id) {
        try {
            rolesPermissionService.delPermissionById(id);
            return ResponseBean.success();
        } catch (ServiceException e) {
            return ResponseBean.error(e.getMessage());
        }
    }

}
