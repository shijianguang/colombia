package com.microsoft.xuetang.service;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.microsoft.xuetang.Federation.FederationContext;
import com.microsoft.xuetang.Federation.FederationEngine;
import com.microsoft.xuetang.Federation.FederationExecution;
import com.microsoft.xuetang.bean.MultiSearchList;
import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.cache.WebFeatureEntity;
import com.microsoft.xuetang.bean.internal.response.BingAcademicSearchEntity;
import com.microsoft.xuetang.bean.internal.response.DialogueEngineSearchEntity;
import com.microsoft.xuetang.bean.internal.response.BingWebResultEntity;
import com.microsoft.xuetang.bean.schema.response.search.BaseSearchViewEntity;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;
import com.microsoft.xuetang.component.*;
import com.microsoft.xuetang.component.Adapter.MultiMediaSearchAdaper;
import com.microsoft.xuetang.internalrpc.response.*;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import com.microsoft.xuetang.schema.response.search.SearchApiResponseV2;
import com.microsoft.xuetang.util.Constants;
import com.microsoft.xuetang.util.CommonUtils;
import com.microsoft.xuetang.util.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by jiash on 8/8/2016.
 */
@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);

    @Autowired
    private ElasticSearchComponent elasticSearchComponent;

    @Autowired
    private FederationEngine federationEngine;

    @Autowired
    private FeatureServerComponent featureServerComponent;

    public SearchApiResponseV2 search(SearchApiRequest searchRequest) {

        FederationContext<SearchList<SearchElementData>> wikiSearchResponseFederationContext = new FederationContext<>("WikiSearch");
        wikiSearchResponseFederationContext.put("request", searchRequest);
        federationEngine.submit(new FederationExecution<SearchList<SearchElementData>>() {
            @Override
            public SearchList<SearchElementData> execute(FederationContext context) throws Exception {
                return wikiSearch(searchRequest);
            }
        }, wikiSearchResponseFederationContext);

        FederationContext<SearchList<SearchElementData>> webSearchResponseFederationContext = new FederationContext<>("WebSearch");
        webSearchResponseFederationContext.put("request", searchRequest);
        federationEngine.submit(new FederationExecution<SearchList<BaseSearchViewEntity>>() {
            @Override
            public SearchList<SearchElementData> execute(FederationContext context) throws Exception {
                return webSearch(searchRequest);
            }
        }, webSearchResponseFederationContext);

        FederationContext<SearchList<SearchElementData>> academicSearchResponseFederationContext = new FederationContext<>("AcademicSearch");
        academicSearchResponseFederationContext.put("request", searchRequest);
        federationEngine.submit(new FederationExecution<SearchList<SearchElementData>>() {
            @Override
            public SearchList<SearchElementData> execute(FederationContext context) throws Exception {
                return academicSearch(searchRequest);
            }
        }, academicSearchResponseFederationContext);

        MultiSearchList multiMediaSearchList = multiMediaSearch(searchRequest);

        SearchApiResponseV2 response = new SearchApiResponseV2();

        response.setMultiMediaList(multiMediaSearchList);

        SearchList<SearchElementData> wikiList = wikiSearchResponseFederationContext.fluentGetResult();
        if (wikiList != null) {
            response.setWikiList(wikiList);
        }

        SearchList<SearchElementData> webList = webSearchResponseFederationContext.fluentGetResult();
        if (webList != null) {
            response.setWebList(webList);
        }

        SearchList<SearchElementData> academicList = academicSearchResponseFederationContext.fluentGetResult();
        if (academicList != null) {
            response.setPaperList(academicList);
        }

        return response;
    }

    public SearchList<SearchElementData> wikiSearch(SearchApiRequest searchRequest) {
        SearchApiRequest newSearchApiRequest = new SearchApiRequest(searchRequest);
        int actualOffset = newSearchApiRequest.getOffsetInt() * Constants.BING_ONCE_CALL_COUNT;
        newSearchApiRequest.setOffsetInt(actualOffset);
        newSearchApiRequest.setOffset(String.valueOf(actualOffset));
        newSearchApiRequest.setCountInt(Constants.BING_ONCE_CALL_COUNT);
        newSearchApiRequest.setCount(Constants.Bing_ONCE_CALL_COUNT_IN_STRING);
        newSearchApiRequest.setFlight("academicflt10");
        return bingSearch(newSearchApiRequest, Constants.DataType.WIKI, true);
    }

    public SearchList<SearchElementData> webSearch(SearchApiRequest searchRequest) {
        SearchApiRequest newSearchApiRequest = new SearchApiRequest(searchRequest);
        int actualOffset = newSearchApiRequest.getOffsetInt() * Constants.BING_ONCE_CALL_COUNT;
        newSearchApiRequest.setOffsetInt(actualOffset);
        newSearchApiRequest.setOffset(String.valueOf(actualOffset));
        newSearchApiRequest.setCountInt(Constants.BING_ONCE_CALL_COUNT);
        newSearchApiRequest.setCount(Constants.Bing_ONCE_CALL_COUNT_IN_STRING);
        newSearchApiRequest.setFlight("academicflt11");
        return bingSearch(newSearchApiRequest, Constants.DataType.WEB, true);
    }


    public SearchList<SearchElementData> bingSearch(SearchApiRequest searchApiRequest, String type, boolean needRerank) {
        long t1 = System.currentTimeMillis();
        BingWebSearchResponse response = null;
        try {
            response = BingHttpComponent.getBingWebSearchResponse(searchApiRequest.getQuery(), searchApiRequest.getCount(), searchApiRequest.getOffset(), searchApiRequest.getFlight());
        } catch (Exception e) {
            logger.error("Request bing search result api with type {} encounter Exception. Message: {}", type, e.getMessage());
        }

        if (response == null || response.getWebPages() == null || response.getWebPages().getValue() == null) {
            logger.error("Request bing search result api with type {} get no result", type);
            return null;
        }

        long t2 = System.currentTimeMillis();
        SearchList<SearchElementData> result = new SearchList<>();

        List<BingWebResultEntity> bingResultList = response.getWebPages().getValue();
        for (BingWebResultEntity entity : bingResultList) {
            SearchElementData data = SearchElementData.transform(entity, type);
            if (data != null) {
                result.add(data);
            }
        }
        LogUtils.debugLogSearchElementList(logger, String.format("Bing search with flight: %s", searchApiRequest.getFlight()), result);
        long t3 = System.currentTimeMillis();
        List<SearchElementData> elementDataList = result.getList();
        List<SearchElementData> newElementDataList = new ArrayList<>();
        if (elementDataList != null) {
            List<String> idList = Lists.transform(elementDataList, new Function<SearchElementData, String>() {
                @Override
                public String apply(SearchElementData input) {
                    return input.getId();
                }
            });
            String[] kvType = Constants.DataTypeKVMap.get(type);
            Map<String, WebFeatureEntity> id2Feature = featureServerComponent.get(kvType[0], kvType[1], idList);
            for (SearchElementData data : elementDataList) {
                String id = data.getId();
                WebFeatureEntity feature = id2Feature.get(id);
                if (feature != null) {
                    if (feature.getImageUrl() != null) {
                        data.setImageUrl(feature.getImageUrl());
                    }
                    if (feature.getTitle() != null) {
                        data.setTitle(feature.getTitle());
                    }

                    newElementDataList.add(data);
                }
            }
        }

        logger.info("Bing search type: {} result count: {}, after backfill feature result count: {}", type, elementDataList.size(), newElementDataList.size());

        featureServerComponent.backFillIconUrlData(newElementDataList);
        result.setCount(newElementDataList.size());
        result.setList(newElementDataList);
        if(needRerank) {
            result = webReRank(result);
        }
        long t4 = System.currentTimeMillis();
        LogUtils.debugLogSearchElementList(logger, String.format("Bing search with flight after meta back fill: %s", searchApiRequest.getFlight()), result);

        logger.info("Bing Api time: {}, parser item: {}, backfill time: {}", t2 - t1, t3 - t2, t4 - t3);

        return result;
    }


    public MultiSearchList multiMediaSearch(SearchApiRequest searchRequest) {
        QueryUnderstandingResponse quResponse = null;
        try {
            quResponse = QueryUnderstandingComponent.getQueryUnderstandingResponse(searchRequest.getQuery());
        } catch (Exception e) {
            logger.error("Request query understanding system error. Message: {}", e.getMessage());
        }
        if(quResponse != null) {
            searchRequest.setKeywords(quResponse.getKeywords());
        }
        MultiSearchList response = new MultiSearchList();
        FederationContext<SearchList<SearchElementData>> pptSearchResponseFederationContext = new FederationContext<>("PPTSearch");
        pptSearchResponseFederationContext.put("request", searchRequest);
        federationEngine.submit(new FederationExecution<SearchList<SearchElementData>>() {
            @Override
            public SearchList<SearchElementData> execute(FederationContext context) throws Exception {
                return pptSearch(searchRequest);
            }
        }, pptSearchResponseFederationContext);

        FederationContext<SearchList<SearchElementData>> videoSearchResponseFederationContext = new FederationContext<>("VideoSearch");
        videoSearchResponseFederationContext.put("request", searchRequest);
        federationEngine.submit(new FederationExecution<SearchList<BaseSearchViewEntity>>() {
            @Override
            public SearchList<SearchElementData> execute(FederationContext context) throws Exception {
                return videoSearch(searchRequest);
            }
        }, videoSearchResponseFederationContext);

        SearchList<SearchElementData> pptResult = pptSearchResponseFederationContext.fluentGetResult();

        if (pptResult != null) {
            response.setSearchList(Constants.DataType.PPT, pptResult);
        }

        SearchList<SearchElementData> videoResult = videoSearchResponseFederationContext.fluentGetResult();

        if (videoResult != null) {
            response.setSearchList(Constants.DataType.VIDEO, videoResult);
        }

        return response;
    }

    public SearchList<SearchElementData> videoSearch(SearchApiRequest searchRequest) {
        String[] indexAndType = Constants.DataTypeIndexMap.get(Constants.DataType.VIDEO);
        try {
            SearchList<SearchElementData> result = elasticSearchComponent.syncSearch(searchRequest, new MultiMediaSearchAdaper.RequestAdaper(indexAndType[0], indexAndType[1]), new MultiMediaSearchAdaper.ResponseAdaper());
            videoReRank(result);

            return result.range(searchRequest.getOffsetInt(), searchRequest.getCountInt());
        } catch (Exception e) {
            logger.info("Search video data error. Query: {}, c ount: {}, offset: {}. Reason is: {}", searchRequest.getQuery(), searchRequest.getCount(), searchRequest.getOffset(), e.getMessage());
        }
        return null;
    }

    public SearchList<SearchElementData> pptSearch(SearchApiRequest searchRequest) {
        String[] indexAndType = Constants.DataTypeIndexMap.get(Constants.DataType.PPT);
        try {
            SearchList<SearchElementData> result = elasticSearchComponent.syncSearch(searchRequest, new MultiMediaSearchAdaper.RequestAdaper(indexAndType[0], indexAndType[1]), new MultiMediaSearchAdaper.ResponseAdaper());
            return result.range(searchRequest.getOffsetInt(), searchRequest.getCountInt());
        } catch (Exception e) {
            logger.info("Search ppt data error. Query: {}, count: {}, offset: {}. Reason is: {}", searchRequest.getQuery(), searchRequest.getCount(), searchRequest.getOffset(), e.getMessage());
        }

        return null;

        /*SearchApiRequest newSearchApiRequest = new SearchApiRequest(searchRequest);
        newSearchApiRequest.setFlight("academicflt13");
        return bingSearch(newSearchApiRequest, Constants.DataType.PPT, false);*/

    }

    public SearchList<SearchElementData> dialogueEngineSearch(SearchApiRequest searchRequest) {
        DialogueInterpretResponse interpretResponse = null;
        try {
            interpretResponse = DialogueEngineHttpComponent.interpret(searchRequest.getQuery().toLowerCase(), "5", "3000");
        } catch (Exception e) {
            logger.error("Request dialogue interpret api encounter exception. Message: {}", e.getMessage());
        }
        if (interpretResponse == null || interpretResponse.getInterpretations() == null || interpretResponse.getInterpretations().size() == 0) {
            return null;
        }

        DialogueEngineSearchResponse dialogueEngineResponse = null;
        try {
            dialogueEngineResponse = DialogueEngineHttpComponent.search(interpretResponse.getInterpretations(), "*",
                    searchRequest.getCount(), searchRequest.getOffset(), "2000");
        } catch (Exception e) {
            logger.error("Request dialogue search api encounter exception. Message: {}", e.getMessage());
        }
        if (dialogueEngineResponse == null || dialogueEngineResponse.getEntities() == null || dialogueEngineResponse.getEntities().size() == 0) {
            return null;
        }

        List<DialogueEngineSearchEntity> entityList = dialogueEngineResponse.getEntities();
        SearchList<SearchElementData> result = new SearchList<>();
        for (DialogueEngineSearchEntity entity : entityList) {
            SearchElementData elementData = SearchElementData.transform(entity);
            if (elementData != null) {
                result.add(elementData);
            }
        }

        return result;
    }

    public SearchList<SearchElementData> academicSearch(SearchApiRequest searchRequest) {
        BingAcademicSearchResponse searchResponse = null;
        try {
            searchResponse = BingHttpComponent.getBingAcademicResponse(searchRequest.getQuery(), searchRequest.getCount(), searchRequest.getOffset());
        } catch (Exception e) {
            logger.error("Request bing academic search api encounter exception. Message: {}", e.getMessage());
        }
        if (searchResponse == null || searchResponse.getResult() == null || searchResponse.getResult().size() == 0) {
            logger.error("Request bing academic search api get no result");
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

        return result.range(searchRequest.getOffsetInt(), searchRequest.getCountInt());
    }

    private SearchList<SearchElementData> webReRank(SearchList<SearchElementData> searchList) {
        int count = searchList.getCount();
        List<SearchElementData> list = searchList.getList();

        if (count > 2) {
            SearchElementData first = list.get(0);
            SearchElementData second = list.get(1);
            String firstHost = CommonUtils.fluentGetDomainName(first.getUrl());
            String urlHost = CommonUtils.fluentGetDomainName(second.getUrl());
            if (firstHost.equals(urlHost)) {
                for (int i = 2; i < list.size(); ++i) {
                    SearchElementData entity = list.get(i);
                    if (entity != null) {
                        urlHost = CommonUtils.fluentGetDomainName(entity.getUrl());
                        if (!firstHost.equalsIgnoreCase(urlHost)) {
                            list.set(1, entity);
                            list.set(i, second);
                            break;
                        }
                    }
                }
            }
        }
        searchList.setList(list);

        return searchList;
    }

    private SearchList<SearchElementData> videoReRank(SearchList<SearchElementData> searchList) {
        int count = searchList.getCount();
        List<SearchElementData> list = searchList.getList();
        List<ListWrapper<SearchElementData>> helperList = new LinkedList<>();
        Map<String, ListWrapper<SearchElementData>> clusterId2List = new HashMap<>();

        int defaultIncreaeClusterId = 0;
        for (SearchElementData ele : list) {
            String clusterId = ele.getVenu();
            if (clusterId == null || clusterId.length() == 0) {
                clusterId = Constants.AUTOINCREASE_ID2STRING_CACHE[defaultIncreaeClusterId];
                ++defaultIncreaeClusterId;
            }

            ListWrapper<SearchElementData> value = clusterId2List.get(clusterId);
            if (value == null) {
                value = new ListWrapper<>(new ArrayList<>());
                clusterId2List.put(clusterId, value);
                helperList.add(value);
            }

            value.add(ele);
        }

        List<SearchElementData> newSearchList = new ArrayList<>();
        int breakCondition = 0;
        while (true) {
            if (breakCondition >= count) {
                break;
            }
            Iterator<ListWrapper<SearchElementData>> iter = helperList.iterator();
            while (iter.hasNext()) {
                ListWrapper<SearchElementData> listWrapper = iter.next();

                SearchElementData ele = listWrapper.next();
                if (ele == null) {
                    iter.remove();
                } else {
                    newSearchList.add(ele);
                    breakCondition++;
                }

            }
        }

        searchList.setCount(count);
        searchList.setList(newSearchList);

        return searchList;
    }

    static class ListWrapper<T> {
        private List<T> list;
        private Iterator<T> iterator = null;

        public ListWrapper(List<T> list) {
            this.list = list;
        }

        public T next() {
            if (iterator == null) {
                iterator = list.iterator();
            }
            if (iterator.hasNext()) {
                return iterator.next();
            } else {
                return null;
            }
        }

        public boolean add(T data) {
            return list.add(data);
        }
    }
}
