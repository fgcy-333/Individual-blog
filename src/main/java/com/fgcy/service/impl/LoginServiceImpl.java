package com.fgcy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fgcy.mapper.UserMapper;
import com.fgcy.pojo.LoginUser;
import com.fgcy.pojo.User;
import com.fgcy.pojo.VerifyCode;
import com.fgcy.pojo.vo.BaseUser;
import com.fgcy.service.LoginService;
import com.fgcy.util.JwtUtil;
import com.fgcy.util.RedisConst;
import com.fgcy.util.SimpleCharVerifyCodeGen;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author fgcy
 * @Date 2022/6/1
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    StringRedisTemplate stringRedisTemplate;
    @Autowired
    private UserMapper userMapper;

    @Autowired
    ObjectMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;




    /*
     *
     * @since: 1.8
     * @description：用户登录 token在redis中存活时间30mins
     * @author: fgcy
     * @date: 2022/6/12
     */
    @Override
    public String login(User user) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        //验证用户名、密码
        Authentication authenticate = authenticationManager.authenticate(authentication);
        Assert.notNull(authenticate, "登录失败");
        //有数据说明登录成功
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        Long id = loginUser.getUser().getId();
        String token = JwtUtil.createJWT(id.toString());

        String jsonLoginUser = null;

        try {

            jsonLoginUser = mapper.writeValueAsString(loginUser);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //将登录成功后的用户信息放到redis中
        stringRedisTemplate.opsForValue().set(RedisConst.LOGIN_USER + id, jsonLoginUser, 30L, TimeUnit.MINUTES);

        return token;
    }

    @Override
    public String logout() {
        UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        long id = loginUser.getUser().getId();
        Boolean delete = stringRedisTemplate.delete(RedisConst.LOGIN_USER + id);
        if (delete) return "true";
        return "false";
    }

    @Override
    public BaseUser getUserInfoByToken(String token) {
        String to = null;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            to = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Long userId = Long.valueOf(to);
        return userMapper.getbaseUserByUserId(userId);

    }

    @Override
    public void createCode(Integer width, Integer height, String codeId, HttpServletResponse response) {

        if (StrUtil.isBlankIfStr(width) || StrUtil.isBlankIfStr(height)) {
            width = 90;
            height = 32;
        }

        VerifyCode verifyCode = SimpleCharVerifyCodeGen.generate(width, height);

        String code = verifyCode.getCode();

        if (StrUtil.isBlankIfStr(codeId)) return;

        System.out.println(code + "--------" + codeId);
        stringRedisTemplate.opsForValue().set(RedisConst.VALIDATE_CODE_ID + codeId, code, 10L, TimeUnit.MINUTES);

        //设置响应头
        response.setHeader("Pragma", "no-cache");
        //设置响应头
        response.setHeader("Cache-Control", "no-cache");
        //在代理服务器端防止缓冲
        response.setDateHeader("Expires", 0);
        //设置响应内容类型
        response.setContentType("image/jpeg");
        try {
            response.getOutputStream().write(verifyCode.getImgBytes());
            response.getOutputStream().flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Transactional
    @Override
    public void doRegister(String username, String password, String imgId, String code) {
        boolean checkCode = validateImgCode(imgId, code);
        if (!checkCode) throw new RuntimeException("验证码错误");

        String encode = passwordEncoder.encode(password.trim());
        User user = new User();
        user.setUsername(username.trim());
        user.setPassword(encode);
        user.setAvatar("https://fgcy-oss-1307808810.cos.ap-guangzhou.myqcloud.com/wallhaven-28v8wm.jpg");
        user.setStatus((byte) 0);
        user.setGmtCreate(new Date());
        user.setGmtModified(new Date());
        userMapper.addUser(user);

        userMapper.addRoleBindUser(user.getId(), 2L);
    }

    private boolean validateImgCode(String imgId, String code) {
        String s = stringRedisTemplate.opsForValue().get(RedisConst.VALIDATE_CODE_ID + imgId);
        if (StrUtil.isBlankIfStr(s)) return false;

        if (s.equals(code)) return true;

        return false;
    }


}

