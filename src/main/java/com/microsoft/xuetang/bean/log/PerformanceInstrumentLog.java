package com.microsoft.xuetang.bean.log;

/**
 * Created by shijianguang on 9/9/16.
 */
public class PerformanceInstrumentLog extends InstrumentLog {
    private String key;
    private long costInMillis;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public long getCostInMillis() {
        return costInMillis;
    }

    public void setCostInMillis(long costInMillis) {
        this.costInMillis = costInMillis;
    }
}
