package com.microsoft.xuetang.schema.response;

/**
 * Created by shijianguang on 9/27/16.
 */
public class JsonPResponse {
    String data;

    public JsonPResponse() {

    }

    public JsonPResponse(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
