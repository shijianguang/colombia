package com.microsoft.xuetang.component;

import com.microsoft.xuetang.internalrpc.response.DialogueEngineSearchResponse;
import com.microsoft.xuetang.internalrpc.response.DialogueInterpretResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/1/2016.
 */
public class DialogueEngineHttpComponent extends BaseHttpComponent {
    public static final String DEV4_INTERPRET_URL = "http://academic-api-ext-eastasia.cloudapp.net/interpret";
    public static final String DEV4_SEARCH_URL = "http://academic-api-ext-eastasia.cloudapp.net/evaluate";
    public static final String PAPER_PROFILE_URL = "http://mylib-hk2.azurewebsites.net/Entity/GetBasicInfo";

    public static final Map<String, String> INTERPRET_PARAM = new HashMap<String, String>()
    {
        {
            put("timeout", "3000");
            put("count", "5");
        }
    };

    public static final Map<String, String> DEV4_SEARCH_PARAM = new HashMap<String, String>()
    {
        {
            put("attributes", "*");
            put("count", "10");
            put("offset", "0");
            put("timeout", "2000");
        }
    };

    public static final Map<String, String> PAPER_PROFILE_PARAM = new HashMap<String, String>()
    {
        {
            put("type", "0");
        }
    };

    public static DialogueInterpretResponse interpret(String query, String count, String timeout) throws Exception {

        Map<String, Object> interpretParam = new HashMap<String, Object>() {
            {
                put("query", query);
                put("timeout", timeout);
                put("count", count);

            }
        };
        interpretParam.put("query", query);
        DialogueInterpretResponse interpretResponse = BaseHttpComponent.fluentSyncGet(DEV4_INTERPRET_URL, interpretParam, DialogueInterpretResponse.class);

        return interpretResponse;
    }

    public static DialogueEngineSearchResponse search(List<DialogueInterpretResponse.Interpretation> interpretations, String attributes, String count, String offset, String timeout) throws Exception {

        StringBuffer interpretResultBuffer = new StringBuffer();
        int interpretCount = 0;
        for(DialogueInterpretResponse.Interpretation interpretation : interpretations) {
            if(interpretation.getRules() == null || interpretation.getRules().size() == 0) {
                continue;
            }

            DialogueInterpretResponse.Interpretation.Rule rule = interpretation.getRules().get(0);
            if(rule.getOutput() == null) {
                continue;
            }

            String value = rule.getOutput().get("value");
            if(value == null) {
                continue;
            }
            if(interpretCount == 0) {
                interpretResultBuffer.append("Or(");
            } else {
                interpretResultBuffer.append(",Or(");
            }

            interpretResultBuffer.append(value);
            ++ interpretCount;
        }

        for(int i = 0 ; i < interpretCount ; ++ i) {
            interpretResultBuffer.append(")");
        }

        String interpretResult = interpretResultBuffer.toString();
        Map<String, Object> searchParam = new HashMap<String, Object>()
        {
            {
                put("attributes", attributes);
                put("count", count);
                put("offset", offset);
                put("timeout", timeout);
                put("expr", interpretResult);
            }
        };

        DialogueEngineSearchResponse dialogueEngineResponse = BaseHttpComponent.fluentSyncGet(DEV4_SEARCH_URL, searchParam, DialogueEngineSearchResponse.class);

        return dialogueEngineResponse;
    }
}
