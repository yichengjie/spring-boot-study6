package com.yicj.study.jpa.service;

import com.yicj.study.jpa.JpaBasicApiApplication;
import com.yicj.study.jpa.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.List;

@Slf4j
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

    @Test
    public void findAll(){
        Sort sort = Sort.by("id") ;
        List<User> users = userService.findAll(sort);
        log.info("users : {}", users);
    }

    @Test
    public void getAllUser(){
        int page = 1 ;
        int size = 10;
        List<User> allUser = userService.getAllUser(page, size);
        log.info("all users : {}", allUser);
    }

    @Test
    public void findByName(){
        String name = "1yicj" ;
        List<User> list = userService.findByName(name);
        log.info("list : {}", list);
    }
}
