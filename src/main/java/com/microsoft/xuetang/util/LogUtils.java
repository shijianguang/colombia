package com.microsoft.xuetang.util;


import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.log.*;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;
import com.microsoft.xuetang.schema.request.Request;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import com.microsoft.xuetang.schema.request.search.TypeSearchApiRequest;
import com.microsoft.xuetang.schema.response.search.SearchApiResponseV2;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by jiash on 8/27/2016.
 */
public class LogUtils {

    public static void debugLogSearchElementList(Logger logger, String desc, SearchList<SearchElementData> searchList) {
        if(logger.isDebugEnabled()) {
            SearchInstrumentLog searchInstrumentLog = new SearchInstrumentLog();
            searchInstrumentLog.setDesc(desc);
            SearchBaseListLog result = formatSearchElementListToString(searchList, desc);
            SingleSearchResultLog singleSearchResultLog = new SingleSearchResultLog();
            singleSearchResultLog.setEntityLog(result);
            searchInstrumentLog.setResult(singleSearchResultLog);
            try {
                logger.info(JsonUtil.object2String(searchInstrumentLog));
            } catch (IOException e) {
            }
        }
    }

    public static void infoLogSearchRequestAndResponse(Logger logger, SearchApiRequest request, SearchList<SearchElementData> searchList) {
        if(logger.isInfoEnabled()) {
            if(request == null) {
                return;
            }
            String type = null;
            if(request instanceof TypeSearchApiRequest) {
                String tmpType = ((TypeSearchApiRequest)request).getType();
                if(tmpType != null) {
                    type = tmpType;
                }
            }

            SearchInstrumentLog searchInstrumentLog = new SearchInstrumentLog();
            searchInstrumentLog.setToken(request.getToken());
            searchInstrumentLog.setDeviceId(request.getDeviceId());
            searchInstrumentLog.setTraceId(request.getTraceId());
            searchInstrumentLog.setRequestTime(request.getTimestamp());
            searchInstrumentLog.setFlight(request.getFlight());
            searchInstrumentLog.setQuery(request.getQuery());
            searchInstrumentLog.setCount(request.getCount());
            searchInstrumentLog.setOffset(request.getOffset());
            searchInstrumentLog.setRequestUri(request.getRequestUri());
            searchInstrumentLog.setDesc(Constants.Log.SEARCH_DESC);
            searchInstrumentLog.setSearchType(type);
            searchInstrumentLog.setForm(request.getForm());

            SearchBaseListLog result = formatSearchElementListToString(searchList, type);

            SingleSearchResultLog singleSearchResultLog = new SingleSearchResultLog();
            singleSearchResultLog.setEntityLog(result);

            searchInstrumentLog.setResult(singleSearchResultLog);
            try {
                logger.info(JsonUtil.object2String(searchInstrumentLog));
            } catch (IOException e) {
            }

        }
    }

    public static void infoLogSearchRequestAndResponse(Logger logger, SearchApiRequest request, SearchApiResponseV2 response) {
        if(logger.isInfoEnabled()) {
            if(request == null) {
                return;
            }

            SearchInstrumentLog searchInstrumentLog = new SearchInstrumentLog();
            searchInstrumentLog.setToken(request.getToken());
            searchInstrumentLog.setDeviceId(request.getDeviceId());
            searchInstrumentLog.setTraceId(request.getTraceId());
            searchInstrumentLog.setRequestTime(request.getTimestamp());
            searchInstrumentLog.setFlight(request.getFlight());
            searchInstrumentLog.setQuery(request.getQuery());
            searchInstrumentLog.setCount(request.getCount());
            searchInstrumentLog.setOffset(request.getOffset());
            searchInstrumentLog.setRequestUri(request.getRequestUri());
            searchInstrumentLog.setDesc(Constants.Log.SEARCH_DESC);
            searchInstrumentLog.setSearchType(Constants.DataType.ALL);
            searchInstrumentLog.setForm(request.getForm());

            MultiSearchResultLog multiSearchResultLog = new MultiSearchResultLog();

            SearchBaseListLog wikiResult = formatSearchElementListToString(response.getWikiList(), Constants.DataType.WIKI);
            if(wikiResult != null) {
                multiSearchResultLog.setEntityLog(Constants.DataType.WIKI, wikiResult);
            }
            SearchBaseListLog webResult = formatSearchElementListToString(response.getWebList(), Constants.DataType.WEB);
            if(webResult != null) {
                multiSearchResultLog.setEntityLog(Constants.DataType.WEB, webResult);
            }
            SearchBaseListLog pptResult = formatSearchElementListToString(response.getMultiMediaList().getSearchList(Constants.DataType.PPT), Constants.DataType.PPT);
            if(pptResult != null) {
                multiSearchResultLog.setEntityLog(Constants.DataType.PPT, pptResult);
            }
            SearchBaseListLog videoResult = formatSearchElementListToString(response.getMultiMediaList().getSearchList(Constants.DataType.VIDEO), Constants.DataType.VIDEO);
            if(videoResult != null) {
                multiSearchResultLog.setEntityLog(Constants.DataType.VIDEO, videoResult);
            }
            SearchBaseListLog paperResult = formatSearchElementListToString(response.getPaperList(), Constants.DataType.PAPER);
            if(paperResult != null) {
                multiSearchResultLog.setEntityLog(Constants.DataType.PAPER, paperResult);
            }

            searchInstrumentLog.setResult(multiSearchResultLog);

            try {
                logger.info(JsonUtil.object2String(searchInstrumentLog));
            } catch (IOException e) {
            }

        }
    }

    private static SearchBaseListLog formatSearchElementListToString(SearchList<SearchElementData> searchList, String desc) {
        SearchBaseListLog searchResultLog = new SearchBaseListLog();
        if(searchList == null || searchList.getList() == null || searchList.getList().size() == 0) {
            return null;
        }
        List<SearchLogEntity> entityLogs = searchList.getList().stream()
                .filter(input -> input != null && input.getId() != null)
                .map(input -> new SearchLogEntity(input.getId(), input.getUrl())).collect(Collectors.toList());

        searchResultLog.setDesc(desc);
        searchResultLog.setEntityList(entityLogs);
        return searchResultLog;
    }


    public static void infoLogPerformance(Logger logger, Request request, String key, long costInMillis) {

        PerformanceInstrumentLog performanceInstrumentLog = new PerformanceInstrumentLog();
        performanceInstrumentLog.setRequestUri(request.getRequestUri());
        performanceInstrumentLog.setTraceId(request.getTraceId());
        performanceInstrumentLog.setRequestTime(request.getTimestamp());
        performanceInstrumentLog.setDesc(Constants.Log.PERFORMANCE_DESC);
        performanceInstrumentLog.setKey(key);
        performanceInstrumentLog.setCostInMillis(costInMillis);

        try {
            logger.info(JsonUtil.object2String(performanceInstrumentLog));
        } catch (IOException e) {
        }

    }

    public static void infoLogPerformance(Logger logger, String requestUri, String traceId, long requestTime, String key, long costInMillis) {

        PerformanceInstrumentLog performanceInstrumentLog = new PerformanceInstrumentLog();
        performanceInstrumentLog.setRequestUri(requestUri);
        performanceInstrumentLog.setTraceId(traceId);
        performanceInstrumentLog.setRequestTime(requestTime);
        performanceInstrumentLog.setDesc(Constants.Log.PERFORMANCE_DESC);
        performanceInstrumentLog.setKey(key);
        performanceInstrumentLog.setCostInMillis(costInMillis);

        try {
            logger.info(JsonUtil.object2String(performanceInstrumentLog));
        } catch (IOException e) {
        }

    }
}
