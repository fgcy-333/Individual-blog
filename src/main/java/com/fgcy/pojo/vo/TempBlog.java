package com.fgcy.pojo.vo;

import com.fgcy.pojo.Blog;
import com.fgcy.pojo.Tag;
import com.fgcy.pojo.Type;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @Author fgcy
 * @Date 2022/5/22
 * 后台博客管理 既有所有标签 类型 又有所有分页博客
 */
public class TempBlog {
    private PageInfo<ExtendBlog> blogs;
    private List<Type> type;
    private List<Tag> tags;

    @Override
    public String toString() {
        return "MBlog{" +
                "blogs=" + blogs +
                ", type=" + type +
                ", tags=" + tags +
                '}';
    }

    public PageInfo<ExtendBlog> getBlogs() {
        return blogs;
    }

    public TempBlog setBlogs(PageInfo<ExtendBlog> blogs) {
        this.blogs = blogs;
        return this;
    }

    public List<Type> getType() {
        return type;
    }

    public TempBlog setType(List<Type> type) {
        this.type = type;
        return this;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public TempBlog setTags(List<Tag> tags) {
        this.tags = tags;
        return this;
    }
}
