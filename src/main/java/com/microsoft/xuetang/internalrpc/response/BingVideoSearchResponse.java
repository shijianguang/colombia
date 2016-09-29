package com.microsoft.xuetang.internalrpc.response;

import com.microsoft.xuetang.bean.internal.response.BingVideoSearchEntity;

import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/1/2016.
 */
public class BingVideoSearchResponse {
    private Map<String,String> instrumentation;
    private String readLink;
    private String webSearchUrl;
    private String webSearchUrlPingSuffix;
    private String totalEstimatedMatches;
    private List<BingVideoSearchEntity> value;

    public Map<String, String> getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Map<String, String> instrumentation) {
        this.instrumentation = instrumentation;
    }

    public String getReadLink() {
        return readLink;
    }

    public void setReadLink(String readLink) {
        this.readLink = readLink;
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

    public String getTotalEstimatedMatches() {
        return totalEstimatedMatches;
    }

    public void setTotalEstimatedMatches(String totalEstimatedMatches) {
        this.totalEstimatedMatches = totalEstimatedMatches;
    }

    public List<BingVideoSearchEntity> getValue() {
        return value;
    }

    public void setValue(List<BingVideoSearchEntity> value) {
        this.value = value;
    }
}
