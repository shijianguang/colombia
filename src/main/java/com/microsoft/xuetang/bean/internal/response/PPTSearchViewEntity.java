package com.microsoft.xuetang.bean.internal.response;

/**
 * Created by jiash on 8/1/2016.
 */
public class PPTSearchViewEntity {
    private String id;
    private String name;
    private String url;
    private String displayUrl;
    private String snippet;
    private String publisher;
    private String publisherOther;
    private String publishData = "2015-07-07";
    private String ImageUrl;
    private String pptImageUrls;
    private String resourceDownloadLink;

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

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getPublisherOther() {
        return publisherOther;
    }

    public void setPublisherOther(String publisherOther) {
        this.publisherOther = publisherOther;
    }

    public String getPublishData() {
        return publishData;
    }

    public void setPublishData(String publishData) {
        this.publishData = publishData;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getPptImageUrls() {
        return pptImageUrls;
    }

    public void setPptImageUrls(String pptImageUrls) {
        this.pptImageUrls = pptImageUrls;
    }

    public String getResourceDownloadLink() {
        return resourceDownloadLink;
    }

    public void setResourceDownloadLink(String resourceDownloadLink) {
        this.resourceDownloadLink = resourceDownloadLink;
    }
}
