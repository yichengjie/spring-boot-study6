package com.yicj.mvc.controller;

import com.yicj.mvc.model.MyData;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
