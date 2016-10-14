package com.microsoft.xuetang.FederationExecution;

import com.microsoft.xuetang.Federation.FederationContext;
import com.microsoft.xuetang.Federation.FederationExecution;
import com.microsoft.xuetang.bean.internal.response.DialogueEngineSearchEntity;
import com.microsoft.xuetang.component.BaseHttpComponent;
import com.microsoft.xuetang.internalrpc.response.BingPaperProfileResponse;
import com.microsoft.xuetang.internalrpc.response.DialogueEngineSearchResponse;
import com.microsoft.xuetang.internalrpc.response.DialogueInterpretResponse;
import com.microsoft.xuetang.internalrpc.response.ErrorResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
public class BingAcademicSearchMatchType extends FederationExecution<DialogueEngineSearchResponse> {
    private static final Logger logger = LoggerFactory.getLogger(BingAcademicSearchMatchType.class);
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

    @Override
    public DialogueEngineSearchResponse execute(FederationContext<DialogueEngineSearchResponse> context) {
        String query = context.getString("query");
        String count = context.getString("count");
        String offset = context.getString("offset");

        Map<String, Object> interpretParam = new HashMap<>(INTERPRET_PARAM);
        interpretParam.put("query", query);
        DialogueInterpretResponse interpretResponse = null;
        try {
            interpretResponse = BaseHttpComponent.fluentSyncGet(DEV4_INTERPRET_URL, interpretParam, DialogueInterpretResponse.class);
        } catch (Exception e) {
            logger.error("Request dialogue interpret api encounter exception. Message: {}", e.getMessage());
        }
        if (interpretResponse == null || interpretResponse.getInterpretations() == null || interpretResponse.getInterpretations().size() == 0) {
            return null;
        }

        StringBuffer interpretResultBuffer = new StringBuffer();
        int interpretCount = 0;
        List<DialogueInterpretResponse.Interpretation> interpretations = interpretResponse.getInterpretations();
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

        Map<String, Object> searchParam = new HashMap<>(DEV4_SEARCH_PARAM);
        searchParam.put("expr", interpretResult);

        DialogueEngineSearchResponse dialogueEngineResponse = null;
        try {
            dialogueEngineResponse = BaseHttpComponent.fluentSyncGet(DEV4_SEARCH_URL, searchParam, DialogueEngineSearchResponse.class);
        } catch (Exception e) {
            logger.error("Request dialogue search api encounter exception. Message: {}", e.getMessage());
        }
        if(dialogueEngineResponse == null || dialogueEngineResponse.getEntities() == null || dialogueEngineResponse.getEntities().size() == 0) {
            return null;
        }
        List<DialogueEngineSearchEntity> entities = dialogueEngineResponse.getEntities();

        Map<String, Map<String, Object>> id2Param = new HashMap<>();
        for(DialogueEngineSearchEntity entity : entities) {
            if(entity.getId() == null) {
                logger.debug("Paper has null Id");
                continue;
            }

            String id = entity.getId().toString();

            Map<String, Object> paperProfileParam = new HashMap<>(PAPER_PROFILE_PARAM);

            paperProfileParam.put("id", id);
            id2Param.put(id, paperProfileParam);
        }

        Map<String, Object> result = BaseHttpComponent.asyncGet(PAPER_PROFILE_URL, id2Param, BingPaperProfileResponse.class);

        for(DialogueEngineSearchEntity entity : entities) {
            entity.setPaperProfile(null);
            if(entity.getId() == null) {
                continue;
            }
            String id = entity.getId().toString();
            Object value = result.get(id);
            if(value instanceof BingPaperProfileResponse) {
                entity.setPaperProfile((BingPaperProfileResponse) value);
            } else if(value instanceof ErrorResponse) {
                logger.error("Get paper %d profile encounter error. Reason is: ", id, ((ErrorResponse) value).getDesc());
            }
        }
        return dialogueEngineResponse;
    }
}
