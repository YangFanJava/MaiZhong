package com.maizhong.dao.impl;


import com.google.common.collect.Lists;
import com.maizhong.common.utils.JsonUtils;
import com.maizhong.dao.JedisClient;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * JREDIS Daoimpl 单例
 * @author Xushd
 * @since 2017/2/4 0004 下午 1:04
 */
public class JedisClientSingleImpl implements JedisClient {

    //配置文件中配置
    @Autowired
    private JedisPool jedisPool;


    public String get(String key) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.get(key);
        jedis.close();
        return value;
    }


    public String set(String key, String value) {
        Jedis jedis = jedisPool.getResource();
        String ret = jedis.set(key, value);
        jedis.close();
        return ret;
    }


    public String hget(String hkey, String key) {
        Jedis jedis = jedisPool.getResource();
        String value = jedis.hget(hkey, key);
        jedis.close();
        return value;
    }


    public long hset(String hkey, String key, String value) {
        Jedis jedis = jedisPool.getResource();
        Long ret = jedis.hset(hkey, key, value);
        jedis.close();
        return ret;
    }


    public long del(String key) {
        Jedis jedis = jedisPool.getResource();
        Long ret = jedis.del(key);
        jedis.close();
        return ret;
    }


    public long hdel(String hkey, String key) {
        Jedis jedis = jedisPool.getResource();
        Long ret = jedis.hdel(hkey, key);
        jedis.close();
        return ret;
    }


    public long incr(String key) {
        Jedis jedis = jedisPool.getResource();
        Long ret = jedis.incr(key);
        jedis.close();
        return ret;
    }


    public long expire(String key, int second) {
        Jedis jedis = jedisPool.getResource();
        Long ret = jedis.expire(key, second);
        jedis.close();
        return ret;
    }


    public long ttl(String key) {
        Jedis jedis = jedisPool.getResource();
        Long ret = jedis.ttl(key);
        jedis.close();
        return ret;
    }

    /**
     * 获取List缓存
     * @param key
     * @param clazz
     * @param start
     * @param end
     * @param <T>
     * @return
     */
    public <T> List<T> getObjectList(String key,Class<T> clazz,int start,int end) {
        List<T> value = null;
        Jedis jedis = jedisPool.getResource();
        try {
            List<String> list = jedis.lrange(key,start,end);
            value = Lists.newArrayList();
            for (String bs : list) {
                value.add(JsonUtils.jsonToPojo(bs,clazz));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return value;
    }

    /**
     * 设置list
     * @param key
     * @param value
     * @return
     */
    public long setObjectList(String key, List<?> value) {
        long result = 0;
        Jedis jedis = jedisPool.getResource();
        try {
            List<String> list = Lists.newArrayList();
            String[] arry = new String[list.size()];
            for (Object o : value) {
                list.add(JsonUtils.objectToJson(o).toString());
            }
            result = jedis.rpush(key, list.toArray(arry));

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return result;

    }



}
