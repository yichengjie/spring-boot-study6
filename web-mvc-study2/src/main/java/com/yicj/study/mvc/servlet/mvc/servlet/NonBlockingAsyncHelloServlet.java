package com.yicj.study.mvc.servlet.mvc.servlet;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.*;

@Slf4j
@WebServlet(value = "/nonBlockingThreadPoolAsync", asyncSupported = true)
public class NonBlockingAsyncHelloServlet extends HttpServlet {



    private static ThreadPoolExecutor executor = new ThreadPoolExecutor(
            100, 200, 50000L, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(100));

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        AsyncContext asyncContext = request.startAsync();
        ServletInputStream inputStream = request.getInputStream();
        inputStream.setReadListener(new ReadListener() {
            @Override
            public void onDataAvailable() throws IOException {
                log.info("on Data Available ...");
            }
            @Override
            public void onAllDataRead() throws IOException {
                log.info("on All Data Read ...");
                executor.execute(() -> {
                    new LongRunningProcess().run();
                    try {
                        asyncContext.getResponse().getWriter().write("Hello World!");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    asyncContext.complete();
                });
            }
            @Override
            public void onError(Throwable t) {
                asyncContext.complete();
            }
        });
    }

    class LongRunningProcess implements Runnable{
        @Override
        public void run() {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}