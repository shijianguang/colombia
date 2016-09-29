package com.microsoft.xuetang.Federation;

import com.microsoft.xuetang.schema.request.Request;
import com.microsoft.xuetang.util.ConfigUtil;
import com.microsoft.xuetang.util.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by jiash on 7/29/2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FederationEngine {
    private static final Logger logger = LoggerFactory.getLogger(FederationEngine.class);
    private static final Logger performanceLogger = LoggerFactory.getLogger("performance_logger");
    private ExecutorService executor = Executors.newFixedThreadPool(ConfigUtil.FEDERATION_THREAD_POOL_SIZE);

    public <T> void submit(FederationExecution execution, FederationContext<T> context) {
        Future<T> future = executor.submit(new Callable<T>() {
            @Override
            public T call() throws Exception {
                long startTime = System.currentTimeMillis();
                T result = (T) execution.execute(context);
                long endTime = System.currentTimeMillis();
                Request request = (Request) context.get("request");
                LogUtils.infoLogPerformance(performanceLogger, request, String.format("MatchType.%s", context.getName()), endTime - startTime);
                return result;
            }
        });
        context.setResultFuture(future);
        return;
    }

    public Map<String, Object> execute(List<FederationContext> contextList) {
        for(FederationContext context : contextList) {
            submit(context.getExecution(), context);
        }

        Map<String, Object> result = new HashMap<>();
        for(FederationContext context : contextList) {
            Object value = null;
            if(context.getTimeOut() > 0 && context.getTimeUnit() != null) {
                try {
                    value = context.getResult(context.getTimeOut(), context.getTimeUnit());
                } catch (ExecutionException e) {
                    logger.error("Get MatchType encounter ExecutionException. MatchType information: {} Message: {}", context.getInfo(), e.getMessage());
                } catch (InterruptedException e) {
                    logger.error("Get MatchType encounter InterruptedException. MatchType information: {} Message: {}", context.getInfo(), e.getMessage());
                } catch (TimeoutException e) {
                    logger.error("Get MatchType encounter TimeoutException. MatchType information: {} Message: {}", context.getInfo(), e.getMessage());
                } catch (Throwable e) {
                    logger.error("Get MatchType encounter unknow error. MatchType information: {} Message: {}", context.getInfo(), e.getMessage());
                }
            }

            result.put(context.getName(), value);
        }

        return result;
    }
}
