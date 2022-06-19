package com.fgcy.service.impl;

import com.fgcy.pojo.vo.ExtendComment;
import com.fgcy.service.CommentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/6/8
 */
@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private CommentService commentService;

    @Test
    public void test1() {
        List<ExtendComment> extendComments = commentService.listCommentByblogId(1l);
        extendComments.forEach(System.out::println);
    }
}
