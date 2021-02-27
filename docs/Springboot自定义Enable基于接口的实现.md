1. 编写configuration
    ```java
    @Configuration
    public class HelloWorldConfiguration {
        @Bean
        public String helloWorld(){
            return "Hello, yicj" ;
        }
    }
    ```
2. 编写EnableXXX导入配置类
    + 直接导入配置类（方式一）
        ```java
        @Documented
        @Retention(RetentionPolicy.RUNTIME)
        @Target(ElementType.TYPE)
        @Import(HelloWorldConfiguration.class)
        public @interface EnableHelloWorld {
        }
        ```
    + 通过ImportSelector导入配置类（方式二）
      ```java
      public class HelloWorldImportSelector implements ImportSelector {
          @Override
          public String[] selectImports(AnnotationMetadata importingClassMetadata) {
              return new String[]{HelloWorldConfiguration.class.getName()};
          }
      }
      @Documented
      @Retention(RetentionPolicy.RUNTIME)
      @Target(ElementType.TYPE)
      @Import(HelloWorldImportSelector.class)
      public @interface EnableHelloWorld {
      }
      ```
3. 启动类编写,导入EnableXXX注解类
    ```java
    @Slf4j
    @EnableHelloWorld
    public class EnableHelloWorldBootstrap {
        public static void main(String[] args) {
            ConfigurableApplicationContext ctx = new SpringApplicationBuilder(EnableHelloWorldBootstrap.class)
                    .web(WebApplicationType.NONE)
                    .run(args);
            String bean = ctx.getBean("helloWorld", String.class);
            log.info("=====> bean : {}", bean);
            ctx.close();
        }
    }
    ```