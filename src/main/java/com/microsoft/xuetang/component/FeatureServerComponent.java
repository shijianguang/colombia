package com.microsoft.xuetang.component;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.microsoft.xuetang.bean.cache.WebFeatureEntity;
import com.microsoft.xuetang.bean.schema.response.detail.WebDetailData;
import com.microsoft.xuetang.bean.schema.response.search.SearchElementData;
import com.microsoft.xuetang.util.ConfigUtil;
import com.microsoft.xuetang.util.Constants;
import com.microsoft.xuetang.util.LogUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
@Component
@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
public class FeatureServerComponent {
    private static final Logger logger = LoggerFactory.getLogger(FeatureServerComponent.class);
    private static final Logger performancelogger = LoggerFactory.getLogger("performance_logger");
    private static RemoteKVStorage remoteKVStorage = RemoteKVStorage.getRemoteKVStorage();

    private static Cache<String, WebFeatureEntity> localCache = CacheBuilder.newBuilder()
            .maximumSize(ConfigUtil.LOCAL_CACHE_ENTITY_SIZE)
            .recordStats()
            .build();

    private static final WebFeatureEntity NOT_EXIST_KEY = new WebFeatureEntity();

    /**
     *
     * @param idList
     * @return null value means the key do have more feature
     */
    public Map<String, WebFeatureEntity> get(String db, String namespace, List<String> idList) {
        Map<String, WebFeatureEntity> map = new HashMap<>();
        List<String> notExist = new ArrayList<>();
        for(String id : idList) {
            WebFeatureEntity value = localCache.getIfPresent(id);
            if(value != null) {
                if(equalNullValue(value)) {
                    map.put(id, null);
                } else {
                    map.put(id, value);
                }
            } else {
                notExist.add(id);
            }
        }

        int totalSize = idList.size();
        int cacheHitSize = totalSize - notExist.size();

        if(notExist.size() > 0) {
            Map<String, WebFeatureEntity> remote = remoteKVStorage.get(db, namespace, notExist);
            if (remote != null) {
                for (String id : notExist) {
                    WebFeatureEntity value = remote.get(id);
                    if (value == null) {
                        localCache.put(id, NOT_EXIST_KEY);
                    } else {
                        localCache.put(id, value);
                    }
                    map.put(id, value);
                }

            }
        }

        LogUtils.infoLogMetric(performancelogger, cacheHitSize, totalSize, "cache", "hitrate");
        LogUtils.infoLogMetric(performancelogger, localCache.size(), "cache", "size");

        return map;
    }

    private boolean equalNullValue(WebFeatureEntity entiry) {
        if(entiry.getTitle() == null && entiry.getImageUrl() == null) {
            return true;
        } else {
            return false;
        }
    }

    public void backFillIconUrlData(List<SearchElementData> list) {
        if(list == null || list.size() == 0) {
            return;
        }

        for(SearchElementData data : list) {
            String url = data.getUrl();
            data.setLogoImageUrl(getIconUrlByHost(url));
        }
    }

    public void backFillIconUrlData(WebDetailData detailData) {
        if(detailData == null) {
            return;
        }
        String url = detailData.getUrl();
        detailData.setLogoImageUrl(getIconUrlByHost(url));
    }

    private String getIconUrlByHost(String url) {
        if (url != null) {
            if (url.contains("baike.com")) {
                return Constants.ImageResource.HUDONG_URL;
            }

            if (url.contains("wikipedia.org")) {
                return Constants.ImageResource.WIKIPEDIA_URL;
            }

            if (url.contains("baike.so.com")) {
                return Constants.ImageResource.QIHU_URL;
            }

            if (url.contains("sogou.com")) {
                return Constants.ImageResource.SOGOU_URL;
            }

            if (url.contains("stackoverflow.com")) {
                return Constants.ImageResource.STACKOVERFLOW_URL;
            }

            if (url.contains("zhihu.com")) {
                return Constants.ImageResource.ZHIHU_URL;
            }

            if (url.contains("baidu.com")) {
                return Constants.ImageResource.BAIDU_URL;
            }

            if (url.contains("csdn.com") || url.contains("csdn.net")) {
                return Constants.ImageResource.CSDN_URL;
            }

            if (url.contains("douban.com")) {
                return Constants.ImageResource.DOUBAN_URL;
            }

            if (url.contains("infoq.com")) {
                return Constants.ImageResource.INFOQ_URL;
            }

            if (url.contains("gushiwen.org")) {
                return Constants.ImageResource.GUSHIWEN_URL;
            }

            if (url.contains("2361.net")) {
                return Constants.ImageResource.ACCOUNTANCY_2361_URL;
            }

            if (url.contains("canet.com.cn")) {
                return Constants.ImageResource.CANET_URL;
            }
        }

        return null;
    }
}
