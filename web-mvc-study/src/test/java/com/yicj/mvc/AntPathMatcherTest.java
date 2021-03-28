package com.yicj.mvc;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.util.AntPathMatcher;

public class AntPathMatcherTest {
    private AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Test
    public void isPattern(){
        Assert.assertFalse(antPathMatcher.isPattern("/user/001")) ;// 返回 false
        Assert.assertTrue(antPathMatcher.isPattern("/user/*")); // 返回 true
    }

    @Test
    public void match(){
        Assert.assertTrue(antPathMatcher.match("/user/001","/user/001"));// 返回 true
        Assert.assertTrue(antPathMatcher.match("/user/*","/user/001")) ;// 返回 true
    }

    @Test
    public void matchStart(){
        Assert.assertTrue(antPathMatcher.matchStart("/user/*","/user/001")); // 返回 true
        Assert.assertTrue(antPathMatcher.matchStart("/user/*","/user")); // 返回 true
        Assert.assertFalse(antPathMatcher.matchStart("/user/*","/user001")); // 返回 false
    }

    @Test
    public void other(){
        Assert.assertEquals("profile.html",antPathMatcher.extractPathWithinPattern("uc/profile*","uc/profile.html")); // 返回 profile.html
        Assert.assertEquals("uc/profile.html",antPathMatcher.combine("uc/*.html","uc/profile.html")); // uc/profile.html
    }
}
