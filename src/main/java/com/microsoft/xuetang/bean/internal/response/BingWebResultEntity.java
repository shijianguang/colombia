package com.microsoft.xuetang.bean.internal.response;

import java.util.List;

/**
 * Created by jiash on 7/29/2016.
 */
public class BingWebResultEntity {
    private String id;
    private String name;
    private String url;
    private String urlPingSuffix;
    private String thumbnailUrl;
    private List<About> about;
    private String displayUrl;
    private String snippet;
    private String dateLastCrawled;
    private String abstractContent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrlPingSuffix() {
        return urlPingSuffix;
    }

    public void setUrlPingSuffix(String urlPingSuffix) {
        this.urlPingSuffix = urlPingSuffix;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public List<About> getAbout() {
        return about;
    }

    public void setAbout(List<About> about) {
        this.about = about;
    }

    public String getDisplayUrl() {
        return displayUrl;
    }

    public void setDisplayUrl(String displayUrl) {
        this.displayUrl = displayUrl;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getDateLastCrawled() {
        return dateLastCrawled;
    }

    public void setDateLastCrawled(String dateLastCrawled) {
        this.dateLastCrawled = dateLastCrawled;
    }

    public String getAbstractContent() {
        return abstractContent;
    }

    public void setAbstractContent(String abstractContent) {
        this.abstractContent = abstractContent;
    }

    public static class About
    {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
