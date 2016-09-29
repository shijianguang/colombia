package com.microsoft.xuetang.bean.log;

/**
 * Created by shijianguang on 9/8/16.
 */
public class SearchInstrumentLog extends InstrumentLog {
    private String query;
    private String count;
    private String offset;
    private String searchType;
    private String flight;
    private SearchResultLog result;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    public SearchResultLog getResult() {
        return result;
    }

    public String getFlight() {
        return flight;
    }

    public void setFlight(String flight) {
        this.flight = flight;
    }

    public void setResult(SearchResultLog result) {
        this.result = result;
    }
}
