package com.yicj.study.mvc.controller;

import com.yicj.study.mvc.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ConvertController {

    @ResponseBody
    @GetMapping("/convert/stringToUserConverter")
    public User stringToUserConverter(User user){
        return user ;
    }
}
