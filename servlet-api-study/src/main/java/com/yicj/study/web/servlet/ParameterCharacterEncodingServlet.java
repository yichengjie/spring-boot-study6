package com.yicj.study.web.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
//https://blog.csdn.net/weixin_33738555/article/details/85438251
@Slf4j
@WebServlet("/postBody")
public class ParameterCharacterEncodingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //String method = req.getParameter("_method");
        //log.info("method name : {}", method);
        final PrintWriter out = resp.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<body>");
        String bodyContent = bodyContent(req.getReader());
        log.info("=====> {}", bodyContent);
        out.println(bodyContent);
        out.println("</body>");
        out.println("</html>");
    }

    private String bodyContent(BufferedReader reader) throws IOException {
        StringBuilder builder = new StringBuilder() ;
        String input = null ;
        while ((input = reader.readLine()) != null){
            builder.append(input) ;
            builder.append("\r\n") ;
        }
        return builder.toString();
    }
}
