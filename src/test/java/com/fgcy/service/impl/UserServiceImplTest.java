package com.fgcy.service.impl;

import com.fgcy.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/6/10
 */
@SpringBootTest
class UserServiceImplTest {

    @Autowired
    UserService userService;
    @Test
    void checkUsername() {
        boolean integer = userService.checkUsername("fgcy");
        System.out.println(integer);
    }
}
