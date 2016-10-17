package com.microsoft.xuetang.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.microsoft.xuetang.Federation.FederationContext;
import com.microsoft.xuetang.Federation.FederationEngine;
import com.microsoft.xuetang.Federation.FederationExecution;
import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.internal.response.BingAcademicSearchEntity;
import com.microsoft.xuetang.bean.internal.response.BingWebResultEntity;
import com.microsoft.xuetang.bean.internal.response.QueryKeyword;
import com.microsoft.xuetang.bean.schema.response.search.BaseSearchViewEntity;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;
import com.microsoft.xuetang.component.Adapter.MultiMediaRecommendAdaper;
import com.microsoft.xuetang.component.*;
import com.microsoft.xuetang.internalrpc.response.BingAcademicSearchResponse;
import com.microsoft.xuetang.internalrpc.response.BingWebSearchResponse;
import com.microsoft.xuetang.schema.request.Request;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import com.microsoft.xuetang.util.Constants;
import com.microsoft.xuetang.util.LogUtils;
import com.microsoft.xuetang.util.SimplePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

/**
 * Created by shijianguang on 10/11/16.
 */
@Service
public class RecommendService {
    private static final Logger logger = LoggerFactory.getLogger(RecommendService.class);
    private static final Logger performancelogger = LoggerFactory.getLogger(Constants.Log.PERFORMANCE_LOGGER_NAME);

    @Autowired
    private BingComponent bingComponent;

    @Autowired
    private ElasticSearchComponent elasticSearchComponent;

    @Autowired
    private FederationEngine federationEngine;

    @Autowired
    private FeatureServerComponent featureServerComponent;

    public SearchList<SearchElementData> recommend(Request request) {

        List<SimplePair<String, Float>> profile = UserProfileComponent.getUserProfile();
        List<QueryKeyword> queryKeywords = Lists.transform(profile,
                new Function<SimplePair<String, Float>, QueryKeyword>() {
                    @Override public QueryKeyword apply(SimplePair<String, Float> input) {
                        return new QueryKeyword(input.getFirst(), 0, 0);
                    }
                });

        SearchApiRequest recallRequest = new SearchApiRequest(request);
        recallRequest.setAllTypeCount(50);
        recallRequest.setAllTypeOffset(0);
        recallRequest.setFlight(Constants.WEB_BING_FLIGHT);
        recallRequest.setQuery(profile.get(0).getFirst());
        recallRequest.setKeywords(queryKeywords);
        FederationContext<SearchList<SearchElementData>> webRecallResponseFederationContext = new FederationContext<>("WebRecall");
        webRecallResponseFederationContext.put("request", recallRequest);
        federationEngine.submit(new FederationExecution<SearchList<BaseSearchViewEntity>>() {
            @Override
            public SearchList<SearchElementData> execute(FederationContext context) throws Exception {
                return bingSearchRecall(recallRequest, Constants.DataType.WEB);
            }
        }, webRecallResponseFederationContext);

        FederationContext<SearchList<SearchElementData>> academicRecallResponseFederationContext = new FederationContext<>("AcademicRecall");
        academicRecallResponseFederationContext.put("request", recallRequest);
        federationEngine.submit(new FederationExecution<SearchList<SearchElementData>>() {
            @Override
            public SearchList<SearchElementData> execute(FederationContext context) throws Exception {
                return academicRecall(recallRequest);
            }
        }, academicRecallResponseFederationContext);


        FederationContext<SearchList<SearchElementData>> pptRecallResponseFederationContext = new FederationContext<>("PPTRecall");
        pptRecallResponseFederationContext.put("request", recallRequest);
        federationEngine.submit(new FederationExecution<SearchList<SearchElementData>>() {
            @Override
            public SearchList<SearchElementData> execute(FederationContext context) throws Exception {
                return pptRecall(recallRequest);
            }
        }, pptRecallResponseFederationContext);

        FederationContext<SearchList<SearchElementData>> videoRecallResponseFederationContext = new FederationContext<>("VideoRecall");
        videoRecallResponseFederationContext.put("request", recallRequest);
        federationEngine.submit(new FederationExecution<SearchList<BaseSearchViewEntity>>() {
            @Override
            public SearchList<SearchElementData> execute(FederationContext context) throws Exception {
                return videoRecall(recallRequest);
            }
        }, videoRecallResponseFederationContext);

        SearchList<SearchElementData> pptResult = pptRecallResponseFederationContext.fluentGetResult();

        SearchList<SearchElementData> videoResult = videoRecallResponseFederationContext.fluentGetResult();


        SearchList<SearchElementData> webResult = webRecallResponseFederationContext.fluentGetResult();


        SearchList<SearchElementData> academicResult = academicRecallResponseFederationContext.fluentGetResult();

        SearchList<SearchElementData> result = rerank(webResult, pptResult, videoResult, academicResult, 40);

        result.setQuery(profile.get(0).getFirst());

        return result;

    }

    public SearchList<SearchElementData> bingSearchRecall(SearchApiRequest searchApiRequest, String type) {
        BingWebSearchResponse response = bingComponent.getBingWebSearchResponseWithFeature(searchApiRequest, type);

        if (response == null || response.getWebPages() == null || response.getWebPages().getValue() == null) {
            logger.error("Request bing search recall result api get no result. TraceId: {}. Flight: {}.", searchApiRequest.getTraceId(), searchApiRequest.getFlight());
            return null;
        }

        SearchList<SearchElementData> result = new SearchList<>();

        List<BingWebResultEntity> bingResultList = response.getWebPages().getValue();
        for (BingWebResultEntity entity : bingResultList) {
            SearchElementData data = SearchElementData.transform(entity, type);
            if (data != null) {
                result.add(data);
            }
        }

        featureServerComponent.backFillIconUrlData(result.getList());
        return result;
    }

    public SearchList<SearchElementData> videoRecall(SearchApiRequest searchRequest) {
        String[] indexAndType = Constants.DataTypeIndexMap.get(Constants.DataType.VIDEO);
        try {
            SearchList<SearchElementData> result = elasticSearchComponent.syncSearch(searchRequest, new MultiMediaRecommendAdaper.RequestAdaper(indexAndType[0], indexAndType[1]), new MultiMediaRecommendAdaper.ResponseAdaper());

            if(result != null) {
                return result.range(searchRequest.getOffsetInt(), searchRequest.getCountInt());
            } else {
                logger.error("Recall video data return null result");
                return null;
            }
        } catch (Exception e) {
            logger.info("Recall video data error. TracdId: {}. Query: {}, count: {}, offset: {}. Reason is: {}", searchRequest.getTraceId(), searchRequest.getQuery(), searchRequest.getCount(), searchRequest.getOffset(), e.getMessage());
        }
        return null;
    }

    public SearchList<SearchElementData> pptRecall(SearchApiRequest searchRequest) {
        String[] indexAndType = Constants.DataTypeIndexMap.get(Constants.DataType.PPT);
        try {
            SearchList<SearchElementData> result = elasticSearchComponent.syncSearch(searchRequest, new MultiMediaRecommendAdaper.RequestAdaper(indexAndType[0], indexAndType[1]), new MultiMediaRecommendAdaper.ResponseAdaper());
            if(result != null) {
                return result.range(searchRequest.getOffsetInt(), searchRequest.getCountInt());
            } else {
                logger.error("Recall ppt data return null result");
                return null;
            }
        } catch (Exception e) {
            logger.info("Recall ppt data error. TracdId: {}. Query: {}, count: {}, offset: {}. Reason is: {}", searchRequest.getTraceId(), searchRequest.getQuery(), searchRequest.getCount(), searchRequest.getOffset(), e.getMessage());
        }

        return null;

    }

    public SearchList<SearchElementData> academicRecall(SearchApiRequest searchRequest) {
        long t1 = System.currentTimeMillis();
        BingAcademicSearchResponse searchResponse = null;
        try {
            searchResponse = BingHttpComponent.getBingAcademicResponse(searchRequest);
        } catch (Exception e) {
            logger.error("Request bing academic recall api encounter exception. TraceId: {}. Message: {}", searchRequest.getTraceId(), e.getMessage());
        }

        long t2 = System.currentTimeMillis();

        LogUtils.infoLogPerformance(performancelogger, searchRequest, t2 - t1, "http", "bing", "academic");

        if (searchResponse == null || searchResponse.getResult() == null || searchResponse.getResult().size() == 0) {
            logger.error("Request bing academic search api get no result. TraceId: {}.", searchRequest.getTraceId());
            return null;
        }

        List<BingAcademicSearchEntity> entityList = searchResponse.getResult();
        SearchList<SearchElementData> result = new SearchList<>();
        for (BingAcademicSearchEntity entity : entityList) {
            SearchElementData elementData = SearchElementData.transform(entity);
            if (elementData != null) {
                result.add(elementData);
            }
        }

        return result;
    }

    public SearchList<SearchElementData> rerank(SearchList<SearchElementData> webRecallList, SearchList<SearchElementData> pptRecallList, SearchList<SearchElementData> videoRecallList, SearchList<SearchElementData> academicRecallList, int resultCount) {
        int batchSize = 5;
        Random random = new Random();
        webRecallList.shuffule(batchSize, random);
        SearchList<SearchElementData> result = new SearchList<>();
        if(webRecallList == null) {
            return result;
        }
        int increment = 0;
        int webStart = 0;
        int pptStart = 0;
        int videoStart = 0;
        int academicStart = 0;
        while(webStart < webRecallList.getCount() && increment < resultCount) {
            if(pptRecallList != null && increment % batchSize == 2) {
                if(increment < batchSize) {
                    SearchElementData data = pptRecallList.getData(pptStart);
                    pptStart += 1;
                    result.add(data);
                } else {
                    double display = random.nextDouble();
                    if (Double.compare(display, 0.9) <= 0) {
                        SearchElementData data = pptRecallList.radomGetInRange(pptStart, batchSize, random);
                        pptStart += batchSize;
                        result.add(data);
                    } else {
                        result.add(webRecallList.getData(webStart));
                        ++webStart;
                    }
                }
            } else if(videoRecallList != null && increment % batchSize == 3) {
                if(increment < batchSize) {
                    SearchElementData data = videoRecallList.getData(pptStart);
                    videoStart += 1;
                    result.add(data);
                } else {
                    double display = random.nextDouble();
                    if (Double.compare(display, 0.9) <= 0) {
                        SearchElementData data = videoRecallList.radomGetInRange(videoStart, batchSize, random);
                        videoStart += batchSize;
                        result.add(data);
                    } else {
                        result.add(webRecallList.getData(webStart));
                        ++webStart;
                    }
                }
            } else if (academicRecallList != null && increment % batchSize == 4) {
                double display = random.nextDouble();
                if(Double.compare(display, 0.15) <= 0) {
                    SearchElementData data = academicRecallList.getData(academicStart);
                    academicStart += 1;
                    result.add(data);
                } else {
                    result.add(webRecallList.getData(webStart));
                    ++ webStart;
                }
            } else {
                result.add(webRecallList.getData(webStart));
                ++ webStart;
            }

            ++ increment;
        }
        return result;
    }
}
