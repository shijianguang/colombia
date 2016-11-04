package com.microsoft.xuetang.bean.schema.response.search;

/**
 * Created by shijianguang on 10/24/16.
 */
public class QueryRelatedEntity {
    private String entity;

    public QueryRelatedEntity() {

    }

    public QueryRelatedEntity(String entity) {
        this.entity = entity;
    }

    public String getEntity() {
        return entity;
    }

    public void setEntity(String entity) {
        this.entity = entity;
    }
}
