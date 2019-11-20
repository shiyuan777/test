package com.jixunkeji.redis;


import com.alibaba.fastjson.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;

import javax.annotation.PostConstruct;

/**
 * @author: shiyuan
 */
public abstract class BasicRedisTemplate {
    private static Logger logger = LoggerFactory.getLogger(BasicRedisTemplate.class);

    protected RedisConnectionFactory connectionFactory;

    public void setConnectionFactory(RedisConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    protected ShardedJedisPool pool;

    @PostConstruct
    protected void initPool() {
        this.pool = connectionFactory.getConnectionPool();
    }

    protected void closeRedis(ShardedJedis jedis) {
        if (jedis != null) {
            jedis.close();
        }
    }

    /**
     * 删除key, 如果key存在返回true, 否则返回false。
     *
     * @param key
     * @return
     */
    public boolean del(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.del(key) == 1 ? true : false;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * true if the key exists, otherwise false
     *
     * @param key
     * @return
     */
    public Boolean exists(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.exists(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * set key expired time
     *
     * @param key
     * @param seconds
     * @return
     */
    public Boolean expire(String key, int seconds) {
        ShardedJedis jedis = null;
        if (seconds == 0) {
            return true;
        }
        try {
            jedis = pool.getResource();
            return jedis.expire(key, seconds) == 1 ? true : false;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     *
     * 把object转换为json byte array
     *
     * @param o
     * @return
     */
    protected byte[] toJsonByteArray(Object o) {
        //TODO String json = JsonUtil.toJsonString(o) != null ? JsonUtil.toJsonString(o) : "";
        String json = "";
        return json.getBytes();
    }

    /**
     *
     * 把json byte array转换为T类型object
     *
     * @param b
     * @param clazz
     * @return
     */
    protected <T> T fromJsonByteArray(byte[] b, Class<T> clazz) {
        if (b == null || b.length == 0) {
            return null;
        }
        return JSONObject.parseObject(new String(b), clazz);
    }

    public Long ttl(final String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.ttl(key);
        } finally {
            this.closeRedis(jedis);
        }
    }
}
