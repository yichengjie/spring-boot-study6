1. 编写Condition实现类（可参考：OnPropertyCondition）
    ```java
    public class OnSystemPropertyCondition  implements Condition {
        @Override
        public boolean matches(ConditionContext context, AnnotatedTypeMetadata metadata) {
            Map<String, Object> attributes =
                    metadata.getAnnotationAttributes(ConditionalOnSystemProperty.class.getName());
            String propertyName = (String) attributes.get("name") ;
            String propertyValue = (String) attributes.get("value") ;
            String systemPropertyValue = System.getProperty(propertyName) ;
            return propertyValue.equalsIgnoreCase(systemPropertyValue);
        }
    }
    ```
2. 编写Conditional注解类（可参考：ConditionalOnProperty）
    ```java
    @Documented
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ ElementType.TYPE, ElementType.METHOD })
    @Conditional(OnSystemPropertyCondition.class)
    public @interface ConditionalOnSystemProperty{
        // Java 系统属性名称
        String name() default "" ;
        // Java 系统属性值
        String value() default "" ;
    }
    ```
3. 业务及启动类编写
    ```java
    @Slf4j
    public class ConditionalOnSystemPropertyApplication {
        @Bean
        @ConditionalOnSystemProperty(name = "user.name", value = "yichengjie")
        public String helloWorld(){
            return "Hello, yicj" ;
        }
        public static void main(String[] args) {
            ConfigurableApplicationContext ctx = new SpringApplicationBuilder(ConditionalOnSystemPropertyApplication.class)
                    .web(WebApplicationType.NONE)
                    .run(args);
            String bean = ctx.getBean("helloWorld", String.class);
            log.info("====> bean : {}", bean);
            ctx.close();
        }
    }
    ```