package com.yicj.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Slf4j
public class PasswordEncoderFactoriesTest {

    @Test
    public void encoder(){
        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        String content = passwordEncoder.encode("Abcd1234");
        log.info("content : {}", content);
    }
}
