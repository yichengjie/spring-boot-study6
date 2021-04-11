package com.yicj.study.shiro.shiro;

import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;

public class MyRealm extends AuthorizingRealm {
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
        simpleAuthorizationInfo.addStringPermission("user");
        return simpleAuthorizationInfo;
    }
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = token.getPrincipal().toString();
        if ("yicj".equalsIgnoreCase(username)){
            //1. 根据用户名查询数据库，获取用户信息
            //2. 根据数据库中查询到得用户信息，构建AuthenticationInfo信息并返回
            return new SimpleAuthenticationInfo("yicj", "9c9e21dfa4b664cfebc32093cb3555bb",ByteSource.Util.bytes("yicj"), getName());
        }
        return null ;
    }
}