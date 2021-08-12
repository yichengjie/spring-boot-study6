package com.yicj.study.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

@Configuration
public class RedisConfig {

    private RedisConnectionFactory connectionFactory ;

    @Bean("redisConnectionFactory")
    public RedisConnectionFactory initRedisConnectFactory(){
        if (this.connectionFactory !=null){
            return this.connectionFactory ;
        }
        JedisPoolConfig poolConfig = new JedisPoolConfig() ;
        // 最大空闲数
        poolConfig.setMaxIdle(30);
        poolConfig.setMaxTotal(30);
        poolConfig.setMaxWaitMillis(2000);
        JedisConnectionFactory connectionFactory = new JedisConnectionFactory(poolConfig) ;
        // 获取单机redis配置
        RedisStandaloneConfiguration rsConfig = connectionFactory.getStandaloneConfiguration() ;
        rsConfig.setHostName("192.168.221.128");
        rsConfig.setPort(6379);
        //rsConfig.setPassword("123456");
        this.connectionFactory = connectionFactory ;
        return connectionFactory ;
    }

    /*@Bean("redisTemplate")
    public RedisTemplate<Object, Object> initRedisTemplate(){
        RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>() ;
        RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
        // 设置字符串序列化器，这样Spring就会把Redis的key当作字符串处理了
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        redisTemplate.setConnectionFactory(initRedisConnectFactory());
        return redisTemplate ;
    }*/
}
