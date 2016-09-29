package com.microsoft.xuetang.schema.response;

import com.microsoft.xuetang.util.RspCodeMsg;

/**
 * Created by jiash on 7/29/2016.
 */
public class Response<T> {
    private String code;
    private String msg;
    private T data;

    public Response() {}

    public Response(RspCodeMsg msg) {
        this.code = msg.getCode();
        this.msg = msg.getMsg();
    }

    public Response(RspCodeMsg msg, T data) {
        this();
        this.data = data;
    }

    public Response(T data) {
        this();
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

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
}
