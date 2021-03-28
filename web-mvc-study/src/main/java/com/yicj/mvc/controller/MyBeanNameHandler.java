package com.yicj.mvc.controller;


import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestHandler;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component("/myBeanNameHandler")
public class MyBeanNameHandler implements HttpRequestHandler {
    @Override
    public void handleRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset:UTF-8");
        // {"msg":"success","status":200}
        response.getWriter().println("{\"msg\":\"success\",\"status\":200}");
        response.getWriter().flush();
    }
   /* @Override
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ModelAndView mav = new ModelAndView("bname");
        mav.addObject("now", LocalDateTime.now().toString());
        String name = request.getParameter("name");
        mav.addObject("name", name == null ? "你是?" : name);
        return mav;
    }*/
}
