package com.microsoft.xuetang.schema.request.recommend;

import com.microsoft.xuetang.Exception.ParamCheckException;
import com.microsoft.xuetang.schema.request.Request;
import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by shijianguang on 10/27/16.
 */
public class RecommendApiRequest extends Request {
    private String query;
    private String offset = "0";
    private int offsetInt;

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public int getOffsetInt() {
        return offsetInt;
    }

    @JsonIgnore
    public void setOffsetInt(int offsetInt) {
        this.offsetInt = offsetInt;
    }

    public void validate() {
        super.validate();

        try {
            offsetInt = Integer.parseInt(offset);
        } catch (Throwable t) {
            throw new ParamCheckException("offset is not a valid integer");
        }


        if(offsetInt < 0) {
            throw new ParamCheckException("offsetInt can't be negative");
        }

        try {
            if(!StringUtils.isBlank(query)) {
                query = URLDecoder.decode(query, "UTF-8");
                query = query.trim().toLowerCase();
            }
        } catch (UnsupportedEncodingException e) {
            throw new ParamCheckException("url decode error.");
        }
    }
}
