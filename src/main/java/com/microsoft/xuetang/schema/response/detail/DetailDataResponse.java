package com.microsoft.xuetang.schema.response.detail;

/**
 * Created by jiash on 8/9/2016.
 */
public class DetailDataResponse<T> {
    private String type;
    private T data;

    public DetailDataResponse(String type, T data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
