package com.fgcy.handler;

import com.fgcy.exception.LoginException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.server.ErrorPageRegistrar;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
@ControllerAdvice
public class ControllerExceptionHandler {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public ModelAndView exceptionHandler(HttpServletRequest request, Exception exception) throws Exception {
        logger.error("请求的接口: {}, 异常原因: {}", request.getRequestURI(), exception.getMessage());


        //若该错误有指定的返回的状态，就不跳error页面
        if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
            throw exception;
        }

        ModelAndView modelAndView = new ModelAndView();
        //org.springframework.security.access.AccessDeniedException: 不允许访问


        if (exception instanceof LoginException) {
            modelAndView.addObject("message", exception.getMessage());
            modelAndView.setViewName("login");
            return modelAndView;
        }

        if (exception instanceof AccessDeniedException) {
            modelAndView.addObject("message", "权限不足，请登录");
            modelAndView.setViewName("login");
            return modelAndView;
        }

        if (exception instanceof BadCredentialsException) {
            modelAndView.addObject("message", "用户名或密码错误");
            modelAndView.setViewName("login");
            return modelAndView;
        }




        modelAndView.addObject("url", request.getRequestURL());
        modelAndView.addObject("exception", exception);
        modelAndView.setViewName("error/500");
        return modelAndView;
    }
}
