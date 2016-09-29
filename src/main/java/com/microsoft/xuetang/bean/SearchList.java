package com.microsoft.xuetang.bean;

import com.google.common.collect.Collections2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by jiash on 7/29/2016.
 */
public class SearchList<T> {
    private int count = 0;
    private List<T> list = null;

    public SearchList() {
    }

    public SearchList(List<T> list) {
        if(list != null) {
            count = list.size();
            this.list = list;
        }
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public boolean add(T data) {
        if(list == null) {
            list = new ArrayList<>();
        }
        boolean result = list.add(data);
        if(result) {
            ++ count;
        }

        return result;
    }

    public SearchList<T> top(int n) {
        List<T> newList = list.subList(0, Math.min(n, list.size()));
        return new SearchList<>(newList);
    }

    public SearchList<T> range(int offset, int count) {
        if(list != null) {
            if(offset > list.size()) {
                return new SearchList<>(Collections.EMPTY_LIST);
            }
            List<T> newList = list.subList(offset, Math.min(offset + count, list.size()));
            return new SearchList<>(newList);
        }
        return new SearchList<>(Collections.EMPTY_LIST);
    }
}
