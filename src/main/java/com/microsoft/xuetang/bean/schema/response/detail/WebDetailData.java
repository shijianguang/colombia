package com.microsoft.xuetang.bean.schema.response.detail;

import java.util.List;

/**
 * Created by jiash on 7/29/2016.
 */
public class WebDetailData {
    private String id;
    private String url;
    private String title;
    private String snippet;
    private String imageUrl;
    private List<String> keywords;
    private List<String> categories;
    private Boolean hasCachedContent;
    private String logoImageUrl;

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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }

    public Boolean getHasCachedContent() {
        return hasCachedContent;
    }

    public void setHasCachedContent(Boolean hasCachedContent) {
        this.hasCachedContent = hasCachedContent;
    }

    public String getLogoImageUrl() {
        return logoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }
}
