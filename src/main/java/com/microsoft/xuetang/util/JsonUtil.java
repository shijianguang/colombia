package com.microsoft.xuetang.util;

import com.google.common.base.Strings;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.io.IOException;

/**
 * Created by shijianguang on 3/31/16.
 */
public class JsonUtil {
    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        //设置输入时忽略在JSON字符串中存在但Java对象实际没有的属性
        objectMapper.configure(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        //控制序列化
        objectMapper.configure(DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true) ;
        //转义字符可以进行处理
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true) ;
        objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    }

    public static <T> T fromJson(String jsonString, Class<T> clazz) throws IOException {
        if (Strings.isNullOrEmpty(jsonString)) {
            return null;
        }

        return objectMapper.readValue(jsonString, clazz);
    }

    /**
     * 将JsonNode转换为对象
     * @param jsonNode    JsonNode
     * @param clazz       数据对象
     * @return
     * @throws IOException
     */
    public static  <T> T fromJson(JsonNode jsonNode, Class<T> clazz) throws IOException {
        if(jsonNode == null || clazz == null) {
            return null ;
        }
        return objectMapper.readValue(jsonNode, clazz) ;
    }

    /**
     * 读取JsonNode内容
     * @param jsonString
     * @param name
     * @return
     * @throws JsonProcessingException
     * @throws IOException
     */
    public static JsonNode readValue(String jsonString, String name) throws IOException {
        if(Strings.isNullOrEmpty(jsonString)) {
            return null ;
        }
        return objectMapper.readTree(jsonString).findValue(name) ;
    }

    public static JsonNode readValue(String jsonString) throws IOException {
        if(Strings.isNullOrEmpty(jsonString)) {
            return null ;
        }
        return objectMapper.readTree(jsonString) ;
    }


    /**
     * 将对象转换为String
     * @param obj
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static String object2String(Object obj) throws IOException {
        return objectMapper.writeValueAsString(obj) ;
    }

    /**
     * 转换为Json
     * @param obj
     * @return
     * @throws JsonGenerationException
     * @throws JsonMappingException
     * @throws IOException
     */
    public static String toJson(Object obj) throws JsonGenerationException, JsonMappingException, IOException {
        return objectMapper.writeValueAsString(obj) ;
    }

    public static String fluentToJson(Object obj) {
        return fluentToJson(obj, "");
    }

    public static String fluentToJson(Object obj, String defaultValue) {

        try {
            return toJson(obj);
        } catch (IOException e) {
            return defaultValue;
        }
    }

    public static boolean isLongJsonNode(JsonNode node) {
        if(node.isInt() || node.isLong()) {
            return true;
        }

        return false;
    }

    public static boolean isDoubleJsonNode(JsonNode node) {
        if(node.isDouble()) {
            return true;
        }

        return false;
    }

    public static String number2String(JsonNode node) {
        if(node.isInt()) {
            return String.valueOf(node.asInt());
        }

        if(node.isLong()) {
            return String.valueOf(node.asLong());
        }

        if(node.isDouble()) {
            return String.valueOf(node.asDouble());
        }

        return null;
    }
}
