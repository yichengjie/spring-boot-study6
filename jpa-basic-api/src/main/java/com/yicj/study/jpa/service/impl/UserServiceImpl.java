package com.yicj.study.jpa.service.impl;

import com.yicj.study.jpa.entity.User;
import com.yicj.study.jpa.repository.UserRepository;
import com.yicj.study.jpa.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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

    @Override
    public List<User> findAll(Sort sort) {
        return userRepository.findAll(sort);
    }

    @Override
    public List<User> getAllUser(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size) ;
        Page<User> pageObject = userRepository.findAll(pageable);
        int totalPages = pageObject.getTotalPages();
        long totalElements = pageObject.getTotalElements();
        log.info("totalPages :{}", totalPages);
        log.info("totalElements : {}", totalElements);
        return pageObject.getContent();
    }

    @Override
    public List<User> findByName(String name) {
        return userRepository.findByName(name);
    }
}
