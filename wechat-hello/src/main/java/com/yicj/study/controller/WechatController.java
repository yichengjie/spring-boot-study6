package com.yicj.study.controller;

import com.yicj.study.model.result.AccessToken;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
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

    // 获取AccessToken接口
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


    /**
     * 重定向到网页授权地址
     * @param response
     * @throws IOException
     */
    @GetMapping("/oauth2/authorization/wechat")
    public void redirectLogin(HttpServletResponse response) throws IOException {
        String callbackUrl = "https://www.yicj1.com/wechatLogin" ;
        String encodeCallbackUrl = URLEncoder.encode(callbackUrl, "UTF-8") ;
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect" ;
        response.sendRedirect(String.format(url, appId, encodeCallbackUrl));
    }

    /**
     * {
     *     "access_token": "45_hy_Cw5vPRFoWd6ra0cvLYNdN7Skto5_zhgl9FFQR51muPULaFAsHqIPgoRiPn62AI5ciyY10LasTPlqvyqoOdw",
     *     "expires_in": 7200,
     *     "refresh_token": "45_IAiui3UXfJm6Qwb6aWNaj4pTXn66CcGPA800Lro9vkmxweB3-RlRDqnaMyndALXlUFJrrpwngiJLzfkt3y8CIw",
     *     "openid": "obC9S6seDG_rYlg90eAk7Qofw5-A",
     *     "scope": "snsapi_userinfo"
     * }
     */
    @GetMapping("/wechatLogin")
    public String wechatLogin(String code){
        String tokenUri = "https://api.weixin.qq.com/sns/oauth2/access_token" ;
        String redirect_uri = "https://www.yicj1.com/wechatLogin" ;
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
