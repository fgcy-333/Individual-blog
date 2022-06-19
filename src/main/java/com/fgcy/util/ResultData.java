package com.fgcy.util;

/**
 * @Author fgcy
 * @Date 2022/5/22
 */
public class ResultData<T> {

    // 结果状态码
    private int code;

    // 响应信息
    private String msg;

    // 响应数据
    private T data;

    // 接口请求时间
    private boolean flag;


    /**
     * 链式编程
     *
     * @param data
     * @param <T>
     * @return
     */
    public static <T> ResultData<T> success(T data) {
        ResultData resultData = new ResultData();
        resultData.setFlag(true);
        resultData.setCode(ReturnCode.SUCCESS.getCode());
        resultData.setMsg(ReturnCode.SUCCESS.getMsg());
        resultData.setData(data);
        return resultData;
    }

    public static <T> ResultData<T> fail() {
        ResultData resultData = new ResultData();
        resultData.setFlag(false);
        resultData.setCode(ReturnCode.FAIL.getCode());
        resultData.setMsg(ReturnCode.FAIL.getMsg());
        return resultData;
    }

    public int getCode() {
        return code;
    }

    public ResultData<T> setCode(int code) {
        this.code = code;
        return this;
    }

    public String getMsg() {
        return msg;
    }

    public ResultData<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public T getData() {
        return data;
    }

    public ResultData<T> setData(T data) {
        this.data = data;
        return this;
    }

    public boolean isFlag() {
        return flag;
    }

    public ResultData<T> setFlag(boolean flag) {
        this.flag = flag;
        return this;
    }

    @Override
    public String toString() {
        return "ResultData{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                ", flag=" + flag +
                '}';
    }
}

