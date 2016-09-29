package com.microsoft.xuetang.bean.log;

/**
 * Created by shijianguang on 9/8/16.
 */
public class SearchLogEntity {
    private String id;
    private String url;

    public SearchLogEntity() {}

    public SearchLogEntity(String id, String url) {
        this.id = id;
        this.url = url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
