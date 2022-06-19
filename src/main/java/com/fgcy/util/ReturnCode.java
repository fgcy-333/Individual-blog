package com.fgcy.util;

/**
 * @Author fgcy
 * @Date 2022/5/22
 */
public enum ReturnCode {
    SUCCESS(200, "操作成功!"),
    FAIL(500, "操作失败！");

    private final int code;
    private final String msg;

    ReturnCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
