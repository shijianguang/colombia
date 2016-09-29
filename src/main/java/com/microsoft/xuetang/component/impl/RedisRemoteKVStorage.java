package com.microsoft.xuetang.component.impl;

import com.microsoft.xuetang.component.RemoteKVStorage;
import org.apache.commons.lang.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiash on 8/8/2016.
 */
public class RedisRemoteKVStorage extends RemoteKVStorage<String> {
    private static JedisPool jedisPool;
    private static final int PORT = 6379;
    private static final String HOST = "stcvm-a92";
    private static int TIMEOUT = 10000;
    private static final String SEPERATOR = ".";

    /**
     * JedisPool is using Apache Common Pool, these config is very similar with
     * commons-pool. The jedis instance is corresponding to the pool object
     */

    /**
     * How many jedis instance the resource pool have, -1 means no limit.
     * If we already use getResource to get MAX_TOTAL jedis instance,
     * the state of the pool will be exhausted;
     */
    private static int MAX_TOTAL = 24;

    /**
     * Define the behaviour of getResource when the pool is in exhausted state,
     * true means the method will block for MAX_WAIT_MILLIS time;
     */
    private static boolean BLOCK_WHEN_EXHAUSTED = true;

    /**
     * Define the wait time for getResource invoke when pool is in exhausted state.
     * -1 means no timeout
     */
    private static long MAX_WAIT_MILLIS = -1;

    /**
     * Define how many jedis instances can be idle state
     */
    private static int MAX_IDEL = 24;

    /**
     * True means when you get jedis instance from pool, pool will do validate operation on it
     * and make sure it is ok
     */
    private static boolean TEST_ON_BORROW = false;

    /**
     * True means when you return jedis instance from pool, pool will do validate operation on it
     */
    private static boolean TEST_ON_RETURN = false;

    /**
     * True means when the idle scanning thread scan the idle instance of the pool, it will do validate
     * operation on it and drop it if the validation failed
     */
    private static boolean TEST_WHILE_IDEL = true;

    /**
     * Define the interval between two idle scanning job
     */
    private static long TIME_BETWEEN_EVICTION_RUN_MILLIS = 30000L;

    /**
     * Define how many instance the idle scanning job scan, -1 means scan all
     */
    private static int NUM_TEST_PER_EVICTION_RUN = -1;

    private static long SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS = -1;

    /**
     * Define the behaviour of the returnResourceObject, true means last in first out,
     * false means use FIFO strategy
     */
    private static boolean LIFO = true;


    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(MAX_TOTAL);
        config.setBlockWhenExhausted(BLOCK_WHEN_EXHAUSTED);
        config.setMaxWaitMillis(MAX_WAIT_MILLIS);
        config.setMaxIdle(MAX_IDEL);
        config.setTestOnBorrow(TEST_ON_BORROW);
        config.setTestOnReturn(TEST_ON_RETURN);
        config.setTestWhileIdle(TEST_WHILE_IDEL);
        config.setTimeBetweenEvictionRunsMillis(TIME_BETWEEN_EVICTION_RUN_MILLIS);
        config.setNumTestsPerEvictionRun(NUM_TEST_PER_EVICTION_RUN);
        config.setLifo(LIFO);
        jedisPool = new JedisPool(config, HOST, PORT, TIMEOUT);
    }

    @Override
    public String get(String db, String namespace, String id) {
        Jedis jedis = null;
        String value = null;
        try {
            String key = getKey(db, namespace, id);
            jedis = jedisPool.getResource();
            value = jedis.get(key);
        } catch (Throwable t) {

        } finally {
            jedisPool.returnResourceObject(jedis);
        }

        return value;
    }

    @Override
    public Map<String, String> get(String db, String namespace, List<String> idList) {
        List<String> keyList = new ArrayList<>();
        for(String id : idList) {
            String key = getKey(db, namespace, id);
            if(StringUtils.isNotEmpty(key)) {
                keyList.add(key);
            }
        }

        Map<String, String> result = new HashMap<>();
        if(keyList.size() == 0) {
            return result;
        }

        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            List<String> valueList = jedis.mget(keyList.toArray(new String[keyList.size()]));
            int i = 0;
            int keyCount = keyList.size();
            int valueCount = valueList.size();
            for (i = 0; i < keyCount && i < valueCount; ++i) {
                String value = valueList.get(i);
                result.put(keyList.get(i), value);
            }
        } catch (Throwable t) {

        } finally {
            jedisPool.returnResourceObject(jedis);
        }

        return result;

    }

    private String getKey(String db, String namespace, String id) {
        StringBuffer buffer = new StringBuffer();
        if(StringUtils.isNotEmpty(db)) {
            buffer.append(db).append(SEPERATOR);
            if(StringUtils.isNotEmpty(namespace)) {
                buffer.append(namespace).append(SEPERATOR);
            }
        }

        buffer.append(id);
        return buffer.toString();
    }
}
