package com.yicj.web.security.config.annotation.web.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

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
            .and()
            .httpBasic()
            .and()
            .sessionManagement()
                .maximumSessions(1)
        ;
    }
}
