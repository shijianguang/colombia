package com.microsoft.xuetang.component.Adapter;

import com.microsoft.xuetang.bean.schema.response.detail.WebDetailData;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import org.elasticsearch.action.get.GetResponse;

import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
public class WebGetDetailDataAdapter extends ElasticSearchComponent.GetResponseAdapter<WebDetailData> {
    @Override
    public WebDetailData trasform(GetResponse data) throws Exception {
        if(data == null) {
            return null;
        }

        Map<String, Object> source = data.getSource();

        if(source == null) {
            return null;
        }

        WebDetailData detailData = new WebDetailData();
        detailData.setId(data.getId());

        Object value = null;
        value = source.get("Url");
        detailData.setUrl(value == null ? "" : value.toString());

        value = source.get("Abstract");
        detailData.setSnippet(value == null ? "" : value.toString());

        value = source.get("ImageUrl");
        detailData.setImageUrl(value == null ? "" : value.toString());

        value = source.get("Title");
        String title = null;
        if(value instanceof String) {
            title = (String)value;
            title = title.trim();
        }

        detailData.setTitle(title);

        value = source.get("Keywords");
        detailData.setKeywords(value instanceof List ? (List)value : null);

        value = source.get("Categories");
        detailData.setCategories(value instanceof List ? (List)value : null);

        value = source.get("CachedContent");
        detailData.setHasCachedContent(value instanceof Boolean ? (Boolean)value : null);

        return detailData;
    }
}
