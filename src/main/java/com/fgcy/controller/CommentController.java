package com.fgcy.controller;

import com.fgcy.pojo.BComment;
import com.fgcy.pojo.vo.ExtendComment;
import com.fgcy.service.CommentService;
import org.apache.ibatis.logging.stdout.StdOutImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/6/7
 */
@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping("/comments/{blogId}")
    public String comments(@PathVariable Long blogId, Model model) {
        model.addAttribute("comments", commentService.listCommentByblogId(blogId));
        return "blog:: commentList";
    }


    @PostMapping("/comments")
    public String post(BComment comment, String token) {
        System.out.println(comment);
        int i = commentService.saveComment(comment, token);
        if (i == -1) return "redirect:/user/login";

        List<ExtendComment> list = commentService.listCommentByblogId(comment.getBlogId());
        return "redirect:/comments/" + comment.getBlogId();
    }

    @ResponseBody
    @GetMapping("/get")
    public List<ExtendComment> getComments(Long id) {
        return commentService.listCommentByblogId(id);
    }
}

