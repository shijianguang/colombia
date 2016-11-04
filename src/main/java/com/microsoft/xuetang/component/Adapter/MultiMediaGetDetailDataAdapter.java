package com.microsoft.xuetang.component.Adapter;

import com.microsoft.xuetang.bean.schema.response.detail.MultiMediaDetailData;
import com.microsoft.xuetang.bean.schema.response.detail.RelatedContent;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import javafx.beans.binding.MapExpression;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.get.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
public class MultiMediaGetDetailDataAdapter extends ElasticSearchComponent.GetResponseAdapter<MultiMediaDetailData> {
    @Override
    public MultiMediaDetailData trasform(GetResponse data) throws Exception {
        if(data == null) {
            return null;
        }

        Map<String, Object> source = data.getSource();
        if(source == null) {
            return null;
        }

        MultiMediaDetailData detailData = new MultiMediaDetailData();

        detailData.setId(data.getId());

        Object value = null;

        value = source.get("Url");
        detailData.setUrl(value instanceof String ? (String)value : null);

        value = source.get("ResourceType");
        detailData.setType(value instanceof String ? (String)value : null);

        value = source.get("Abstract");
        detailData.setSnippet(value instanceof String ? (String)value : null);

        value = source.get("PublisherOther");
        detailData.setPublisherOther(value instanceof String ? (String)value : null);

        value = source.get("Publisher");
        detailData.setPublisher(value instanceof String ? (String)value : null);

        value = source.get("Title");
        detailData.setTitle(value instanceof String ? (String)value : null);

        value = source.get("ResourceUri");
        detailData.setResourceUri(value instanceof String ? (String)value : null);

        value = source.get("ResourceDomain");
        detailData.setResourceDomain(value instanceof String ? (String)value : null);

        value = source.get("PublishDate");
        detailData.setPublishDate(value instanceof String ? (String)value : null);

        value = source.get("IconImageUrl");
        detailData.setIconUrl(value instanceof String ? (String)value : null);

        value = source.get("ImageUrl");
        detailData.setImageUrl(value instanceof String ? (String)value : null);

        value = source.get("Keywords");
        detailData.setKeywords(!(value instanceof List) ? null : (List<String>)value);

        value = source.get("Categories");
        detailData.setCategories(!(value instanceof List) ? null : (List<String>)value);

        value = source.get("ClusterId");
        detailData.setClusterId(value instanceof String ? (String)value : null);

        value = source.get("Related");
        if(value instanceof List) {
            List<RelatedContent> relatedContents = new ArrayList<>();
            ((List) value).stream().filter(ele -> ele instanceof Map).forEach(ele -> {
                Map<String, String> mapEle = (Map) ele;
                String id = mapEle.get("Id");
                String title = mapEle.get("Title");
                String imageUrl = mapEle.get("ImageUrl");
                if (StringUtils.isNotBlank(id) && StringUtils.isNotBlank(title) && StringUtils.isNotBlank(imageUrl)) {
                    relatedContents.add(new RelatedContent(id, imageUrl, title));
                }
            });

            detailData.setRelatedContents(relatedContents);
        }
        detailData.setClusterId(value instanceof String ? (String)value : null);


        return detailData;
    }
}