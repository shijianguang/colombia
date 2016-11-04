package com.microsoft.xuetang.schema.response.search;

import com.microsoft.xuetang.bean.MultiSearchList;
import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.internal.response.QueryRelatedEntity;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiash on 8/8/2016.
 */
public class SearchApiResponseV2 {
    public static final SearchList<SearchElementData> EMPTY_SEARCH_LIST = new SearchList<>();
    public static final MultiSearchList EMPTY_MULTI_SEARCH_LIST = new MultiSearchList();

    private String query;
    private List<String> recommendQuery = new ArrayList<>();
    private List<QueryRelatedEntity> relatedEntity = new ArrayList<>();
    /*{
        {
            add(new QueryRelatedEntity("Deep Learning"));
            add(new QueryRelatedEntity("神经网络"));
            add(new QueryRelatedEntity("深度学习"));
            add(new QueryRelatedEntity("人工智能"));
            add(new QueryRelatedEntity("自然语言处理"));
            add(new QueryRelatedEntity("Reinforcement Learning"));
            add(new QueryRelatedEntity("云计算"));
            add(new QueryRelatedEntity("Infrastructure as a Service"));
            add(new QueryRelatedEntity("Platform as a Service"));
            add(new QueryRelatedEntity("WEB开发"));
            add(new QueryRelatedEntity("Java Web"));
            add(new QueryRelatedEntity("WEB开发框架"));
            add(new QueryRelatedEntity("大数据"));
            add(new QueryRelatedEntity("Hadoop"));
            add(new QueryRelatedEntity("Spark Streaming"));
            add(new QueryRelatedEntity("机器学习"));
            add(new QueryRelatedEntity("Machine Learning"));
            add(new QueryRelatedEntity("Scikit Learn"));
            add(new QueryRelatedEntity("信息检索"));
            add(new QueryRelatedEntity("搜索引擎"));
        }
    };*/
    private SearchList<SearchElementData> wikiList = EMPTY_SEARCH_LIST;
    private SearchList<SearchElementData> webList = EMPTY_SEARCH_LIST;
    private MultiSearchList multiMediaList = EMPTY_MULTI_SEARCH_LIST;
    private SearchList<SearchElementData> paperList = EMPTY_SEARCH_LIST;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public List<String> getRecommendQuery() {
        return recommendQuery;
    }

    public void setRecommendQuery(List<String> recommendQuery) {
        this.recommendQuery = recommendQuery;
    }

    public List<QueryRelatedEntity> getRelatedEntity() {
        return relatedEntity;
    }

    public void setRelatedEntity(List<QueryRelatedEntity> relatedEntity) {
        this.relatedEntity = relatedEntity;
    }

    public SearchList<SearchElementData> getWikiList() {
        return wikiList;
    }

    public void setWikiList(SearchList<SearchElementData> wikiList) {
        this.wikiList = wikiList;
    }

    public SearchList<SearchElementData> getWebList() {
        return webList;
    }

    public void setWebList(SearchList<SearchElementData> webList) {
        this.webList = webList;
    }

    public MultiSearchList getMultiMediaList() {
        return multiMediaList;
    }

    public void setMultiMediaList(MultiSearchList multiMediaList) {
        this.multiMediaList = multiMediaList;
    }

    public SearchList<SearchElementData> getPaperList() {
        return paperList;
    }

    public void setPaperList(SearchList<SearchElementData> paperList) {
        this.paperList = paperList;
    }
}
