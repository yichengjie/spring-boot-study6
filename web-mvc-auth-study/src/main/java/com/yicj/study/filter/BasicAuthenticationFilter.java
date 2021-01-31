package com.yicj.study.filter;

import com.yicj.study.model.entity.User;
import com.yicj.study.repository.UserRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// basic认证流程
@Component
public class BasicAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private UserRepository repository ;
    @Override
    protected void doFilterInternal(HttpServletRequest request,
        HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (StringUtils.isNoneBlank(authHeader)){
            String token64 = StringUtils.substringAfter(authHeader, "Basic ");
            String token = new String(Base64Utils.decodeFromString(token64)) ;
            String [] items = StringUtils.splitByWholeSeparatorPreserveAllTokens(token, ":") ;
            String username = items[0] ;
            String password = items[1] ;
            User user = repository.findByUsername(username);
            if (user != null && StringUtils.equals(password, user.getPassword())){
                request.setAttribute("user", user);
            }
        }
        chain.doFilter(request, response);
    }
}
