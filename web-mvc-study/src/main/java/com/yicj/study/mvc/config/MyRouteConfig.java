package com.yicj.study.mvc.config;

import com.yicj.study.mvc.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class MyRouteConfig {
    @Autowired
    private UserHandler userHandler ;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(){
        return RouterFunctions
                .route(POST("/api/users/add"), userHandler::add) ;
                //.andRoute(POST("/api/users/add2").and(accept(APPLICATION_JSON)), userHandler::add2);
    }
}
