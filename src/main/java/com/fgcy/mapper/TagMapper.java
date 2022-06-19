package com.fgcy.mapper;

import com.fgcy.pojo.Tag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TagMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Tag record);

    int insertSelective(Tag record);

    Tag selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Tag record);

    int updateByPrimaryKey(Tag record);

    @Select("select id,name ,count from t_tag")
    List<Tag> findAll();

    @Select("select count(1) from t_tag where name=#{name}")
    int getCountByName(String name);

    List<Tag> findTagsByIds(String ids);

    List<Tag> getTagsByIds(@Param("list") List<Integer> list);

    @Select("select tag_id from t_blog_tag where blog_id=#{blogId}")
    List<Integer> findTagIdsByBlogId(Long blogId);

    @Delete("delete from t_blog_tag where blog_id=#{id}")
    int deleteBlogTagsByBlogId(Long id);


    @Select("select blog_id from t_blog_tag where tag_id=#{id}")
    List<Long> getBlogIdsByTagId(Integer id);

    void addTagCount(@Param("asList") List<Long> asList);

    void reduceTagCount(@Param("asList") List<Integer> oldTags);
}
