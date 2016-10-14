package com.microsoft.xuetang.schema.response.search;

import com.microsoft.xuetang.component.UserProfileComponent;

import java.util.List;

/**
 * Created by shijianguang on 10/14/16.
 */
public class QueryBallResponse {
    private List<String> queryList = UserProfileComponent.getQueryBall();

    public List<String> getQueryList() {
        return queryList;
    }

    public void setQueryList(List<String> queryList) {
        this.queryList = queryList;
    }
}
