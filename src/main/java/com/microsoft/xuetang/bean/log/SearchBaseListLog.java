package com.microsoft.xuetang.bean.log;

import java.util.List;

/**
 * Created by shijianguang on 9/8/16.
 */

public class SearchBaseListLog {
    private String desc;
    private List<SearchLogEntity> entityList;

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public List<SearchLogEntity> getEntityList() {
        return entityList;
    }

    public void setEntityList(List<SearchLogEntity> entityList) {
        this.entityList = entityList;
    }
}
