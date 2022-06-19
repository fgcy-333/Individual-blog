package com.fgcy.service.impl;

import com.fgcy.mapper.MenuMapper;
import com.fgcy.mapper.UserMapper;
import com.fgcy.pojo.LoginUser;
import com.fgcy.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;

/*
 *
 * @since: 1.8
 * @description：根据名字查询用户
 * @author: fgcy
 * @date: 2022/6/1
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    UserMapper userMapper;
    @Autowired
    MenuMapper menuMapper;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userMapper.getUserByName(username);
        Assert.notNull(user, "请检查用户名是否存在");
        List<String> strings = menuMapper.getPermsByUserId(user.getId());
        return new LoginUser(user, strings);
    }
}
