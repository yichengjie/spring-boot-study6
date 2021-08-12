1. 编写HandlerMethodReturnValueHandler实现类
    ```java
    public class PropertiesHandlerMethodReturnValueHandler implements  HandlerMethodReturnValueHandler {
        @Override
        public boolean supportsReturnType(MethodParameter returnType) {
            // 判断方法的返回值，是否为Properties
            Class<?> returnType1 = returnType.getMethod().getReturnType();
            return Properties.class.isAssignableFrom(returnType1);
        }
        @Override
        public void handleReturnValue(Object returnValue, MethodParameter returnType, ModelAndViewContainer mavContainer, NativeWebRequest webRequest) throws Exception {
            // 告知 spring mvc当前请求已经处理完毕
            mavContainer.setRequestHandled(true);
            Properties properties = (Properties) returnValue ;
            PropertiesHttpMessageConverter converter = new PropertiesHttpMessageConverter() ;
            ServletWebRequest servletWebRequest = (ServletWebRequest) webRequest ;
            HttpServletRequest request = servletWebRequest.getRequest();
            String contentType = request.getHeader("Content-Type");
            MediaType mediaType = MediaType.parseMediaType(contentType);
            //获取Servlet Response对象
            HttpServletResponse response = servletWebRequest.getResponse() ;
            HttpOutputMessage message = new ServletServerHttpResponse(response) ;
            converter.write(properties, mediaType, message);
        }
    }
    ```
2. 编写配置类将HandlerMethodReturnValueHandler注册到RequestMappingHandlerAdapter中
    ```java
    @Configuration
    public class RestWebMvcConfigurer implements WebMvcConfigurer {
        @Autowired
        private RequestMappingHandlerAdapter requestMappingHandlerAdapter ;
        @PostConstruct
        public void init(){
            // 注册PropertiesHandlerMethodReturnValueHandler
            List<HandlerMethodReturnValueHandler> handlers =
                    requestMappingHandlerAdapter.getReturnValueHandlers();
            List<HandlerMethodReturnValueHandler> newHandlers = new ArrayList<>(handlers.size() + 1) ;
            newHandlers.add(new PropertiesHandlerMethodReturnValueHandler());
            newHandlers.addAll(handlers) ;
            // 重置Handler对象集合
            requestMappingHandlerAdapter.setReturnValueHandlers(newHandlers);
        }
    }
    ```
3. 编写Controller业务方法，注意这里无@ResponseBody注解,使用@Controller而不是@RestController
    ```java
    @Controller
    public class PropertiesRestController {
        @PostMapping(value = "/add/props",
            consumes = "text/properties;charset=UTF-8"/*,produces = "text/properties"*/)
        public Properties addProp(/*@RequestBody*/ Properties properties){
            log.info("====> prop : {}" , properties);
            return properties ;
        }
    }
    ```
4. 使用PostMan发送请求，将Header的Content-Type设置为text/properties
    ```text
    4.1 header内容
        Content-Type设置为text/properties
    4.1 body内容
        username=yicj
        addr=北京
    ```