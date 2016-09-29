package com.microsoft.xuetang.component;

import com.google.common.base.Joiner;
import com.google.common.collect.Iterables;
import com.microsoft.xuetang.Exception.HttpRequestException;
import com.microsoft.xuetang.internalrpc.response.ErrorResponse;
import com.microsoft.xuetang.util.JsonUtil;
import com.microsoft.xuetang.util.SamplePair;
import com.microsoft.xuetang.util.CommonUtils;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.AsyncHttpClientConfig;
import com.ning.http.client.Response;
import org.eclipse.jetty.http.HttpStatus;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by jiash on 7/29/2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class BaseHttpComponent implements DisposableBean {
    private static final Object lock = new Object();
    private static volatile boolean opened;
    private static AsyncHttpClient asyncHttpClient;

    static {
        opened = false;
        AsyncHttpClientConfig config = new AsyncHttpClientConfig.Builder()
                .setRequestTimeout(100000)
                .setMaxRequestRetry(3)
                .build();
        asyncHttpClient = new AsyncHttpClient(config);
        opened = true;
    }

    private static <T> T syncGet(String finalUrl, Class<T> clazz) throws ExecutionException, InterruptedException, IOException {
        Future<Response> future = asyncHttpClient.prepareGet(finalUrl).execute();


        Response response = future.get();
        if (response.getStatusCode() != HttpStatus.OK_200) {
            throw new HttpRequestException(
                    String.format("Http return code is [%d], not 200", response.getStatusCode()));
        }

        if(clazz == String.class) {
            return (T) response.getResponseBody("UTF-8");
        } else {
            return JsonUtil.fromJson(response.getResponseBody("UTF-8"), clazz);
        }
    }



    private static String getFinalUrl(String url,Map<String, Object> param ) {
        String paramString = Joiner.on("&").join(Iterables.transform(
                    param.entrySet(), (Map.Entry<String, Object> input) -> {
                        String key = input.getKey();
                        Object value = input.getValue();
                        if (value == null) {
                            return String.format("%s=", key);
                        } else {
                            String encodeValue = CommonUtils.fluentUrlParamEncodeUTF8(value.toString());
                            if(encodeValue == null) {
                                return String.format("%s=", key);
                            } else {
                                return String.format("%s=%s", key, encodeValue);
                            }
                        }
                    }
            ));

        String finalUrl = url;
        if (paramString != null && paramString.length() > 0) {
            finalUrl = String.format("%s?%s", url, paramString);
        }

        return finalUrl;
    }

    public static <T> T syncGet(String url, Map<String, Object> param, Class<T> clazz) throws ExecutionException, InterruptedException, IOException {
        String finalUrl = getFinalUrl(url, param);

        return syncGet(finalUrl, clazz);

    }

    public static <T> T fluentSyncGet(String url, Map<String, Object> param, Class<T> clazz) throws Exception {
        String finalUrl = getFinalUrl(url, param);
        try {
            T result = syncGet(finalUrl, clazz);
            return result;
        } catch (ExecutionException e) {
            throw new Exception(String.format("Http request to %s encounter ExecutionException. Reason is: %s", finalUrl, e.getMessage()), e);
        } catch (InterruptedException e) {
            throw new Exception(String.format("Http request to %s encounter InterruptedException. Reason is: %s", finalUrl, e.getMessage()), e);
        } catch (IOException e) {
            throw new Exception(String.format("Parse http %s result to json encounter IOException  Reason is: ", finalUrl, e.getMessage()), e);
        }
    }

    public static Future<Response> asyncGet(String url, Map<String, Object> param) {
        String finalUrl = getFinalUrl(url, param);

        Future<Response> future = asyncHttpClient.prepareGet(finalUrl).execute();

        return future;
    }

    public static Map<String, Object> asyncGet(Map<String, SamplePair<Map<String, Object>, Class>> param) {
        Map<String, SamplePair<Future<Response>, Class>> futureMap = new HashMap<>();
        if (param != null) {
            for(Map.Entry<String, SamplePair<Map<String, Object>, Class>> entry : param.entrySet()) {
                String url = entry.getKey();
                SamplePair<Map<String, Object>, Class> data = entry.getValue();

                Map<String, Object> paramMap = data.getFirst();

                Future<Response> responseFuture = asyncGet(url, paramMap);
                futureMap.put(url, new SamplePair<>(responseFuture, data.getSecond()));
            }
        }
        Map<String, Object> result = new HashMap<>();

        for(Map.Entry<String, SamplePair<Future<Response>, Class>> entry : futureMap.entrySet()) {
            String url = entry.getKey();
            SamplePair<Future<Response>, Class> data = entry.getValue();
            Future<Response> responseFuture = data.getFirst();

            Object value = null;

            try {
                Response response = responseFuture.get();
                if (response.getStatusCode() != HttpStatus.OK_200) {
                    value = new ErrorResponse(String.format("Http return code is [%d], not 200", response.getStatusCode()));
                } else {
                    value = JsonUtil.fromJson(response.getResponseBody(), data.getSecond());
                }
            } catch (ExecutionException e) {
                value = new ErrorResponse(String.format("Request to %s encounter ExecutionException. Reason is: ", url, e.getMessage()), e);
            } catch (InterruptedException e) {
                value = new ErrorResponse(String.format("Request to %s encounter ExecutionException. Reason is: ", url, e.getMessage()), e);
            } catch (IOException e) {
                value = new ErrorResponse(String.format("Parse %s http result to json encounter IOException  Reason is: ", url, e.getMessage()), e);
            }

            result.put(url, value);
        }

        return result;
    }

    public static <T> Map<String, Object> asyncGet(Map<String, Map<String, Object>> param, Class<T> clazz) {
        Map<String, Future<Response>> futureMap = new HashMap<>();
        if (param != null) {
            for(Map.Entry<String, Map<String, Object>> entry : param.entrySet()) {
                String url = entry.getKey();

                Map<String, Object> paramMap = entry.getValue();

                Future<Response> responseFuture = asyncGet(url, paramMap);
                futureMap.put(url, responseFuture);
            }
        }
        Map<String, Object> result = new HashMap<>();

        for(Map.Entry<String, Future<Response>> entry : futureMap.entrySet()) {
            String url = entry.getKey();
            Future<Response> responseFuture = entry.getValue();

            Object value = null;

            try {
                Response response = responseFuture.get();
                if (response.getStatusCode() != HttpStatus.OK_200) {
                    value = new ErrorResponse(String.format("Http return code is [%d], not 200", response.getStatusCode()));
                } else {
                    value = JsonUtil.fromJson(response.getResponseBody(), clazz);
                }
            } catch (ExecutionException e) {
                value = new ErrorResponse(String.format("Request to %s encounter ExecutionException. Reason is: ", url, e.getMessage()), e);
            } catch (InterruptedException e) {
                value = new ErrorResponse(String.format("Request to %s encounter ExecutionException. Reason is: ", url, e.getMessage()), e);
            } catch (IOException e) {
                value = new ErrorResponse(String.format("Parse %s http result to json encounter IOException  Reason is: ", url, e.getMessage()), e);
            }

            result.put(url, value);
        }

        return result;
    }

    public static <T> Map<String, Object> asyncGet(String url, Map<String, Map<String, Object>> id2Param , Class<T> clazz) {
        Map<String, Future<Response>> futureMap = new HashMap<>();
        if (id2Param != null) {
            for(Map.Entry<String, Map<String, Object>> entry : id2Param.entrySet()) {
                String id = entry.getKey();

                Map<String, Object> paramMap =  entry.getValue();

                Future<Response> responseFuture = asyncGet(url, paramMap);
                futureMap.put(id, responseFuture);
            }
        }
        Map<String, Object> result = new HashMap<>();

        for(Map.Entry<String, Future<Response>> entry : futureMap.entrySet()) {
            String id = entry.getKey();
            Future<Response> responseFuture = entry.getValue();

            Object value = null;

            try {
                Response response = responseFuture.get();
                if (response.getStatusCode() != HttpStatus.OK_200) {
                    value = new ErrorResponse(String.format("Http return code is [%d], not 200", response.getStatusCode()));
                } else {
                    value = JsonUtil.fromJson(response.getResponseBody(), clazz);
                }
            } catch (ExecutionException e) {
                value = new ErrorResponse(String.format("Request to %s encounter ExecutionException. Reason is: ", url, e.getMessage()), e);
            } catch (InterruptedException e) {
                value = new ErrorResponse(String.format("Request to %s encounter ExecutionException. Reason is: ", url, e.getMessage()), e);
            } catch (IOException e) {
                value = new ErrorResponse(String.format("Parse %s http result to json encounter IOException  Reason is: ", url, e.getMessage()), e);
            }

            result.put(id, value);
        }

        return result;
    }

    @Override public void destroy() throws Exception {
        synchronized (lock.getClass()) {
            if (!opened) {
                return;
            }

            asyncHttpClient.close();
            opened = false;
        }
    }
}
