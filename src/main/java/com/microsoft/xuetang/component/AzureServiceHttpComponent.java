package com.microsoft.xuetang.component;

import com.microsoft.xuetang.internalrpc.response.BingPaperProfileResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/1/2016.
 */
public class AzureServiceHttpComponent extends BaseHttpComponent {

    public static final String PAPER_PROFILE_URL = "http://mylib-hk2.azurewebsites.net/Entity/GetBasicInfo";


    public static final Map<String, String> PAPER_PROFILE_PARAM = new HashMap<String, String>()
    {
        {
            put("type", "0");
        }
    };

    public static Map<String, Object> batchGetPaperProfile(List<String> ids) {
        Map<String, Map<String, Object>> id2Param = new HashMap<>();
        for (String id : ids) {
            Map<String, Object> paperProfileParam = new HashMap<>(PAPER_PROFILE_PARAM);

            paperProfileParam.put("id", id);
            id2Param.put(id, paperProfileParam);
        }

        Map<String, Object> result = BaseHttpComponent.asyncGet(PAPER_PROFILE_URL, id2Param, BingPaperProfileResponse.class);

        return result;
    }

    public static BingPaperProfileResponse getPaperProfile(String id) throws Exception {
        Map<String, Object> param = new HashMap<>(PAPER_PROFILE_PARAM);

        param.put("id", id);

        BingPaperProfileResponse result = null;

        result = BaseHttpComponent.fluentSyncGet(PAPER_PROFILE_URL, param, BingPaperProfileResponse.class);

        return result;
    }
}
