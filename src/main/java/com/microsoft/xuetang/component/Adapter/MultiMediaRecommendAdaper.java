package com.microsoft.xuetang.component.Adapter;

import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.internal.response.QueryKeyword;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import com.microsoft.xuetang.util.CommonUtils;
import com.microsoft.xuetang.util.Constants;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;

/**
 * Created by shijianguang on 10/12/16.
 */
public class MultiMediaRecommendAdaper {
    public static class RequestAdaper extends ElasticSearchComponent.SearchRequestAdapter<SearchApiRequest> {

        private static final Logger logger = LoggerFactory.getLogger(RequestAdaper.class);

        public RequestAdaper(String index, String type) {
            super(index, type);
        }

        @Override
        public SearchRequestBuilder build(SearchRequestBuilder newBuilder, SearchApiRequest searchApiRequest) throws Exception {

            newBuilder.setTypes(getType()).setFrom(searchApiRequest.getOffsetInt()).setSize(searchApiRequest.getCountInt());

            List<QueryKeyword> keywords = searchApiRequest.getKeywords();
            BoolQueryBuilder keywordsBuilder = QueryBuilders.boolQuery();
            for (QueryKeyword ele : keywords) {
                keywordsBuilder.should(QueryBuilders.matchQuery("Keywords", ele.getTerm()));
            }
            newBuilder.setQuery(QueryBuilders.boolQuery().should(keywordsBuilder));
            logger.info(newBuilder.toString());
            return newBuilder;

        }
    }

    public static class ResponseAdaper extends ElasticSearchComponent.SearchResponseAdapter<SearchList<SearchElementData>> {

        @Override
        public SearchList<SearchElementData> trasform(SearchResponse response) throws Exception {
            SearchList<SearchElementData> result = new SearchList<>();
            for (SearchHit hit : response.getHits()) {
                Map<String, Object> data = hit.getSource();
                SearchElementData entity = new SearchElementData();
                entity.setId(hit.getId());
                Object value = null;
                value = data.get("Publisher");
                entity.setSinglePublisher(value instanceof String ? (String)value : null);
                value = data.get("Title");
                entity.setTitle(value instanceof String ? (String)value : null);
                value = data.get("Abstract");
                if(value instanceof String) {
                    String abstractContent = CommonUtils.trimBefore((String) value, Constants.NEW_LINE_CHARACTER_ARRAY);
                    entity.setSnippet(abstractContent);
                } else {
                    entity.setSnippet(null);
                }
                value = data.get("ImageUrl");
                entity.setImageUrl(value instanceof String ? (String)value : null);
                value = data.get("Url");
                entity.setUrl(value instanceof String ? (String)value : null);
                value = data.get("ResourceType");
                entity.setType(value instanceof String ? (String)value : null);
                value = data.get("ClusterId");
                entity.setVenu(value instanceof String ? (String)value : null);

                result.add(entity);
            }

            return result;
        }
    }
}
