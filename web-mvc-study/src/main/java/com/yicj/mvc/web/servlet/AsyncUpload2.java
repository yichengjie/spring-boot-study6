package com.yicj.mvc.web.servlet;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.time.Instant;
import java.util.regex.Pattern;

public class AsyncUpload2 extends HttpServlet {
    private final Pattern fileNameRegex = Pattern.compile("filename=\"(.*)\"") ;
    private final Pattern fileRangeRegex = Pattern.compile("filename=\".*\"\\r\\n.*\\r\\n\\r\\n(.*+)") ;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        final AsyncContext ctx = req.startAsync();
        final ServletInputStream in = req.getInputStream();
        in.setReadListener(new ReadListener() {
            ByteArrayOutputStream out = new ByteArrayOutputStream() ;
            @Override
            public void onDataAvailable() throws IOException {
                byte [] buffer = new byte[1024] ;
                int length = -1 ;
                while (in.isReady() && (length = in.read(buffer))  != -1){
                    out.write(buffer, 0 , length);
                }
            }

            @Override
            public void onAllDataRead() throws IOException {
                byte[] content = out.toByteArray();
                String contextAsTxt = new String(content, "ISO-8859-1") ;
                //String fileName = filename(contextAsTxt) ;
                //Range fileRange = fileRange(contextAsTxt, req.getContextPath()) ;
                //write(content, contextAsTxt.substring(0, file)) ;
                resp.getWriter().println("Upload Successfully");
                ctx.complete();
            }

            @Override
            public void onError(Throwable throwable) {
                ctx.complete();
                throw new RuntimeException(throwable) ;
            }
        });

    }

    private void write(Part part){
        String submittedFileName = part.getSubmittedFileName();
        String ext = submittedFileName.substring(submittedFileName.lastIndexOf('.'));
        try {
            part.write(String.format("%s%s", Instant.now().toEpochMilli(), ext));
        }catch (IOException e){
            throw new UncheckedIOException(e) ;
        }

    }
}
