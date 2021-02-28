1. 编写自动装配XXXAutoConfiguration
    ```java
    @Configuration // Spring模式注解装配
    @EnableHelloWorld // Spring @Enable 模块装配
    @ConditionalOnSystemProperty(name = "user.name", value = "yichengjie")//条件装配
    public class HelloWorldAutoConfiguration {
    }
    ```
2. 编写META-INF/spring.factories配置
    ```properties
    org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
    com.yicj.hello.configuration.HelloWorldAutoConfiguration
    ```
3. 启动类添加@EnableAutoConfiguration注解
    ```java
    @EnableAutoConfiguration
    public class EnableAutoConfigurationApplication {
        public static void main(String[] args) {
            ConfigurableApplicationContext ctx = new SpringApplicationBuilder(EnableAutoConfigurationApplication.class)
                    .web(WebApplicationType.NONE)
                    .run(args);
            String bean = ctx.getBean("helloWorld", String.class);
            log.info("=====> bean : {}", bean);
            ctx.close();
        }
    }
    ```