package com.microsoft.xuetang.service;

import com.microsoft.xuetang.Federation.FederationContext;
import com.microsoft.xuetang.Federation.FederationEngine;
import com.microsoft.xuetang.Federation.FederationExecution;
import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.internal.response.BingVideoSearchEntity;
import com.microsoft.xuetang.bean.internal.response.BingWebResultEntity;
import com.microsoft.xuetang.bean.internal.response.DialogueEngineSearchEntity;
import com.microsoft.xuetang.bean.internal.response.PPTSearchViewEntity;
import com.microsoft.xuetang.bean.schema.response.detail.WebDetailData;
import com.microsoft.xuetang.bean.schema.response.search.BaseSearchViewEntity;
import com.microsoft.xuetang.component.Adapter.PptSearchAdaper;
import com.microsoft.xuetang.component.Adapter.WebMultiGetDetailDataAdapter;
import com.microsoft.xuetang.component.AzureServiceHttpComponent;
import com.microsoft.xuetang.component.BingHttpComponent;
import com.microsoft.xuetang.component.DialogueEngineHttpComponent;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import com.microsoft.xuetang.internalrpc.response.*;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import com.microsoft.xuetang.schema.response.search.SearchApiResponse;
import com.microsoft.xuetang.util.CommonUtils;
import com.microsoft.xuetang.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
@Service
public class DataService {
    private static final Logger logger = LoggerFactory.getLogger(DataService.class);

    @Autowired
    private ElasticSearchComponent elasticSearchComponent;

    @Autowired
    private FederationEngine federationEngine;

    public SearchApiResponse search(SearchApiRequest searchRequest) {

        FederationContext<SearchList<BaseSearchViewEntity>> wikiSearchResponseFederationContext = new FederationContext<>("Wiki Search In HTTP");
        federationEngine.submit(new FederationExecution<SearchList<BaseSearchViewEntity>>() {
            @Override
            public SearchList<BaseSearchViewEntity> execute(FederationContext context) throws Exception {
                return wikiSearch(searchRequest);
            }
        }, wikiSearchResponseFederationContext);

        FederationContext<SearchList<BaseSearchViewEntity>> webSearchResponseFederationContext = new FederationContext<>("Web Search In HTTP");
        federationEngine.submit(new FederationExecution<SearchList<BaseSearchViewEntity>>() {
            @Override
            public SearchList<BaseSearchViewEntity> execute(FederationContext context) throws Exception {
                return webSearch(searchRequest);
            }
        }, webSearchResponseFederationContext);

        FederationContext<SearchList<DialogueEngineSearchEntity>> academicSearchResponseFederationContext = new FederationContext<>("Academic Search In HTTP");
        federationEngine.submit(new FederationExecution<SearchList<DialogueEngineSearchEntity>>() {
            @Override
            public SearchList<DialogueEngineSearchEntity> execute(FederationContext context) throws Exception {
                return academicSearch(searchRequest);
            }
        }, academicSearchResponseFederationContext);

        Map<String, SearchList> multiMediaResult = multiMediaSearch(searchRequest);

        SearchApiResponse response = new SearchApiResponse();

        SearchList<BaseSearchViewEntity> wikiList = wikiSearchResponseFederationContext.fluentGetResult();
        if(wikiList != null) {
            response.setWikiList(wikiList);
        }

        SearchList<BaseSearchViewEntity> webList = webSearchResponseFederationContext.fluentGetResult();
        if(webList != null) {
            response.setWebList(webList);
        }
        if(multiMediaResult != null) {
            response.setMultiMediaList(multiMediaResult);
        }

        SearchList<DialogueEngineSearchEntity> academicList = academicSearchResponseFederationContext.fluentGetResult();
        if(academicList != null) {
            response.setPaperList(academicList);
        }

        return response;
    }

    public SearchList<BaseSearchViewEntity> wikiSearch(SearchApiRequest searchRequest) {
        searchRequest.setFlight("academicflt10");
        return convertToSearchListView(bingSearch(searchRequest));
    }

    public SearchList<BaseSearchViewEntity> webSearch(SearchApiRequest searchRequest) {
        searchRequest.setFlight("academicflt11");
        return convertToSearchListView(bingSearch(searchRequest));
    }

    public BingWebSearchResponse bingSearch(SearchApiRequest searchApiRequest) {

        BingWebSearchResponse response = null;
        try {
            response = BingHttpComponent.getBingWebSearchResponse(searchApiRequest);
        } catch (Exception e) {
            logger.error("Request bing web result api encounter Exception. Message: {}", e.getMessage());
        }

        if(response == null) {
            return null;
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
                    Map<String, WebDetailData> richData = elasticSearchComponent.syncMultiGet(ids, new ElasticSearchComponent.DefaultMulitiGetRequestAdapte("keyvalue", "Wiki"), new WebMultiGetDetailDataAdapter());
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
                    logger.error("Backfill web result rich data encounter fatal error. Query: {}. Count: {}. Offset: {}. Flight: {}. Message: {}",
                            searchApiRequest.getQuery(), searchApiRequest.getCount(), searchApiRequest.getOffset(), searchApiRequest.getFlight(), e.getMessage());
                }

            }

        }
        return response;
    }


    public Map<String, SearchList> multiMediaSearch(SearchApiRequest searchRequest) {

        FederationContext<PPTSearchResponse> pptSearchResponseFederationContext = new FederationContext<>("PPT Search In Elasticsearch");
        federationEngine.submit(new FederationExecution<PPTSearchResponse>() {
            @Override
            public PPTSearchResponse execute(FederationContext context) throws Exception {
                return elasticSearchComponent.syncSearch(searchRequest, new PptSearchAdaper.RequestAdaper(), new PptSearchAdaper.ResponseAdaper(), "ppt");
            }
        }, pptSearchResponseFederationContext);

        FederationContext<BingVideoSearchResponse> bingVideoSearchResponseFederationContext = new FederationContext<>("Bing video search In HTTP");

        federationEngine.submit(new FederationExecution<BingVideoSearchResponse>() {
            @Override
            public BingVideoSearchResponse execute(FederationContext context) throws Exception {
                return BingHttpComponent.getBingVideoSearchResponse(searchRequest.getQuery(), searchRequest.getCount(), searchRequest.getOffset());
            }
        }, bingVideoSearchResponseFederationContext);

        PPTSearchResponse pptSearchResponse = pptSearchResponseFederationContext.fluentGetResult();
        BingVideoSearchResponse bingVideoSearchResponse = bingVideoSearchResponseFederationContext.fluentGetResult();

        Map<String, SearchList> result = new HashMap<>();
        SearchList<PPTSearchViewEntity> pptList = new SearchList<>();
        if(pptSearchResponse != null) {
            List<PPTSearchViewEntity> pptEntityList = pptSearchResponse.getPptSearchEntities();
            if(pptEntityList != null) {
                pptList.setCount(pptEntityList.size());
                pptList.setList(pptEntityList);
            }
        }
        result.put("ppt", pptList);

        SearchList<BingVideoSearchEntity> videoList = new SearchList<>();
        if(bingVideoSearchResponse != null) {
            List<BingVideoSearchEntity> videoSearchEntities = bingVideoSearchResponse.getValue();
            if(videoSearchEntities != null) {
                videoList.setCount(videoSearchEntities.size());
                videoList.setList(videoSearchEntities);
            }
        }
        result.put("video", videoList);

        return result;

    }

    public SearchList<DialogueEngineSearchEntity> academicSearch(SearchApiRequest searchRequest) {
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
            dialogueEngineResponse = DialogueEngineHttpComponent.search(interpretResponse.getInterpretations(), "*", searchRequest.getCount(), searchRequest.getOffset(), "2000");
        } catch (Exception e) {
            logger.error("Request dialogue search api encounter exception. Message: {}", e.getMessage());
        }
        if(dialogueEngineResponse != null || dialogueEngineResponse.getEntities() != null || dialogueEngineResponse.getEntities().size() != 0) {


            List<String> ids = new ArrayList<>();
            List<DialogueEngineSearchEntity> entities = dialogueEngineResponse.getEntities();

            for (DialogueEngineSearchEntity entity : entities) {
                if (entity.getId() == null) {
                    logger.debug("Paper has null Id");
                    continue;
                }

                String id = entity.getId().toString();
                ids.add(id);
            }

            Map<String, Object> paperProfiles = AzureServiceHttpComponent.batchGetPaperProfile(ids);

            for (DialogueEngineSearchEntity entity : entities) {
                entity.setPaperProfile(null);
                if (entity.getId() == null) {
                    continue;
                }
                String id = entity.getId().toString();
                Object value = paperProfiles.get(id);
                if (value instanceof BingPaperProfileResponse) {
                    entity.setPaperProfile((BingPaperProfileResponse) value);
                } else if (value instanceof ErrorResponse) {
                    logger.error("Get paper %d profile encounter error. Reason is: ", id, ((ErrorResponse) value).getDesc());
                }
            }
        }

        SearchList<DialogueEngineSearchEntity> academicList = new SearchList<>();

        if(dialogueEngineResponse != null && dialogueEngineResponse.getEntities() != null) {
            List<DialogueEngineSearchEntity> entityList = dialogueEngineResponse.getEntities();
            academicList.setCount(entityList.size());
            academicList.setList(entityList);
        }

        return academicList;
    }

    private SearchList<BaseSearchViewEntity> convertToSearchListView(BingWebSearchResponse bingWebSearchResponse) {
        SearchList<BaseSearchViewEntity> searchViewEntitySearchListView = new SearchList<>();
        searchViewEntitySearchListView.setCount(0);
        searchViewEntitySearchListView.setList(null);
        int count = 0;
        List<BaseSearchViewEntity> list = new ArrayList<>();
        if(bingWebSearchResponse.getWebPages() == null) {
            return searchViewEntitySearchListView;
        }
        List<BaseSearchViewEntity> withRichCcntent = new ArrayList<>();
        List<BaseSearchViewEntity> withOutRichContent = new ArrayList<>();
        for(BingWebResultEntity entry : bingWebSearchResponse.getWebPages().getValue()) {
            String url = entry.getUrl();
            BaseSearchViewEntity searchView = new BaseSearchViewEntity();
            searchView.setId(entry.getId());
            if(url != null) {
                searchView.setRichDataId(DigestUtils.md5DigestAsHex(url.getBytes()));
                if(url.contains("baike.com")) {
                    searchView.setLogoImageUrl(Constants.ImageResource.HUDONG_URL);
                }

                if(url.contains("wikipedia.org")) {
                    searchView.setLogoImageUrl(Constants.ImageResource.WIKIPEDIA_URL);
                }

                if(url.contains("baike.so.com")) {
                    searchView.setLogoImageUrl(Constants.ImageResource.QIHU_URL);
                }

                if(url.contains("sogou.com")) {
                    searchView.setLogoImageUrl(Constants.ImageResource.SOGOU_URL);
                }

                if(url.contains("stackoverflow.com")) {
                    searchView.setLogoImageUrl(Constants.ImageResource.STACKOVERFLOW_URL);
                }

                if(url.contains("zhihu.com")) {
                    searchView.setLogoImageUrl(Constants.ImageResource.ZHIHU_URL);
                }

                if(url.contains("baidu.com")) {
                    searchView.setLogoImageUrl(Constants.ImageResource.BAIDU_URL);
                }

                if(url.contains("csdn.com") || url.contains("csdn.net")) {
                    searchView.setLogoImageUrl(Constants.ImageResource.CSDN_URL);

                }

                if(url.contains("douban.com")) {
                    searchView.setLogoImageUrl(Constants.ImageResource.DOUBAN_URL);
                }
            }
            searchView.setName(entry.getName());
            searchView.setUrl(url);
            searchView.setDisplayUrl(entry.getDisplayUrl());
            searchView.setImageUrl(entry.getThumbnailUrl());
            searchView.setSnippet(entry.getSnippet());
            if(entry.getAbstractContent() != null && entry.getAbstractContent().length() > 0) {
                searchView.setSnippet(entry.getAbstractContent());
                withRichCcntent.add(searchView);
            } else {
                //searchView.setAbstractContent(searchView.getSnippet());
                withOutRichContent.add(searchView);
            }
            ++ count;
        }

        list.addAll(withRichCcntent);
        list.addAll(withOutRichContent);

        if(count > 2) {
            BaseSearchViewEntity first = list.get(0);
            BaseSearchViewEntity second = list.get(1);
            String firstHost = CommonUtils.fluentGetDomainName(first.getUrl());
            String urlHost = CommonUtils.fluentGetDomainName(second.getUrl());
            if(firstHost.equals(urlHost)) {
                for(int i = 2 ; i < list.size() ; ++ i) {
                    BaseSearchViewEntity entity = list.get(i);
                    if(entity != null) {
                        urlHost = CommonUtils.fluentGetDomainName(entity.getUrl());
                        if(!firstHost.equalsIgnoreCase(urlHost)) {
                            list.set(1, entity);
                            list.set(i, second);
                            break;
                        }
                    }
                }
            }
        }

        searchViewEntitySearchListView.setCount(count);
        searchViewEntitySearchListView.setList(list);

        return searchViewEntitySearchListView;
    }
}
