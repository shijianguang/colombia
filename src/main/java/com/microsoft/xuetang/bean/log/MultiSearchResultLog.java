package com.microsoft.xuetang.bean.log;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by shijianguang on 9/8/16.
 */
public class MultiSearchResultLog extends SearchResultLog {
    private Map<String, SearchBaseListLog> entityLog;

    public Map<String, SearchBaseListLog> getEntityLog() {
        return entityLog;
    }

    public void setEntityLog(Map<String, SearchBaseListLog> entityLog) {
        this.entityLog = entityLog;
    }

    public void setEntityLog(String key, SearchBaseListLog log) {
        if(entityLog == null) {
            entityLog = new HashMap<>();
        }

        entityLog.put(key, log);
    }
}
