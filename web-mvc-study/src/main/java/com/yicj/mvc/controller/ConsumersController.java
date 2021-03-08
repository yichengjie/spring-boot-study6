package com.yicj.mvc.controller;

import com.yicj.mvc.model.MyData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
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
