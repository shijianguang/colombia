package com.microsoft.xuetang.bean.internal.response;

import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/1/2016.
 */
public class BingVideoSearchEntity {
    private String name;
    private String description;
    private String webSearchUrl;
    private String webSearchUrlPingSuffix;
    private String thumbnailUrl;
    private String datePublished;
    private List<Map<String, Object>> publisher;
    private Map<String, Object> creator;
    private String contentUrl;
    private String hostPageUrl;
    private String hostPageUrlPingSuffix;
    private String encodingFormat;
    private String hostPageDisplayUrl;
    private String width;
    private String height;
    private String duration;
    private String motionThumbnailUrl;
    private String embedHtml;
    private String allowHttpsEmbed;
    private String viewCount;
    private Map<String, Object> thumbnail;
    private String videoId;
    private boolean allowMobileEmbed;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getWebSearchUrl() {
        return webSearchUrl;
    }

    public void setWebSearchUrl(String webSearchUrl) {
        this.webSearchUrl = webSearchUrl;
    }

    public String getWebSearchUrlPingSuffix() {
        return webSearchUrlPingSuffix;
    }

    public void setWebSearchUrlPingSuffix(String webSearchUrlPingSuffix) {
        this.webSearchUrlPingSuffix = webSearchUrlPingSuffix;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String getDatePublished() {
        return datePublished;
    }

    public void setDatePublished(String datePublished) {
        this.datePublished = datePublished;
    }

    public List<Map<String, Object>> getPublisher() {
        return publisher;
    }

    public void setPublisher(List<Map<String, Object>> publisher) {
        this.publisher = publisher;
    }

    public Map<String, Object> getCreator() {
        return creator;
    }

    public void setCreator(Map<String, Object> creator) {
        this.creator = creator;
    }

    public String getContentUrl() {
        return contentUrl;
    }

    public void setContentUrl(String contentUrl) {
        this.contentUrl = contentUrl;
    }

    public String getHostPageUrl() {
        return hostPageUrl;
    }

    public void setHostPageUrl(String hostPageUrl) {
        this.hostPageUrl = hostPageUrl;
    }

    public String getHostPageUrlPingSuffix() {
        return hostPageUrlPingSuffix;
    }

    public void setHostPageUrlPingSuffix(String hostPageUrlPingSuffix) {
        this.hostPageUrlPingSuffix = hostPageUrlPingSuffix;
    }

    public String getEncodingFormat() {
        return encodingFormat;
    }

    public void setEncodingFormat(String encodingFormat) {
        this.encodingFormat = encodingFormat;
    }

    public String getHostPageDisplayUrl() {
        return hostPageDisplayUrl;
    }

    public void setHostPageDisplayUrl(String hostPageDisplayUrl) {
        this.hostPageDisplayUrl = hostPageDisplayUrl;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getMotionThumbnailUrl() {
        return motionThumbnailUrl;
    }

    public void setMotionThumbnailUrl(String motionThumbnailUrl) {
        this.motionThumbnailUrl = motionThumbnailUrl;
    }

    public String getEmbedHtml() {
        return embedHtml;
    }

    public void setEmbedHtml(String embedHtml) {
        this.embedHtml = embedHtml;
    }

    public String getAllowHttpsEmbed() {
        return allowHttpsEmbed;
    }

    public void setAllowHttpsEmbed(String allowHttpsEmbed) {
        this.allowHttpsEmbed = allowHttpsEmbed;
    }

    public String getViewCount() {
        return viewCount;
    }

    public void setViewCount(String viewCount) {
        this.viewCount = viewCount;
    }

    public Map<String, Object> getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Map<String, Object> thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }

    public boolean isAllowMobileEmbed() {
        return allowMobileEmbed;
    }

    public void setAllowMobileEmbed(boolean allowMobileEmbed) {
        this.allowMobileEmbed = allowMobileEmbed;
    }
}
