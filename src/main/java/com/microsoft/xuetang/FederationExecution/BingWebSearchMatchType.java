package com.microsoft.xuetang.FederationExecution;

import com.microsoft.xuetang.Federation.FederationContext;
import com.microsoft.xuetang.Federation.FederationExecution;
import com.microsoft.xuetang.bean.internal.response.BingWebResultEntity;
import com.microsoft.xuetang.bean.schema.response.detail.WebDetailData;
import com.microsoft.xuetang.component.Adapter.WebMultiGetDetailDataAdapter;
import com.microsoft.xuetang.component.BaseHttpComponent;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import com.microsoft.xuetang.internalrpc.response.BingWebSearchResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
public class BingWebSearchMatchType extends FederationExecution<BingWebSearchResponse> {
    private static final Logger logger = LoggerFactory.getLogger(BingWebSearchMatchType.class);
    private static final String BING_URL = "https://www.bingapis.com/api/v5/search";

    @Autowired
    private ElasticSearchComponent elasticSearchComponent;
    @Override
    public BingWebSearchResponse execute(FederationContext<BingWebSearchResponse> context) {
        String query = context.getString("query");
        String count = context.getString("count");
        String offset = context.getString("offset");
        String fligth = context.getString("fligth");

        if(query == null || count == null || offset == null) {
            return null;
        }

        Map<String, Object> param = new HashMap<String, Object>()
        {
            {
                put("appid", "D41D8CD98F00B204E9800998ECF8427E461DC0E7");
                put("mkt", "zh-cn");
                put("responseFilter", "WebPages");
                put("sf", fligth);
                put("q", query);
            }
        };

        BingWebSearchResponse response = null;
        try {
            response = BaseHttpComponent.fluentSyncGet(BING_URL, param, BingWebSearchResponse.class);
        } catch (Exception e) {
            logger.error("Request bing web result api encounter Exception. Message: {}", e.getMessage());
        }

        List<String> ids = new ArrayList<>();
        if(response != null && response.getWebPages() != null) {
            List<BingWebResultEntity> entityList = response.getWebPages().getValue();
            if(entityList != null && entityList.size() > 0) {
                Map<String, BingWebResultEntity> id2Entity = new HashMap<>();
                for (BingWebResultEntity entity : entityList) {
                    String url = entity.getUrl();
                    if(url != null) {
                        String id = DigestUtils.md5DigestAsHex(url.getBytes());
                        ids.add(id);
                        id2Entity.put(id, entity);
                    }
                }
                try {
                    Map<String, WebDetailData> richData = elasticSearchComponent.syncMultiGet(ids, new ElasticSearchComponent.DefaultMulitiGetRequestAdapte(), new WebMultiGetDetailDataAdapter());
                    for(Map.Entry<String, BingWebResultEntity> entry : id2Entity.entrySet()) {
                        String id = entry.getKey();
                        BingWebResultEntity bingWebResultEntity = entry.getValue();
                        WebDetailData richDataEntity = richData.get(id);
                        if(richDataEntity == null) {
                            logger.debug("Id: {}, url: {} do not get rich data.", id, bingWebResultEntity.getUrl());
                        } else {
                            bingWebResultEntity.setAbstractContent(richDataEntity.getSnippet());
                        }
                    }
                } catch (Exception e) {
                    logger.error("Backfill web result rich data encounter fatal error. Query: {}. Count: {}. Offset: {}. Flight: {}. Message: {}", query, count, offset, fligth, e.getMessage());
                }

            }

        }
        return response;
    }
}
