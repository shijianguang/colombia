package com.microsoft.xuetang.bean.internal.response;

import com.microsoft.xuetang.bean.EntityBean;

import java.util.List;

/**
 * Created by shijianguang on 9/19/16.
 */
public class BingAcademicSearchEntity {
    private String id;
    private String title;
    private String url;
    private String publishYear;
    private List<EntityBean> authors;
    private List<EntityBean> affiliations;
    private List<EntityBean> fieldOfStudy;
    private String snippet;
    private String venue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPublishYear() {
        return publishYear;
    }

    public void setPublishYear(String publishYear) {
        this.publishYear = publishYear;
    }

    public List<EntityBean> getAuthors() {
        return authors;
    }

    public void setAuthors(List<EntityBean> authors) {
        this.authors = authors;
    }

    public List<EntityBean> getAffiliations() {
        return affiliations;
    }

    public void setAffiliations(List<EntityBean> affiliations) {
        this.affiliations = affiliations;
    }

    public List<EntityBean> getFieldOfStudy() {
        return fieldOfStudy;
    }

    public void setFieldOfStudy(List<EntityBean> fieldOfStudy) {
        this.fieldOfStudy = fieldOfStudy;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }
}
