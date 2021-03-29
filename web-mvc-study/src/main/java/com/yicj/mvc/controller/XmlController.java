package com.yicj.mvc.controller;

import lombok.Data;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.xml.bind.annotation.XmlRootElement;

@RestController
public class XmlController {

    @GetMapping(value = "/xml")
    public UserInfo xml(){
        return UserInfo.builder().username("yicj").addr("bjs").build() ;
    }

    @Data
    @XmlRootElement
    static class UserInfo{
        private String username ;
        private String addr ;
        public UserInfo(String username, String addr){
            this.username = username ;
            this.addr = addr ;
        }

        public static UserInfoBuilder builder(){
            return new UserInfoBuilder() ;
        }

        static class UserInfoBuilder{
            private String username ;
            private String addr ;


            public UserInfoBuilder username(String username){
                this.username = username ;
                return this ;
            }
            public UserInfoBuilder addr(String addr){
                this.addr = addr ;
                return this ;
            }
            public UserInfo build(){
                return new UserInfo(this.username, this.addr) ;
            }
        }
    }



}
