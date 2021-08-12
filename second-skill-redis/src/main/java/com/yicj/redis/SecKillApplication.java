package com.yicj.redis;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.yicj.redis.mapper")
@SpringBootApplication
public class SecKillApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecKillApplication.class);
    }

}