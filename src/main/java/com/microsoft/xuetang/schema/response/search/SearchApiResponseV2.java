package com.microsoft.xuetang.schema.response.search;

import com.microsoft.xuetang.bean.MultiSearchList;
import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;

/**
 * Created by jiash on 8/8/2016.
 */
public class SearchApiResponseV2 {
    public static final SearchList<SearchElementData> EMPTY_SEARCH_LIST = new SearchList<>();
    public static final MultiSearchList EMPTY_MULTI_SEARCH_LIST = new MultiSearchList();

    private SearchList<SearchElementData> wikiList = EMPTY_SEARCH_LIST;
    private SearchList<SearchElementData> webList = EMPTY_SEARCH_LIST;
    private MultiSearchList multiMediaList = EMPTY_MULTI_SEARCH_LIST;
    private SearchList<SearchElementData> paperList = EMPTY_SEARCH_LIST;

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
