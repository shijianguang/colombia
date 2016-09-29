package com.microsoft.xuetang.Exception;

import com.microsoft.xuetang.util.RspCodeMsg;

/**
 * Created by shijianguang on 4/8/16.
 */
public class HttpRequestException extends RspRuntimeException {
    private static RspCodeMsg defaultCodeMsg = RspCodeMsg.HTTP_EXP;

    public HttpRequestException() {
        super(defaultCodeMsg);
    }

    public HttpRequestException(Throwable t) {
        super(defaultCodeMsg, t);
    }

    public HttpRequestException(String message) {
        super(defaultCodeMsg, message);
    }
    public HttpRequestException(RspCodeMsg codeMsg) {
        super(codeMsg);
    }

    public HttpRequestException(RspCodeMsg codeMsg, String message) {
        super(codeMsg, message);
    }

    public HttpRequestException(RspCodeMsg codeMsg, String message, Throwable cause) {
        super(codeMsg, message, cause);
    }

    public HttpRequestException(RspCodeMsg codeMsg, Throwable cause) {
        super(codeMsg, cause);
    }
}
