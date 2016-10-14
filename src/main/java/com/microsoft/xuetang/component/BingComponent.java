package com.microsoft.xuetang.component;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.microsoft.xuetang.bean.cache.WebFeatureEntity;
import com.microsoft.xuetang.bean.internal.response.BingWebResultEntity;
import com.microsoft.xuetang.bean.internal.response.BingWebResultEntityList;
import com.microsoft.xuetang.internalrpc.response.BingWebSearchResponse;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import com.microsoft.xuetang.util.Constants;
import com.microsoft.xuetang.util.LogUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * Created by shijianguang on 10/11/16.
 * Do some logic to bing http result
 */
@Component
public class BingComponent {
    private static final Logger logger = LoggerFactory.getLogger(BingComponent.class);
    private static final Logger performancelogger = LoggerFactory.getLogger(Constants.Log.PERFORMANCE_LOGGER_NAME);
    @Autowired
    private FeatureServerComponent featureServerComponent;

    public BingWebSearchResponse getBingWebSearchResponseWithFeature(SearchApiRequest searchApiRequest, String type) {

        BingWebSearchResponse response = null;
        long t1 = System.currentTimeMillis();
        try {
            response = BingHttpComponent
                    .getBingWebSearchResponse(searchApiRequest);
        } catch (Exception e) {
            logger.error("Request bing search result api encounter Exception. TraceId: {}. Flight: {}. Message: {}", searchApiRequest.getTraceId(), searchApiRequest.getFlight(), e.getMessage());
        }
        long t2 = System.currentTimeMillis();
        if (response == null || response.getWebPages() == null) {
            return response;
        }

        BingWebResultEntityList webPages = response.getWebPages();
        List<BingWebResultEntity> rawResultEntityList = webPages.getValue();
        if (rawResultEntityList == null || rawResultEntityList.size() == 0) {
            return response;
        }

        List<BingWebResultEntity> resultEntityList = new ArrayList<>(rawResultEntityList.size());
        rawResultEntityList.stream().filter(new Predicate<BingWebResultEntity>() {
            @Override public boolean test(BingWebResultEntity bingWebResultEntity) {
                return bingWebResultEntity != null && StringUtils.isNotBlank(bingWebResultEntity.getUrl());
            }
        }).forEach(new Consumer<BingWebResultEntity>() {
            @Override public void accept(BingWebResultEntity bingWebResultEntity) {
                bingWebResultEntity.setId(DigestUtils.md5DigestAsHex(bingWebResultEntity.getUrl().getBytes()));
                resultEntityList.add(bingWebResultEntity);
            }
        });

        List<BingWebResultEntity> newResultEntityList = new ArrayList<>(resultEntityList.size());
        List<String> idList = Lists.transform(resultEntityList, new Function<BingWebResultEntity, String>() {
            @Override
            public String apply(BingWebResultEntity input) {
                return input.getId();
            }
        });

        long t3 = System.currentTimeMillis();
        String[] kvType = Constants.DataTypeKVMap.get(type);
        Map<String, WebFeatureEntity> id2Feature = featureServerComponent.get(kvType[0], kvType[1], idList);
        resultEntityList.stream().forEach(new Consumer<BingWebResultEntity>() {
            @Override public void accept(BingWebResultEntity bingWebResultEntity) {
                String id = bingWebResultEntity.getId();
                WebFeatureEntity feature = id2Feature.get(id);
                if (feature != null) {
                    if (feature.getImageUrl() != null) {
                        bingWebResultEntity.setThumbnailUrl(feature.getImageUrl());
                    }
                    if (feature.getTitle() != null) {
                        bingWebResultEntity.setName(feature.getTitle());
                    }

                    newResultEntityList.add(bingWebResultEntity);
                }
            }
        });

        webPages.setValue(newResultEntityList);
        long t4 = System.currentTimeMillis();
        logger.info(
                "Bing search type: {} result count: {}, after invalid filter count: {}, after backfill feature result count: {}",
                type,
                rawResultEntityList.size(), resultEntityList.size(), newResultEntityList.size());

        LogUtils.infoLogPerformance(performancelogger, searchApiRequest, t2 - t1, "http", "bing", "web",
                searchApiRequest.getFlight());
        LogUtils.infoLogPerformance(performancelogger, searchApiRequest, t4 - t3, "feature", "backfill", searchApiRequest.getFlight());
        return response;
    }

}
