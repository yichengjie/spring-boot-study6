package com.yicj.mvc.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumersController {

    @GetMapping(value = "/myConsumes", consumes = MediaType.APPLICATION_JSON_VALUE)
    public String applicationJsonView(){

        return "myFormAgreedJsonView" ;
    }

    @GetMapping(value = "/myConsumes", consumes = MediaType.APPLICATION_XML_VALUE)
    public String applicationXmlView(){
        return "myFormAgreedXmlView" ;
    }
}
