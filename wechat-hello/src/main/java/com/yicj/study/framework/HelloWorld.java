package com.yicj.study.framework;

import me.chanjar.weixin.common.exception.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpInMemoryConfigStorage;
import me.chanjar.weixin.mp.api.WxMpService;
import me.chanjar.weixin.mp.api.WxMpServiceImpl;
import me.chanjar.weixin.mp.bean.WxMpCustomMessage;
import me.chanjar.weixin.mp.bean.WxMpTemplateData;
import me.chanjar.weixin.mp.bean.WxMpTemplateMessage;

public class HelloWorld {

    private static String appId = "wxca158834f336dba1" ;
    private static String appSecret ="dbcbeb51c51ba389e8e08bfef347a060" ;

    public static void main(String[] args) throws WxErrorException {
        WxMpInMemoryConfigStorage config = new WxMpInMemoryConfigStorage();
        config.setAppId(appId); // 设置微信公众号的appid
        config.setSecret(appSecret); // 设置微信公众号的app corpSecret
        //config.setToken("..."); // 设置微信公众号的token
        //config.setAesKey("..."); // 设置微信公众号的EncodingAESKey
        WxMpService wxService = new WxMpServiceImpl();
        wxService.setWxMpConfigStorage(config);

        // 用户的openid在下面地址获得
        //https://mp.weixin.qq.com/debug/cgi-bin/apiinfo?t=index&type=用户管理&form=获取关注者列表接口%20/user/get
        String openid = "obC9S6seDG_rYlg90eAk7Qofw5-A";
        //WxMpCustomMessage message = WxMpCustomMessage.TEXT().toUser(openid).content("Hello World").build();
        //wxService.customMessageSend(message);

        /*WxMpCustomMessage.WxArticle article1 = new WxMpCustomMessage.WxArticle();
        article1.setUrl("http://www.baiduxfzx.com/gyj/?bd_vid=9784367128317145609");
        article1.setPicUrl("http://www.baiduxfzx.com/gyj/pc.jpg");
        article1.setDescription("Is Really A Happy Day");
        article1.setTitle("Happy Day");

        WxMpCustomMessage message = WxMpCustomMessage.NEWS()
                .toUser(openid)
                .addArticle(article1)
                .build();*/

        //wxService.customMessageSend(templateMessage);

        //{{first.DATA}} {{keyword1.DATA}} 报名链接{{keyword2.DATA}}
        //HKvainS_QdHhR42BgpwTLwlRY4fNJusVarGWsg2k6YI

        WxMpTemplateMessage templateMessage = new WxMpTemplateMessage();
        templateMessage.setToUser(openid);
        templateMessage.setTemplateId("HKvainS_QdHhR42BgpwTLwlRY4fNJusVarGWsg2k6YI");
        templateMessage.setUrl("http://www.baiduxfzx.com/gyj/?bd_vid=9784367128317145609");
        templateMessage.setTopColor("#333");
        templateMessage.getDatas().add(new WxMpTemplateData("first", "2021/06/07"));
        templateMessage.getDatas().add(new WxMpTemplateData("keyword1", "测试"));
        templateMessage.getDatas().add(new WxMpTemplateData("keyword2", "测试内容"));
        wxService.templateSend(templateMessage);

    }
}
