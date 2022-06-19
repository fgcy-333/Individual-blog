package com.fgcy.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author fgcy
 * @Date 2022/5/21
 */
@ResponseStatus(HttpStatus.NOT_FOUND)//springboot拿到这个页面会跳转到404页面
public class CustomNotFountException extends RuntimeException {
    public CustomNotFountException() {
        super();
    }

    public CustomNotFountException(String message) {
        super(message);
    }

    public CustomNotFountException(String message, Throwable cause) {
        super(message, cause);
    }
}
