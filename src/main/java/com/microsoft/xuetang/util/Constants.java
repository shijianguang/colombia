package com.microsoft.xuetang.util;

import com.microsoft.xuetang.schema.response.search.SearchApiResponseV2;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by jiash on 8/1/2016.
 */
public class Constants {

    public static class ImageResource {
        public static final String HUDONG_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/hudong.jpg";
        public static final String SOGOU_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/sogou.jpg";
        public static final String STACKOVERFLOW_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/stackoverflow.jpg";
        public static final String WIKIPEDIA_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/wiki.jpg";
        public static final String QIHU_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/360.jpeg";
        public static final String ZHIHU_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/zhihu.jpg";
        public static final String BAIDU_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/baidu.jpg";
        public static final String CSDN_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/csdn.jpg";
        public static final String DOUBAN_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/douban.jpg";
        public static final String INFOQ_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/infoq.jpg";
        public static final String GUSHIWEN_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/gushiwen.jpg";
        public static final String ACCOUNTANCY_2361_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/2361.jpg";
        public static final String CANET_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/kuaijiwang.jpg";
        public static final String XUETANG_URL = "http://xuetangstoragetest.blob.core.chinacloudapi.cn/search/xuetang.png";
    }

    public static class DataType {
        public static final String WIKI = "wiki";
        public static final String WEB = "web";
        public static final String PPT = "ppt";
        public static final String VIDEO = "video";
        public static final String PAPER = "paper";

        public static final String ALL = "all";

        private static final Set<String> dataTypeSet = new HashSet<String>() {
            {
                add(WIKI);
                add(WEB);
                add(PPT);
                add(VIDEO);
                add(PAPER);
            }
        };

        public static boolean contains(String dataType) {
            return dataTypeSet.contains(dataType);
        }
    }

    public static Map<String, String[]> DataTypeIndexMap = new HashMap<String, String[]>() {
        {
            put(DataType.WIKI, new String[] {"keyvalue", "web"});
            put(DataType.WEB, new String[] {"keyvalue", "web"});
            put(DataType.PPT, new String[] {"ppt", "ppt"});
            put(DataType.VIDEO, new String[] {"video", "video"});
        }
    };

    public static Map<String, String[]> DataTypeKVMap = new HashMap<String, String[]>() {
        {
            put(DataType.WIKI, new String[] {"keyvalue", "web"});
            put(DataType.WEB, new String[] {"keyvalue", "web"});
            put(DataType.PPT, new String[] {"keyvalue", "multimedia"});
            put(DataType.VIDEO, new String[] {"keyvalue", "multimedia"});
        }
    };

    public static class Log {
        public final static String PERFORMANCE_LOGGER_NAME = "performance_logger";
        public final static String ANALYSIS_LOGGER_NAME = "analysis_logger";
        public final static String SEARCH_DESC = "search";
        public final static String PERFORMANCE_DESC = "performance";
        public final static String SYSTEM_METRIC_DESC = "system.metric";
    }

    public static String[] AUTOINCREASE_ID2STRING_CACHE;
    public static int MAX_AUTOINCREASE_ID = 2000;

    static {
        AUTOINCREASE_ID2STRING_CACHE = new String[MAX_AUTOINCREASE_ID];
        for (int i = 0 ; i < MAX_AUTOINCREASE_ID ; ++ i) {
            AUTOINCREASE_ID2STRING_CACHE[i] = Integer.valueOf(i).toString();
        }
    }

    public static final int BING_ONCE_CALL_COUNT = 20;
    public static final String Bing_ONCE_CALL_COUNT_IN_STRING = String.valueOf(BING_ONCE_CALL_COUNT);

    public static final SearchApiResponseV2 EMPTY_SEARCH_API_RESPONSE = new SearchApiResponseV2();

    public static final char[] NEW_LINE_CHARACTER_ARRAY = new char[] {'\r', '\n'};

    public static class Metric {
        public static String KEY_ARGS_SEPARATOR = "-";
        public static String ARGS_SEPARATOR = ".";
    }

    public static String WIKI_BING_FLIGHT = "academicflt10";
    public static String WEB_BING_FLIGHT = "academicflt11";
}
