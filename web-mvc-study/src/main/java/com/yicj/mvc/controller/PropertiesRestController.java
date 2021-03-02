package com.yicj.mvc.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Properties;


/**
 * {@link Properties} {@link RestController}
 */
@Slf4j
@RestController
public class PropertiesRestController {
    @PostMapping(value = "/add/props",
        consumes = "text/properties;charset=UTF-8"/*,produces = "text/properties"*/)
    public Properties addProp(@RequestBody Properties properties){
        log.info("====> prop : {}" , properties);
        return properties ;
    }
}
