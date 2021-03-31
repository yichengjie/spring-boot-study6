package com.yicj.web.security.config.annotation.web.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Configuration
public class MyWebSecurityConfigurer extends WebSecurityConfigurerAdapter {
    @Bean
    public PasswordEncoder passwordEncoder(){
        log.info("===> init passwordEncoder !");
        return new BCryptPasswordEncoder() ;
    }
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        InMemoryUserDetailsManagerConfigurer<AuthenticationManagerBuilder> userConfig =
                auth.inMemoryAuthentication().passwordEncoder(passwordEncoder());
        userConfig.withUser("admin")
                .password(passwordEncoder().encode("123"))
                .roles("USER","ADMIN");
        userConfig.withUser("user")
                .password(passwordEncoder().encode("123"))
                .authorities("ROLE_USER");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .anyRequest()
                .authenticated()
            .and()
            .formLogin()
                //.successHandler(successHandler())
                .defaultSuccessUrl("/success.html")
            .and()
            .httpBasic()
            //.and()
            //.userDetailsService()
            .and()
            .sessionManagement()
                .maximumSessions(1)
        ;
    }

//    private SavedRequestAwareAuthenticationSuccessHandler successHandler(){
//        SavedRequestAwareAuthenticationSuccessHandler handler = new SavedRequestAwareAuthenticationSuccessHandler() ;
//        handler.setDefaultTargetUrl("/index");
//        handler.setTargetUrlParameter("target");
//        return handler ;
//    }

}
