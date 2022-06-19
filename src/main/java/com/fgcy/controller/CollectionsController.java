package com.fgcy.controller;

import com.fgcy.pojo.BCollection;
import com.fgcy.pojo.Blog;
import com.fgcy.service.CollectionService;
import com.fgcy.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;

/**
 * @Author fgcy
 * @Date 2022/5/26
 */
@Controller
public class CollectionsController {
    @Autowired
    private CollectionService collectionService;

    @Autowired
    private UserService userService;



    @PreAuthorize("hasAnyAuthority('admin','user')")
    @GetMapping("/collections")
    public String archivesShow(Model model, Long userId) {
        Map<String, List<BCollection>> map = userService.getCollections(userId);
        model.addAttribute("collectionMap", map);
        int count = userService.getCollectionsCountByUserId(userId);
        model.addAttribute("blogCount", count);
        return "collection";
    }


    @GetMapping("/dd")
    @ResponseBody
    public Map<String, List<BCollection>> aaa(Long userId) {
        Map<String, List<BCollection>> map = userService.getCollections(userId);
        return map;
    }

}
