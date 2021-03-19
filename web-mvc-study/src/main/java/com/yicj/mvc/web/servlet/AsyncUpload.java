package com.yicj.mvc.web.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.concurrent.CompletableFuture;

@MultipartConfig
@WebServlet(
    urlPatterns = "/asyncUpload",
    asyncSupported = true
)
public class AsyncUpload extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        AsyncContext ctx = req.startAsync();
        doAsync(ctx).thenRun(()->{
            try {
                ctx.getResponse().getWriter().println("Upload Successfully");
                ctx.complete();
            }catch (IOException e){
                throw new UncheckedIOException(e) ;
            }
        }) ;
    }

    private CompletableFuture<Void> doAsync(AsyncContext ctx) throws IOException, ServletException {
        Part photo = ((HttpServletRequest) ctx.getRequest()).getPart("photo");
        String fileName = photo.getSubmittedFileName();
        return CompletableFuture.runAsync(() ->{
            // 读取是阻断式
            try (InputStream in = photo.getInputStream();
                 OutputStream out = new FileOutputStream("d:/workspace/" + fileName)){
                byte [] buffer = new byte[1024] ;
                int length = -1 ;
                while ((length = in.read(buffer)) != -1){
                    out.write(buffer, 0 , length);
                }
            }catch (IOException e){
                throw new UncheckedIOException(e) ;
            }
        }) ;
    }
}
