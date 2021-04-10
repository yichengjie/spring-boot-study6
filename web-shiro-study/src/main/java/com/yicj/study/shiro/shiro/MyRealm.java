package com.yicj.study.shiro.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;
import org.springframework.stereotype.Component;

public class MyRealm implements Realm {
    public String getName() {
        return "MyRealm";
    }
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String password = new String(((char[]) token.getCredentials()));
        String username = token.getPrincipal().toString();
        if (!"yicj".equals(username)) {
            throw new UnknownAccountException("用户不存在");
        }
        if (!"123".equals(password)) {
            throw new IncorrectCredentialsException("密码不正确");
        }
        return new SimpleAuthenticationInfo(username, password, getName());
    }

}