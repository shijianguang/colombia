package com.microsoft.xuetang.internalrpc.response;

import com.microsoft.xuetang.bean.internal.response.QueryKeyword;
import com.microsoft.xuetang.bean.internal.response.QueryRelatedEntity;

import java.util.List;

/**
 * Created by jiash on 8/25/2016.
 */
public class QueryUnderstandingResponse {
    private String query;
    private List<QueryKeyword> keywords;
    private List<QueryRelatedEntity> relatedQuery;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<QueryKeyword> getKeywords() {
        return keywords;
    }

    public void setKeywords(List<QueryKeyword> keywords) {
        this.keywords = keywords;
    }

    public List<QueryRelatedEntity> getRelatedQuery() {
        return relatedQuery;
    }

    public void setRelatedQuery(List<QueryRelatedEntity> relatedQuery) {
        this.relatedQuery = relatedQuery;
    }
}
