package com.yicj.study.mvc.handler;

import com.yicj.study.mvc.model.JsonResult;
import com.yicj.study.mvc.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.function.Consumer;
import java.util.function.Function;


@Slf4j
@Component
public class UserHandler {

    public Mono<ServerResponse> add(ServerRequest request) {
        Mono<MultiValueMap<String, String>> formData = request.formData() ;
        return formData.flatMap(multiValueMap -> {
            String username = multiValueMap.getFirst("username");
            String addr = multiValueMap.getFirst("addr") ;
            log.info("=====> username : {}， addr: {}", username, addr);
            return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(
                    Mono.just(JsonResult.success("success")),JsonResult.class
            );
        }) ;
    }

    public Mono<ServerResponse> add2(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);
        log.info("add2 method execute ...");
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(userMono.map(user -> {
            String retContent = doBusi(user);
            return JsonResult.success(retContent);
        }),JsonResult.class);
    }

    //https://blog.csdn.net/zhangjun62/article/details/91967491
    // 下面这种写法有问题，不执行业务代码
    // 这里一直无法获取到参数，所以改为使用Controller形式，可以正常获取
    public Mono<ServerResponse> add3(ServerRequest request) {
        Mono<User> userMono = request.bodyToMono(User.class);
        log.info("add3 method execute ...");
        userMono.map(user -> {
            String retContent = doBusi(user);
            return JsonResult.success(retContent);
        }).subscribe(info->{
            log.info("====> {}", info);
        });
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(Mono.just(JsonResult.success("success")),JsonResult.class);
    }

    private String doBusi(User user){
        log.info("username : {}, addr : {}", user.getUsername(), user.getAddr());
        log.info("do busi .....");
        sleep(1000);
        return "Hello world" ;
    }

    private void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
