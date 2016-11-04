package com.microsoft.xuetang.bean;

import com.microsoft.xuetang.bean.internal.response.QueryRelatedEntity;
import com.microsoft.xuetang.schema.response.search.SearchApiResponseV2;
import com.microsoft.xuetang.util.Constants;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/11/2016.
 */
public class MultiSearchList {
    private Map<String, SearchList> multiSearchList = new HashMap<String, SearchList>() {
        {
            put(Constants.DataType.PPT, SearchApiResponseV2.EMPTY_SEARCH_LIST);
            put(Constants.DataType.VIDEO, SearchApiResponseV2.EMPTY_SEARCH_LIST);
        }
    };

    private List<QueryRelatedEntity> relatedEntities;

    public Map<String, SearchList> getMultiSearchList() {
        return multiSearchList;
    }

    public void setMultiSearchList(Map<String, SearchList> multiSearchList) {
        this.multiSearchList = multiSearchList;
    }

    public void setSearchList(String key, SearchList searchList) {
        multiSearchList.put(key, searchList);
    }

    public SearchList getSearchList(String key) {
        return multiSearchList.get(key);
    }

    public List<QueryRelatedEntity> getRelatedEntities() {
        return relatedEntities;
    }

    public void setRelatedEntities(List<QueryRelatedEntity> relatedEntities) {
        this.relatedEntities = relatedEntities;
    }
}
