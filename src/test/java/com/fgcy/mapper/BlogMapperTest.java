package com.fgcy.mapper;

import com.fgcy.pojo.MUserCollection;
import com.fgcy.pojo.vo.ExtendBlog;
import com.fgcy.pojo.vo.IndexPageBlog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/5/29
 */
@SpringBootTest
class BlogMapperTest {

    @Autowired
    private BlogMapper blogMapper;

    @Test
    void getIndexPageBlogById() {
        ExtendBlog blog = blogMapper.getExtendBlogByBlogId(1l);
        System.out.println(blog);
    }

    @Test
    public void tes() {
        List<Long> gmt_modified_desc = blogMapper.getBlogsBySize(10, "gmt_modified desc");
        System.out.println(gmt_modified_desc);
    }

    @Test
    public void tessst1() {
        ExtendBlog updateBlogInfoByBlogId = blogMapper.getUpdateBlogInfoByBlogId(1L);
        System.out.println(updateBlogInfoByBlogId);
    }

    @Test
    public void tessdft1() {
        int i = blogMapper.deleteBlogByBlogId(1L);
        System.out.println(i);
    }

    @Test
    public void dfst1() {
        List<Integer> list = Arrays.asList(new Integer[]{100, 200});
        blogMapper.deleteBlogTagByTagIds(list, 1l);
    }


    @Test
    public void t123t1() {
        List<ExtendBlog> order = blogMapper.getExtendBlogOrderByCondition("view DESC,comment_count DESC,collect_count DESC");
        order.forEach(System.out::println);
    }

    @Test
    public void taest1() {
        Long aLong = blogMapper.checkIsCollectionBlog(12L, 3L);
        System.out.println(aLong + "----------------");
    }

    @Test
    public void te234ft1() {
        blogMapper.updateUserCollectionStatus(1L, 0);
    }


    @Test
    public void td2t1() {
        MUserCollection userCollectionStatus = blogMapper.getUserCollectionStatus(70L, 1L);
        System.out.println(userCollectionStatus);
    }
}
