package com.fgcy.service.impl;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUnit;
import cn.hutool.core.date.DateUtil;
import com.fgcy.mapper.BCommentMapper;
import com.fgcy.mapper.BlogMapper;
import com.fgcy.pojo.BComment;
import com.fgcy.pojo.Blog;
import com.fgcy.service.CommentSservice;
import com.fgcy.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.unit.DataUnit;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

/**
 * @Author fgcy
 * @Date 2022/5/26
 */
@Service
public class CommentSserviceImpl implements CommentSservice {

    @Autowired
    private BCommentMapper bCommentMapper;

    @Autowired
    private BlogMapper blogMapper;

    @Autowired
    RedisUtil redisUtil;

    /*
     *
     * @since: 1.8
     * @description：获取七天内所有评论
     * @author: fgcy
     * @date: 2022/5/26
     */
    @Override
    public List<BComment> getLatest7DaysComments() {
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime minusDays = localDateTime.minusDays(7);
        ZoneId zoneId = ZoneId.systemDefault();
        Date from = Date.from(minusDays.atZone(zoneId).toInstant());
        return bCommentMapper.getCommentsByDate(from);
    }

    @Override
    public Map<Long, Integer> getMaxBlogIdAndCommentCount() {
        List<BComment> commentList = getLatest7DaysComments();
        HashMap<Long, Integer> map = new HashMap<>();
        for (BComment bComment : commentList) {
            if (map.get(bComment.getBlogId()) != null) {
                map.put(bComment.getBlogId(), map.get(bComment.getBlogId()) + 1);
            } else {
                map.put(bComment.getBlogId(), 0);
            }
        }
        return map;
    }

    @Override
    public void initWeekRank() {
/*        Map<Long, Integer> map = getMaxBlogIdAndCommentCount();
        Set<Long> blogIds = map.keySet();
        String key = "day:rank: " + DateUtil.format(new Date(), DatePattern.PURE_DATE_FORMAT);
        for (Long blogId : blogIds) {
            redisUtil.zSet(key, blogId, map.get(blogId));
        }
        initRankBlogsInfo(blogIds, map);*/
    }

/*    private void initRankBlogsInfo(Set<Long> blogIds, Map map) {
        Long expireTime = 3600 * 24 * 1L;
        ArrayList<Long> list = new ArrayList<>(blogIds);
        List<Blog> blogs = blogMapper.getInitRankBlogInfo(list);
        for (Blog blog : blogs) {
            String key = "rank:blog:" + blog.getId();
            if (!redisUtil.hasKey(key)) {
                redisUtil.hset(key, "blog:id", blog.getId(), expireTime);
                redisUtil.hset(key, "blog:title", blog.getTitle(), expireTime);
                redisUtil.hset(key, "blog:commentCount", map.get(blog.getId()), expireTime);
            }
        }
    }*/
}
