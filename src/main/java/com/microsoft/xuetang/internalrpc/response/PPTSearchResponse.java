package com.microsoft.xuetang.internalrpc.response;

import com.microsoft.xuetang.bean.internal.response.PPTSearchViewEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jiash on 8/1/2016.
 */
public class PPTSearchResponse {
    private List<PPTSearchViewEntity> pptSearchEntities;

    public List<PPTSearchViewEntity> getPptSearchEntities() {
        return pptSearchEntities;
    }

    public void setPptSearchEntities(List<PPTSearchViewEntity> pptSearchEntities) {
        this.pptSearchEntities = pptSearchEntities;
    }

    public void addPPTSearchEntity(PPTSearchViewEntity entity) {
        if(pptSearchEntities == null) {
            pptSearchEntities = new ArrayList<>();
        }

        pptSearchEntities.add(entity);
    }
}
