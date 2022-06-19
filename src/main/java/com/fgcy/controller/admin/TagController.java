package com.fgcy.controller.admin;

import com.fgcy.pojo.Tag;
import com.fgcy.service.TagService;
import com.fgcy.util.ResultData;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * @Author fgcy
 * @Date 2022/5/22
 */
@Controller
@RequestMapping("/admin")
public class TagController {
    @Autowired
    private TagService tagService;

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/tags")
    public String listTags(Integer page, Integer offset, Model model) {
        PageInfo<Tag> all = tagService.findPageByPageInfo(page, offset);
        model.addAttribute("page", all);
        return "admin/tags";
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/tags/{id}/input")
    public String toEditPage(@PathVariable Integer id, Model model) {
        ResultData data = tagService.getTagById(id);
        model.addAttribute("tag", data.getData());
        return "admin/tags-input";
    }


    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/tags/{id}/delete")
    public String deleteTagById(@PathVariable Integer id, RedirectAttributes attributes) {
        ResultData data = tagService.deleteTagById(id);
        attributes.addFlashAttribute("message", data.getMsg());
        return "redirect:/admin/tags";
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/tags/{id}")
    public String editTag(Tag tag, RedirectAttributes attributes) {
        ResultData data = tagService.editTag(tag);
        attributes.addFlashAttribute("message", data.getMsg());
        return "redirect:/admin/tags";
    }


    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/tags")
    public String addTag(Tag tag, RedirectAttributes attributes) {
        ResultData data = tagService.addTag(tag);
        attributes.addFlashAttribute("message", data.getMsg());
        return "redirect:/admin/tags";
    }


    //{/admin/tags/input}
    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/tags/input")
    public String toAddTagPage() {
        return "admin/tags-input";
    }
}
