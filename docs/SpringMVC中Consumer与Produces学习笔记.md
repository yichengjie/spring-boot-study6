1. 内容类型，客户端通过Content-Type设置,对应Controller的consumes配置
    ```java
    @RestController
    public class ConsumersController {
        /**客户端发送json数据
         * {
         *   "firstName":"yi",
         *   "lastName":"cj"
         * }
         */
        // Content-Type为application/json，接收json类型的请求
        @GetMapping(value = "/myConsumes", consumes = MediaType.APPLICATION_JSON_VALUE)
        public String applicationJsonView(@RequestBody MyData myData){
            log.info("json ====> {}", myData);
            return "myFormAgreedJsonView" ;
        }
       /**客户端发送xml数据
        * <?xml version="1.0" encoding="UTF-8" standalone="yes"?>
        * <myData>
        *     <firstName>yi</firstName>
        *     <lastName>cj</lastName>
        * </myData>
        */
        // Content-Type为application/json，接收xml类型的请求
        @GetMapping(value = "/myConsumes", consumes = MediaType.APPLICATION_XML_VALUE)
        public String applicationXmlView(@RequestBody MyData myData){
            
            log.info("xml ====> {}", myData);
            return "myFormAgreedXmlView" ;
        }
    }
    ```
2. 可接收内容类型，客户端通过Accept设置，对应Controller的produces配置
    ```java
    @RestController
    public class ProducesController {
        // Accept 为application/json的处理器，用于产生json类型的返回值
        @ResponseBody
        @GetMapping(value = "/myProduces", produces = MediaType.APPLICATION_JSON_VALUE)
        public MyData returnJsonValue(){
            return getMyData() ;
        }
        // Accept为application/xml的处理器，用于产生xml类型的返回值
        @GetMapping(value = "/myProduces", produces = MediaType.APPLICATION_XML_VALUE)
        public MyData returnXmlValue(){
            return getMyData() ;
        }
        // 测试Accept为*/*, 通过produces指定返回值类型为json
        @GetMapping(value = "/myAcceptAll", produces = MediaType.APPLICATION_JSON_VALUE)
        public MyData acceptAll(){
            return getMyData() ;
        }
        private MyData getMyData(){
            MyData myData = new MyData() ;
            myData.setFirstName("yi");
            myData.setLastName("cj");
            return myData ;
        }
    }
    ```
3. Model实体对象
    ```java
    @Data
    @XmlRootElement
    public class MyData {
        private String firstName ;
        private String lastName ;
    }
    ```