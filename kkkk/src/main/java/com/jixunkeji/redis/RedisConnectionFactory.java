package com.jixunkeji.redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedisPool;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: caikai
 * @description: Redis连接工厂类
 * @date:2019/3/9
 */
public class RedisConnectionFactory {

    private static Logger logger = LoggerFactory.getLogger(RedisConnectionFactory.class);

    // 格式为： 127.0.0.1:6379, 128.1.1.1:6379 多个之间用逗号分隔
    protected String servers;

    private String password;

    // 3seconds
    private int timeout = 3000;

    //ShardedJedisPool可用于redis集群
    protected ShardedJedisPool pool;

    private JedisPoolConfig poolConfig;

    public void init() throws Exception {
        List<JedisShardInfo> shardInfoList = new ArrayList<JedisShardInfo>();
        for (String server : servers.split("[,]")) {
            String[] sa = server.split("[:]");
            if (sa.length == 2) {
                String host = sa[0];
                int port = Integer.parseInt(sa[1]);
                JedisShardInfo info = new JedisShardInfo(host, port, timeout);
                if (password != null && !"".equals(password)) {
                    info.setPassword(password);
                }
                shardInfoList.add(info);
            }
        }
        pool = new ShardedJedisPool(poolConfig, shardInfoList);
    }

    public void destroy() throws Exception {
        if (pool != null) {
            try {
                pool.destroy();
            } catch (Exception ex) {
                logger.warn("Cannot properly close Jedis pool", ex);
            }
            pool = null;
        }
    }

    public ShardedJedisPool getConnectionPool() {
        return this.pool;
    }

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public JedisPoolConfig getPoolConfig() {
        return poolConfig;
    }

    public void setPoolConfig(JedisPoolConfig poolConfig) {
        this.poolConfig = poolConfig;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
