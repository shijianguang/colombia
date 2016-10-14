package com.microsoft.xuetang.bean.schema.response.search;

import com.microsoft.xuetang.bean.EntityBean;
import com.microsoft.xuetang.bean.internal.response.BingAcademicSearchEntity;
import com.microsoft.xuetang.bean.internal.response.DialogueEngineSearchEntity;
import com.microsoft.xuetang.bean.internal.response.BingWebResultEntity;
import com.microsoft.xuetang.util.Constants;
import com.microsoft.xuetang.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.util.DigestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
public class SearchElementData {
    private String id;
    private String title;
    private String url;
    private String snippet;
    private String imageUrl;
    private String logoImageUrl;
    private List<String> publisher;
    private String singlePublisher;
    private String venu;
    private String type;
    private String year;

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

    public String getLogoImageUrl() {
        return logoImageUrl;
    }

    public void setLogoImageUrl(String logoImageUrl) {
        this.logoImageUrl = logoImageUrl;
    }

    public List<String> getPublisher() {
        return publisher;
    }

    public void setPublisher(List<String> publisher) {
        this.publisher = publisher;
    }

    public String getSinglePublisher() {
        return singlePublisher;
    }

    public void setSinglePublisher(String singlePublisher) {
        this.singlePublisher = singlePublisher;
    }

    public String getVenu() {
        return venu;
    }

    public void setVenu(String venu) {
        this.venu = venu;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public static SearchElementData transform(BingWebResultEntity entity, String type) {
        if(entity == null) {
            return null;
        }
        SearchElementData data = new SearchElementData();
        data.setType(type);
        data.setUrl(entity.getUrl());
        data.setId(entity.getId());
        data.setTitle(entity.getName());
        data.setImageUrl(entity.getThumbnailUrl());
        data.setSnippet(entity.getSnippet());

        return data;
    }

    public static SearchElementData transform(DialogueEngineSearchEntity entity) {
        if(entity == null || entity.getId() == null) {
            return null;
        }
        SearchElementData data = new SearchElementData();
        data.setId(entity.getId().toString());
        data.setType(Constants.DataType.PAPER);
        data.setTitle(entity.getTi());
        data.setLogoImageUrl(Constants.ImageResource.XUETANG_URL);
        data.setYear(entity.getY());
        String extraInfo = entity.getE();
        if(StringUtils.isNotEmpty(extraInfo)) {
            try {
                Map<String, Object> extraInfoMap = JsonUtil.fromJson(extraInfo, HashMap.class);
                if(extraInfoMap != null) {
                    Object title = extraInfoMap.get("DN");
                    if(title instanceof String) {
                        data.setTitle((String)title);
                    }

                    Object snippet = extraInfoMap.get("D");
                    if(snippet instanceof String) {
                        data.setSnippet((String)snippet);
                    }
                }
            } catch (IOException e) {
            }
        }

        List<Map<String, Object>> authors = entity.getAA();
        List<String> authorList = new ArrayList<>();
        if(authors != null) {
            for (Map<String, Object> map : authors) {
                Object authorName = map.get("DAuN");
                if (authorName instanceof String) {
                    authorList.add((String) authorName);
                }
            }
        }
        data.setPublisher(authorList);

        return data;
    }

    public static SearchElementData transform(BingAcademicSearchEntity entity) {
        if(entity == null || entity.getId() == null) {
            return null;
        }
        SearchElementData data = new SearchElementData();
        data.setId(entity.getId().toString());
        data.setType(Constants.DataType.PAPER);
        data.setTitle(entity.getTitle());
        data.setLogoImageUrl(Constants.ImageResource.XUETANG_URL);
        data.setYear(entity.getPublishYear());
        data.setSnippet(entity.getSnippet());

        List<EntityBean> authorBeans = entity.getAuthors();
        List<String> authorNames = new ArrayList<>();
        if(authorBeans != null) {
            for(EntityBean bean : authorBeans) {
                authorNames.add(bean.getName());
            }
        }

        /** This action is used to bypass a bug in app version 1.0 */
        if(authorNames.size() == 0) {
            authorNames.add("");
        }

        data.setPublisher(authorNames);

        return data;
    }
}
