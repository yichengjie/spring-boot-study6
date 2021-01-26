package com.yicj.study.mvc.handler;

import com.yicj.study.mvc.model.JsonResult;
import com.yicj.study.mvc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;


@Slf4j
@Component
public class UserHandler {

    public Mono<ServerResponse> add(ServerRequest request) {
        String username = request.queryParam("username").orElse("noname");
        String addr = request.queryParam("addr").orElse("bjs");
        log.info("username : {}, addr : {}", username, addr);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
            Mono.just(JsonResult.success("success")),JsonResult.class
        );
    }

    //https://blog.csdn.net/zhangjun62/article/details/91967491
    // 这里一直无法获取到参数，所以改为使用Controller形式，可以正常获取
    public Mono<ServerResponse> add2(ServerRequest request) {
        Mono<User> body = request.bodyToMono(User.class);
        body.subscribe(user -> {
            log.info("===> user : {}", user);
        }) ;
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        log.info("body : {}", body);
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                Mono.just(JsonResult.success("success")),JsonResult.class
        );
    }
}
