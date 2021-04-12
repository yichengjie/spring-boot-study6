package com.yicj.study.mvc.controller;

import com.yicj.study.mvc.model.MyData;
import com.yicj.study.mvc.model.User;
import com.yicj.study.mvc.servlet.mvc.annotation.ResultEnhancerTag;
import com.yicj.study.mvc.exception.AppException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class HelloController {

    @GetMapping("/hello")
    public Map<String, Object> hello(){
        Map<String, Object> map = new HashMap<>() ;
        map.put("name", "Yicj") ;
        return map ;
    }

    @GetMapping("/hello2")
    @ModelAttribute("data")
    public MyData hello2(){
        return getMyData() ;
    }


    @ResponseBody
    @GetMapping("/hello3")
    @ResultEnhancerTag(success = "删除用户信息成功")
    public boolean hello3(){
        return true;
    }

    @ResponseBody
    @GetMapping("/hello4")
    @ResultEnhancerTag(success = "删除用户信息成功",error = "删除用户信息失败")
    public boolean hello4(){
        return false ;
    }

    @ResponseBody
    @GetMapping("/hello5")
    public Boolean hello5(){
        return false ;
    }



    @ResponseBody
    @GetMapping("/hello6")
    public boolean hello6(){
        boolean flag = false ;
        if (!flag){
            throw new AppException("数据库操作出错") ;
        }
        return true ;
    }


    @ResponseBody
    @GetMapping("/hello7")
    public boolean hello7(){
        int a = 1/0 ;
        return true ;
    }

    @ResponseBody
    @GetMapping(value = "/hello8")
    @ResultEnhancerTag(success = "删除用户信息成功",error = "删除用户信息失败")
    public String hello8(){
        return "hello world" ;
    }

    @ResponseBody
    @GetMapping("/converter")
    @ResultEnhancerTag(success = "删除用户信息成功")
    public User getUserByConverter(){
        User user = new User() ;
        user.setUserName("yicj");
        user.setNote("test");
        user.setId(1001L);
        return user ;
    }

    @ResponseBody
    @GetMapping("/converter2")
    @ResultEnhancerTag(success = "删除用户信息成功")
    public List<User> getUserByConverter2(){
        User user = new User() ;
        user.setUserName("yicj");
        user.setNote("test");
        user.setId(1001L);
        //
        User user2 = new User() ;
        user2.setUserName("yicj2");
        user2.setNote("test2");
        user2.setId(1003L);
        return Arrays.asList(user, user2);
    }


    @ResponseBody
    @PostMapping("/user")
    public User getUser(User user){
        return user ;
    }

    private MyData getMyData(){
        return MyData.builder().firstName("yi").lastName("cj").build() ;
    }
}
