package com.yicj.study.filter;

import org.springframework.util.Assert;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yicj1
 * @title: OAuth2AuthorizationRequestRedirectFilter
 * @description: TODO
 * @email yicj1@lenovo.com
 * @date 2021/6/8 16:43
 */
public class OAuth2AuthorizationRequestRedirectFilter extends OncePerRequestFilter {
    public static final String DEFAULT_AUTHORIZATION_REQUEST_BASE_URI = "/oauth2/authorization";

        @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


    }
}
