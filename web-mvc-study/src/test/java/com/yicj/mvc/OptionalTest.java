package com.yicj.mvc;

import com.yicj.mvc.model.User;
import org.junit.Test;

import java.util.Optional;

public class OptionalTest {

    @Test
    public void ofNullable(){
        User user = new User() ;
        user.setUsername("yic<j>");
        String retContent = Optional.ofNullable(user)
                .map(User::getUsername)
                .map(name -> name.replaceAll("<", "&lt;"))
                .map(name -> name.replaceAll(">", "&gt;"))
                .orElse("Guest");
        System.out.println(retContent);
    }
}
