package com.microsoft.xuetang.util;

import org.apache.commons.lang.StringUtils;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.List;
import java.util.Random;
import java.util.RandomAccess;

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

    public static String trimBefore(String string, char... c) {
        int charLength = c.length;
        int strLength = string.length();
        int i = 0;
        for(; i < strLength ; ++ i) {
            char t = string.charAt(i);
            boolean contains = false;
            for(int j = 0 ; j < charLength ; ++ j) {
                if(c[j] == t) {
                    contains = true;
                    break;
                }
            }

            if(!contains) {
                break;
            }
        }

        if(i < strLength) {
            return string.substring(i);
        } else {
            return StringUtils.EMPTY;
        }
    }

    /**
     * For now list must be implement RandomAccess interface
     * @param list
     * @param start
     * @param end
     * @return Return true if the list has been shuffled. Otherwise return false
     */
    public static boolean shuffle(List<?> list, int start, int end) {
        return shuffle(list, start, end, new Random());
    }

    public static boolean shuffle(List<?> list, int start, int end, Random random) {
        if(list == null) {
            return false;
        }
        int size = list.size();
        end = end > size ? size : end;
        if(start >= end) {
            return false;
        }
        if (list instanceof RandomAccess) {
            int terminateBound = start + 1;
            for (int i = end; i > terminateBound; i--)
                swap(list, i-1, start + random.nextInt(i - start));
            return true;
        }

        return false;
    }

    public static void swap(List<?> list, int i, int j) {
        final List l = list;
        l.set(i, l.set(j, l.get(i)));
    }

}
