package com.microsoft.xuetang.Exception;

import com.microsoft.xuetang.util.RspCodeMsg;

/**
 * Created by shijianguang on 3/19/16.
 */
public class RspRuntimeException extends RuntimeException {

    private static final long serialVersionUID = 87012420802149927L;

    // 子类需要设置一个默认的异常返回状态码
    protected RspCodeMsg codeMsg;

    public RspRuntimeException(RspCodeMsg codeMsg) {
        super();
        this.codeMsg = codeMsg;
    }

    public RspRuntimeException(RspCodeMsg codeMsg, String message) {
        super(message);
        this.codeMsg = codeMsg;
    }

    public RspRuntimeException(RspCodeMsg codeMsg, String message, Throwable cause) {
        super(message, cause);
        this.codeMsg = codeMsg;
    }

    public RspRuntimeException(RspCodeMsg codeMsg, Throwable cause) {
        super(cause);
        this.codeMsg = codeMsg;
    }

    public RspCodeMsg getCodeMsg() {
        return codeMsg;
    }

    public void setCodeMsg(RspCodeMsg codeMsg) {
        this.codeMsg = codeMsg;
    }
}