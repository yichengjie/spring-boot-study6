package com.yicj.study.oauth.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                //.antMatchers("/oauth2/authorization/github").permitAll()
                .anyRequest().authenticated()
                //.and()
                //.formLogin()
                /*.and()
                .exceptionHandling()
                    .authenticationEntryPoint( (request, resp, authException) -> {
                        resp.setStatus(HttpStatus.UNAUTHORIZED.value());
                        resp.getWriter().write("please login");
                    })
                    .accessDeniedHandler( (req, resp, ex) -> {
                        resp.setStatus(HttpStatus.FORBIDDEN.value());
                        resp.getWriter().write("forbidden");
                    })*/
                .and()
                //.oauth2Login() ;
                .httpBasic()
                .and()
                .csrf().disable();
    }
}
