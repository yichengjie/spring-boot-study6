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
        shiroFilter.setLoginUrl("/login.html");
        shiroFilter.setSuccessUrl("/success.html");
        shiroFilter.setUnauthorizedUrl("/unauthorized.html");
        Map<String, String> map = new HashMap<String, String>();
        map.put("/postLogin","anon") ;
        map.put("/**", "authc");
        shiroFilter.setFilterChainDefinitionMap(map);
        return shiroFilter ;
    }

}
