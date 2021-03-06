package com.jixunkeji.redis;

import redis.clients.jedis.ShardedJedis;

/**
 * @author: caikai
 * @description: com.zdkj.template
 * @date:2019/3/9
 */
public class StringRedisTemplate extends BasicRedisTemplate {

    /**
     * set key-value
     * @param key
     * @param value String
     */
    public void set(String key, String value) {
        set(key, value, 0);
    }


    public void set(String key, String value, int ttl) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.set(key, value);
            if (ttl > 0) {
                jedis.expire(key.getBytes(), ttl);
            }
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * set key-value For Object(NOT String)
     *
     * @param key
     * @param value Object
     */
    public void set(String key, Object value) {
        set(key, value, 0);
    }

    /**
     * set key-value For Object(NOT String)
     *
     * @param key
     * @param value Object
     * @param ttl int
     */
    public void set(String key, Object value, int ttl) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            // jedis.set(key.getBytes(), HessianSerializer.serialize(value));
            jedis.set(key.getBytes(), toJsonByteArray(value));
            if (ttl > 0) {
                jedis.expire(key.getBytes(), ttl);
            }
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * set key-value with expired time(s)
     *
     * @param key
     * @param seconds
     * @param value
     */
    public String setex(String key, int seconds, String value) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.setex(key, seconds, value);

        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * set key-value For Object(NOT String) with expired time(s)
     *
     * @param key
     * @param seconds
     * @param value
     */
    public String setex(String key, int seconds, Object value) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            // return jedis.setex(key.getBytes(), seconds, HessianSerializer.serialize(value));
            return jedis.setex(key.getBytes(), seconds, toJsonByteArray(value));
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 如果key还不存在则进行设置，返回true，否则返回false.
     *
     * @param key
     * @param value
     */
    public boolean setnx(String key, String value, int ttl) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            Long reply = jedis.setnx(key, value);
            if(reply == null){
                reply = 0L;
            }
            if (ttl > 0 && reply.longValue() == 1) {
                jedis.expire(key, ttl);
            }
            return reply.longValue() == 1;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 如果key还不存在则进行设置，返回true，否则返回false.
     *
     * @param key
     * @param value
     */
    public boolean setnx(String key, String value) {
        return setnx(key, value, 0);
    }

    /**
     * 如果key还不存在则进行设置 For Object，返回true，否则返回false.
     *
     * @param key
     * @param value
     */
    public boolean setnx(String key, Object value) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            // return jedis.setnx(key.getBytes(), HessianSerializer.serialize(value)) == 1 ? true : false;
            return jedis.setnx(key.getBytes(), toJsonByteArray(value)) == 1 ? true : false;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 如果key还不存在则进行设置 For Object，返回true，否则返回false.
     *
     * @param key
     * @param value
     * @param ttl
     */
    public boolean setnx(String key, Object value, int ttl) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            // Long reply = jedis.setnx(key.getBytes(), HessianSerializer.serialize(value));
            Long reply = jedis.setnx(key.getBytes(), toJsonByteArray(value));
            if (ttl > 0) {
                jedis.expire(key.getBytes(), ttl);
            }
            return reply == 1;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 如果key不存在, 返回null.
     *
     * @param key
\\
     */
    public String get(String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.get(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For Object, 如果key不存在, 返回null.
     *
     * @param key
     */
    public <T> T get(String key, Class<T> clazz) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            // return (T) HessianSerializer.deserialize(jedis.get(key.getBytes()));
            return fromJsonByteArray(jedis.get(key.getBytes()), clazz);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 自增 +1
     *
     * @param key
     * @return 返回自增后结果
     */
    public Long incr(final String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incr(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 自增 +1
     *
     * @param key key
     * @param integer 起始值
     * @return 返回自增后结果
     */
    public Long incrBy(final String key, long integer) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.incrBy(key, integer);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * 自减 -1
     *
     * @param key
     * @return 返回自减后结果
     */
    public Long decr(final String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.decr(key);
        } finally {
            this.closeRedis(jedis);
        }
    }
}
