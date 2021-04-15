package com.yicj.study.redis;

import com.yicj.study.redis.component.RedisMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.Topic;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class RedisApplication implements ApplicationRunner {
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate ;
    @Autowired // redis 连接工厂
    private RedisConnectionFactory redisConnectionFactory ;
    @Autowired // 消息监听器
    private MessageListener messageListener ;
    // 任务池
    private ThreadPoolTaskExecutor threadExecutor ;

    public static void main(String[] args) {
        SpringApplication.run(RedisApplication.class, args) ;
    }
    @PostConstruct
    public void init(){
        initRedisTemplate();
    }
    private void initRedisTemplate(){
        RedisSerializer<String> stringSerializer = redisTemplate.getStringSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
    }

    @Bean // 创建任务池，运行线程等待处理Redis消息
    public ThreadPoolTaskExecutor initTaskScheduler(){
        if (threadExecutor != null){
            return threadExecutor ;
        }
        threadExecutor = new ThreadPoolTaskExecutor() ;
        threadExecutor.setCorePoolSize(20);
        threadExecutor.setMaxPoolSize(20);
        return threadExecutor ;
    }

    @Bean // 定义Redis的监听容器
    public RedisMessageListenerContainer initRedisContainer(){
        RedisMessageListenerContainer container = new RedisMessageListenerContainer() ;
        container.setConnectionFactory(redisConnectionFactory);
        container.setTaskExecutor(initTaskScheduler());
        Topic topic = new ChannelTopic("topic1") ;
        // 使用监听器监听Redis消息
        container.addMessageListener(messageListener, topic);
        return container ;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        /*redisTemplate.opsForValue().set("key1","value1");
        redisTemplate.opsForHash().put("hash","field","hvalue");*/
        // 发送消息
        redisTemplate.convertAndSend("topic1", "hello world");
    }
}
