package com.yicj.mvc.web.servlet;

import com.yicj.mvc.common.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Slf4j
@WebServlet(asyncSupported = true, //激活异步特性
        name = "asyncServlet", // Servlet名称
        urlPatterns = "/asyncServlet")
public class AsyncServlet extends HttpServlet {
    private final Random random = new Random(59) ;

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.setCharacterEncoding("UTF-8");
        resp.setContentType(getServletContext().getInitParameter("content"));
        //进入异步模式,调用业务处理线程进行业务处理
        //Servlet不会被阻塞,而是直接往下执行
        //业务处理完成后的回应由AsyncContext管理
        AsyncContext asyncContext = req.startAsync();
        asyncContext.setTimeout(900000000);
        asyncContext.addListener(new AsyncListener() {
            @Override
            public void onComplete(AsyncEvent event) throws IOException {
                String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm:ss"));
                ServletResponse response = asyncContext.getResponse();
                response.setContentType("text/plain;charset=UTF-8");
                //获取字符流输出流
                try {
                    PrintWriter writer = response.getWriter();
                    writer.println("Hello, world " + format);
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onTimeout(AsyncEvent event) throws IOException {
                log.info("执行超时...");
               /*HttpServletResponse suppliedResponse = (HttpServletResponse)event.getSuppliedResponse();
               suppliedResponse.setStatus(503);
               suppliedResponse.getWriter().write("503 error ");
               suppliedResponse.getWriter().flush();*/
            }
            @Override
            public void onError(AsyncEvent event) throws IOException {
                log.info("执行出错...");
            }

            @Override
            public void onStartAsync(AsyncEvent event) throws IOException {
                log.info("开始执行异步...");
            }
        });

        int time= random.nextInt(100) % 2 == 0 ? 1000 : 300 ;
        // 模拟等待时间，rpc或db查询
        CommonUtils.sleep(time,()->{

            asyncContext.complete() ;
        });
    }
}
