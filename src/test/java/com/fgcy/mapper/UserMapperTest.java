package com.fgcy.mapper;

import com.fgcy.pojo.vo.BaseUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
@SpringBootTest
class UserMapperTest {
    @Autowired
    private UserMapper userMapper;

    @Test
    void selectUserByNameAndPassword() {
        BaseUser baseUser = userMapper.getbaseUserByUserId(1l);
        System.out.println(baseUser);
    }
}
