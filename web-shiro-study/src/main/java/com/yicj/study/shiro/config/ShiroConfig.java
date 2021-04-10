package com.yicj.study.shiro.config;


import com.yicj.study.shiro.shiro.MyRealm;
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
        return new MyRealm() ;
    }
    @Bean
    public SecurityManager securityManager(){
        return new DefaultWebSecurityManager(myRealm()) ;
    }
    @Bean
    public ShiroFilterFactoryBean shiroFilterFactoryBean(){
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean() ;
        shiroFilter.setSecurityManager(securityManager());
        // 这个这个地址POST方式提交作为登录处理页面
        shiroFilter.setLoginUrl("/login");
        shiroFilter.setSuccessUrl("/success.html");
        shiroFilter.setUnauthorizedUrl("/unauthorized.html");
        Map<String, String> map = new HashMap<String, String>();
        map.put("/login","anon") ;
        map.put("/**", "authc,perms[user]");
        shiroFilter.setFilterChainDefinitionMap(map);
        return shiroFilter ;
    }
}
