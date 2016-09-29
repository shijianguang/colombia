package com.microsoft.xuetang.component.Adapter;

import com.microsoft.xuetang.bean.schema.response.detail.MultiMediaDetailData;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiash on 8/11/2016.
 */
public class MultiMediaMultiGetDetailDataAdapter extends ElasticSearchComponent.MultiGetResponseAdapter<Map<String, MultiMediaDetailData>> {
    private static MultiMediaGetDetailDataAdapter singleAdapter = new MultiMediaGetDetailDataAdapter();
    @Override
    public Map<String, MultiMediaDetailData> trasform(MultiGetResponse data) throws Exception {
        Map<String, MultiMediaDetailData> result = new HashMap<>();
        for(MultiGetItemResponse item : data) {
            GetResponse getResponse = item.getResponse();
            if(getResponse.isExists()) {
                try {
                    MultiMediaDetailData detailData = singleAdapter.trasform(getResponse);
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
