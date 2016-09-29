package com.microsoft.xuetang.schema.request.detail;

import com.microsoft.xuetang.Exception.ParamCheckException;
import com.microsoft.xuetang.schema.request.Request;
import com.microsoft.xuetang.util.Constants;

import java.util.List;

/**
 * Created by jiash on 8/11/2016.
 */
public class BatchDetailDataApiRequest extends Request {
    private List<String> idList;
    private String type;
    private String query;

    public List<String> getIdList() {
        return idList;
    }

    public void setIdList(List<String> idList) {
        this.idList = idList;
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
        if(idList == null) {
            throw new ParamCheckException("idList param is not set");
        }

        if(type == null) {
            throw new ParamCheckException("type param is not set");
        }

        if(!Constants.DataType.contains(type)) {
            throw new ParamCheckException(String.format("%s is not a valid type", type));
        }
    }
}
