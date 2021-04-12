package com.yicj.study.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootApplication
public class RedisApplication implements ApplicationRunner {

    @Autowired
    private RedisTemplate<Object,Object> redisTemplate ;

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args) ;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        redisTemplate.opsForValue().set("key1","value1");
        redisTemplate.opsForHash().put("hash","field","hvalue");
    }
}
