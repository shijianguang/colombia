package com.microsoft.xuetang.schema.response.search;

import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.internal.response.DialogueEngineSearchEntity;
import com.microsoft.xuetang.bean.schema.response.search.BaseSearchViewEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
public class SearchApiResponse {
    private static final SearchList<BaseSearchViewEntity> EMPTY_SEARCH_LIST = new SearchList<>();
    private static final SearchList<DialogueEngineSearchEntity> EMPTY_ACADEMIC_SEARCH_LIST = new SearchList<>();
    private static final Map<String, SearchList> EMPTY_MULTIMEDIA_RESULT = new HashMap<String, SearchList>()
    {
        {
            put("ppt", new SearchList());
            put("video", new SearchList());
        }
    };

    private SearchList<BaseSearchViewEntity> wikiList = EMPTY_SEARCH_LIST;
    private SearchList<BaseSearchViewEntity> webList = EMPTY_SEARCH_LIST;
    private Map<String, SearchList> multiMediaList = EMPTY_MULTIMEDIA_RESULT;
    private SearchList<DialogueEngineSearchEntity> paperList = EMPTY_ACADEMIC_SEARCH_LIST;

    public SearchList<BaseSearchViewEntity> getWikiList() {
        return wikiList;
    }

    public void setWikiList(SearchList<BaseSearchViewEntity> wikiList) {
        this.wikiList = wikiList;
    }

    public SearchList<BaseSearchViewEntity> getWebList() {
        return webList;
    }

    public void setWebList(SearchList<BaseSearchViewEntity> webList) {
        this.webList = webList;
    }

    public Map<String, SearchList> getMultiMediaList() {
        return multiMediaList;
    }

    public void setMultiMediaList(Map<String, SearchList> multiMediaList) {
        this.multiMediaList = multiMediaList;
    }

    public SearchList<DialogueEngineSearchEntity> getPaperList() {
        return paperList;
    }

    public void setPaperList(SearchList<DialogueEngineSearchEntity> paperList) {
        this.paperList = paperList;
    }
}
