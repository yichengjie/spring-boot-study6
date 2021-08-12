package com.yicj.study.shiro;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.util.Factory;
import org.junit.Before;
import org.junit.Test;

@Slf4j
public class HelloTest {
    private Subject currentUser ;

    @Before
    public void hello(){
        Factory<SecurityManager> factory = new IniSecurityManagerFactory("classpath:shiro.ini");
        org.apache.shiro.mgt.SecurityManager securityManager = factory.getInstance();
        SecurityUtils.setSecurityManager(securityManager);
        currentUser = SecurityUtils.getSubject();
        log.info("currentUser : {}", currentUser);
    }


    @Test
    public void session(){
        //获取session
        Session session = currentUser.getSession();
        //给session设置属性值
        session.setAttribute("someKey", "aValue");
        //获取session中的属性值
        String value = (String) session.getAttribute("someKey");
        log.info("value : {}", value);
    }

    @Test
    public void login(){
        if (!currentUser.isAuthenticated()) {
            UsernamePasswordToken token = new UsernamePasswordToken("yicj", "123");
            try {
                currentUser.login(token);
            } catch (UnknownAccountException uae) {
                log.error("There is no user with username of " + token.getPrincipal());
            } catch (IncorrectCredentialsException ice) {
                log.error("Password for account " + token.getPrincipal() + " was incorrect!");
            } catch (LockedAccountException lae) {
                log.error("The account for username " + token.getPrincipal() + " is locked.  " +
                        "Please contact your administrator to unlock it.");
            } catch (AuthenticationException ae) {
            }
            log.info("login success ...");
        }
    }


    @Test
    public void hasRole(){
        UsernamePasswordToken token = new UsernamePasswordToken("yicj", "123");
        currentUser.login(token);
        if (currentUser.hasRole("admin")) {
            log.info("May the Schwartz be with you!");
        } else {
            log.info("Hello, mere mortal.");
        }
        if (currentUser.isPermitted("lightsaber:wield")) {
            log.info("You may use a lightsaber ring.  Use it wisely.");
        } else {
            log.info("Sorry, lightsaber rings are for schwartz masters only.");
        }
    }

}
