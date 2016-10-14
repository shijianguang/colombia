package com.microsoft.xuetang.controller;

import com.microsoft.xuetang.aspect.ApiRequest;
import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;
import com.microsoft.xuetang.component.QueryUnderstandingComponent;
import com.microsoft.xuetang.schema.request.Request;
import com.microsoft.xuetang.schema.response.Response;
import com.microsoft.xuetang.schema.response.search.SearchApiResponseV2;
import com.microsoft.xuetang.service.RecommendService;
import com.microsoft.xuetang.util.Constants;
import com.microsoft.xuetang.util.LogUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by shijianguang on 10/12/16.
 */
@Controller
@RequestMapping("/recommend")
public class RecommendApiV1Controller {
    @Autowired
    private RecommendService recommendService;
    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/recommend", method = { RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response search(HttpServletRequest request, HttpServletResponse response, Request param) {
        SearchList<SearchElementData> ret = recommendService.recommend(param);
        return new Response<>(ret);
    }
}
