#### 实现Content-Type为text/properties媒体类型的HttpMessageConverter
1. 编写HttpMessageConverter实现类（核心处理逻辑）
    ```java
    // 参考MappingJackson2HttpMessageConverter
    public class PropertiesHttpMessageConverter extends AbstractGenericHttpMessageConverter<Properties> {
        public PropertiesHttpMessageConverter(){
            super(new MediaType("text", "properties"));
        }
        @Override
        protected void writeInternal(Properties properties, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
            OutputStream body = outputMessage.getBody();
            Charset charset = this.getCharset(outputMessage) ;
            Writer writer = new OutputStreamWriter(body,charset) ;
            // 这里要使用字符流，以防止中文乱码
            properties.store(writer,"form PropertiesHttpMessageConverter");
        }
        @Override
        protected Properties readInternal(Class<? extends Properties> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            InputStream body = inputMessage.getBody();
            Properties properties = new Properties() ;
            // 从Content-Type解析编码
            Charset charset = getCharset(inputMessage) ;
            InputStreamReader reader = new InputStreamReader(body, charset) ;
            // 注意这里要使用字符流，否则会有中文乱码
            properties.load(reader);
            return properties;
        }
        private Charset getCharset(HttpMessage inputMessage){
            // 从Content-Type解析编码
            HttpHeaders headers = inputMessage.getHeaders();
            MediaType contentType = headers.getContentType();
            Charset charset = contentType.getCharset();
            charset = charset == null ? Charset.forName("UTF-8") : charset ;
            return charset ;
        }
        @Override
        public Properties read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
            return readInternal(null, inputMessage);
        }
    }
    ```
2. 将自定义HttpMessageConverter添加到RequestMappingHandlerAdapter中
    ```java
    @Configuration
    public class RestWebMvcConfigurer implements WebMvcConfigurer {
        @Override
        public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
            // 插入到第一位，其他Converter向后移动
            converters.add(0,new PropertiesHttpMessageConverter());
            // 添加到最后时，结果会被MappingJackson2HttpMessageConverter处理返回json
            //converters.add(new PropertiesHttpMessageConverter());
        }
    }
    ```
3. 编写业务Controller
    ```java
    @RestController
    public class PropertiesRestController {
        @PostMapping(value = "/add/props",
            consumes = "text/properties;charset=UTF-8"/*,produces = "text/properties"*/)
        public Properties addProp(@RequestBody Properties properties){
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

