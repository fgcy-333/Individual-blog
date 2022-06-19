package com.fgcy.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/6/13
 */
@SpringBootTest
class RedisUtilTest {
    @Autowired
    private RedisUtil redisUtil;

    @Test
    void clearAllTags() {
        redisUtil.clearAllTags();
    }

    @Test
    public void test1() {
        redisUtil.clearAllTypes();
    }

    @Test
    public void tessdf1() {
        redisUtil.clearIndexCache();
    }
}
