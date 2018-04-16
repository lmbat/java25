package com.kaisheng.tms.controller;

import com.kaisheng.tms.entity.Account;
import com.kaisheng.tms.exception.ServiceException;
import com.kaisheng.tms.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;

/**
 * 首页,登录,退出控制器
 * @author mh
 * @date 2018/4/12
 */
@Controller
public class HomeController {

    @Autowired
    private AccountService accountService;

    /**
     * 系统登录页面
     * @date 2018/4/12
     * @param
     * @return java.lang.String
     */
    @GetMapping("/")
    public String index() {
        return "index";
    }

    /**
     * 系统登录
     * @date 2018/4/12
     * @param accountMobile, accountPassword, request, session, redirectAttributes
     * @return java.lang.String
     */
    @PostMapping("/")
    public String login(String accountMobile,
                        String accountPassword,
                        HttpServletRequest request,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        //获取登录的IP地址
        String requestIp = request.getRemoteAddr();
        try {
            Account account = accountService.login(accountMobile, accountPassword, requestIp);
            session.setAttribute("current_Account:", account);
            return "redirect:/home";
        } catch (ServiceException e) {
            redirectAttributes.addFlashAttribute("mobile", accountMobile);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/";
        }
    }

    /**
     * 登陆后叶页面
     * @date 2018/4/12
     * @param
     * @return java.lang.String
     */
    @GetMapping("/home")
    public String home() {
        return "home";
    }


}
