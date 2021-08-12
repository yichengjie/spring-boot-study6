1. 编写Condition实现类
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
2. 编写Conditional注解类
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
3. 业务代码添加Conditional注解
    ```java
    @ConditionalOnSystemProperty(name = "user.name", value = "yichengjie")//条件装配
    public class HelloWorldAutoConfiguration {
    }
    ```