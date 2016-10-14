package com.microsoft.xuetang.aspect;

import com.microsoft.xuetang.Exception.ParamCheckException;
import com.microsoft.xuetang.Exception.RspRuntimeException;
import com.microsoft.xuetang.schema.request.Request;
import com.microsoft.xuetang.schema.response.JsonPResponse;
import com.microsoft.xuetang.schema.response.Response;
import com.microsoft.xuetang.util.Constants;
import com.microsoft.xuetang.util.LogUtils;
import com.microsoft.xuetang.util.RspCodeMsg;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

/**
 * Created by shijianguang on 3/19/16.
 */
@Aspect
@Order(0)
@Component
public class ApiRequestAspect {

    private static final Logger logger = LoggerFactory.getLogger(ApiRequestAspect.class);
    private static final Logger performancelogger = LoggerFactory.getLogger(Constants.Log.PERFORMANCE_LOGGER_NAME);

    /**
     * 定义 api 接口的切面，方法上有 ApiRequest 注解
     */
    @Pointcut(value = "@annotation(apiRequest)")
    private void apiReqAspect(ApiRequest apiRequest) {
    }

    /**
     * api 接口需要对结果进行封装
     *
     * @author daihui.gu
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("apiReqAspect(apiRequest)")
    public Object doApiReqAspect(ProceedingJoinPoint pjp , ApiRequest apiRequest) throws Throwable {
        Object result = doReqAspect(pjp, "Api");
            if (result == null) {
                result = new Response(RspCodeMsg.NULL);
            } else if (result instanceof RspCodeMsg) {
                result = new Response((RspCodeMsg) result);
            } else if (result instanceof Response) {
            } else if (result instanceof JsonPResponse) {
                result = ((JsonPResponse)result).getData().getBytes("UTF-8");
            } else {
                result = new Response(RspCodeMsg.UNKNOW, result);
            }
        return result;
    }

    /**
     * 处理参数校验、异常捕获、日志记录
     *
     * @author daihui.gu
     * @param pjp
     * @param tag
     * @return
     * @throws Throwable
     */
    public Object doReqAspect(ProceedingJoinPoint pjp, String tag) throws Throwable {
        // 存储最终返回的结果
        long start = System.currentTimeMillis();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        //HttpServletResponse Response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
        Object result = null;

        // 请求校验
        String traceId = UUID.randomUUID().toString();
        long requestTime = System.currentTimeMillis();
        String requestUri = request.getRequestURI();
        String token = request.getHeader("AccessToken");
        Object[] args = pjp.getArgs();
        try {
            dealWithParam(args, traceId, requestTime, token, requestUri);
        } catch (ParamCheckException e) {
            // 直接返回参数验证失败
            result = new Response(e.getCodeMsg(), e.getMessage());
            return result;
        }

        // 监控的关键Metric
        //String monitorKey = getMonitorKey(pjp, tag);

        try {
            // 调用 Controller 具体逻辑
            result = pjp.proceed(args);
            if(result instanceof Response) {
                ((Response) result).setCode(RspCodeMsg.SUCCESS.getCode());
                ((Response) result).setMsg(RspCodeMsg.SUCCESS.getMsg());

            }

        } catch (Exception e) {
            RspCodeMsg rsp = getExceptionRsp(e);
            // 未知异常不抛出错误详情
            if (rsp == RspCodeMsg.SYSTEM_ERR) {
                result = new Response(rsp);
            } else {
                result = new Response(rsp, e.getMessage());
            }

        } finally {
            // 记日志监控
            long time = System.currentTimeMillis() - start;
            LogUtils.infoLogPerformance(performancelogger, requestUri, traceId, requestTime, time, "interface", "total");
        }

        return result;
    }

    /* *
     * 参数校验，失败会抛出异常，否则表示成功
     * @param args
     * @throws ParamCheckException
     */
    private void dealWithParam(Object[] args, String traceId, long requestTime, String token, String requestUri) throws ParamCheckException {
        if (args == null || args.length == 0) {
            return;
        }
        for (Object arg : args) {
            if (!(arg instanceof Request)) {
                // 非 Request 的参数不错校验
                continue;
            }

            // 参数校验
            Request param = (Request) arg;
            try {
                param.validate();
                param.setTraceId(traceId);
                param.setTimestamp(requestTime);
                param.setRequestUri(requestUri);
                param.setToken(token);
            } catch (Exception e) {
                if (e instanceof ParamCheckException) {
                    // 如果是 ParamCheckException 异常，直接抛出
                    throw (ParamCheckException) e;
                }
                // 其他异常返回错误信息
                throw new ParamCheckException(RspCodeMsg.INNER_PARAM_ERR, e.getMessage());
            }
        }
    }

    /**
     * 获取监控的Key
     *
     * @author daihui.gu
     * @param pjp
     * @param tag
     * @return
     */
    private String getMonitorKey(ProceedingJoinPoint pjp, String tag) {
        Signature signature = pjp.getSignature();
        if (signature instanceof MethodSignature) {
            MethodSignature methodSignature = (MethodSignature) signature;
            // 此处需要考虑一下性能问题
            // mapping 方法对应的 类全名 + 方法名
            return tag + "#" + methodSignature.getDeclaringTypeName() + "#" + methodSignature.getName();
        }
        return "NONE_Interface_MonitorKey";
    }

    /* *
     * 从 Exception 中获取 codeMsg，如果没有则返回 UNKNOWN_ERR
     * @param e
     * @return
     */

    private RspCodeMsg getExceptionRsp(Exception e) {
        RspCodeMsg rsp = null;
        if (e instanceof RspRuntimeException) {
            rsp = ((RspRuntimeException) e).getCodeMsg();
        }
        if (rsp == null) {
            rsp = RspCodeMsg.SYSTEM_ERR;
        }
        return rsp;
    }
}