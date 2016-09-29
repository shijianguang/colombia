package com.microsoft.xuetang.internalrpc.response;

import com.microsoft.xuetang.bean.internal.response.BingAcademicSearchEntity;

import java.util.List;

/**
 * Created by shijianguang on 9/19/16.
 */
public class BingAcademicSearchResponse {
    private String query;
    private int total;
    private double Latency;
    private String eventId;
    private int count;
    private List<BingAcademicSearchEntity> result;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public double getLatency() {
        return Latency;
    }

    public void setLatency(double latency) {
        Latency = latency;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<BingAcademicSearchEntity> getResult() {
        return result;
    }

    public void setResult(List<BingAcademicSearchEntity> result) {
        this.result = result;
    }
}
