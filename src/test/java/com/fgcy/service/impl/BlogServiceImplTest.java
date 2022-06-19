package com.fgcy.service.impl;

import com.fgcy.pojo.vo.ExtendBlog;
import com.fgcy.pojo.vo.TwoLables;
import com.fgcy.service.BlogService;
import com.fgcy.util.RedisConst;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.crypto.spec.RC2ParameterSpec;
import java.util.Set;

import static com.fgcy.util.RedisConst.INTERFILE;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/5/28
 */
@SpringBootTest
class BlogServiceImplTest {

    @Autowired
    BlogServiceImpl blogService;
    @Autowired
    StringRedisTemplate stringRedisTemplate;

    @Test
    void getAllTagsAndAllType() {
        TwoLables allTagsAndAllType = blogService.getAllTagsAndAllType();
        System.out.println(allTagsAndAllType);
    }

    @Test
    public void getBlogById() {
        ExtendBlog blogById = blogService.getBlogById(1l);
        System.out.println(blogById);
    }


    @Test
    public void tdsast1() {
        TwoLables all = blogService.getAllTagsAndAllType();
        System.out.println(all);
    }


    @Test
    public void testd1() {
        //blogService.createCollection(1l,1l);
    }

    @Test
    public void t7451() {
        Boolean delete = stringRedisTemplate.delete(INTERFILE);
        System.out.println(delete);
    }


    @Test
    public void t4li2t1() {
        Boolean delete = stringRedisTemplate.delete(RedisConst.INDEX_BLOG + 12L);
        System.out.println(delete);
    }
}
