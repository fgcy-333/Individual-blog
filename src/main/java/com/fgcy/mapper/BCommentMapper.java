package com.fgcy.mapper;

import com.fgcy.pojo.BComment;
import com.fgcy.pojo.vo.ExtendComment;

import java.util.Date;
import java.util.List;

public interface BCommentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(BComment record);

    int insertSelective(BComment record);


    int updateByPrimaryKeySelective(BComment record);

    int updateByPrimaryKeyWithBLOBs(BComment record);

    int updateByPrimaryKey(BComment record);

    List<BComment> getCommentsByDate(Date date);

    List<ExtendComment> selectCommentByBlogId(Long blogId);

    int addComment(BComment comment);

    Long getUserIdByCommentId(Long parentId);
}

