package com.microsoft.xuetang.controller;

import com.microsoft.xuetang.aspect.ApiRequest;
import com.microsoft.xuetang.bean.SearchList;
import com.microsoft.xuetang.bean.internal.response.DialogueEngineSearchEntity;
import com.microsoft.xuetang.bean.schema.response.search.BaseSearchViewEntity;
import com.microsoft.xuetang.schema.request.search.SearchApiRequest;
import com.microsoft.xuetang.schema.response.Response;
import com.microsoft.xuetang.schema.response.search.PopularSearchResponse;
import com.microsoft.xuetang.schema.response.search.SearchApiResponse;
import com.microsoft.xuetang.service.DataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
@Controller
@RequestMapping("/search")
public class SearchApiController {
    @Autowired
    private DataService dataService;

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/list", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response search(HttpServletRequest request, HttpServletResponse response, SearchApiRequest param) {
        SearchApiResponse ret = dataService.search(param);
        return new Response<>(ret);
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/wikiSearch", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response wikiSearch(HttpServletRequest request, HttpServletResponse response, SearchApiRequest param) {
        SearchList<BaseSearchViewEntity> ret = dataService.wikiSearch(param);
        return new Response<>(ret);
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/webSearch", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response webSearch(HttpServletRequest request, HttpServletResponse response, SearchApiRequest param) {
        SearchList<BaseSearchViewEntity> ret = dataService.webSearch(param);
        return new Response<>(ret);
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/multiMediaSearch", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response multiMediaSearch(HttpServletRequest request, HttpServletResponse response, SearchApiRequest param) {
        Map<String, SearchList> ret = dataService.multiMediaSearch(param);
        return new Response<>(ret);
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/academicSearch", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response academicSearch(HttpServletRequest request, HttpServletResponse response, SearchApiRequest param) {
        SearchList<DialogueEngineSearchEntity> ret = dataService.academicSearch(param);
        return new Response<>(ret);
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/popularSearch", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response popularSearch(HttpServletRequest request, HttpServletResponse response) {
        return new Response<>(new PopularSearchResponse());
    }
}
