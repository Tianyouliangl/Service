package com.service.zgbj.im;

/**
 * Created by fengzhangwei on 2020/10/19.
 */
public class BaseResponse<T> {
    private String msg;
    private T data;
    private int code;


    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
