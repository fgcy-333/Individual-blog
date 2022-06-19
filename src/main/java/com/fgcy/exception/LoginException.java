package com.fgcy.exception;

/**
 * @Author fgcy
 * @Date 2022/6/10
 */
public class LoginException extends RuntimeException {
    public LoginException() {
        super();
    }

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
