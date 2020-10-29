package com.yicj.study.jpa.service;

import com.yicj.study.jpa.JpaBasicApiApplication;
import com.yicj.study.jpa.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = JpaBasicApiApplication.class)
public class UserServiceTest {

    @Autowired
    private UserService userService ;

    @Test
    public void addUser(){
        User user = new User() ;
        user.setName("yicj");
        user.setCreateTime(new Date());
        userService.addUser(user) ;
    }
}
