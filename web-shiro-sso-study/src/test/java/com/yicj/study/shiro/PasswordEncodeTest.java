package com.yicj.study.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.crypto.hash.Sha512Hash;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Test;

@Slf4j
public class PasswordEncodeTest {

    @Test
    public void md5(){
        Md5Hash md5Hash = new Md5Hash("123", null, 1024);
        log.info("md5 hash : {}", md5Hash);
    }

    @Test
    public void sha512(){
        Sha512Hash sha512Hash = new Sha512Hash("123", null, 1024);
        log.info("sha512 hash : {}", sha512Hash);
    }

    @Test
    public void simHash(){
        SimpleHash md5 = new SimpleHash("md5", "123", null, 1024);
        SimpleHash sha512 = new SimpleHash("sha-512", "123", null, 1024);
        log.info("md5 : {}", md5);
        log.info("sha512 : {}", sha512);
    }


    @Test
    public void salt(){
        Md5Hash md5Hash = new Md5Hash("123", "yicj", 1024);
        Sha512Hash sha512Hash = new Sha512Hash("123", "sang", 1024);
        SimpleHash md5 = new SimpleHash("md5", "123", "sang", 1024);
        SimpleHash sha512 = new SimpleHash("sha-512", "123", "sang", 1024) ;
        log.info("md5Hash : {}", md5Hash);
    }

}
