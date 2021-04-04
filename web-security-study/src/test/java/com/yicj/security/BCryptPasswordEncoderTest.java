package com.yicj.security;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Slf4j
public class BCryptPasswordEncoderTest {

    @Test
    public void encoder(){

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder() ;

        final String content = encoder.encode("Abcd1234");

        log.info("content : {}", content);

        final boolean matches = encoder.matches("Abcd1234", "$2a$10$Wis1GgZNGApKc./nm/BdG.QGQyulO1CPkAAB62bzpP2Lyhgx4QQXu");

        log.info("matches : {}", matches);
    }
}
