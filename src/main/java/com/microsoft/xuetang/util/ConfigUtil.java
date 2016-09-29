package com.microsoft.xuetang.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Properties;
import java.util.Set;

/**
 * Created by jiash on 8/12/2016.
 */
public class ConfigUtil {
    private static final Logger logger = LoggerFactory.getLogger(ConfigUtil.class);

    public static volatile String ELASTIC_CONNECTION = "localhost:9300";

    public static volatile String ELASTIC_PING_TIMEOUT = "5s";

    public static volatile String ELASTIC_CLUSTER_NAME = "";

    public static volatile String REDIS_CONNECTION = "stcvm-a92:9300";

    public static volatile int LOCAL_CACHE_ENTITY_SIZE = 1000000;

    public static volatile int FEDERATION_THREAD_POOL_SIZE = 20;

    static {
        // FIXME 初始化配置文件，放在最下面防止被默认覆盖。可考虑延迟加载
        init();
    }

    public static Object parseObj(Class<?> type, String value) {
        Object obj = null;
        if (type == Integer.class || type == int.class) {
            obj = Integer.parseInt(value);
        } else if (type == Long.class || type == long.class) {
            obj = Long.parseLong(value);
        } else if (type == Float.class || type == float.class) {
            obj = Float.parseFloat(value);
        } else if (type == Double.class || type == double.class) {
            obj = Double.parseDouble(value);
        } else if (type == Boolean.class || type == boolean.class) {
            obj = Boolean.parseBoolean(value);
        } else {
            obj = value;
        }
        return obj;
    }

    public static Object parseObj(Class<?> type, Object object) {
        if (object.getClass() == type) {
            return object;
        }
        if (object.getClass() != String.class) {
            return null;
        }
        String value = (String) object;
        Object obj = null;
        if (type == Integer.class || type == int.class) {
            obj = Integer.parseInt(value);
        } else if (type == Float.class || type == float.class) {
            obj = Float.parseFloat(value);
        } else if (type == Double.class || type == double.class) {
            obj = Double.parseDouble(value);
        } else if (type == Boolean.class || type == boolean.class) {
            obj = Boolean.parseBoolean(value);
        } else {
            obj = value;
        }
        return obj;
    }

    public static String updateField(String fieldName, String value) {
        String msg = "Update success";
        try {
            Field field = ConfigUtil.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            Class<?> type = field.getType();

            Object obj = parseObj(type, value);
            field.set(null, obj);
            msg = "Update success " + fieldName + "(" + type + ")" + " to:" + obj;
        } catch (NoSuchFieldException | SecurityException e) {
            msg = "Field is not exist or can't be access. Field name:" + fieldName;
        } catch (IllegalArgumentException e) {
            msg = "Wrong update data or type. Field name:" + fieldName;
        } catch (IllegalAccessException e) {
            msg = "No permission to update field. Field name:" + fieldName;
        } catch (NullPointerException e) {
            msg = "Parameter is not correct. Field name:" + fieldName;
        }
        return msg;
    }

    /**
     * 初始化时的更新，可更新final变量
     *
     * @param fieldName
     * @param value
     * @return
     */
    private static boolean updateInnerField(String fieldName, Object value) {
        try {
            Field field = ConfigUtil.class.getDeclaredField(fieldName);
            field.setAccessible(true);

            // 修改final不可修改变为可反射修改
            Field modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            Class<?> type = field.getType();
            Object obj = parseObj(type, value);
            field.set(null, obj);
            logger.info("Config {} with value {}", field, obj);
            return true;
        } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException e) {
            logger.error("Init Config field {} to value {} error. Reason is: {}", fieldName, value, e.getMessage());
        }
        return false;
    }

    /**
     * 初始化配置文件中的变量
     *
     */
    private static void init() {
        Properties pro = new Properties();
        try {
            String configPath = Thread.currentThread().getContextClassLoader()
                    .getResource("config.properties").getPath();
            pro.load(new FileInputStream(new File(configPath)));
            Set<Object> keys = pro.keySet();
            for (Object key : keys) {
                String field = (String) key;
                Object value = pro.get(key);
                updateInnerField(field, value);
            }
            // 当本机IP不匹配配置中的masterIP时，运行模式均改为Slave，当匹配时才为设置的启动模式
        } catch (Exception t) {
            t.printStackTrace();
        }
    }

}
