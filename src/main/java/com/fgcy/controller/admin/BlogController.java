package com.fgcy.controller.admin;

import com.fgcy.pojo.Blog;
import com.fgcy.pojo.Type;
import com.fgcy.pojo.vo.ExtendBlog;
import com.fgcy.pojo.vo.TempBlog;
import com.fgcy.pojo.vo.TwoLables;
import com.fgcy.service.BlogService;
import com.fgcy.service.TypeService;
import com.fgcy.util.ResultData;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
@Controller
@RequestMapping("/admin")
public class BlogController {

    @Autowired
    private BlogService blogService;

    @Autowired
    TypeService typeService;

    /*
     *
     * @since: 1.8
     * @description：博客管理首页
     * @author: fgcy
     * @date: 2022/5/27
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/blogs")
    public String listBlog(Integer page, Model model) {
        TempBlog pageInfo = blogService.searchPageByConditions(page, null, null, null);
        List<Type> allTypes = typeService.getAllTypes();
        model.addAttribute("data", pageInfo);
        model.addAttribute("allTypes", allTypes);
        return "admin/blogs";
    }


    /*
     *
     * @since: 1.8
     * @description：博客查找
     * @author: fgcy
     * @date: 2022/6/14
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/blogs/search")
    public String search(Integer page, String title, Integer typeId, Boolean recommend, Model model) {
        TempBlog pageInfo = blogService.searchPageByConditions(page, title, typeId, recommend);
        model.addAttribute("data", pageInfo);
        return "admin/blogs :: blogList";
    }


    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/blogs/input")
    public String toBlogInput(Model model) {
        TwoLables twoLables = blogService.getAllTagsAndAllType();
        model.addAttribute("inf", twoLables);
        return "admin/blogs-input";
    }


    /*
     *
     * @since: 1.8
     * @description：发布博客
     * @author: fgcy
     * @date: 2022/6/14
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/blogs")
    public String postBlog(Blog blog, RedirectAttributes attributes) {
        ResultData data = blogService.addBlog(blog);
        attributes.addFlashAttribute("message", data.getMsg());
        return "redirect:/admin/blogs";
    }


    /*
     *
     * @since: 1.8
     * @description：跳转博客修改页面
     * @author: fgcy
     * @date: 2022/6/14
     */
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/blogs/{id}/input")
    public String toEditPage(@PathVariable Long id, Model model) {
        ExtendBlog blog = blogService.getBlogById(id);
        TwoLables twoLables = blogService.getAllTagsAndAllType();
        model.addAttribute("inf", twoLables);
        model.addAttribute("blog", blog);
        return "admin/blogs-input";
    }





    /*
     *
     * @since: 1.8
     * @description：博客修改
     * @author: fgcy
     * @date: 2022/6/11
     */

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/blogs/{id}")
    public String doUpdateBlog(Blog blog, RedirectAttributes attributes) {
        ResultData data = blogService.updateBlogById(blog);
        attributes.addFlashAttribute("message", data.getMsg());
        return "redirect:/admin/blogs";
    }


    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/blo/{id}")
    @ResponseBody
    public ExtendBlog tee(@PathVariable Long id) {
        ExtendBlog blog = blogService.getBlogById(id);
        return blog;
    }


    /*
     *
     * @since: 1.8
     * @description：博客逻辑删除
     * @author: fgcy
     * @date: 2022/6/11
     */

    @GetMapping("/blogs/{id}/delete")
    public String deleteBlogById(@PathVariable Long id, RedirectAttributes attributes) {
        ResultData data = blogService.deleteBlogsByBlogId(id);
        attributes.addFlashAttribute("message", data.getMsg());
        return "redirect:/admin/blogs";
    }

}
