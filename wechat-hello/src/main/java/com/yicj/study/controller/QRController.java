package com.yicj.study.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yicj1
 * @title: QRController
 * @description: TODO
 * @email 626659321@qq.com
 * @date 2021/6/9 14:26
 */
@RestController
public class QRController {
    //品质365
    private static String appId = "wxd99431bbff8305a0" ;
    private static String appSecret ="60f78681d063590a469f1b297feff3c4" ;

    @Autowired
    private RestTemplate restTemplate ;

    @GetMapping("/oauth2/authorization/qr")
    public void redirectLogin(HttpServletResponse response) throws IOException {
        String redirectUrl = "https://open.weixin.qq.com/connect/qrconnect?response_type=code&client_id=wxd99431bbff8305a0&scope=snsapi_login&state=PuqZ2Nz5t8FEV52UBBnFckjKKamUYgh4Z_FEK0eMQ-o%3D&redirect_uri=http://www.pinzhi365.com/qqLogin/weixin&appid=wxd99431bbff8305a0" ;
        response.sendRedirect(redirectUrl);
    }

    // 直接跳转到品质365上
    //@GetMapping("/qqLogin/weixin")
    public String fetchToken(String code){
        String tokenUri = "https://api.weixin.qq.com/sns/oauth2/access_token" ;
        String redirect_uri = "http://www.pinzhi365.com/qqLogin/weixin" ;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() ;
        params.set("appid", appId);
        params.set("secret", appSecret);
        params.set("code", code);
        params.set("redirect_uri", redirect_uri);
        params.set("grant_type", "authorization_code");
        String tmpTokenResponse = this.restTemplate.postForObject(
                tokenUri, params, String.class) ;
        // 判断用户是否已经注
        // 如果已经注册，db查询用户信息，并自动登录
        // 如果为注册，则跳转到注册页面（获取微信用户信息），注册后并登录
        // 登录成功以后跳转到对应页面
        return tmpTokenResponse ;
    }

}
