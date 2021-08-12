package com.yicj.mvc.web.servlet;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.View;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


@Component("beanNameViewBean")
public class CustomView implements View {

    @Override
    public String getContentType() {
        return MediaType.TEXT_HTML_VALUE;
    }

    @Override
    public void render(Map<String, ?> model, HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 从Model中拿出模型数据
        Object name = model.get("name");
        //返回页面内容
        response.getWriter().append("name is " + name) ;
    }
}
