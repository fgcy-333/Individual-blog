package com.fgcy.filter;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.fgcy.exception.LoginException;
import com.fgcy.pojo.LoginUser;
import com.fgcy.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.omg.CORBA.PRIVATE_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.fgcy.util.RedisConst.LOGIN_USER;

/*
 *
 * @since: 1.8
 * @description：前置过滤器；当请求带token过来时；进行用户认证
 * @author: fgcy
 * @date: 2022/6/1
 */
@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    StringRedisTemplate stringRedisTemplate;


    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver resolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //cookie中携带token
        String token = null;
        Cookie[] cookies = request.getCookies();

        if (Objects.isNull(cookies)) {
            filterChain.doFilter(request, response);
            return;
        }

        List<Cookie> collect = Arrays.stream(cookies).filter(s -> "token".equals(s.getName())).collect(Collectors.toList());
        if (Objects.isNull(collect) || collect.isEmpty()) {
            filterChain.doFilter(request, response);
            return;
        }

        Cookie cookie = collect.get(0);
        token = cookie.getValue();

        //没有token直接放行
        if (StrUtil.isBlank(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = JwtUtil.parseJWT(token);
            token = claims.getSubject();

        } catch (Exception e) {
            e.printStackTrace();
            throw new LoginException("请重新登陆");
        }


        String jsonLoginUser = stringRedisTemplate.opsForValue().get(LOGIN_USER + token);
        if (StrUtil.isBlank(jsonLoginUser)) {
            //response.sendRedirect("/user/login");
            filterChain.doFilter(request, response);
            //resolver.resolveException(request, response, null, new LoginException("未登录"));
            return;
        }

        //从redis中获取到用户信息，说明已登陆
        LoginUser loginUser = JSONUtil.toBean(jsonLoginUser, LoginUser.class);

        //每次带token访问都会延时30分钟
        stringRedisTemplate.expire(LOGIN_USER + token, 30L, TimeUnit.MINUTES);

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(loginUser, null, loginUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);

        filterChain.doFilter(request, response);
    }
}
