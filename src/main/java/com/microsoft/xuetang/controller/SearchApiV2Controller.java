package com.microsoft.xuetang.controller;

import com.microsoft.xuetang.aspect.ApiRequest;
import com.microsoft.xuetang.bean.MultiSearchList;
import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;
import com.microsoft.xuetang.component.QueryUnderstandingComponent;
import com.microsoft.xuetang.internalrpc.response.QueryUnderstandingResponse;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import com.microsoft.xuetang.schema.request.search.TypeSearchApiRequest;
import com.microsoft.xuetang.schema.response.Response;
import com.microsoft.xuetang.schema.response.search.PopularSearchResponse;
import com.microsoft.xuetang.schema.response.search.SearchApiResponseV2;
import com.microsoft.xuetang.service.SearchService;
import com.microsoft.xuetang.util.Constants;
import com.microsoft.xuetang.util.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by jiash on 8/9/2016.
 */
@Controller
@RequestMapping("/search")
public class SearchApiV2Controller {

    private static final Logger logger = LoggerFactory.getLogger(SearchApiV2Controller.class);
    private static final Logger analysisLogger = LoggerFactory.getLogger("analysis_logger");
    @Autowired
    private SearchService searchService;

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v2/list", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response search(HttpServletRequest request, HttpServletResponse response, SearchApiRequest param) {
        if(QueryUnderstandingComponent.needBlock(param.getQuery())) {
            return new Response<>(Constants.EMPTY_SEARCH_API_RESPONSE);
        }
        SearchApiResponseV2 ret = searchService.search(param);
        SearchList<SearchElementData> web = ret.getWebList();
        SearchList<SearchElementData> wiki = ret.getWikiList();
        if((web == null || web.getCount() == 0) && (wiki == null || wiki.getCount() == 0)) {
            ret.setMultiMediaList(SearchApiResponseV2.EMPTY_MULTI_SEARCH_LIST);
            ret.setPaperList(SearchApiResponseV2.EMPTY_SEARCH_LIST);
        }
        LogUtils.infoLogSearchRequestAndResponse(analysisLogger, param, ret);
        return new Response<>(ret);
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v2/typeSearch", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response typeSearch(HttpServletRequest request, HttpServletResponse response, TypeSearchApiRequest param) {
        QueryUnderstandingResponse quResponse = null;
        SearchList<SearchElementData> ret = null;
        if(param.getCountInt() + param.getOffsetInt() > 500) {
            ret = SearchApiResponseV2.EMPTY_SEARCH_LIST;
        } else if(Constants.DataType.WIKI.equalsIgnoreCase(param.getType())) {
            ret = searchService.wikiSearch(param);
        } else if (Constants.DataType.WEB.equalsIgnoreCase(param.getType())) {
            ret = searchService.webSearch(param);
        } else if (Constants.DataType.PPT.equalsIgnoreCase(param.getType())) {
            try {
                quResponse = QueryUnderstandingComponent.getQueryUnderstandingResponse(param.getQuery());
            } catch (Exception e) {
                logger.error("Request query understanding system error. Message: {}", e.getMessage());
            }
            if(quResponse != null) {
                param.setKeywords(quResponse.getKeywords());
            }
            ret = searchService.pptSearch(param);
        } else if (Constants.DataType.VIDEO.equalsIgnoreCase(param.getType())) {
            try {
                quResponse = QueryUnderstandingComponent.getQueryUnderstandingResponse(param.getQuery());
            } catch (Exception e) {
                logger.error("Request query understanding system error. Message: {}", e.getMessage());
            }
            if(quResponse != null) {
                param.setKeywords(quResponse.getKeywords());
            }
            ret = searchService.videoSearch(param);
        } else if (Constants.DataType.PAPER.equalsIgnoreCase(param.getType())) {
            ret = searchService.academicSearch(param);
        }

        LogUtils.infoLogSearchRequestAndResponse(analysisLogger, param, ret);
        return new Response<>(ret);
    }
}
