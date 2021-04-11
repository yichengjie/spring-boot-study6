package com.yicj.study.shiro.config;


import com.yicj.study.shiro.shiro.MyRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ShiroConfig {

    @Bean
    public MyRealm myRealm(){
        MyRealm myRealm = new MyRealm() ;
        HashedCredentialsMatcher credentialsMatcher = new HashedCredentialsMatcher("MD5") ;
        credentialsMatcher.setHashIterations(1024);
        myRealm.setCredentialsMatcher(credentialsMatcher);
        return myRealm ;
    }

    @Bean
    public SecurityManager securityManager(){
        DefaultWebSecurityManager webSecurityManager = new DefaultWebSecurityManager() ;
        webSecurityManager.setRealm(myRealm());
        return webSecurityManager ;
    }

    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean() ;
        shiroFilter.setSecurityManager(securityManager());
        // 这个这个地址POST方式提交作为登录处理页面
        shiroFilter.setLoginUrl("/doLogin");
        shiroFilter.setSuccessUrl("/index.html");
        shiroFilter.setUnauthorizedUrl("/unauthorized.html");
        Map<String, String> map = new HashMap<String, String>();
        map.put("/favicon.ico","anon") ;
        map.put("/logout","logout") ;
        map.put("/admin","authc,perms[admin]") ;
        map.put("/hello","authc,perms[user]") ;
        map.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(map);
        return shiroFilter ;
    }
}
