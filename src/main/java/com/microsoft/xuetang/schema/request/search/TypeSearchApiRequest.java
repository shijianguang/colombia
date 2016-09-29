package com.microsoft.xuetang.schema.request.search;

import com.microsoft.xuetang.Exception.ParamCheckException;
import com.microsoft.xuetang.util.Constants;

/**
 * Created by jiash on 8/25/2016.
 */
public class TypeSearchApiRequest extends SearchApiRequest {
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void validate() {
        super.validate();
        if(type == null) {
            throw new ParamCheckException("type param is not set");
        }

        if(!Constants.DataType.contains(type)) {
            throw new ParamCheckException(String.format("%s is not a valid type", type));
        }
    }
}
