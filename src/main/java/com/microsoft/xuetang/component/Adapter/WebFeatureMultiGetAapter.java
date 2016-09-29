package com.microsoft.xuetang.component.Adapter;

import com.microsoft.xuetang.bean.cache.WebFeatureEntity;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiash on 8/9/2016.
 */
public class WebFeatureMultiGetAapter {
    public static class ResponseAdapter extends ElasticSearchComponent.MultiGetResponseAdapter<Map<String, WebFeatureEntity>> {
        @Override
        public Map<String, WebFeatureEntity> trasform(MultiGetResponse data) throws Exception {
            Map<String, WebFeatureEntity> result = new HashMap<>();
            for (MultiGetItemResponse item : data) {
                GetResponse getResponse = item.getResponse();
                if (getResponse.isExists()) {
                    Map<String, Object> source = getResponse.getSource();
                    if (source == null) {
                        continue;
                    }
                    WebFeatureEntity entryData = new WebFeatureEntity();
                    Object value = null;

                    value = source.get("Title");
                    String title = null;
                    if(value instanceof String) {
                        title = (String)value;
                    }

                    title = title.trim();

                    if(title == null && title.length() == 0) {
                        continue;
                    }
                    entryData.setTitle(title);

                    value = source.get("ImageUrl");
                    entryData.setImageUrl(value instanceof String ? (String) value : null);
                    result.put(getResponse.getId(), entryData);
                    //result.put(getResponse.getId(), new WebDetailData(urlString, StringEscapeUtils.unescapeHtml(abstractContent.toString().replaceAll("#N#", ""))));
                }
            }

            return result;
        }
    }
}
