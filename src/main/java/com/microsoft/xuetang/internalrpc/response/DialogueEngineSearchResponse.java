package com.microsoft.xuetang.internalrpc.response;

import com.microsoft.xuetang.bean.internal.response.DialogueEngineSearchEntity;

import java.util.List;

/**
 * Created by jiash on 7/29/2016.
 */
public class DialogueEngineSearchResponse {
    private String expr;
    private List<DialogueEngineSearchEntity> entities;

    public String getExpr() {
        return expr;
    }

    public void setExpr(String expr) {
        this.expr = expr;
    }

    public List<DialogueEngineSearchEntity> getEntities() {
        return entities;
    }

    public void setEntities(List<DialogueEngineSearchEntity> entities) {
        this.entities = entities;
    }
}
