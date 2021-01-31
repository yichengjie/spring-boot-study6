package com.yicj.study.controller;

import com.yicj.study.model.entity.User;
import com.yicj.study.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository repository ;

    @PostMapping
    public User addUser(@RequestBody @Validated User user){
        return repository.save(user) ;
    }
}
