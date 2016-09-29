package com.microsoft.xuetang.bean.log;

/**
 * Created by shijianguang on 9/8/16.
 */
public class SingleSearchResultLog extends SearchResultLog {
    private SearchBaseListLog entityLog;

    public SearchBaseListLog getEntityLog() {
        return entityLog;
    }

    public void setEntityLog(SearchBaseListLog entityLog) {
        this.entityLog = entityLog;
    }
}
