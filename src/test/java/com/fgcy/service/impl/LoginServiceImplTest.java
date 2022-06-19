package com.fgcy.service.impl;

import com.fgcy.pojo.vo.BaseUser;
import com.fgcy.service.LoginService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @Author fgcy
 * @Date 2022/6/9
 */
@SpringBootTest
class LoginServiceImplTest {
    @Autowired
    private LoginService loginService;

    @Test
    void getUserInfoByToken() {
        BaseUser userInfoByToken = loginService.getUserInfoByToken("eyJhbGciOiJIUzI1NiJ9.eyJqdGkiOiI2MGVmZGU4ZTVkMDA0OTYzOTg0M2Y0NGFhYTg3M2ZlOCIsInN1YiI6IjEiLCJpc3MiOiJzZyIsImlhdCI6MTY1NDc2MTU3NywiZXhwIjoxNjU0NzY1MTc3fQ.hWZw-gHun2WDB8UJ1FdTguUyQOOyJ2PwJD8UnUFrNM4");
        System.out.println(userInfoByToken);
    }
}
