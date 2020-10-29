package com.yicj.study.jpa.service;

import com.yicj.study.jpa.entity.User;
import org.springframework.data.domain.Sort;

import java.util.List;

public interface UserService {
    Integer addUser(User user) ;

    List<User> findAll(Sort sort) ;

    List<User> getAllUser(int page, int size) ;

    List<User> findByName(String name) ;
}
