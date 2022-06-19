package com.fgcy;

import com.fgcy.mapper.*;
import com.fgcy.pojo.BComment;
import com.fgcy.pojo.Blog;
import com.fgcy.pojo.Type;
import com.fgcy.pojo.vo.ExtendBlog;
import com.fgcy.pojo.vo.TempBlog;
import com.fgcy.service.BlogService;
import com.fgcy.service.TypeService;
import com.fgcy.service.UserService;
import com.fgcy.service.impl.BlogServiceImpl;
import com.fgcy.util.COSClientUtil;
import com.github.pagehelper.PageInfo;
import com.qcloud.cos.COSClient;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.*;

@SpringBootTest
class BlogApplicationTests {

    @Autowired
    UserMapper userMapper;
    @Autowired
    UserService userService;
    @Autowired
    TypeService typeService;
    @Autowired
    BlogMapper blogMapper;
    @Autowired
    BlogService blogService;
    @Autowired
    BCommentMapper bCommentMapper;
    @Autowired
    TagMapper tagMapper;

    @Test
    void contextLoads() {
        //System.out.println(userMapper);

    }

    @Test
    public void test() {
        PageInfo<Type> allByPage = typeService.findAllByPage(1, 3);
        System.out.println(allByPage);
    }

    @Test
    public void tes1() {
        List<ExtendBlog> blog = blogMapper.getBlogByCondition(null);
        blog.forEach(System.out::println);
    }

    @Test
    public void tesss1() {
        TempBlog tempBlog = blogService.searchPageByConditions(null, null, null, null);
        System.out.println(tempBlog);
    }

    @Test
    public void testaB() {
        List<String> args = Arrays.asList("1", "2", "3", "4");
        blogMapper.addBlogTags(args, 1l);
    }


    @Test
    public void tess1() {
        List<BComment> commentsByDate = bCommentMapper.getCommentsByDate(new Date());
        System.out.println(commentsByDate);
    }

    @Test
    public void tessst1() {
        List<Long> list = new ArrayList<>();
        Collections.addAll(list, 1L, 2L, 3L, 4L, 5L);
        tagMapper.addTagCount(list);
    }


    @Test
    public void test1() {
        BlogServiceImpl service = (BlogServiceImpl) blogService;
        List<Integer> integers = new ArrayList<>();
        Collections.addAll(integers, 1, 2, 3);
        List<String> strings = new ArrayList<>();
        Collections.addAll(strings, "1", "2", "4");
        service.updateTagCount(strings, integers, 1L);

    }
}
