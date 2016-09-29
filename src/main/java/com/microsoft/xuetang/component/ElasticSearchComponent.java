package com.microsoft.xuetang.component;

import com.microsoft.xuetang.util.ConfigUtil;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.elasticsearch.action.get.GetRequestBuilder;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequestBuilder;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.analysis.StandardAnalyzerProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.net.InetAddress;
import java.util.List;

/**
 * Created by jiash on 7/29/2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class ElasticSearchComponent implements DisposableBean {
    private static final Object lock = new Object();
    private static volatile boolean opened = false;
    private static final Logger logger = LoggerFactory.getLogger(ElasticSearchComponent.class);
    private static TransportClient client;

    public ElasticSearchComponent() {
        try {
            if (!opened) {
                synchronized (lock) {
                    if (!opened) {
                        Settings settings = Settings.settingsBuilder()
                                .put("client.transport.ping_timeout", ConfigUtil.ELASTIC_PING_TIMEOUT)
                                .put("cluster.name", ConfigUtil.ELASTIC_CLUSTER_NAME)
                                .build();
                        String elasticConnection = ConfigUtil.ELASTIC_CONNECTION;
                        String[] connections = elasticConnection.split(",");
                        client = TransportClient.builder().settings(settings).build();
                        for(String connection : connections) {
                            String[] hostPort = connection.split(":");
                            client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName(hostPort[0]), Integer.parseInt(hostPort[1])));

                        }
                        opened = true;

                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <Param, Response> Response syncSearch
            (Param param,
             SearchRequestAdapter<Param> requestAdapte,
             SearchResponseAdapter<Response> responseAdapter,
             String... indices) throws Exception {
        if(requestAdapte == null || responseAdapter == null) {
            return null;
        }
        SearchRequestBuilder searchRequestBuilder = requestAdapte.build(client.prepareSearch(indices), param);
        if(searchRequestBuilder == null) {
            return null;
        }

        SearchResponse response = searchRequestBuilder.get();

        return responseAdapter.trasform(response);

    }

    public <Param, Response> Response syncSearch
            (Param param,
             SearchRequestAdapter<Param> requestAdapte,
             SearchResponseAdapter<Response> responseAdapter) throws Exception {
        if(requestAdapte == null || responseAdapter == null) {
            return null;
        }
        SearchRequestBuilder searchRequestBuilder = requestAdapte.build(client.prepareSearch(requestAdapte.getIndex()), param);
        if(searchRequestBuilder == null) {
            return null;
        }

        SearchResponse response = searchRequestBuilder.get();

        return responseAdapter.trasform(response);

    }

    public <Param, Response> Response syncMultiGet
            (Param param,
             MulitiGetRequestAdapter<Param> requestAdapte,
             MultiGetResponseAdapter<Response> responseAdapter) throws Exception {
        if(requestAdapte == null || responseAdapter == null) {
            return null;
        }
        MultiGetRequestBuilder multiGetRequestBuilder = requestAdapte.build(client.prepareMultiGet(), param);

        if(multiGetRequestBuilder == null) {
            return null;
        }
        MultiGetResponse response = multiGetRequestBuilder.get();

        return responseAdapter.trasform(response);

    }

    public <Param, Response> Response syncGet
            (Param param,
             GetRequestAdapter<Param> requestAdapte,
             GetResponseAdapter<Response> responseAdapter) throws Exception {
        if(requestAdapte == null || responseAdapter == null) {
            return null;
        }
        GetRequestBuilder getRequestBuilder = requestAdapte.build(client.prepareGet(), param);

        if(getRequestBuilder == null) {
            return null;
        }
        GetResponse response = getRequestBuilder.get();

        return responseAdapter.trasform(response);

    }

    @Override public void destroy() throws Exception {
        synchronized (lock.getClass()) {
            if (!opened) {
                return;
            }

            client.close();
            opened = false;
        }
    }

    public static abstract class RequestAdapter<Param, RequestBuilder> {
        public abstract RequestBuilder build(RequestBuilder newBuilder, Param param) throws Exception;

    }

    public static abstract class SearchRequestAdapter<Param> extends RequestAdapter<Param, SearchRequestBuilder> {
        private String index;
        private String type;

        public SearchRequestAdapter() {}
        public SearchRequestAdapter(String index, String type) {
            this.index = index;
            this.type = type;
        }

        public String getIndex() {
            return index;
        }

        public void setIndex(String index) {
            this.index = index;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    };

    public static abstract class GetRequestAdapter<Param> extends RequestAdapter<Param, GetRequestBuilder> {};

    public static abstract class MulitiGetRequestAdapter<Param> extends RequestAdapter<Param, MultiGetRequestBuilder> {};

    public static class DefaultMulitiGetRequestAdapte extends MulitiGetRequestAdapter<List<String>> {
        public String index;
        public String type;
        public DefaultMulitiGetRequestAdapte() {}

        public DefaultMulitiGetRequestAdapte(String index, String type) {
            this.index = index;
            this.type = type;
        }
        @Override
        public MultiGetRequestBuilder build(MultiGetRequestBuilder newBuilder, List<String> ids) throws Exception {
            for(String id : ids ) {
                newBuilder.add(index, type, id);
            }

            return newBuilder;
        }
    }

    public static class DefaultGetRequestAdapte extends GetRequestAdapter<String> {
        public String index;
        public String type;
        public DefaultGetRequestAdapte() {}

        public DefaultGetRequestAdapte(String index, String type) {
            this.index = index;
            this.type = type;
        }
        @Override
        public GetRequestBuilder build(GetRequestBuilder newBuilder, String id) throws Exception {
            newBuilder.setIndex(index);
            newBuilder.setType(type);
            newBuilder.setId(id);
            return newBuilder;
        }
    }

    public static abstract class ResponseAdapter<From, To> {
        public abstract To trasform(From data) throws Exception;
    }

    public static abstract class SearchResponseAdapter<To> extends ResponseAdapter<SearchResponse, To> {
    }

    public static abstract class GetResponseAdapter<To> extends ResponseAdapter<GetResponse, To> {
    }

    public static abstract class MultiGetResponseAdapter<To> extends ResponseAdapter<MultiGetResponse, To> {
    }
}
