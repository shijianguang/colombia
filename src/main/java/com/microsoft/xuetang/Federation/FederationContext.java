package com.microsoft.xuetang.Federation;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Created by jiash on 7/29/2016.
 */
public class FederationContext<T> {
    private Map<String, Object> data;
    private volatile Future<T> result;
    private long timeOut;
    private TimeUnit timeUnit;

    private FederationExecution execution;

    private String name;

    public FederationContext() {
        data = new HashMap<>();
        timeOut = -1;
    }

    public FederationContext(String name) {
        this();
        this.name = name;
    }

    public Object put(String key, Object value) {
        return data.put(key, value);
    }

    public Object get(String key) {
        return data.get(key);
    }

    public String getString(String key) {
        Object obj = data.get(key);

        if (obj instanceof String) {
            return (String)obj;
        }

        return null;
    }

    public Integer getInteger(String key) {
        Object obj = data.get(key);

        if (obj instanceof Integer) {
            return (Integer) obj;
        }

        return null;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public T getResult() throws ExecutionException, InterruptedException {
        if(this.result == null) {
            return null;
        }

        return result.get();
    }

    public T getResult(long timeout, TimeUnit timeUnit) throws ExecutionException, InterruptedException, TimeoutException {
        if(this.result == null) {
            return null;
        }

        return result.get(timeout, timeUnit);
    }

    public T fluentGetResult() {
        if(this.result == null) {
            return null;
        }

        try {
            return result.get();
        } catch (InterruptedException | ExecutionException e) {
            return null;
        }
    }

    public T fluentGetResult(long timeout, TimeUnit timeUnit) {
        if(this.result == null) {
            return null;
        }

        try {
            return result.get(timeout, timeUnit);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            return null;
        }
    }

    public TimeUnit getTimeUnit() {
        return timeUnit;
    }

    public void setTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public FederationExecution getExecution() {
        return execution;
    }

    public void setExecution(FederationExecution execution) {
        this.execution = execution;
    }

    public String getInfo() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Name: ");
        buffer.append(name);
        buffer.append(" timeout: ");
        buffer.append(timeOut);
        if(timeUnit != null) {
            buffer.append(" timeunit: ");
            buffer.append(timeUnit.toString());
        }

        return buffer.toString();
    }

    void setResultFuture(Future<T> future) {
        this.result = future;
    }
}
