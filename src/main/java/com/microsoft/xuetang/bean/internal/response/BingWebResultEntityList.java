package com.microsoft.xuetang.bean.internal.response;

import java.util.List;

/**
 * Created by jiash on 7/29/2016.
 */
public class BingWebResultEntityList {
    private String webSearchUrl;
    private String webSearchUrlPingSuffix;
    private long totalEstimatedMatches;
    private List<BingWebResultEntity> value;

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

    public long getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    public void setTotalEstimatedMatches(long totalEstimatedMatches) {
        this.totalEstimatedMatches = totalEstimatedMatches;
    }

    public List<BingWebResultEntity> getValue() {
        return value;
    }

    public void setValue(List<BingWebResultEntity> value) {
        this.value = value;
    }
}
