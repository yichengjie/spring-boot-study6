1. 编写MethodArgumentResolver实现类
    ```java
    public class PropertiesHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
        @Override
        public boolean supportsParameter(MethodParameter parameter) {
            return Properties.class.isAssignableFrom(parameter.getParameterType());
        }
        @Override
        public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
            ServletWebRequest servletWebRequest = (ServletWebRequest)webRequest ;
            HttpServletRequest request = servletWebRequest.getRequest();
            String contentType = request.getHeader("Content-Type") ;
            MediaType mediaType = MediaType.parseMediaType(contentType);
            Charset charset = mediaType.getCharset();
            charset = charset == null ? Charset.forName("UTF-8") : charset ;
            // 请求输入字节流
            ServletInputStream inputStream = request.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream,charset);
            Properties properties = new Properties() ;
            // 加载字符流成为Properties对象
            properties.load(reader);
            return properties;
        }
    }
    ```
2. 编写配置类将MethodArgumentResolver注册到RequestMappingHandlerAdapter中
    ```java
    @Configuration
    public class RestWebMvcConfigurer implements WebMvcConfigurer {
        @Autowired
        private RequestMappingHandlerAdapter requestMappingHandlerAdapter ;
        @PostConstruct
        public void init(){
            // 获取当前RequestMappingHandlerAdapter中所有的Resolver对象
            //HandlerMethodArgumentResolverComposite.getResolvers()获取集合不可变，所以这里需要新建List
            List<HandlerMethodArgumentResolver> resolvers =
                    requestMappingHandlerAdapter.getArgumentResolvers();
            List<HandlerMethodArgumentResolver> newResolvers = new ArrayList<>(resolvers.size() + 1) ;
            newResolvers.add(new PropertiesHandlerMethodArgumentResolver());
            newResolvers.addAll(resolvers) ;
            // 重置Resolver对象
            requestMappingHandlerAdapter.setArgumentResolvers(newResolvers);
        }
        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            // 自定义HandlerMethodArgumentResolver优先级低于内建HandlerMethodArgumentResolver，
            // 详情见:
            // RequestMappingHandlerAdapter.afterPropertiesSet() => {
            //    List<HandlerMethodArgumentResolver> resolvers = getDefaultArgumentResolvers();
            //    new HandlerMethodArgumentResolverComposite().addResolvers(resolvers)
            // }
            // 所以这里添加了也没用，参数将被MapMethodProcessor处理，因为Properties extends Hashtable
            // 这里调整到@PostConstruct注解的init()中处理
            //resolvers.add(new PropertiesHandlerMethodArgumentResolver());
        }
    }
    ```
3. 编写Controller业务方法，注意这里无@RequestBody注解
    ```java
    @Slf4j
    @RestController
    public class PropertiesRestController {
        @PostMapping(value = "/add/props",
            consumes = "text/properties;charset=UTF-8")
        public Properties addProp(Properties properties){
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