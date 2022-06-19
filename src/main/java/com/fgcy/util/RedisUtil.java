package com.fgcy.util;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fgcy.pojo.vo.ExtendBlog;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.fgcy.util.RedisConst.INDEX_BLOG;
import static com.fgcy.util.RedisConst.INTERFILE;

@Component
public class RedisUtil {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private SynTask synTask;

    public static final String COLLECTION = "collection";
    public static final String COMMENT = "comment";
    public static final String VIEW = "view";

    public <R, ID> R getIfNotExistsThenSet(String key, ID id, Class<R> returnType
            , Function<ID, R> dbCallback, Long time, TimeUnit timeUnit) {
        //取
        String jsonObject = stringRedisTemplate.opsForValue().get(key);
        //redis中有，直接返回
        if (!StrUtil.isBlank(jsonObject)) {
            return JSONUtil.toBean(jsonObject, returnType);
        }
        //没有，查数据库
        R apply = dbCallback.apply(id);
        //存
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(apply), time, timeUnit);
        return apply;
    }


    public <R, ID> List<R> getIfNotExistsThenSetForList(String key, ID id, Class<R> returnType
            , Function<ID, List<R>> dbCallback, Long time, TimeUnit timeUnit) {
        //取
        String jsonObject = stringRedisTemplate.opsForValue().get(key);
        //redis中有，直接返回
        if (!StrUtil.isBlank(jsonObject)) {
            return JSONUtil.toList(jsonObject, returnType);
        }
        //没有，查数据库
        List<R> apply = dbCallback.apply(id);
        //存
        stringRedisTemplate.opsForValue().set(key, JSONUtil.toJsonStr(apply), time, timeUnit);
        return apply;
    }


    public void increanExtendBlogFieldCount(Long blogId, String field) {

        String extendBlogStr = stringRedisTemplate.opsForValue().get(INDEX_BLOG + blogId);
        ExtendBlog extendBlog = JSONUtil.toBean(extendBlogStr, ExtendBlog.class);

        switch (field) {
            case VIEW:
                extendBlog.setView(extendBlog.getView() + 1);
                break;
            case COLLECTION:
                extendBlog.setCollectCount(extendBlog.getCollectCount() + 1);
                break;
            case COMMENT:
                extendBlog.setCommentCount(extendBlog.getCommentCount() + 1);
                break;
        }

        String jsonStr = JSONUtil.toJsonStr(extendBlog);
        //每次修改 阅读量 收藏数 评论数 该博客就会多在redis中存在3天
        stringRedisTemplate.opsForValue().set(INDEX_BLOG + blogId, jsonStr, 3L, TimeUnit.DAYS);

        //更新数据库时删除redis中的set【下面的set】
        stringRedisTemplate.opsForSet().add(RedisConst.NEED_TO_UPDATE, blogId.toString());
    }


    public void reduceFieldCountByNum(Long blogId, String field, Long num) {
        String extendBlogStr = stringRedisTemplate.opsForValue().get(INDEX_BLOG + blogId);
        ExtendBlog extendBlog = JSONUtil.toBean(extendBlogStr, ExtendBlog.class);

        switch (field) {
            case COLLECTION:
                extendBlog.setCollectCount(extendBlog.getCollectCount() - num);
                break;
            case COMMENT:
                extendBlog.setCommentCount(extendBlog.getCommentCount() - num);
                break;
        }

        String jsonStr = JSONUtil.toJsonStr(extendBlog);
        stringRedisTemplate.opsForValue().set(INDEX_BLOG + blogId, jsonStr, 3L, TimeUnit.DAYS);

        //更新数据库时删除redis中的set【下面的set】
        stringRedisTemplate.opsForSet().add(RedisConst.NEED_TO_UPDATE, blogId.toString());
    }

    public List<Long> getSetList(String needToUpdate) {
        Set<String> members = stringRedisTemplate.opsForSet().members(needToUpdate);
        return members.stream().map(Long::new).collect(Collectors.toList());
    }


    public <R, ID> R getObjectBykey(String key, ID id, Class<R> returnType) {
        String s = stringRedisTemplate.opsForValue().get(key + id);
        return JSONUtil.toBean(s, returnType);
    }

    public void clearIndexCache() {

        //先同步redis和mysql再删除
        synTask.SyncCountToDatabase();
        Set<String> keys = stringRedisTemplate.keys("index*");
        stringRedisTemplate.delete(keys);
        stringRedisTemplate.delete(INTERFILE);
    }

    public void clearAllTags() {
        stringRedisTemplate.delete(RedisConst.ALL_TAGS);
    }

    public void clearAllTypes() {
        stringRedisTemplate.delete(RedisConst.ALL_TYPES);
    }
}
