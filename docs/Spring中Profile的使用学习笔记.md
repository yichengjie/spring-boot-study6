1. 编写Profile业务代码
    ```text
    @Bean
    @Profile("dev")
    public User user1(){
        User user =  new User() ;
        user.setUsername("张三");
        return user ;
    }
    @Bean
    @Profile("test")
    public User user2(){
        User user =  new User() ;
        user.setUsername("李四");
        return user ;
    }
    ```
2. 启动参数配置
    ```text
    2.1 java项目中添加 JAVA_OPTS="-Dspring.profiles.active=dev"
    2.2 ide环境中在VM arguments中添加-Dspring.profiles.active=dev 
    ```
3. 配置说明
    ```text
    2.1 通过spring.profiles.active，或则spring.profiles.default配置profile
    2.2 两个属性都没配置时，被@Profile标注的Bean将不会被Spring容器装配
    2.3 spring.profiles.active优先级更高
    ```
4. SpringBoot中profile的其他用法
    ```text
    1. 在resources目录中新建application-dev.properties
    2. -Dspring.profiles.active配置的值作为{profile}，
       则它会用application-{profile}.properties去替代默认的application.properties文件
    ```