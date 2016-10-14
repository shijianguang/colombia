package com.microsoft.xuetang.controller;

import com.microsoft.xuetang.aspect.ApiRequest;
import com.microsoft.xuetang.bean.schema.response.detail.MultiMediaDetailData;
import com.microsoft.xuetang.bean.schema.response.detail.PaperProfileData;
import com.microsoft.xuetang.schema.request.detail.BatchDetailDataApiRequest;
import com.microsoft.xuetang.schema.request.detail.DetailDataApiRequest;
import com.microsoft.xuetang.schema.request.detail.JsonPDetailDataApiRequest;
import com.microsoft.xuetang.schema.response.JsonPResponse;
import com.microsoft.xuetang.schema.response.Response;
import com.microsoft.xuetang.schema.response.detail.DetailDataResponse;
import com.microsoft.xuetang.service.DetailDataService;
import com.microsoft.xuetang.util.Constants;
import com.microsoft.xuetang.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
@Controller
@RequestMapping("/detail")
public class DetailDataApiController {
    @Autowired
    private DetailDataService detailDataService;

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/get", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response get(HttpServletRequest request, HttpServletResponse response, DetailDataApiRequest param) {
        Object ret = null;
        if(Constants.DataType.WEB.equalsIgnoreCase(param.getType()) || Constants.DataType.WIKI.equalsIgnoreCase(param.getType())) {
            ret = detailDataService.getWebDetailData(param);
        } else if (Constants.DataType.PPT.equalsIgnoreCase(param.getType()) || Constants.DataType.VIDEO.equalsIgnoreCase(param.getType())) {
            ret = detailDataService.getMultiMediaDetailData(param);
        } else if (Constants.DataType.PAPER.equalsIgnoreCase(param.getType())) {
            ret = detailDataService.getPaperDetailData(param);
        }
        return new Response<>(new DetailDataResponse<>(param.getType(), ret));
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/jsonPGet", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Object jsonPGet(HttpServletRequest request, HttpServletResponse response, JsonPDetailDataApiRequest param) {
        Object ret = null;
        if(Constants.DataType.WEB.equalsIgnoreCase(param.getType()) || Constants.DataType.WIKI.equalsIgnoreCase(param.getType())) {
            ret = detailDataService.getWebDetailData(param);
        } else if (Constants.DataType.PPT.equalsIgnoreCase(param.getType()) || Constants.DataType.VIDEO.equalsIgnoreCase(param.getType())) {
            ret = detailDataService.getMultiMediaDetailData(param);
        } else if (Constants.DataType.PAPER.equalsIgnoreCase(param.getType())) {
            ret = detailDataService.getPaperDetailData(param);
        }

        return new JsonPResponse(String.format("%s(%s)", param.getCallback(), JsonUtil.fluentToJson(new Response<>(new DetailDataResponse<>(param.getType(), ret)))));
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/batchGet", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response batchGet(HttpServletRequest request, HttpServletResponse response, BatchDetailDataApiRequest param) {
        Object ret = null;
        if(Constants.DataType.WEB.equalsIgnoreCase(param.getType()) || Constants.DataType.WIKI.equalsIgnoreCase(param.getType())) {
            ret = detailDataService.batchGetWebDetailData(param);
        } else if (Constants.DataType.PPT.equalsIgnoreCase(param.getType()) || Constants.DataType.VIDEO.equalsIgnoreCase(param.getType())) {
            ret = detailDataService.batchGetMultiMediaDetailData(param);
        } else if (Constants.DataType.PAPER.equalsIgnoreCase(param.getType())) {
            ret = detailDataService.batchGetPaperProfileData(param);
        }

        return new Response<>(new DetailDataResponse<>(param.getType(), ret));
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/multiMedia", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response multiMedia(HttpServletRequest request, HttpServletResponse response, DetailDataApiRequest param) {
        MultiMediaDetailData ret = detailDataService.getMultiMediaDetailData(param);
        return new Response<>(new DetailDataResponse<>(param.getType(), ret));
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/batchMultiMedia", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response batchMultiMedia(HttpServletRequest request, HttpServletResponse response, BatchDetailDataApiRequest param) {
        Map<String, MultiMediaDetailData> ret = detailDataService.batchGetMultiMediaDetailData(param);
        return new Response<>(new DetailDataResponse<>(param.getType(), ret));
    }

    @ApiRequest
    @ResponseBody
    @RequestMapping(value = "v1/academic", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json")
    public Response academic(HttpServletRequest request, HttpServletResponse response, DetailDataApiRequest param) {
        PaperProfileData ret = detailDataService.getPaperDetailData(param);
        return new Response<>(new DetailDataResponse<>(param.getType(), ret));
    }

}
