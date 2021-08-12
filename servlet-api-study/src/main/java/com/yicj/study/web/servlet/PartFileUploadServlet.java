package com.yicj.study.web.servlet;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// 必须添加@MultipartConfig注解才能使用getPart()相关的api
@MultipartConfig
@WebServlet("/photo")
public class PartFileUploadServlet extends HttpServlet {

    // filename="(.*)"
    private final Pattern fileNameRegex = Pattern.compile("filename=\"(.*)\"") ;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Part photo = req.getPart("photo");
        String filename = getSubmittedFileName(photo) ;
        

    }

    private String getSubmittedFileName(Part part) {
        final String header = part.getHeader("Content-Disposition");
        final Matcher matcher = fileNameRegex.matcher(header);
        matcher.find() ;
        final String filename = matcher.group(1);
        if (filename.contains("\\")){
            return filename.substring(filename.lastIndexOf("\\") +1) ;
        }
        return filename ;
    }

    private void write(Part photo, String filename) throws IOException, FileNotFoundException {
        try (InputStream in = photo.getInputStream();
             OutputStream out = new FileOutputStream(String.format("d:/workspace/%s", filename))){
            int len = -1 ;
            byte [] buffer = new byte[1024] ;
            while ((len = in.read(buffer)) != -1){
                out.write(buffer, 0 , len);
            }
        }
    }
}
