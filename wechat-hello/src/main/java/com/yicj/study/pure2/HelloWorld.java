package com.yicj.study.pure2;

import com.alibaba.fastjson.JSON;
import com.yicj.study.model.SendTemplateMessageVo;
import com.yicj.study.model.SlotItemVo;

import java.util.HashMap;
import java.util.Map;

public class HelloWorld {

    private static String appId = "wxca158834f336dba1" ;
    private static String appSecret ="dbcbeb51c51ba389e8e08bfef347a060" ;
    //45_RBYK_ATviXZeDuPv8OLj_uo73JVpkyvOUY-mefWsxfnzcnzyrvYzZDcLVmO5g4sBN0IiY28-bqjCa_jy5yypPAi_muVmgpzfz70DoLDbHJa9vgU8aIsqLCSRKFTGAl2CJqUaJDOidJA0IweqWJMcAEAZMP
    public static void main(String[] args) {
        new HelloWorld().sendMessage();
        //String accessToken = new HelloWorld().getAccessToken();
        //System.out.println(accessToken);
    }


    public void sendMessage(){
        String openid ="obC9S6seDG_rYlg90eAk7Qofw5-A" ;
        String template_id = "HKvainS_QdHhR42BgpwTLwlRY4fNJusVarGWsg2k6YI" ;
        String msgUrl = "http://www.baiduxfzx.com/gyj/?bd_vid=9784367128317145609" ;
        String first = "2021/06/07" ;
        String keyword1 = "测试" ;
        String keyword2 = "测试数据" ;
        String accessToken = "45_RBYK_ATviXZeDuPv8OLj_uo73JVpkyvOUY-mefWsxfnzcnzyrvYzZDcLVmO5g4sBN0IiY28-bqjCa_jy5yypPAi_muVmgpzfz70DoLDbHJa9vgU8aIsqLCSRKFTGAl2CJqUaJDOidJA0IweqWJMcAEAZMP" ;
        //{{first.DATA}} {{keyword1.DATA}} 报名链接{{keyword2.DATA}}


        Map<String, SlotItemVo> slots = new HashMap<>() ;
        slots.put("first", SlotItemVo.builder().value(first).color("#44bd32").build()) ;
        slots.put("keyword1", SlotItemVo.builder().value(keyword1).color("#273c75").build()) ;
        slots.put("keyword2", SlotItemVo.builder().value(keyword2).color("#273c75").build()) ;

        SendTemplateMessageVo templateMessageVo = SendTemplateMessageVo.builder()
                .touser(openid)
                .template_id(template_id)
                .url(msgUrl)
                .data(slots).build();
        System.out.println(JSON.toJSON(templateMessageVo).toString());
        sendTemplateMsg(JSON.toJSON(templateMessageVo).toString(), accessToken) ;
    }

    /**
     * 发布微信模版消息
     *
     * @param json
     * @param access_token
     * @return
     */
    private String sendTemplateMsg(String json, String access_token) {
        String url = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=" + access_token;
        String responseContent = HttpClientUtil.httpPostWithJson(url, json);
        return responseContent;
    }


    public String getAccessToken(){
        String url = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s" ;
        return HttpClientUtil.httpGet(String.format(url, appId, appSecret)) ;
    }
}
