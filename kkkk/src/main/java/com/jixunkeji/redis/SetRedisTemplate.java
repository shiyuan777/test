package com.jixunkeji.redis;

import redis.clients.jedis.ShardedJedis;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author: caikai
 * @description: com.zdkj.template
 * @date:2019/3/9
 */
public class SetRedisTemplate extends BasicRedisTemplate {
    /**
     * Add one or more members to a set
     *
     * @param key
     * @param members
     * @return
     */
    public Boolean sadd(final String key, final String... members) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, members) == 1 ? true : false;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * Add one or more members to a set
     *
     * @param key
     * @param ttl
     * @param members
     * @return
     */
    public Boolean sadd(final String key,int ttl, final String... members) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            Boolean ret = jedis.sadd(key, members) == 1 ? true : false;
            if (ret && ttl > 0) {
                jedis.expire(key, ttl);
            }
            return ret;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For Object, Add one or more members to a set
     *
     * @param key
     * @param members
     * @return
     */
    public Boolean sadd(final String key, final Object... members) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[][] strings = new byte[members.length][];
            for (int j = 0; j < members.length; j++) {
                strings[j] = toJsonByteArray(members[j]);
            }
            return jedis.sadd(key.getBytes(), strings) == 1 ? true : false;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * Remove one or more members from a set
     *
     * @param key
     * @param members
     * @return
     */
    public Boolean srem(final String key, final String... members) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, members) == 1 ? true : false;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For Object, Remove one or more members from a set
     *
     * @param key
     * @param members
     * @return
     */
    public Boolean srem(final String key, final Object... members) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            byte[][] strings = new byte[members.length][];
            for (int j = 0; j < members.length; j++) {
                strings[j] = toJsonByteArray(members[j]);
            }
            return jedis.srem(key.getBytes(), strings) == 1 ? true : false;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * Get all the members in a set.
     *
     * @param key
     * @return
     */
    public Set<String> smembers(final String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.smembers(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * For Object, Get all the members in a set.
     *
     * @param key
     * @param clazz
     * @return
     */
    public <T> Set<T> smembers(final String key, Class<T> clazz) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            Set<byte[]> tempSet = jedis.smembers(key.getBytes());
            if (tempSet != null && tempSet.size() > 0) {
                TreeSet<T> result = new TreeSet<T>();
                for (byte[] value : tempSet) {
                    result.add(fromJsonByteArray(value, clazz));
                }
                return result;
            }
            return null;
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * Get all the members is number in a set.
     *
     * @param key
     * @return
     */
    public Long scard(final String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    /**
     * true if the meber exists in a set,else false
     *
     * @param key
     * @param member
     * @return
     */
    public Boolean sismember(final String key, final String member) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, member);
        } finally {
            this.closeRedis(jedis);
        }
    }

    public String spop(final String key) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.spop(key);
        } finally {
            this.closeRedis(jedis);
        }
    }

    public List<String> srandmember(final String key, final int count) {
        ShardedJedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srandmember(key, count);
        } finally {
            this.closeRedis(jedis);
        }
    }
}
