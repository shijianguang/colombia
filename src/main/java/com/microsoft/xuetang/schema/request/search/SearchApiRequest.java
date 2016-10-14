package com.microsoft.xuetang.schema.request.search;

import com.microsoft.xuetang.Exception.ParamCheckException;
import com.microsoft.xuetang.bean.internal.response.QueryKeyword;
import com.microsoft.xuetang.schema.request.Request;
import com.microsoft.xuetang.util.Constants;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

/**
 * Created by jiash on 7/29/2016.
 */
public class SearchApiRequest extends Request {
    private String query;
    private String count = "10";
    private String offset = "0";
    private int countInt;
    private int offsetInt;
    private String flight;

    private List<QueryKeyword> keywords;

    public SearchApiRequest() {
    }

    public SearchApiRequest(Request request) {
        super(request);
    }

    public SearchApiRequest(SearchApiRequest other) {
        super(other);
        query = other.query;
        count = other.count;
        offset = other.offset;
        countInt = other.countInt;
        offsetInt = other.offsetInt;
        flight = other.flight;
    }

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

    public String getFlight() {
        return flight;
    }

    @JsonIgnore
    public void setFlight(String flight) {
        this.flight = flight;
    }


    public int getCountInt() {
        return countInt;
    }

    @JsonIgnore
    public void setCountInt(int countInt) {
        this.countInt = countInt;
    }

    public int getOffsetInt() {
        return offsetInt;
    }

    @JsonIgnore
    public void setOffsetInt(int offsetInt) {
        this.offsetInt = offsetInt;
    }

    @JsonIgnore
    public void setAllTypeCount(int count) {
        this.countInt = count;
        this.count = Constants.AUTOINCREASE_ID2STRING_CACHE[count];
    }

    @JsonIgnore
    public void setAllTypeOffset(int offset) {
        this.offsetInt = offset;
        this.offset = Constants.AUTOINCREASE_ID2STRING_CACHE[offset];
    }

    public List<QueryKeyword> getKeywords() {
        return keywords;
    }
    @JsonIgnore
    public void setKeywords(List<QueryKeyword> keywords) {
        this.keywords = keywords;
    }

    public void validate() {
        super.validate();
        try {
            countInt = Integer.parseInt(count);
        } catch (Throwable t) {
            throw new ParamCheckException("count is not a valid integer");
        }

        try {
            offsetInt = Integer.parseInt(offset);
        } catch (Throwable t) {
            throw new ParamCheckException("offset is not a valid integer");
        }

        if(countInt < 0) {
            throw new ParamCheckException("count can't be negative");
        }

        if(offsetInt < 0) {
            throw new ParamCheckException("offsetInt can't be negative");
        }

        try {
            offsetInt = Integer.parseInt(offset);
        } catch (Throwable t) {
            throw new ParamCheckException("offset is not a valid integer");
        }

        try {
            query = URLDecoder.decode(query, "UTF-8");
            query = query.trim().toLowerCase();
        } catch (UnsupportedEncodingException e) {
            throw new ParamCheckException("url decode error.");
        }
    }
}
