package com.yicj.study.redis.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisMessageListener implements MessageListener {
    @Override
    public void onMessage(Message message, byte[] pattern) {
        // 消息体
        String body = new String(message.getBody()) ;
        // 渠道名称
        String topic = new String(pattern) ;
        log.info("body : {}", body);
        log.info("topic : {}", topic);
    }
}
