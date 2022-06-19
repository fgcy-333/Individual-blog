package com.fgcy.mapper;

import com.fgcy.pojo.Blog;
import com.fgcy.pojo.MUserCollection;
import com.fgcy.pojo.vo.ExtendBlog;
import com.fgcy.pojo.vo.IndexPageBlog;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;


import java.util.List;

public interface BlogMapper {


    int deleteByPrimaryKey(Long id);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKeyWithBLOBs(Blog record);

    int updateByPrimaryKey(Blog record);

    List<ExtendBlog> getBlogByCondition(Blog blog);

    int addBlogTags(@Param("tags") List tags,
                    @Param("blogId") Long blogId);


    List<ExtendBlog> getExtendBlogOrderByCondition(@Param("condition") String condition);

    List<Long> getBlogsBySize(@Param("size") Integer size, @Param("condition") String condition);

    List<ExtendBlog> queryByTitleOrContentAndDescription(String query);

    ExtendBlog getExtendBlogByBlogId(Long id);

    List<ExtendBlog> getBlogsByConditionId(Blog blog);

    List<ExtendBlog> getBlogsByBlogIds(@Param("blogIds") List<Long> blogIds);

    List<Blog> getAllBlogs();

    @Select("select count(1) from t_blog")
    Long getAllBlogCount();

    //List<Blog> getInitRankBlogInfo(List ls);

    //IndexPageBlog getIndexPageBlogById(Long blogId);

    ExtendBlog getUpdateBlogInfoByBlogId(Long id);

    int deleteBlogByBlogId(Long id);

    void deleteBlogTagByTagIds(@Param("oldTags") List<Integer> oldTags, @Param("blogId") Long blogId);

    Blog getBaseBlogForDelete(Long id);

    Long checkIsCollectionBlog(@Param("userId") Long userId, @Param("blogId") Long blogId);

    void updateUserCollectionStatus(@Param("collectionId") Long collectionId, @Param("status") int status);

    MUserCollection getUserCollectionStatus(@Param("userId") Long userId, @Param("blogId") Long blogId);

    Long getUserIdByBlogId(Long blogId);

    void addUserCollection(MUserCollection mUserCollection);

    void updateBlogCount(ExtendBlog extendBlog);
}
