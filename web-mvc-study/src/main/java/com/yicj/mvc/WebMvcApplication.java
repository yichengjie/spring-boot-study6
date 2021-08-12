package com.yicj.mvc;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;

@ServletComponentScan("com.yicj.mvc.web.servlet")
@SpringBootApplication
public class WebMvcApplication {
    public static void main(String[] args) {

        SpringApplication.run(WebMvcApplication.class, args);
    }

   /* @Bean
    public ServletContextInitializer servletContextInitializer(){
        return servletContext ->{
            CharacterEncodingFilter filter = new CharacterEncodingFilter() ;
            FilterRegistration.Dynamic dynamic = servletContext.addFilter("c-filter", filter);
            dynamic.addMappingForUrlPatterns(EnumSet.of(DispatcherType.REQUEST), false,"/");
        } ;
    }*/

   /* @Bean
    public ServletRegistrationBean servletRegistrationBean(){
        ServletRegistrationBean registrationBean =
                new ServletRegistrationBean(new AsyncServlet(), "/async-servlet") ;
        return registrationBean ;
    }*/
}
