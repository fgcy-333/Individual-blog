package com.fgcy.mapper;

import com.fgcy.pojo.BComment;
import com.fgcy.pojo.vo.ExtendComment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/6/7
 */
@SpringBootTest
class BCommentMapperTest {
    @Autowired
    BCommentMapper bCommentMapper;

    @Test
    void selectCommentByBlogId() {
        List<ExtendComment> commentList = bCommentMapper.selectCommentByBlogId(1L);
        commentList.forEach(System.out::println);
    }
}
