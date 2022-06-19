package com.fgcy.service;

import com.fgcy.pojo.User;
import com.fgcy.pojo.vo.BaseUser;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author fgcy
 * @Date 2022/6/1
 */
public interface LoginService {
    String login(User user);

    String logout();

    BaseUser getUserInfoByToken(String token);

    void createCode(Integer width, Integer height, String codeId, HttpServletResponse response);

    void doRegister(String username, String password, String imgId, String code);
}
