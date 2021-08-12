package com.yicj.mvc.controller;

import com.yicj.mvc.model.MyData;
import com.yicj.mvc.model.MyData2;
import com.yicj.mvc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.async.DeferredResult;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Slf4j
@Controller
public class HelloController {

    @GetMapping("/hello")
    public String hello(){
        return "hello" ;
    }

    @ResponseBody
    @CrossOrigin
    @GetMapping("/hello1")
    public Map<String,String> hello(String message){
        Map<String,String> map = new HashMap<>() ;
        map.put("code","200") ;
        map.put("msg","success") ;
        map.put("data", message) ;
        return map ;
    }

    @ResponseBody
    @PostMapping(value = "/hello2",
            //consumes = "application/xxx;charset=GBK",
            produces = "application/json;charset=UTF-8")
    public User hello2(@RequestBody User user){

        return user ;
    }

    @GetMapping("/hello3")
    @ResponseBody
    public User hello3(User user){

        return user ;
    }

    @ResponseBody
    @GetMapping("/asyncHello")
    public DeferredResult<String> asyncHello(){
        DeferredResult<String> result = new DeferredResult<>() ;
        log.info("========> other busi");
        result.setResult("hello world " + LocalDateTime.now()) ;
        result.onCompletion(()->{
            log.info("异步业务执行结束..");
        });
        return result ;
    }


    @GetMapping("/beanNameView")
    public String beanNameView(Model model){
        // 添加一个Model属性
        model.addAttribute("name", "BeanNameView") ;
        // 返回ViewName，用于查找
        return "beanNameViewBean" ;
    }

    @GetMapping("/myDataResponseEntity")
    public ResponseEntity<MyData2> myDataResponseEntity(){
        return ResponseEntity.status(HttpStatus.OK)
                .header("Test", "For Test")
                .body(getMyData()) ;
    }

    private MyData2 getMyData(){
        MyData2 myData = new MyData2();
        myData.setFirstName("yi");
        myData.setLastName("cj");
        return myData ;
    }

}
