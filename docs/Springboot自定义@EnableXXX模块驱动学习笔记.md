#### 使用ImportSelector
1. 编写业务及业务实现代码
    ```java
    // 服务器接口
    public interface Server {
        void start() ;
        void stop() ;
        enum Type{
            HTTP,
            FTP
        }
    }
    // http 服务器实现类
    @Component
    public class HttpServer implements Server {
        @Override
        public void start() {
            log.info("HTTP服务器启动中...");
        }
    
        @Override
        public void stop() {
            log.info("HTTP服务器关闭中...");
        }
    }
    // ftp 服务器实现类
    @Component
    public class FtpServer implements Server {
        @Override
        public void start() {
            log.info("FTP服务器启动中...");
        }
        @Override
        public void stop() {
            log.info("FTP服务器关闭中...");
        }
    }
    ```
2. 编写核心代码，实现ImportSelector接口
    ```java
    public class ServerImportSelector implements ImportSelector {
        @Override
        public String[] selectImports(AnnotationMetadata importingClassMetadata) {
            Map<String, Object> attributes =
                    importingClassMetadata.getAnnotationAttributes(EnableServer.class.getName());
            Server.Type type = (Server.Type)attributes.get("type") ;
            String [] importClassNames = new String[0] ;
            switch (type){
                case HTTP:
                    importClassNames = new String[]{HttpServer.class.getName()} ;
                    break;
                case FTP:
                    importClassNames = new String[]{FtpServer.class.getName()} ;
                    break;
            }
            return importClassNames;
        }
    }
    ```
3. 编写EnableXXX注解
    ```java
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Import(ServerImportSelector.class)
    public @interface EnableServer {
        Server.Type type() ;
    }
    ```
4. 测试代码(使用EnableXXX)
    ```java
    @EnableServer(type = Server.Type.HTTP)
    @SpringBootApplication
    public class WebMvcApplication  implements ApplicationRunner {
        @Autowired
        private Server server ;
        public static void main(String[] args) {
            SpringApplication.run(WebMvcApplication.class, args) ;
        }
        @Override
        public void run(ApplicationArguments args) throws Exception {
            server.start();
        }
    }
    ```
#### 使用ImportBeanDefinitionRegistrar
1. 编写核心代码，实现ImportBeanDefinitionRegistrar接口
    ```java
    public class ServerImportBeanDefinitionRegistrar implements ImportBeanDefinitionRegistrar {
        @Override
        public void registerBeanDefinitions(AnnotationMetadata importingClassMetadata, BeanDefinitionRegistry registry) {
            // 复用{@link ServerImportSelector} 实现，避免重复劳动
            ImportSelector importSelector = new ServerImportSelector() ;
            String[] selectedClassNames = importSelector.selectImports(importingClassMetadata);
            //创建Bean的定义
            Stream.of(selectedClassNames)
                    // 转化为BeanDefinitionBuilder对象
                  .map(BeanDefinitionBuilder::genericBeanDefinition)
                    //转化为BeanDefinition
                  .map(BeanDefinitionBuilder::getBeanDefinition)
                  .forEach(beanDefinition -> {
                      // 注册BeanDefinition到BeanDefinitionRegistry
                      BeanDefinitionReaderUtils.registerWithGeneratedName(beanDefinition,registry) ;
                  });
        }
    }
    ```
2. 修改EnableServer类，导入ServerImportBeanDefinitionRegistrar
    ```java
    @Documented
    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    //@Import(ServerImportSelector.class)
    @Import(ServerImportBeanDefinitionRegistrar.class)
    public @interface EnableServer {
        Server.Type type() ;
    }
    ```
3. 测试代码同ImportSelector部分