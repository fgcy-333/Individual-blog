package com.fgcy.controller.admin;

import com.fgcy.pojo.Type;
import com.fgcy.service.TypeService;
import com.fgcy.util.ResultData;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.One;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.lang.model.element.TypeElement;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
@Controller
@RequestMapping("/admin")
public class TypeController {
    @Autowired
    private TypeService typeService;

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/types")
    public String types(Integer page, Integer offset, Model model) {
        PageInfo<Type> all = typeService.findAllByPage(page, offset);
        model.addAttribute("page", all);
        return "admin/types";
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/types/input")
    public String inputPage() {
        return "admin/types-input";
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/types/{id}/input")
    public String getTypeById(@PathVariable Integer id, Model model) {
        model.addAttribute("type", typeService.getTypeById(id));
        return "admin/types-input";
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/types")
    public String inputType(Type type, RedirectAttributes attributes) {
        ResultData data = typeService.addType(type);
        attributes.addFlashAttribute("message", data.getMsg());
        return "redirect:/admin/types";
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @PostMapping("/types/{id}")
    public String editType(Type type, RedirectAttributes attributes) {
        ResultData data = typeService.editType(type);
        attributes.addFlashAttribute("message", data.getMsg());
        return "redirect:/admin/types";
    }

    @PreAuthorize("hasAnyAuthority('admin')")
    @GetMapping("/types/{id}/delete")
    public String deleteTypeById(@PathVariable Integer id, RedirectAttributes attributes) {
        ResultData data = typeService.deleteType(id);
        attributes.addFlashAttribute("message", data.getMsg());
        return "redirect:/admin/types";
    }

}
