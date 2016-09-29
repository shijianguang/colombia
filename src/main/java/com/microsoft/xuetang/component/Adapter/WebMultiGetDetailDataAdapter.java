package com.microsoft.xuetang.component.Adapter;

import com.microsoft.xuetang.bean.schema.response.detail.WebDetailData;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
public class WebMultiGetDetailDataAdapter extends ElasticSearchComponent.MultiGetResponseAdapter<Map<String, WebDetailData>> {
    private static WebGetDetailDataAdapter singleAdapter = new WebGetDetailDataAdapter();
    @Override
    public Map<String, WebDetailData> trasform(MultiGetResponse data) throws Exception {
        Map<String, WebDetailData> result = new HashMap<>();
        for(MultiGetItemResponse item : data) {
            GetResponse getResponse = item.getResponse();
            if(getResponse.isExists()) {
                try {
                    WebDetailData detailData = singleAdapter.trasform(getResponse);
                    if(detailData != null) {
                        result.put(detailData.getId(), detailData);
                    }
                } catch (Throwable t) {

                }
            }
        }

        return result;
    }
}
