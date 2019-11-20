package com.jixunkeji.configuration;


import com.jixunkeji.redis.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPoolConfig;


/**

 */
@Configuration
public class RedisBeanConfig {
    private static final Logger logger = LoggerFactory.getLogger(RedisBeanConfig.class);

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

//    @Value("${spring.redis.password}")
//    private String password;

    @Value("${spring.redis.jedis.pool.max-wait}")
    private Integer maxWait;

    @Value("${spring.redis.jedis.pool.min-idle}")
    private Integer minIdle;

    @Value("${spring.redis.jedis..pool.max-idle}")
    private Integer maxIdle;

    @Value("${spring.redis.jedis.pool.max-active}")
    private Integer maxActive;

    @Bean
    public JedisPoolConfig jedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxTotal(maxActive);
        return jedisPoolConfig;
    }

    @Bean(initMethod = "init", destroyMethod = "destroy")
    public RedisConnectionFactory redisConnectionFactory() {
        RedisConnectionFactory redisConnectionFactory = new RedisConnectionFactory();
        redisConnectionFactory.setPoolConfig(jedisPoolConfig());
        redisConnectionFactory.setServers(host + ":" + port);
//        redisConnectionFactory.setPassword(password);
        return redisConnectionFactory;
    }

    @Bean
    public ZsetRedisTemplate zSetRedisTemplate() {
        ZsetRedisTemplate zSetRedisTemplate = new ZsetRedisTemplate();
        zSetRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return zSetRedisTemplate;
    }

    @Bean
    public ListRedisTemplate listRedisTemplate() {
        ListRedisTemplate listRedisTemplate = new ListRedisTemplate();
        listRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return listRedisTemplate;
    }

    @Bean
    public SetRedisTemplate setRedisTemplate() {
        SetRedisTemplate setRedisTemplate = new SetRedisTemplate();
        setRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return setRedisTemplate;
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return stringRedisTemplate;
    }

    @Bean
    public HashRedisTemplate hashRedisTemplate() {
        HashRedisTemplate hashRedisTemplate = new HashRedisTemplate();
        hashRedisTemplate.setConnectionFactory(redisConnectionFactory());
        return hashRedisTemplate;
    }



}
