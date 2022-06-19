package com.fgcy.service;

import com.fgcy.pojo.BComment;
import com.fgcy.pojo.vo.ExtendComment;
import org.springframework.beans.factory.annotation.Autowired;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/6/7
 */
public interface CommentService {

    public List<ExtendComment> listCommentByblogId(Long blogId);

    int saveComment(BComment comment, String token);
}
