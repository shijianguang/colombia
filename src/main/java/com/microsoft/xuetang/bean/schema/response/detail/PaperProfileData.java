package com.microsoft.xuetang.bean.schema.response.detail;

import com.microsoft.xuetang.internalrpc.response.BingPaperProfileResponse;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
public class PaperProfileData {
    private long id;
    private List<String> keywords;
    private String previewUrl;
    private String sourceUrl;
    private String title;
    private String publishDate;
    private String year;
    private String originalVenue;
    private String volume;
    private String issue;
    private String firstPage;
    private String lastPage;
    private String isbn;
    private String snippet;
    private int referenceCount;
    private int citationCount;
    private List<String> publisher;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<String> keywords) {
        this.keywords = keywords;
    }

    public String getPreviewUrl() {
        return previewUrl;
    }

    public void setPreviewUrl(String previewUrl) {
        this.previewUrl = previewUrl;
    }

    public String getSourceUrl() {
        return sourceUrl;
    }

    public void setSourceUrl(String sourceUrl) {
        this.sourceUrl = sourceUrl;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getOriginalVenue() {
        return originalVenue;
    }

    public void setOriginalVenue(String originalVenue) {
        this.originalVenue = originalVenue;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getFirstPage() {
        return firstPage;
    }

    public void setFirstPage(String firstPage) {
        this.firstPage = firstPage;
    }

    public String getLastPage() {
        return lastPage;
    }

    public void setLastPage(String lastPage) {
        this.lastPage = lastPage;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }


    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    public int getReferenceCount() {
        return referenceCount;
    }

    public void setReferenceCount(int referenceCount) {
        this.referenceCount = referenceCount;
    }

    public int getCitationCount() {
        return citationCount;
    }

    public void setCitationCount(int citationCount) {
        this.citationCount = citationCount;
    }

    public List<String> getPublisher() {
        return publisher;
    }

    public void setPublisher(List<String> publisher) {
        this.publisher = publisher;
    }

    public static PaperProfileData transform(BingPaperProfileResponse bingResponse) {
        if(bingResponse == null) {
            return null;
        }
        BingPaperProfileResponse.ResultPart resultPart = bingResponse.getResult();
        if(resultPart == null) {
            return null;
        }

        PaperProfileData data = new PaperProfileData();

        data.setId(resultPart.getId());
        data.setSnippet(resultPart.getAbstract());
        data.setCitationCount(resultPart.getCitationCount());
        data.setFirstPage(resultPart.getFirstPage());
        data.setIsbn(resultPart.getISBN());
        data.setIssue(resultPart.getIssue());
        data.setLastPage(resultPart.getLastPage());
        data.setOriginalVenue(resultPart.getOriginalVenue());
        data.setPreviewUrl(resultPart.getPreviewUrl());
        data.setPublishDate(resultPart.getPublishDate());
        data.setTitle(resultPart.getTitle());
        data.setYear(resultPart.getYear());
        data.setVolume(resultPart.getVolume());
        data.setReferenceCount(resultPart.getReferenceCount());

        List<Map<String, String>> sourceUrlList = resultPart.getSourceUrls();
        String sourceUrl = null;
        if(sourceUrlList != null) {
            for(Map<String, String> entity : sourceUrlList) {
                if(entity != null) {
                    String link = entity.get("link");
                    if(StringUtils.isNotEmpty(link)) {
                        if(!link.endsWith("pdf")) {
                            sourceUrl = link;
                            break;
                        }
                    }
                }
            }
        }

        data.setSourceUrl(sourceUrl);

        List<Map<String, Object>> authors = resultPart.getAuthors();
        List<String> publisherList = new ArrayList<>();
        if(authors != null) {
            for(Map<String, Object> entity : authors) {
                if(entity != null) {
                    Object name = entity.get("Name");
                    if(name instanceof String) {
                        publisherList.add((String) name);
                    }
                }
            }
        }
        data.setPublisher(publisherList);

        List<Map<String, Object>> fos = resultPart.getFieldsOfStudy();
        List<String> keywordList = new ArrayList<>();
        if(authors != null) {
            for(Map<String, Object> entity : fos) {
                if(entity != null) {
                    Object name = entity.get("Name");
                    if(name instanceof String) {
                        keywordList.add((String) name);
                    }
                }
            }
        }
        data.setKeywords(keywordList);
        return data;
    }
}
