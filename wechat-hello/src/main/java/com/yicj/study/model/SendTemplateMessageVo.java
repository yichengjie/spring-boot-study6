package com.yicj.study.model;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

/**
 * {
 *     "touser": "obC9S6seDG_rYlg90eAk7Qofw5-A",
 *     "template_id": "HKvainS_QdHhR42BgpwTLwlRY4fNJusVarGWsg2k6YI",
 *     "url": "http://www.baiduxfzx.com/gyj/?bd_vid=9784367128317145609",
 *     "data": {
 *         "first": {
 *             "value": "2021/06/07",
 *             "color": "#44bd32"
 *         },
 *         "keyword1": {
 *             "value": "测试",
 *             "color": "#273c75"
 *         },
 *         "keyword2": {
 *             "value": "测试数据",
 *             "color": "#273c75"
 *         }
 *     }
 * }
 */
@Data
@Builder
public class SendTemplateMessageVo {
    private String touser ;
    private String template_id ;
    private String url ;
    private Map<String, SlotItemVo> data;
}
