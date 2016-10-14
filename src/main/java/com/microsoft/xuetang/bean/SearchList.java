package com.microsoft.xuetang.bean;

import com.microsoft.xuetang.util.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by jiash on 7/29/2016.
 */
public class SearchList<T> {
    private String query;
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

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
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
        if(list != null) {
            this.count = list.size();
        } else {
            this.count = 0;
        }
    }

    public T getData(int i) {
        if(list == null || i >= list.size()) {
            return null;
        }

        return list.get(i);
    }

    public boolean add(T data) {
        if(data == null) {
            return true;
        }
        if(list == null) {
            list = new ArrayList<>();
        }
        boolean result = list.add(data);
        if(result) {
            ++ count;
        }

        return result;
    }

    public boolean add(SearchList<T> data) {
        if(data == null || data.getList() == null || data.getList().size() == 0) {
            return true;
        }
        if(list == null) {
            list = new ArrayList<>();
        }
        boolean result = list.addAll(data.getList());
        if(result) {
            count += data.getList().size();
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

    public void shuffule(int chunkSize) {
        shuffule(chunkSize, new Random());
    }

    public void shuffule(int chunkSize, Random random) {
        if(list == null || list.size() == 0) {
            return;
        }

        int idx = 0;
        int size = list.size();
        while(idx < size) {
            CommonUtils.shuffle(list, idx, idx + chunkSize);
            idx += chunkSize;
        }
    }

    public T radomGetInRange(int start, int length, Random random) {
        if(list == null || start >= list.size()) {
            return null;
        }

        int size = list.size();
        int left = size - start;
        length = left > length ? length : left;
        return list.get(start + random.nextInt(length));
    }
}
