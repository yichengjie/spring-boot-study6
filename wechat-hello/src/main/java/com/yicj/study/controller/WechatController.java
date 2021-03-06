package com.yicj.study.controller;

import com.yicj.study.model.SendTemplateMessageVo;
import com.yicj.study.model.SlotItemVo;
import com.yicj.study.model.result.AccessToken;
import com.yicj.study.model.result.TemplateInfoList;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
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
 * @email 626659321@qq.com
 * @date 2021/6/9 16:43
 */
@Data
@RestController
public class WechatController {
    @Value("${wechat.app-id}")
    private String appId ;
    @Value("${wechat.app-secret}")
    private String appSecret ;
    @Value("${wechat.redirect-uri}")
    private String redirectUri ;

    @Autowired
    private RestTemplate restTemplate ;

    @GetMapping("/wechat/check")
    public String check(String signature, String timestamp, String nonce, String echostr){
        // 校验echostr合法性
        return echostr ;
    }

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
        String encodeCallbackUrl = URLEncoder.encode(redirectUri, "UTF-8") ;
        String url = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=%s&redirect_uri=%s&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect" ;
        response.sendRedirect(String.format(url, appId, encodeCallbackUrl));
    }

    /**
     * 微信重定向回来接口，通过code获取用户信息
     * @param code
     * @return
     */
    @GetMapping("/wechatLogin")
    public String wechatLogin(String code){
        String tokenUri = "https://api.weixin.qq.com/sns/oauth2/access_token" ;
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>() ;
        params.set("appid", appId);
        params.set("secret", appSecret);
        params.set("code", code);
        params.set("redirect_uri", redirectUri);
        params.set("grant_type", "authorization_code");
        String tmpTokenResponse = this.restTemplate.postForObject(
                tokenUri, params, String.class) ;
        // 判断用户是否已经注
        // 如果已经注册，db查询用户信息，并自动登录
        // 如果为注册，则跳转到注册页面（获取微信用户信息），注册后并登录
        // 登录成功以后跳转到对应页面
        return tmpTokenResponse ;
    }


    @GetMapping("/sendTemplateMessage")
    public String sendTemplateMessage(String accessToken){
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token={ACCESS_TOKEN}";
        String openid ="obC9S6seDG_rYlg90eAk7Qofw5-A" ;
        String template_id = "HKvainS_QdHhR42BgpwTLwlRY4fNJusVarGWsg2k6YI" ;
        String msgUrl = "http://www.baiduxfzx.com/gyj/?bd_vid=9784367128317145609" ;
        String first = "hello world" ;
        String keyword1 = "测试" ;
        String keyword2 = "测试数据" ;
        //{{first.DATA}} {{keyword1.DATA}} 报名链接{{keyword2.DATA}}
        Map<String, SlotItemVo> slots = new HashMap<>() ;
        slots.put("first", SlotItemVo.builder().value(first).color("#44bd32").build()) ;
        slots.put("keyword1", SlotItemVo.builder().value(keyword1).color("#273c75").build()) ;
        slots.put("keyword2", SlotItemVo.builder().value(keyword2).color("#273c75").build()) ;

        SendTemplateMessageVo templateMessageVo = SendTemplateMessageVo.builder()
                //.touser(Arrays.asList(openid))
                .touser(openid)
                .template_id(template_id)
                .url(msgUrl)
                .data(slots).build();
        Map<String, String> param = new HashMap<>() ;
        param.put("ACCESS_TOKEN", accessToken) ;
        //1. 中文乱码设置requestHeader的Content-Type为UTF-8
        //HttpHeaders headers = new HttpHeaders();
        //MediaType type = MediaType.parseMediaType("application/x-www-form-urlencoded; charset=UTF-8");
        //headers.setContentType(type);
        //HttpEntity<String> requestEntity = new HttpEntity<>(JSON.toJSON(templateMessageVo).toString(),headers);
        //2. 设置RestTemplate中的StringHttpMessageConverter的默认编码为UTF-8即可
        return restTemplate.postForObject(url, templateMessageVo, String.class, param);
    }

    // 获取模板id（目前报错提示）
    @PostMapping("/fetchTemplateId")
    public String fetchTemplateId(String accessToken){
        String templateUrl = "https://api.weixin.qq.com/cgi-bin/template/api_add_template?access_token={ACCESS_TOKEN}" ;
        Map<String, String> param = new HashMap<>() ;
        param.put("ACCESS_TOKEN", accessToken) ;
        return restTemplate.postForObject(templateUrl, "{\"template_id_short\":\"TM00015\"}",String.class, param) ;
    }

    // 获取模板列表
    //GET https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token=ACCESS_TOKEN
    @GetMapping("/fetchTemplateList")
    public TemplateInfoList fetchTemplateList(String accessToken){
        String templateUrl = "https://api.weixin.qq.com/cgi-bin/template/get_all_private_template?access_token={ACCESS_TOKEN}" ;
        Map<String, String> param = new HashMap<>() ;
        param.put("ACCESS_TOKEN", accessToken) ;
        return restTemplate.getForObject(templateUrl, TemplateInfoList.class, param) ;
    }
}
