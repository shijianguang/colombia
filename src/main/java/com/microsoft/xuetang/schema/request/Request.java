package com.microsoft.xuetang.schema.request;

import java.util.UUID;

/**
 * Created by shijianguang on 3/19/16.
 */
public class Request {
    private String deviceId;
    private String agent;
    private String traceId;
    private String requestUri;
    private String token;
    private String form;
    private long timestamp;
    private int timeOut = -1;

    public Request() {}

    public Request(Request other) {
        this.deviceId = other.deviceId;
        this.agent = other.agent;
        this.traceId = other.traceId;
        this.requestUri = other.requestUri;
        this.token = other.token;
        this.form = other.form;
        this.timestamp = other.timestamp;
        this.timeOut = other.timeOut;

    }

    public String getAgent() {
        return agent;
    }

    public void setAgent(String agent) {
        this.agent = agent;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public int getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(int timeOut) {
        this.timeOut = timeOut;
    }

    public void validate() {
    }
}
