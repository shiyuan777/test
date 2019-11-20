package com.jixunkeji.utils.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * 线程池工具类
 * 分三个线程池：pool-1、pool-2、pool-3 ,数字越小的线程池执行越核心的任务
 */
public class ThreadPoolExecutorUtil {

    private static final Logger logger = LoggerFactory.getLogger(ThreadPoolExecutorUtil.class);

    private static final ConcurrentMap<String, ThreadPoolExecutor> threadPoolExecutorMap = new ConcurrentHashMap<>();
    private static final String FIRST_LEVEL = "firstLevel";
    private static final String SECOND_LEVEL = "secondLevel";
    private static final String THIRD_LEVEL = "thirdLevel";
    private static final String NAME_PREFIX = "cygo_thread_pool_";

    private static boolean allowDegrade = false; //核心线程池任务是否可以降级到下一级线程池执行，默认关闭
    private static final int CORE_POOL_SIZE = 500; //核心线程数
    private static final int MAX_POOL_SIZE = 1000; //最大线程数
    private static final int KEEP_ALIVE_SECONDS = 60;
    private static final int QUEUE_CAPACITY = 10000;
    private static final long MONITOR_INTERVAL = 5000;

    //private static final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(3);
    //TODO 先不做监听和降级处理
    /*static {
        listenAndChange();
        scheduledExecutorService.scheduleWithFixedDelay(new Runnable() {
            public void run() {
                monitor();
                listenAndChange();
            }
        }, MONITOR_INTERVAL, MONITOR_INTERVAL, TimeUnit.MILLISECONDS);
    }*/

    public static void listenAndChange() {
        String allowDegradeConfig = ""; //TODO 读配置
        allowDegrade = allowDegradeConfig != null ? Boolean.valueOf(allowDegradeConfig) : false;

    }

    /*public static void monitor() {
        try {
            Set<String> keySet = threadPoolExecutorMap.keySet();
            if(keySet != null){
                for(Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
                    String name = iterator.next();
                    ThreadPoolExecutor threadPoolExecutor = threadPoolExecutorMap.get(name);
                    if(logger.isInfoEnabled()){
                        echoLog(NAME_PREFIX + name, threadPoolExecutor.getActiveCount(), threadPoolExecutor.getPoolSize(), threadPoolExecutor.getQueue().size(), QUEUE_CAPACITY);
                    }
                }
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }*/

    //线上调试用
    public static void echoLog(Object o1, Object o2, Object o3, Object o4, Object o5) {
        logger.debug("【 线程池：{} [ 当前活跃线程数：{}，最大线程数：{} ],[ 当前队列任务数：{}，最大队列任务数：{} ] 】",
                o1,o2,o3,o4,o5);
    }


    /**
     * 使用一级（最高级别）线程池执行任务，任务满时降级线程池处理
     * @param task
     */
    public static void executeByFirstPool(Runnable task) {
        if(allowDegrade && getCurrentLevelExecutor(FIRST_LEVEL).getQueue().size() >= QUEUE_CAPACITY) {//允许降级且当前线程池队列已满
            if(logger.isWarnEnabled()){
                logger.warn("firstLevel线程池满了，降级到secondLevel线程池处理");
            }
            executeBySecondPool(task);
        }else {
            getCurrentLevelExecutor(FIRST_LEVEL).execute(task);
        }

    }

    /**
     * 使用二级（次级别）线程池执行任务，任务满时降级线程池处理
     * @param task
     */
    public static void executeBySecondPool(Runnable task) {
        if(allowDegrade && getCurrentLevelExecutor(SECOND_LEVEL).getQueue().size() >= QUEUE_CAPACITY ) {
            if(logger.isWarnEnabled()) {
                logger.warn("secondLevel线程池满了，降级到thirdLevel线程池处理");
            }
            executeByThirdPool(task);
        }else {
            getCurrentLevelExecutor(SECOND_LEVEL).execute(task);
        }
    }

    /**
     * 使用三级（最低级别）线程池执行任务
     * @param task
     */
    public static void executeByThirdPool(Runnable task) {
        getCurrentLevelExecutor(THIRD_LEVEL).execute(task);
    }


    public static ThreadPoolExecutor getCurrentLevelExecutor(String level) {
        ThreadPoolExecutor threadPoolTaskExecutor = threadPoolExecutorMap.get(level);
        if(threadPoolTaskExecutor == null) {
            init(level);
        }
        return threadPoolExecutorMap.get(level);
    }

    public static synchronized void init(final String level) {
        if(threadPoolExecutorMap.get(level) != null)
            return ;

        BlockingQueue<Runnable> blockingQueue = new LinkedBlockingDeque<Runnable>(QUEUE_CAPACITY);
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_SECONDS,
                TimeUnit.SECONDS,
                blockingQueue,
                new RejectedExecutionHandler() {
                    @Override
                    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
                        logger.error("【 拒收任务 - 线程池：{},当前活跃线程数：{}，最大线程数：{}，当前队列任务数：{}，最大队列任务数：{}，累计任务总数：{} 】",
                                NAME_PREFIX + level, executor.getActiveCount(), executor.getPoolSize(), executor.getQueue().size(), QUEUE_CAPACITY, executor.getTaskCount());
                    }
                });
        threadPoolExecutorMap.put(level, threadPoolExecutor);
    }



}
