package com.microsoft.xuetang.schema.request.detail;

import com.microsoft.xuetang.Exception.ParamCheckException;
import com.microsoft.xuetang.schema.request.Request;
import com.microsoft.xuetang.util.Constants;
import org.apache.commons.lang.StringUtils;

/**
 * Created by jiash on 8/8/2016.
 */
public class DetailDataApiRequest extends Request {
    private String id;
    private String type;
    private String query;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void validate() {
        super.validate();
        if(StringUtils.isEmpty(id)) {
            throw new ParamCheckException("Id is empty");
        }

        if(StringUtils.isEmpty(type)) {
            throw new ParamCheckException("Type is empty");
        }
        if(!Constants.DataType.contains(type)) {
            throw new ParamCheckException("No type called " + type);
        }
    }
}
