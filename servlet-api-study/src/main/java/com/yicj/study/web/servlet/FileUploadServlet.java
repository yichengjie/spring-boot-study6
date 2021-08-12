package com.yicj.study.web.servlet;

import com.yicj.study.common.FileUploadUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@WebServlet
public class FileUploadServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        byte [] content = bodyContent(req) ;
        String contentAsTxt = new String(content, "ISO-8859-1") ;
        String filename = FileUploadUtil.filename(contentAsTxt);
        FileUploadUtil.Range fileRange = FileUploadUtil.fileRange(contentAsTxt, req.getContentType()) ;
        int start = contentAsTxt.substring(0, fileRange.getStart()).getBytes("ISO-8859-1").length ;
        int end = contentAsTxt.substring(0, fileRange.getEnd()).getBytes("ISO-8859-1").length ;
        String fullFileName = String.format("d:/workspace/%s", filename);
        FileUploadUtil.write (content,start,end, fullFileName);
    }

    private byte[] bodyContent(HttpServletRequest request) throws IOException{
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()){
            InputStream in = request.getInputStream() ;
            byte [] buffer = new byte[1024] ;
            int length = -1 ;
            while ((length = in.read(buffer)) != -1){
                out.write(buffer,0, length);
            }
            return out.toByteArray() ;
        }
    }


}
