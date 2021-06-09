package com.yicj.study.controller;

import com.yicj.study.model.result.AccessToken;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @author yicj1
 * @title: WechatController
 * @description: TODO
 * @email yicj1@lenovo.com
 * @date 2021/6/9 16:43
 */
@Data
@RestController
public class WechatController {
    @Value("${wechat.app-id}")
    private String appId ;
    @Value("${wechat.app-secret}")
    private String appSecret ;
    @Autowired
    private RestTemplate restTemplate ;

    @GetMapping("/fetchAccessToken")
    public AccessToken fetchAccessToken(HttpServletResponse response){
        //https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={APPID}&secret={APPSECRET}" ;
        Map<String, String> param = new HashMap<>() ;
        param.put("APPID", appId) ;
        param.put("APPSECRET", appSecret) ;
        AccessToken accessToken =
                restTemplate.getForObject(String.format(url, appId, appSecret), AccessToken.class, param);
        response.addHeader("x-token", accessToken.getAccess_token());
        return accessToken ;
    }

}
