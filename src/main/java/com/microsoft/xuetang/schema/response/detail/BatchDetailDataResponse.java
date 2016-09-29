package com.microsoft.xuetang.schema.response.detail;

import java.util.Map;

/**
 * Created by jiash on 8/11/2016.
 */
public class BatchDetailDataResponse<T> {
    private String type;
    private Map<String, T> data;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Map<String, T> getData() {
        return data;
    }

    public void setData(Map<String, T> data) {
        this.data = data;
    }
}
