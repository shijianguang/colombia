package com.microsoft.xuetang.internalrpc.response;

import com.microsoft.xuetang.bean.internal.response.BingWebResultEntityList;

import java.util.Map;

/**
 * Created by jiash on 7/29/2016.
 */
public class BingWebSearchResponse {
    private Map<String,String> instrumentation;
    private BingWebResultEntityList webPages;

    public Map<String, String> getInstrumentation() {
        return instrumentation;
    }

    public void setInstrumentation(Map<String, String> instrumentation) {
        this.instrumentation = instrumentation;
    }

    public BingWebResultEntityList getWebPages() {
        return webPages;
    }

    public void setWebResultEntityList(BingWebResultEntityList webPages) {
        this.webPages = webPages;
    }
}
