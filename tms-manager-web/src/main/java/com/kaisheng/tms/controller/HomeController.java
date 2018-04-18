package com.kaisheng.tms.controller;

import com.kaisheng.tms.entity.Account;
import com.kaisheng.tms.exception.ServiceException;
import com.kaisheng.tms.service.AccountService;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServlet;
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
   /* @GetMapping("/")
    public String index() {
        return "index";
    }*/

    @GetMapping("/")
    public String index() {
        Subject subject = SecurityUtils.getSubject();

        if(subject.isAuthenticated()) {
            subject.logout();
        }

        if(subject.isRemembered()) {
            return "redirect:/home";
        }


        return "index";
    }

    /**
     * 系统登录
     * @date 2018/4/12
     * @param accountMobile, accountPassword, request, session, redirectAttributes
     * @return java.lang.String
     */
    /*@PostMapping("/")
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
    }*/

    @PostMapping("/")
    public String login(String accountMobile,
                        String accountPassword,
                        HttpServletRequest request,
                        RedirectAttributes redirectAttributes,
                        String rememberMe) {

        String requestIp = request.getRemoteAddr();

        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(accountMobile,
                DigestUtils.md5Hex(accountPassword), rememberMe != null, requestIp);

        try {
            subject.login(usernamePasswordToken);

            //将登录成功的对象放入session（没必要）
            Account account = accountService.findByMobile(accountMobile);
            Session session = subject.getSession();
            session.setAttribute("curr_account",account);

            SavedRequest savedRequest = WebUtils.getSavedRequest(request);
            String url = "/home";
            if(savedRequest != null) {
                url = savedRequest.getRequestUrl();
            }

            return "redirect:"+url;

        } catch(UnknownAccountException | IncorrectCredentialsException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "账号或密码错误");
        } catch(LockedAccountException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "账号锁定");
        } catch(AuthenticationException e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("message", "账号或密码错误");
        }

        return "redirect:/";

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



    /**
     * 无权限异常 401
     * @date 2018/4/17
     * @param
     * @return java.lang.String
     */
    @GetMapping("/401")
    public String unauthorizedUrl() {
        return "error/401";
    }


    /**
     * 安全退出
     * @date 2018/4/17
     * @param redirectAttributes
     * @return java.lang.String
     */
/*
    @GetMapping("/logout")
    public String logout(RedirectAttributes redirectAttributes) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        redirectAttributes.addFlashAttribute("message","安全退出系统");
        return "redirect:/";
    }
*/

}
