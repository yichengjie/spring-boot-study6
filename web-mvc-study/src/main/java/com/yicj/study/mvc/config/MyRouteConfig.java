package com.yicj.study.mvc.config;

import com.yicj.study.mvc.handler.UserHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

@Configuration
public class MyRouteConfig {
    @Autowired
    private UserHandler userHandler ;

    @Bean
    public RouterFunction<ServerResponse> routerFunction(){
        return RouterFunctions
                .route(POST("/api/users/add"), userHandler::add)
                .andRoute(RequestPredicates.POST("/api/users/add2")
                        .and(RequestPredicates.accept(MediaType.APPLICATION_JSON_UTF8)), userHandler::add2);
                //.andRoute(POST("/api/users/add2").and(accept(APPLICATION_JSON)), userHandler::add2);
    }
}
