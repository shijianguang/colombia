package com.microsoft.xuetang.bean.schema.response.detail;

/**
 * Created by shijianguang on 10/24/16.
 */
public class RelatedContent {
    private String id;
    private String imageUrl;
    private String title;

    public RelatedContent() {
    }

    public RelatedContent(String id, String imageUrl, String title) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
