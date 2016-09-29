package com.microsoft.xuetang.util;

/**
 * Created by shijianguang on 3/19/16.
 */
public enum RspCodeMsg {
    SUCCESS("0000", "Success"), //
    NULL("0002", "结果为null"), //
    FAIL("0001", "Service Unavailable"), //
    CONFIG_ERROR("0003","服务器配置不正确"),
    INNER_PARAM_ERR("1001", "Parameter Check Fail"), //
    INNER_PARSE_EXP("1002", "数据解析异常"), //
    IP_CODE_ERR("1011", "获取当前机器对应code失败"), //
    NOT_MATCH("3007", "手机号密码不匹配"),
    NOT_EXIST("3008", "用户不存在"),
    ALREADY_EXIST("3009", "手机号已被注册"),
    PASSWORD_CHANGED("3010", "密码已被修改"),
    NOT_LOGIN("3011", "您未登录，请进行登录"),
    OUT_OF_TIME("3012", "验证码超时"),
    AUTHENTICATION_FAIL("3013", "权限认证失败"),
    OPERATION_TOKEN_INVALID("3014","无效的操作令牌"),
    EXCEEDED_LIMIT("3015","验证密码超过次数限制"),
    VERIFY_CODE_INVALID("3016", "验证码错误"),
    SMS_SEND_FAIL("4004", "短信发送失败"),
    SMS_PHONE_NOT_EXIT("4005", "接收短信的手机不存在"),

    INSERT_FAIL("5001", "插入新纪录失败"),
    SELECT_FAIL("5002", "select纪录失败"),
    UPDATE_FAIL("5003", "更新纪录失败"),
    MODIFY_CONFLICT("5004", "发生碰撞"),

    ALREADY_BOOK_ROOM("6001", "已经占过房间"),
    ALREADY_CANCEL_ROOM("6002", "已经退过房间"),
    CANCLE_WITHOUT_BOOKING("6003", "尚未占房，不能退房"),
    NOT_ENOUGH_ROOM("6004", "剩余房量不够此次占房"),
    CANCEL_ROOM_FAILED("6005", "退房失败"),

    ONLINE_HOTEL("7001", "公寓下有已上线免费房"),
    UN_ONLINE_HOTEL("7002", "公寓下没有已上线免费房"),

    CONN_EXP("9000", "系统通信异常"), //
    UNKNOW("9001", "未知的返回结果"),
    HTTP_EXP("9002", "HTTP请求异常"), //
    SYSTEM_ERR("9999", "系统繁忙，请稍后重试"), //

    PLAIN_TEXT("-1", "文本格式");

    private String code;
    private String msg;

    RspCodeMsg(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    /**
     * 根据code搜索RspCodeMsg实例
     * @param code      状态码
     * @return          状态实例，可能为null
     */
    public static RspCodeMsg find(String code){
        RspCodeMsg result = null;
        for (RspCodeMsg rsp : RspCodeMsg.values()){
            if (rsp.getCode().equals(code))
                result = rsp;
        }
        return result;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

