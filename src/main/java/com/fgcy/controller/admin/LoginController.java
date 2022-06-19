package com.fgcy.controller.admin;

import cn.hutool.core.util.StrUtil;
import com.fgcy.pojo.User;
import com.fgcy.pojo.vo.BaseUser;
import com.fgcy.service.LoginService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @Author fgcy
 * @Date 2022/5/21
 */
@Controller
@RequestMapping("/admin")
public class LoginController {
    @Autowired
    LoginService loginService;



    @GetMapping({"/", "/login"})
    public String loginPage() {
        return "admin/login";
    }


    @PostMapping({"/", "/login"})
    public String login(User user, RedirectAttributes attributes, HttpServletResponse response, HttpServletRequest request) {
        String token = loginService.login(user);

        if (StrUtil.isBlank(token)) {
            attributes.addFlashAttribute("message", "请稍后重试");
        }

        Cookie cookie = new Cookie("token", token);
        cookie.setPath(request.getContextPath());
        response.addCookie(cookie);

        BaseUser userInfo = loginService.getUserInfoByToken(token);
        attributes.addFlashAttribute("user", userInfo);
        attributes.addFlashAttribute("token", token);

        return "redirect:/admin/login";
    }


    @GetMapping("/logout")
    public String logout(Model model) {
        loginService.logout();
        model.addAttribute("logout", "logout");
        return "admin/login";
    }

}



