package com.microsoft.xuetang.Federation;

/**
 * Created by jiash on 7/29/2016.
 */
public abstract class FederationExecution<T> {
    public abstract T execute(FederationContext<T> context) throws Exception;
}
