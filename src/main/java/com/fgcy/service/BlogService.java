package com.fgcy.service;

import com.fgcy.pojo.Blog;
import com.fgcy.pojo.vo.ExtendBlog;
import com.fgcy.pojo.vo.IndexPageBlog;
import com.fgcy.pojo.vo.TempBlog;
import com.fgcy.pojo.vo.TwoLables;
import com.fgcy.util.ResultData;
import com.github.pagehelper.PageInfo;
import org.springframework.http.HttpRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * @Author fgcy
 * @Date 2022/5/22
 */
public interface BlogService {
    TempBlog selectPage(Integer page, Integer offset);

    TwoLables getAllTagsAndAllType();

    ResultData addBlog(Blog blog);

    TempBlog searchPageByConditions(Integer page, String title, Integer typeId, Boolean recommend);

    ExtendBlog getBlogById(Long id);

    ResultData updateBlogById(Blog blog);

    PageInfo<ExtendBlog> getIndexPage(Integer page);

    List<ExtendBlog> getLatestBlog(Integer size);

    PageInfo<ExtendBlog> searchBlogPageByQueryContent(String query, Integer page);

    ExtendBlog getDetailBlogById(Long id, HttpServletRequest request);

    PageInfo<ExtendBlog> getBlogPageByTypeId(Long id, Integer page);

    PageInfo<ExtendBlog> getBlogPageByTagId(Integer id, Integer page);

    Map<String, List<Blog>> archiveBlog();

    Long getAllBlogsCount();

    ResultData deleteBlogsByBlogId(Long id);

    boolean checkBlogCollection(Long userId, Long blogId);

    boolean doUserCollection(Long userId, Long blogId);

    void updateBlogCount(ExtendBlog extendBlog);
}
