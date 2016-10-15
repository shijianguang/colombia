package com.microsoft.xuetang.service;

import com.google.common.base.Joiner;
import com.microsoft.xuetang.bean.schema.response.detail.MultiMediaDetailData;
import com.microsoft.xuetang.bean.schema.response.detail.PaperProfileData;
import com.microsoft.xuetang.bean.schema.response.detail.WebDetailData;
import com.microsoft.xuetang.component.Adapter.MultiMediaGetDetailDataAdapter;
import com.microsoft.xuetang.component.Adapter.MultiMediaMultiGetDetailDataAdapter;
import com.microsoft.xuetang.component.Adapter.WebGetDetailDataAdapter;
import com.microsoft.xuetang.component.Adapter.WebMultiGetDetailDataAdapter;
import com.microsoft.xuetang.component.AzureServiceHttpComponent;
import com.microsoft.xuetang.component.ElasticSearchComponent;
import com.microsoft.xuetang.component.FeatureServerComponent;
import com.microsoft.xuetang.internalrpc.response.BingPaperProfileResponse;
import com.microsoft.xuetang.internalrpc.response.ErrorResponse;
import com.microsoft.xuetang.schema.request.detail.BatchDetailDataApiRequest;
import com.microsoft.xuetang.schema.request.detail.DetailDataApiRequest;
import com.microsoft.xuetang.util.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
@Service
public class DetailDataService {
    private static final Logger logger = LoggerFactory.getLogger(DetailDataService.class);

    @Autowired
    private ElasticSearchComponent elasticSearchComponent;

    @Autowired
    private FeatureServerComponent featureServerComponent;

    public WebDetailData getWebDetailData(DetailDataApiRequest request) {
        try {
            String[] indexAndType = Constants.DataTypeKVMap.get(request.getType());
            WebDetailData richData = elasticSearchComponent.syncGet(
                    request.getId(),
                    new ElasticSearchComponent.DefaultGetRequestAdapte(indexAndType[0], indexAndType[1]),
                    new WebGetDetailDataAdapter());
            featureServerComponent.backFillIconUrlData(richData);
            richData.setKeywords(queryKeywordDedup(request.getQuery(), richData.getKeywords()));
            return richData;
        } catch (Exception e) {
            logger.error("Get {} detail data encounter fatal error. id: {}. Reason is: {}",
                    request.getType(), request.getId(), e.getMessage());
        }

        return null;
    }

    public Map<String, WebDetailData> batchGetWebDetailData(BatchDetailDataApiRequest request) {
        try {
            String[] indexAndType = Constants.DataTypeKVMap.get(request.getType());
            Map<String, WebDetailData> webData = elasticSearchComponent.syncMultiGet(
                    request.getIdList(),
                    new ElasticSearchComponent.DefaultMulitiGetRequestAdapte(indexAndType[0], indexAndType[1]),
                    new WebMultiGetDetailDataAdapter());
            if(webData != null) {
                for(WebDetailData detailData : webData.values()) {
                    featureServerComponent.backFillIconUrlData(detailData);
                    detailData.setKeywords(queryKeywordDedup(request.getQuery(), detailData.getKeywords()));
                }
            }
            return webData;
        } catch (Exception e) {
            logger.error("Get {} detail data encounter fatal error. id: {}. Reason is: {}",
                    request.getType(), Joiner.on(",").join(request.getIdList()), e.getMessage());
        }

        return null;
    }

    public PaperProfileData getPaperDetailData(DetailDataApiRequest request) {
        try {
            BingPaperProfileResponse bingPaperProfileResponse = AzureServiceHttpComponent.getPaperProfile(request.getId());
            PaperProfileData data = PaperProfileData.transform(bingPaperProfileResponse);
            return data;
        } catch (Exception e) {
            logger.error("Get paper profile data encounter fatal error. id: {}. Reason is: {}",
                    request.getId(), e.getMessage());
        }

        return null;
    }

    public MultiMediaDetailData getMultiMediaDetailData(DetailDataApiRequest request) {
        try {
            String[] indexAndType = Constants.DataTypeKVMap.get(request.getType());
            MultiMediaDetailData richData = elasticSearchComponent.syncGet(
                    request.getId(),
                    new ElasticSearchComponent.DefaultGetRequestAdapte(indexAndType[0], indexAndType[1]),
                    new MultiMediaGetDetailDataAdapter());
            richData.setKeywords(queryKeywordDedup(request.getQuery(), richData.getKeywords()));

            // Now ppt is from slideshare. Slideshare is blocked in China. So return empty string can make
            // front end hid "查看原网页" button. This is a temp solution. It should be controlled in data site
            if(Constants.DataType.PPT.equals(request.getType())) {
                richData.setUrl("");
            }
            return richData;
        } catch (Exception e) {
            logger.error("Get {} detail data encounter fatal error. id: {}. Reason is: {}",
                    request.getType(), request.getId(), e.getMessage());
        }

        return null;
    }

    public Map<String, MultiMediaDetailData> batchGetMultiMediaDetailData(BatchDetailDataApiRequest request) {
        try {
            String[] indexAndType = Constants.DataTypeKVMap.get(request.getType());
            Map<String, MultiMediaDetailData> data = elasticSearchComponent.syncMultiGet(
                    request.getIdList(),
                    new ElasticSearchComponent.DefaultMulitiGetRequestAdapte(indexAndType[0], indexAndType[1]),
                    new MultiMediaMultiGetDetailDataAdapter());
            if(Constants.DataType.PPT.equals(request.getType())) {
                for (MultiMediaDetailData detailData : data.values()) {
                    detailData.setKeywords(queryKeywordDedup(request.getQuery(), detailData.getKeywords()));

                    // Now ppt is from slideshare. Slideshare is blocked in China. So return empty string can make
                    // front end hid "查看原网页" button. This is a temp solution. It should be controlled in data site
                    detailData.setUrl("");
                }
            } else {
                for (MultiMediaDetailData detailData : data.values()) {
                    detailData.setKeywords(queryKeywordDedup(request.getQuery(), detailData.getKeywords()));
                }
            }
            return data;
        } catch (Exception e) {
            logger.error("Get {} detail data encounter fatal error. id: {}. Reason is: {}",
                    request.getType(), Joiner.on(",").join(request.getIdList()), e.getMessage());
        }

        return null;
    }

    public Map<String, PaperProfileData> batchGetPaperProfileData(BatchDetailDataApiRequest request) {
        List<String> idList = request.getIdList();
        Map<String, Object> paperProfiles = AzureServiceHttpComponent.batchGetPaperProfile(idList);

        Map<String, PaperProfileData> result = new HashMap<>();
        for (String id : idList) {
            Object value = paperProfiles.get(id);
            if (value instanceof BingPaperProfileResponse) {
                PaperProfileData detail = PaperProfileData.transform((BingPaperProfileResponse) value);
                if(detail != null) {
                    result.put(id, detail);
                }
            } else if (value instanceof ErrorResponse) {
                logger.error("Get paper %d profile encounter error. Reason is: ", id, ((ErrorResponse) value).getDesc());
            }
        }

        return result;
    }

    public List<String> queryKeywordDedup(String query, List<String> keywordList) {
        if(keywordList == null || keywordList.size() == 0 || query == null) {
            return keywordList;
        }

        List<String> newKeywords = new ArrayList<>();
        for(String keyword : keywordList) {
            if(!query.equalsIgnoreCase(keyword)) {
                newKeywords.add(keyword);
            }
        }

        return newKeywords;
    }
}
