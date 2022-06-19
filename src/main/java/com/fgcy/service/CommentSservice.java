package com.fgcy.service;

import com.fgcy.pojo.BComment;

import java.util.List;
import java.util.Map;

/**
 * @Author fgcy
 * @Date 2022/5/26
 */
public interface CommentSservice {

    List<BComment> getLatest7DaysComments();

    Map<Long, Integer> getMaxBlogIdAndCommentCount();

    void initWeekRank();
}
