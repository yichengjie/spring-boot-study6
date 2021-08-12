1. 添加依赖
    ```text
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webflux</artifactId>
    </dependency>
    ```
2. 编写Handler业务处理
    ```java
    @Slf4j
    @Component
    public class UserHandler {
        // 替代以@RequestParam接收的参数
        public Mono<ServerResponse> add(ServerRequest request) {
            Mono<MultiValueMap<String, String>> formData = request.exchange().getFormData() ;
            return formData.flatMap(multiValueMap -> {
                String username = multiValueMap.getFirst("username");
                String addr = multiValueMap.getFirst("addr") ;
                log.info("=====> username : {}， addr: {}", username, addr);
                return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                        Mono.just(JsonResult.success("success")),JsonResult.class
                );
            }) ;
        }
        // 替待springmvc以@RequestBody接收的参数
        public Mono<ServerResponse> add2(ServerRequest request) {
            Mono<User> userMono = request.bodyToMono(User.class);
            log.info("add2 method execute ...");
            Mono<JsonResult<String>> resultMono = userMono.map(user -> {
                String retContent = doBusi(user);
                return JsonResult.success(retContent);
            });
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(resultMono,JsonResult.class);
        }
        // 业务处理方法一般调用service
        private String doBusi(User user){
           log.info("username : {}, addr : {}", user.getUsername(), user.getAddr());
           log.info("do busi .....");
           sleep(1000);
           return "Hello world" ;
        }
        private void sleep(int time){
           try {
               Thread.sleep(time);
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
        }
    }
    ```
3. 编写route配置
    ```java
    @Configuration
    public class MyRouteConfig {
        @Autowired
        private UserHandler userHandler ;
        @Bean
        public RouterFunction<ServerResponse> routerFunction(){
            return RouterFunctions
                    .route(POST("/api/users/add"), userHandler::add)
                    .andRoute(RequestPredicates.POST("/api/users/add2")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userHandler::add2) ;
        }
    }
    ```
4. 可以用Controller形式编写webflux处理(与上面步骤3，4中的配置/api/users/add2效果一样)
    ```java
    @Slf4j
    @RestController
    @RequestMapping("/api/users")
    public class UserController {
        @PostMapping("/add3")
        public Mono<JsonResult<String>> add2(@RequestBody User user){
            log.info("user : {}", user);
            return Mono.just(JsonResult.success("success")) ;
        }
    }
    ```
5. Webflux官网地址：https://docs.spring.io/spring-framework/docs/current/reference/html/web-reactive.html