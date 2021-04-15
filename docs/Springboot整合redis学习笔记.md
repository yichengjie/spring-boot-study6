####  基础知识点
1. 修改RedisTemplate的序列化器
    ```java
    @SpringBootApplication
    public class RedisApplication implements ApplicationRunner {
        @Autowired
        private RedisTemplate<Object,Object> redisTemplate ;
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
        @Override
        public void run(ApplicationArguments args) throws Exception {
            redisTemplate.opsForValue().set("key1","value1");
            redisTemplate.opsForHash().put("hash","field","hvalue");
        }
    }
    ```
#### 使用Redis发布订阅
1. 编写消息监听器
    ```java
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
    ```
2. 配置RedisMessageListenerContainer
    ```java
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
    }
    ```
3. redis客户端输入命令
    ```cmd
    publish topic1 msg
    ```
4. redisTemplate发送消息
    ```text
    redisTemplate.convertAndSend("topic1", "hello world");
    ```