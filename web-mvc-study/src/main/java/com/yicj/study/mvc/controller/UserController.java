package com.yicj.study.mvc.controller;

import com.yicj.study.mvc.model.JsonResult;
import com.yicj.study.mvc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
public class UserController {



    @PostMapping("/add")
    public JsonResult<String> add(@RequestParam("username") String username, @RequestParam("addr") String addr){
        log.info("=====>  username : {}, addr : {}", username, addr);
        return JsonResult.success("success");
    }

    @PostMapping("/add2")
    public JsonResult add2(User user){
        log.info("=====> {}", user);
        return JsonResult.success("success");
    }

    @PostMapping("/add3")
    public JsonResult add3(@RequestBody User user){
        log.info("=====> {}", user);
        return JsonResult.success("success");
    }

}
