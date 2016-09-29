package com.microsoft.xuetang.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * Created by jiash on 8/1/2016.
 */
public class CommonUtils {
    public static String formatUrl(String url) {
        return formatZhihuZhuanlanUrl(url);
    }

    public static String formatZhihuZhuanlanUrl(String url) {

        if(url == null) {
            return url;
        }

        String formattedUrl = url;
        if(url.contains("zhuanlan.zhihu.com") && url.endsWith("#!")) {
            formattedUrl = url.replace("#!", "");
        }
        return formattedUrl.toLowerCase();
    }

    /**
     * If get domain of url encounter error. return "". Don't throw exception
     * @param url
     * @return
     */
    public static String fluentGetDomainName(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (URISyntaxException e) {
            return "";
        }
    }

    public static String fluentUrlParamEncodeUTF8(String param) {
        if(StringUtils.isEmpty(param)) {
            return null;
        }

        try {
            return URLEncoder.encode(param, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }

    /**
     * The length calculation is:
     *  One english word (detect boundary by while space or non alphabet char) contribute 1 to length
     *  One non alphabet char contribute 1 to length
     *  Whilespace will be ignore
     * @param string
     * @return
     */
    public static int calStringLength(String string) {
        int ret = 0;
        int len = string.length();
        int i = 0;
        while (i < len) {
            if(string.charAt(i) == ' ') {
                ++ i;
                continue;
            }
            if(isNotAlphabet(string.charAt(i))) {
                ret += 1;
                ++ i;
            } else {
                for ( ; i < len ; ++ i) {
                    if(isNotAlphabet(string.charAt(i))) {
                        break;
                    }
                }

                ret += 1;
            }
        }

        return ret;
    }

    public static boolean isAlphabet(char c) {
        if((c >= 'a' && c <= 'z')
                || (c >= 'A' && c <= 'Z')) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNotAlphabet(char c) {
        return !isAlphabet(c);
    }


}