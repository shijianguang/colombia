package com.microsoft.xuetang.util;

/**
 * Created by jiash on 7/29/2016.
 */
public class SamplePair<V1, V2> {
    private V1 first;
    private V2 second;

    public SamplePair() {};
    public SamplePair(V1 first, V2 second) {
        this.first = first;
        this.second = second;
    }

    public V1 getFirst() {
        return first;
    }

    public void setFirst(V1 first) {
        this.first = first;
    }

    public V2 getSecond() {
        return second;
    }

    public void setSecond(V2 second) {
        this.second = second;
    }
}
