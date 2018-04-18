package com.kaisheng.tms.controller;

import com.kaisheng.tms.controller.exception.NotFoundException;
import com.kaisheng.tms.entity.Account;
import com.kaisheng.tms.entity.Roles;
import com.kaisheng.tms.service.AccountService;
import com.kaisheng.tms.service.RolesPermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账号管理器控制器
 * @author mh
 * @date 2018/4/16
 */
@Controller
@RequestMapping("/manage/account")
public class AccountController {

    @Autowired
    private RolesPermissionService rolespermissionService;
    @Autowired
    private AccountService accountService;

    /**
     * 账号主页
     * @date 2018/4/16
     * @param model, rolesId, nameOrMobile
     * @return java.lang.String
     */
    @GetMapping
    public String home(Model model,
                       @RequestParam(required = false) Integer rolesId,
                       @RequestParam(required = false) String nameMobile) {

        Map<String, Object> requestParam = new HashMap<>();
        requestParam.put("rolesId", rolesId);
        requestParam.put("nameMobile", nameMobile);

        model.addAttribute("rolesList", rolespermissionService.findAllRoles());
        model.addAttribute("accountList", accountService.findAllAccountWWithRolesByQueryParam(requestParam));

        return "/manage/account/home";
    }

    /**
     * 新增账号
     * @date 2018/4/16
     * @param model
     * @return java.lang.String
     */
    @GetMapping("/new")
    public String newAccount(Model model) {
        List<Roles> rolesList = rolespermissionService.findAllRoles();

        model.addAttribute("rolesList",rolesList);
        return "manage/account/new";
    }

    @PostMapping("/new")
    public String newAccount(Account account, Integer[] rolesIds) {
        accountService.saveAccount(account,rolesIds);
        return "redirect:/manage/account";
    }

    /**
     * 修改账号
     * @date 2018/4/16
     * @param id, model
     * @return java.lang.String
     */
    @GetMapping("/{id:\\d+}/edit")
    public String updateAccount(@PathVariable Integer id, Model model) {
        Account account = accountService.findById(id);
        if(account == null) {
            throw new NotFoundException();
        }

        List<Roles> rolesList = rolespermissionService.findAllRoles();
        List<Roles> accountRolesList = rolespermissionService.findRolesByAccountId(id);

        model.addAttribute("accountRolesList",accountRolesList);
        model.addAttribute("rolesList",rolesList);
        model.addAttribute("account",account);
        return "manage/account/edit";
    }

    @PostMapping("/{id:\\d+}/edit")
    public String updateAccount(Account account, Integer[] rolesIds, RedirectAttributes redirectAttributes) {
        accountService.updateAccount(account,rolesIds);
        redirectAttributes.addFlashAttribute("message","修改账号成功");
        return "redirect:/manage/account";
    }

}
