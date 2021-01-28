package com.yicj.study.mvc.controller;

import com.yicj.study.mvc.model.JsonResult;
import com.yicj.study.mvc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@Slf4j
//@RestController
//@RequestMapping("/api/users")
public class UserController {
    @PostMapping("/add2")
    public Mono<JsonResult<String>> add2(@RequestBody User user){
        log.info("user : {}", user);
        return Mono.just(JsonResult.success("success")) ;
    }
}
