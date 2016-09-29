package com.microsoft.xuetang.schema.response.search;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiash on 8/1/2016.
 */
public class PopularSearchResponse {
    private List<String> popularQuery = new ArrayList<>();

    public PopularSearchResponse() {
        popularQuery.add("Data Mining");
        popularQuery.add("人工智能");
        popularQuery.add("Deep Learning");
        popularQuery.add("机器学习");
        popularQuery.add("Spark Streaming");
    }
    public List<String> getPopularQuery() {
        return popularQuery;
    }

    public void setPopularQuery(List<String> popularQuery) {
        this.popularQuery = popularQuery;
    }
}
