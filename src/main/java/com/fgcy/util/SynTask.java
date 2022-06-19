package com.fgcy.util;

import com.fgcy.pojo.vo.ExtendBlog;
import com.fgcy.service.BlogService;
import com.sun.corba.se.impl.orbutil.concurrent.Sync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.fgcy.util.RedisConst.NEED_TO_UPDATE;

/**
 * @Author fgcy
 * @Date 2022/6/12
 */
@Component
public class SynTask {
    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private BlogService blogService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Scheduled(cron = "0 0/1 * * * *")
    public void SyncCountToDatabase() {
        List<Long> blogIds = redisUtil.getSetList(NEED_TO_UPDATE);
        stringRedisTemplate.delete(NEED_TO_UPDATE);
        for (Long blogId : blogIds) {
            ExtendBlog extendBlog = redisUtil.getObjectBykey(RedisConst.INDEX_BLOG, blogId, ExtendBlog.class);
            blogService.updateBlogCount(extendBlog);
        }
    }
}
