package com.fgcy.controller;

import cn.hutool.core.util.StrUtil;
import com.fgcy.pojo.Blog;
import com.fgcy.pojo.User;

import com.fgcy.pojo.vo.BaseUser;
import com.fgcy.pojo.vo.ExtendBlog;

import com.fgcy.pojo.vo.TwoLables;
import com.fgcy.service.BlogService;
import com.fgcy.service.LoginService;
import com.fgcy.service.UserService;

import com.github.pagehelper.PageInfo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
@Controller
public class IndexController {
    @Autowired
    BlogService blogService;

    @Autowired
    private LoginService loginService;

    @Autowired
    private UserService userService;


    @GetMapping("/register")
    public String toRegister() {
        return "register";
    }


    @PostMapping("/register")
    public String register(String username, String password, String imgId, String code) {
        System.out.println(username + "," + password + "," + imgId + "," + code);
        loginService.doRegister(username, password, imgId, code);
        return "redirect:/user/login";
    }


    @RequestMapping("/createCode")
    public void createCode(HttpServletResponse response, String codeId, Integer width, Integer height) {
        loginService.createCode(width, height, codeId, response);
    }


    @PostMapping("validateName")
    @ResponseBody
    public boolean validateUserName(String username) {
        return userService.checkUsername(username);
    }




    @GetMapping("/user/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, RedirectAttributes attributes) {
        List<Cookie> collect = Arrays.stream(request.getCookies()).filter(cookie -> "token".equals(cookie.getName())).collect(Collectors.toList());
        Cookie cookie = collect.get(0);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        loginService.logout();

        String s = "logout";
        attributes.addFlashAttribute("logout", s);
        attributes.addFlashAttribute("token", "");
        return "redirect:/user/login";
    }

    @GetMapping("/user/login")
    public String toLoginPage() {
        return "login";
    }


    @PostMapping("/user/login")
    public String login(User user, RedirectAttributes attributes, HttpServletRequest request, HttpServletResponse response) {
        String token = loginService.login(user);

        if (StrUtil.isBlank(token)) {
            attributes.addFlashAttribute("message", "请稍后重试");
            return "redirect:/user/login";
        }
        Cookie cookie = new Cookie("token", token);
        cookie.setPath("/");
        response.addCookie(cookie);

        BaseUser userInfo = loginService.getUserInfoByToken(token);
        attributes.addFlashAttribute("user", userInfo);
        attributes.addFlashAttribute("token", token);

        return "redirect:/user/login";
    }


    /*
     *
     * @since: 1.8
     * @description：首页
     * @author: fgcy
     * @date: 2022/6/11
     */
    @GetMapping({"/blog"})
    public String toBlog(Integer page, Model model) {
        PageInfo<ExtendBlog> pageInfo = blogService.getIndexPage(page);
        model.addAttribute("page", pageInfo);
        TwoLables twoLables = blogService.getAllTagsAndAllType();
        model.addAttribute("twoLables", twoLables);
        List<ExtendBlog> latestBlogs = blogService.getLatestBlog(10);
        model.addAttribute("latestBlog", latestBlogs);
        return "index";
    }

    @GetMapping("/extendPage")
    @ResponseBody
    public PageInfo<ExtendBlog> JJJ(Integer page) {
        return blogService.getIndexPage(page);
    }

    @GetMapping("/")
    public String toWelcomePage(Model model) {
        Map<String, List<Blog>> map = blogService.archiveBlog();
        model.addAttribute("archiveMap", map);
        Long count = blogService.getAllBlogsCount();
        model.addAttribute("blogCount", count);
        return "welcome";
    }


    @GetMapping("/admin")
    public String toAdminIndex() {
        return "admin/login";
    }


    @GetMapping("/search")
    public String search(String query, Model model, Integer page) {
        if (query == null || "".equals(query)) return "redirect:/blog";
        PageInfo<ExtendBlog> pageInfo = blogService.searchBlogPageByQueryContent(query, page);
        model.addAttribute("query", query);
        model.addAttribute("page", pageInfo);
        return "search";
    }


    /*
     *
     * @since: 1.8
     * @description：查看博客详情【查看加一】
     * @author: fgcy
     * @date: 2022/5/29
     */
    @GetMapping("/blog/{id}")
    public String toBlogDetail(@PathVariable Long id, Model model, HttpServletRequest request) {
        ExtendBlog extendBlog = blogService.getDetailBlogById(id, request);
        model.addAttribute("blog", extendBlog);
        return "blog";
    }


    /*
     *
     * @since: 1.8
     * @description：判断是否有收藏
     * @author: fgcy
     * @date: 2022/6/12
     */
    @ResponseBody
    @GetMapping("/collection")
    public boolean getIsUserCollection(Long userId, Long blogId) {
        return blogService.checkBlogCollection(userId, blogId);
    }


    @ResponseBody
    @PostMapping("/collection")
    public boolean doUserCollection(Long userId, Long blogId) {
        return blogService.doUserCollection(userId, blogId);
    }
}
