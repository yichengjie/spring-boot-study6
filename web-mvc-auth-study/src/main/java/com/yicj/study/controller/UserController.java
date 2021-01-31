package com.yicj.study.controller;

import com.yicj.study.model.entity.User;
import com.yicj.study.repository.UserRepository;
import com.yicj.study.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserRepository repository ;
    @Autowired
    private UserService service ;

    @PostMapping
    public User addUser(@RequestBody @Validated User user){
        return repository.save(user) ;
    }

    @PostMapping("/login")
    public void login(String username, String password, HttpSession session){
        User user = service.login(username, password);
        session.setAttribute("user", user);
    }
}
