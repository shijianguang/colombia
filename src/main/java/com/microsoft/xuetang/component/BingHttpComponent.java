package com.microsoft.xuetang.component;

import com.microsoft.xuetang.bean.EntityBean;
import com.microsoft.xuetang.bean.internal.response.BingAcademicSearchEntity;
import com.microsoft.xuetang.internalrpc.response.BingAcademicSearchResponse;
import com.microsoft.xuetang.internalrpc.response.BingVideoSearchResponse;
import com.microsoft.xuetang.internalrpc.response.BingWebSearchResponse;
import com.microsoft.xuetang.util.JsonUtil;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.JsonNode;

import java.io.IOException;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Created by jiash on 8/1/2016.
 */
public class BingHttpComponent extends BaseHttpComponent {
    private static final String BING_URL = "https://www.bingapis.com/api/v5/search";
    private static final String VIDEO_SEARCH_BACKEND_URL = "https://www.bingapis.com/api/v5/videos/search";
    private static final String BING_ACADEMIC_URL = "http://dict.bing.com.cn/api/http/v2/academic/";

    private static final Pattern HIGHLIGHT_PATTERN = Pattern.compile("\uE000|\uE001");

    public static BingWebSearchResponse getBingWebSearchResponse(String query, String count, String offset, String flight) throws Exception {
        if(query == null || count == null || offset == null) {
            return null;
        }

        Map<String, Object> param = new HashMap<String, Object>()
        {
            {
                put("appid", "D41D8CD98F00B204E9800998ECF8427E20855545");
                put("mkt", "zh-cn");
                put("responseFilter", "WebPages");
                put("sf", flight);
                put("q", query);
                put("count", count);
                put("offset", offset);
            }
        };

        BingWebSearchResponse response = BaseHttpComponent.fluentSyncGet(BING_URL, param, BingWebSearchResponse.class);

        return response;
    }

    public static BingVideoSearchResponse getBingVideoSearchResponse(String query, String count, String offset) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>()
        {
            {
                put("appid", "d41d8cd98f00b204e9800998ecf8427e461dc0e7");
                put("mkt", "en-US");
                put("count", count);
                put("offset", offset);
                put("q", query);
            }
        };

        BingVideoSearchResponse response = BaseHttpComponent.fluentSyncGet(VIDEO_SEARCH_BACKEND_URL, param, BingVideoSearchResponse.class);

        return response;
    }

    public static BingAcademicSearchResponse getBingAcademicResponse(String query, String count, String offset) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>()
        {
            {
                put("count", count);
                put("offset", offset);
                put("q", query);
            }
        };
        String responseString = BaseHttpComponent.fluentSyncGet(BING_ACADEMIC_URL, param, String.class);

        return string2BingAcademicSearchResponse(responseString);
    }

    static BingAcademicSearchResponse string2BingAcademicSearchResponse(String response) throws IOException {
        if(StringUtils.isBlank(response)) {
            return null;
        }
        int start = 0, end = response.length();
        while(response.charAt(start) == '\"') {
            start += 1;
        }
        while(response.charAt(end - 1) == '\"') {
            end -= 1;
        }

        response = response.substring(start, end).replace("\\\"", "\"").replace("\\\\\\", "\\").replace("\\\\\"", "\\\\\\\"");
        JsonNode root = JsonUtil.readValue(response);
        if(root == null) {
            return null;
        }
        JsonNode bingResponse = root.get("BdiGeneric_BingResponse_1_0");
        if(bingResponse == null) {
            return null;
        }

        JsonNode responseArray = bingResponse.get("Responses");

        if(responseArray == null || !responseArray.isArray()) {
            return null;
        }

        List<String> urlList = new ArrayList<>();
        String query = null;
        int total = -1;
        double latency = -1;
        String eventId = null;
        Map<String, BingAcademicSearchEntity> url2Entity = new HashMap<>();
        List<BingAcademicSearchEntity> dev4List = new ArrayList<>();
        HashSet<String> dev4IdSet = new HashSet<>();

        int size = responseArray.size();
        for(int i = 0 ; i < size ; ++ i) {
            JsonNode ele = responseArray.get(i);
            if(ele == null) {
                continue;
            }

            JsonNode result = ele.get("results");
            if(result != null && result.isArray()) {
                int resultSize = result.size();
                for(int j = 0 ; j < resultSize ; ++ j) {
                    JsonNode resultEle = result.get(j);
                    if(resultEle == null) {
                        continue;
                    }
                    JsonNode url = resultEle.get("Url");
                    if (url == null || !url.isTextual()) {
                        continue;
                    }

                    urlList.add(url.asText().toLowerCase());
                }

                JsonNode jsonNodeQuery = ele.get("Query");
                if(jsonNodeQuery != null && jsonNodeQuery.isTextual()) {
                    query = jsonNodeQuery.asText();
                }
                JsonNode jsonNodeTotal = ele.get("Total");
                if(jsonNodeTotal != null && jsonNodeTotal.isInt()) {
                    total = jsonNodeTotal.asInt();
                }
                JsonNode jsonNodeLatency = ele.get("Latency");
                if(jsonNodeLatency != null && jsonNodeLatency.isDouble()) {
                    latency = jsonNodeLatency.asDouble();
                }
                JsonNode jsonNodeEventId = ele.get("EventId");
                if(jsonNodeEventId != null && jsonNodeEventId.isTextual()) {
                    eventId = jsonNodeEventId.asText();
                }
            }

            JsonNode papers = ele.get("Papers");
            JsonNode answerType = ele.get("AnswerType");
            if(answerType != null && answerType.isTextual() && answerType.asText().equals("DEv4") && papers != null && papers.isArray()) {
                int paperSize = papers.size();
                for(int j = 0 ; j < paperSize ; ++ j) {
                    JsonNode paperEle = papers.get(j);
                    BingAcademicSearchEntity entity = transformToEntity(paperEle);
                    if(entity == null) {
                        continue;
                    }

                    dev4List.add(entity);
                    dev4IdSet.add(entity.getId());
                }
            }

            papers = ele.get("Papers");
            answerType = ele.get("AnswerType");
            if(answerType != null && answerType.isTextual() && answerType.asText().equals("WebRichCaption") && papers != null && papers.isArray()) {
                int paperSize = papers.size();
                for(int j = 0 ; j < paperSize ; ++ j) {
                    JsonNode paperEle = papers.get(j);
                    BingAcademicSearchEntity entity = transformToEntity(paperEle);
                    if(entity == null) {
                        continue;
                    }

                    url2Entity.put(entity.getUrl().toLowerCase(), entity);
                }
            }
        }

        List<BingAcademicSearchEntity> entityList = new ArrayList<>(dev4List);
        for(String url : urlList) {
            BingAcademicSearchEntity entity = url2Entity.get(url);
            if(entity != null && !dev4IdSet.contains(entity.getId())) {
                entityList.add(entity);
            }
        }

        BingAcademicSearchResponse result = new BingAcademicSearchResponse();
        result.setQuery(query);
        result.setEventId(eventId);
        result.setTotal(total);
        result.setLatency(latency);
        result.setCount(entityList.size());
        result.setResult(entityList);

        return result;
    }

    private static BingAcademicSearchEntity transformToEntity(JsonNode paperEle) {
        JsonNode jsonNode = paperEle.get("Url");
        if(jsonNode == null || !jsonNode.isTextual()) {
            return null;
        }
        String url = jsonNode.asText();
        if(StringUtils.isBlank(url)) {
            return null;
        }

        jsonNode = paperEle.get("Id");
        if(jsonNode == null || !JsonUtil.isLongJsonNode(jsonNode) ) {
            return null;
        }

        BingAcademicSearchEntity entity = new BingAcademicSearchEntity();
        entity.setId(String.valueOf(JsonUtil.number2String(jsonNode)));
        entity.setUrl(url);

        jsonNode = paperEle.get("Title");
        if(jsonNode != null) {
            jsonNode = jsonNode.get("NormalizedTitle");
            if(jsonNode != null && jsonNode.isTextual()) {
                entity.setTitle(HIGHLIGHT_PATTERN.matcher(jsonNode.asText()).replaceAll(""));
            }
        }

        jsonNode = paperEle.get("PublishYear");
        if(jsonNode != null && jsonNode.isInt()) {
            entity.setPublishYear(String.valueOf(jsonNode.asInt()));
        }

        jsonNode = paperEle.get("Abstract");
        if(jsonNode != null) {
            jsonNode = jsonNode.get("Content");
            if(jsonNode != null && jsonNode.isTextual()) {
                entity.setSnippet(HIGHLIGHT_PATTERN.matcher(jsonNode.asText()).replaceAll(""));
            }
        }

        jsonNode = paperEle.get("Venue");
        if(jsonNode != null) {
            jsonNode = jsonNode.get("NormalizedVenue");
            if(jsonNode != null && jsonNode.isTextual()) {
                entity.setVenue(HIGHLIGHT_PATTERN.matcher(jsonNode.asText()).replaceAll(""));
            }
        }

        jsonNode = paperEle.get("Authors");
        if(jsonNode != null && jsonNode.isArray()) {
            List<EntityBean> entityBeans = string2BingAcademicSearchResponseHelper(jsonNode);
            entity.setAuthors(entityBeans);
        }

        jsonNode = paperEle.get("Affiliations");
        if(jsonNode != null && jsonNode.isArray()) {
            List<EntityBean> entityBeans = string2BingAcademicSearchResponseHelper(jsonNode);
            entity.setAffiliations(entityBeans);
        }

        jsonNode = paperEle.get("FieldsOfStudies");
        if(jsonNode != null && jsonNode.isArray()) {
            List<EntityBean> entityBeans = string2BingAcademicSearchResponseHelper(jsonNode);
            entity.setFieldOfStudy(entityBeans);
        }

        return entity;
    }

    static List<EntityBean> string2BingAcademicSearchResponseHelper(JsonNode arrayNode) {
        int size = arrayNode.size();
        List<EntityBean> entiyBeans = new ArrayList<>();
        for (int i = 0 ; i < size ; ++ i) {
            JsonNode jsonNode = arrayNode.get(i);
            JsonNode id = jsonNode.get("Id");
            JsonNode name = jsonNode.get("Name");

            if(id != null && JsonUtil.isLongJsonNode(id) && name != null && name.isTextual()) {
                entiyBeans.add(new EntityBean(JsonUtil.number2String(id), name.asText()));
            }


        }

        return entiyBeans;
    }

}
