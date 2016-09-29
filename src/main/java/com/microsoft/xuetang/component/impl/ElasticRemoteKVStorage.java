package com.microsoft.xuetang.component.impl;

import com.microsoft.xuetang.bean.cache.WebFeatureEntity;
import com.microsoft.xuetang.component.Adapter.WebFeatureMultiGetAapter;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import com.microsoft.xuetang.component.RemoteKVStorage;

import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/9/2016.
 */
public class ElasticRemoteKVStorage extends RemoteKVStorage<WebFeatureEntity> {
    //@Autowired
    private ElasticSearchComponent elasticSearchComponent =  new ElasticSearchComponent();
    @Override
    public WebFeatureEntity get(String db, String namespace, String id) {
        return null;
    }

    @Override
    public Map<String, WebFeatureEntity> get(String db, String namespace, List<String> idList) {
        try {
            return elasticSearchComponent.syncMultiGet(idList, new ElasticSearchComponent.DefaultMulitiGetRequestAdapte(db, namespace), new WebFeatureMultiGetAapter.ResponseAdapter());
        } catch (Exception e) {
            return null;
        }
    }
}
