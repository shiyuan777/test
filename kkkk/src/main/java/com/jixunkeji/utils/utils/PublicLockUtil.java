package com.jixunkeji.utils.utils;


import com.jixunkeji.redis.StringRedisTemplate;

/**
 * @description: 基于redis的分布式锁
 * @date:2019/3/20
 */
public class PublicLockUtil {
    private static final long TOTAL_WAIT_SENCODS = 10 * 1000L;// 10s获取不到算超时
    private static final int REPLY_WAIT_SENCODS = 5;// 重试获取锁间隔时间ms
    private static final int LOCK_SENCODS = 1;// 默认锁1s

    public static StringRedisTemplate stringRedisTemplate;

    static {
        stringRedisTemplate = SpringContextHolder.getBean(StringRedisTemplate.class);
    }


    /**
     * 获取操作锁
     *
     * @return true 获取成功，false获取失败
     * @throws InterruptedException
     */
    public static boolean getLock(String lockKey, long waitSencods, int lockSeconds) throws InterruptedException {
        long t1 = System.currentTimeMillis();
        boolean isSuccess = false;
        while (!isSuccess) {
            if (System.currentTimeMillis() <= t1 + waitSencods) {
                isSuccess = tryLock(lockKey, lockSeconds);
                if (!isSuccess) {
                    Thread.sleep(REPLY_WAIT_SENCODS);//sleep 10s
                }
            } else {//超过等待时间，返回失败
                break;
            }
        }
        return isSuccess;
    }

    /**
     * 获取操作锁
     *
     * @return true 获取成功，false获取失败
     * @throws InterruptedException
     */
    public static boolean getLock(String lockKey, int lockSeconds) throws InterruptedException {
        long waitSencods = TOTAL_WAIT_SENCODS;
        return getLock(lockKey, waitSencods, lockSeconds);
    }

    /**
     * 获取操作锁
     *
     * @return true 获取成功，false获取失败
     * @throws InterruptedException
     */
    public static boolean getLock(String lockKey) throws InterruptedException {
        long waitSencods = TOTAL_WAIT_SENCODS;
        int lockSeconds = LOCK_SENCODS;
        return getLock(lockKey, waitSencods, lockSeconds);
    }

    /**
     * 释放锁
     */
    public static void freeLock(String lockKey) {
        stringRedisTemplate.del(lockKey);
    }

    /**
     * 锁是否存在
     *
     * @param lockKey
     * @return
     */
    public static boolean isLock(String lockKey) {
        return stringRedisTemplate.exists(lockKey);
    }

    /**
     * 尝试获取锁
     *
     * @return
     */
    private static boolean tryLock(String lockKey, int lockSeconds) {
        return stringRedisTemplate.setnx(lockKey, "Y", lockSeconds);//返回1则成功
    }


}
