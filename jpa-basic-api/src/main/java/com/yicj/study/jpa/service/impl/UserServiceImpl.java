package com.yicj.study.jpa.service.impl;

import com.yicj.study.jpa.entity.User;
import com.yicj.study.jpa.repository.UserRepository;
import com.yicj.study.jpa.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository ;

    @Override
    public Integer addUser(User user) {
        userRepository.save(user) ;
        Integer id = user.getId();
        user.setName("1" + user.getName());
        userRepository.save(user) ;
        return id ;
    }
}
