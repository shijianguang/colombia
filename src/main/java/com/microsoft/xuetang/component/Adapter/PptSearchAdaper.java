package com.microsoft.xuetang.component.Adapter;

import com.microsoft.xuetang.bean.internal.response.PPTSearchViewEntity;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import com.microsoft.xuetang.internalrpc.response.PPTSearchResponse;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import org.apache.commons.lang.StringEscapeUtils;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.util.Map;

/**
 * Created by jiash on 8/1/2016.
 */
public class PptSearchAdaper {
    public static class RequestAdaper extends ElasticSearchComponent.SearchRequestAdapter<SearchApiRequest> {

        @Override
        public SearchRequestBuilder build(SearchRequestBuilder newBuilder, SearchApiRequest searchApiRequest) throws Exception {

            newBuilder.setTypes("ppt")
                    .setFrom(searchApiRequest.getOffsetInt())
                    .setSize(searchApiRequest.getCountInt())
                    .setQuery(
                            QueryBuilders.boolQuery()
                                    .should(QueryBuilders.matchQuery("Title", searchApiRequest.getQuery()).boost(2))
                                    .should(QueryBuilders.matchQuery("Abstract", searchApiRequest.getQuery()))
                    );

            return newBuilder;

        }
    }

    public static class ResponseAdaper extends ElasticSearchComponent.SearchResponseAdapter<PPTSearchResponse> {

        @Override
        public PPTSearchResponse trasform(SearchResponse response) throws Exception {
            PPTSearchResponse pptSearchResponse = new PPTSearchResponse();
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> data = hit.getSource();
                PPTSearchViewEntity entity = new PPTSearchViewEntity();
                entity.setId(hit.getId());
                Object value = null;
                value = data.get("PublisherOther");
                entity.setPublisherOther(value == null ? "" : value.toString());
                value = data.get("Publisher");
                entity.setPublisher(value == null ? "" : value.toString());
                value = data.get("Title");
                entity.setName(value == null ? "" : value.toString());
                value = data.get("Abstract");
                entity.setSnippet(value == null ? "" : StringEscapeUtils.unescapeHtml(value.toString()));
                value = data.get("FileUrl");
                entity.setPptImageUrls(value == null ? "" : value.toString());
                if(value != null) {
                    String pptImageUrl = value.toString();
                    String[] pptImageUrlArray = pptImageUrl.split(" ");
                    if(pptImageUrlArray != null && pptImageUrl.length() > 0) {
                        String image = pptImageUrlArray[0];
                        if(image != null) {
                            image = image.replace("slide-thumb-1=", "");
                            entity.setImageUrl(image);
                        }
                    }
                }
                value = data.get("Url");
                entity.setDisplayUrl(value == null ? "" : value.toString());
                entity.setUrl(value == null ? "" : value.toString());

                pptSearchResponse.addPPTSearchEntity(entity);
            }

            return pptSearchResponse;
        }
    }
}
