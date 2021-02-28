1. 编写业务接口
    ```java
    public interface CalculateService {
        Integer sum(Integer ... values) ;
    }
    ```
2. 编写业务实现类并添加@Profile注解
    ```java
    @Service
    @Profile("Java7")
    public class Java7CalculateService implements CalculateService {
        @Override
        public Integer sum(Integer... values) {
            log.info("===> Java7 for 循环实现");
            Integer result = 0 ;
            for (Integer value : values){
                result += value ;
            }
            return result;
        }
    }
    @Service
    @Profile("Java8")
    public class Java8CalculateService implements CalculateService {
        @Override
        public Integer sum(Integer... values) {
            log.info("===> Java8 Lambda 表达式实现");
            Integer result = Stream.of(values).reduce(0 , Integer::sum) ;
            return result;
        }
    }
    ```
3. 启动类编写并配置profile
    ```java
    @Slf4j
    @SpringBootApplication(scanBasePackages = "com.yicj.hello.service")
    public class CalculateProfileApplication {
        public static void main(String[] args) {
            ConfigurableApplicationContext ctx = new SpringApplicationBuilder(CalculateProfileApplication.class)
                    .web(WebApplicationType.NONE)
                    .profiles("Java8")
                    .run(args);
            CalculateService bean = ctx.getBean(CalculateService.class);
            log.info("=====> bean : {}", bean.sum(1,2,3,4,5,6,7,8,9,10));
            ctx.close();
        }
    }
    ```