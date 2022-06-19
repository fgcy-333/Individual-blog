package com.fgcy.controller;

import com.fgcy.pojo.Tag;
import com.fgcy.pojo.Type;
import com.fgcy.pojo.vo.ExtendBlog;
import com.fgcy.service.BlogService;
import com.fgcy.service.TagService;
import com.fgcy.service.TypeService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/24
 */
@Controller
public class TypeShowController {

    @Autowired
    TypeService typeService;
    @Autowired
    BlogService blogService;
    @Autowired
    TagService tagService;


    @GetMapping("/types/{id}")
    public String type(@PathVariable Long id, Model model, Integer page) {
        List<Type> types = typeService.getAllTypes();
        if (id == -1) id = Long.valueOf(types.get(0).getId());
        PageInfo<ExtendBlog> blog = blogService.getBlogPageByTypeId(id, page);
        model.addAttribute("types", types);
        model.addAttribute("page", blog);
        model.addAttribute("activeTypeId", id);
        return "btypes";
    }

    @ResponseBody
    @GetMapping("/lmk/{id}")
    public PageInfo<ExtendBlog> asdf(@PathVariable Long id) {
        PageInfo<ExtendBlog> blog = blogService.getBlogPageByTypeId(id, null);
        return blog;
    }

    @GetMapping("/tags/{id}")
    public String tags(@PathVariable Integer id, Model model, Integer page) {
        List<Tag> tags = tagService.getAllTags();
        model.addAttribute("tags", tags);
        if (id == -1) id = tags.get(0).getId();
        PageInfo<ExtendBlog> blog = blogService.getBlogPageByTagId(id, page);
        model.addAttribute("page", blog);
        model.addAttribute("activeTagId", id);
        return "tags";
    }
}
