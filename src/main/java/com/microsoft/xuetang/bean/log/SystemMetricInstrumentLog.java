package com.microsoft.xuetang.bean.log;

/**
 * Created by shijianguang on 10/11/16.
 */
public class SystemMetricInstrumentLog extends InstrumentLog {
    private String key;
    private long first;
    private long second;

    public SystemMetricInstrumentLog() {

    }

    public SystemMetricInstrumentLog(String key, long first, long second) {
        this.key = key;
        this.first = first;
        this.second = second;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getFirst() {
        return first;
    }

    public void setFirst(long first) {
        this.first = first;
    }

    public long getSecond() {
        return second;
    }

    public void setSecond(long second) {
        this.second = second;
    }
}
