package com.microsoft.xuetang.internalrpc.response;

/**
 * Created by jiash on 7/19/2016.
 */
public class ErrorResponse {
    private String desc;
    private Throwable cause;

    public ErrorResponse(String desc) {
        this.desc = desc;
    }

    public ErrorResponse(String desc, Throwable cause) {
        this(desc);
        this.cause = cause;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
