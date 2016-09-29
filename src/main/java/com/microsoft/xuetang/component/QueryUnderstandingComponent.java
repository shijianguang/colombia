package com.microsoft.xuetang.component;

import com.microsoft.xuetang.internalrpc.response.QueryUnderstandingResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiash on 8/25/2016.
 */
public class QueryUnderstandingComponent extends BaseHttpComponent {

    private static final Logger logger = LoggerFactory.getLogger(QueryUnderstandingComponent.class);

    private static final String HOST = "http://10.0.0.5:4080";
    private static final String QU_ANNOTATION_URL = HOST + "/qu/query_annotation";
    private static final String SENSITIVE_DETECT_URL = HOST + "/qu/sensitive_detect";


    public static QueryUnderstandingResponse getQueryUnderstandingResponse(String query) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>()
        {
            {
                put("query", query);
            }
        };

        QueryUnderstandingResponse response = BaseHttpComponent.fluentSyncGet(QU_ANNOTATION_URL, param, QueryUnderstandingResponse.class);

        if(response != null || response.getKeywords() != null) {
            logger.debug("Query Understanding Response: keywords: {}", response.getKeywords());
        }

        return response;
    }

    public static QueryUnderstandingResponse getSensitiveDetectResponse(String query) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>()
        {
            {
                put("query", query);
            }
        };

        QueryUnderstandingResponse response = BaseHttpComponent.fluentSyncGet(SENSITIVE_DETECT_URL, param, QueryUnderstandingResponse.class);

        if(response != null || response.getKeywords() != null) {
            logger.debug("Query Understanding Response: keywords: {}", response.getKeywords());
        }

        return response;
    }

    public static boolean needBlock(String query) {
        try {
            QueryUnderstandingResponse response = getSensitiveDetectResponse(query);
            if(response != null && response.getKeywords() != null && response.getKeywords().size() > 0) {
                return true;
            }
        } catch (Exception e) {
            logger.error("Detect sensitive encounter exception. Reason is: {}", e.getMessage());
        }

        return false;
    }


}
