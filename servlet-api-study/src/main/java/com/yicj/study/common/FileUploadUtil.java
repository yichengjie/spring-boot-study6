package com.yicj.study.common;

import lombok.Getter;
import lombok.ToString;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileUploadUtil {

    //"(.*)"
    private static Pattern fileNameRegex = Pattern.compile("filename=\"(.*)\"") ;
    //".*"\r\n.*\r\n.*\r\n\r\n(.*+)
    private static Pattern fileRangeRegex = Pattern.compile("filename=\".*\"\\r\\n.*\\r\\n\\r\\n(.*+)") ;

    public static String filename(String contentTxt) throws UnsupportedEncodingException {
        Matcher matcher = fileNameRegex.matcher(contentTxt) ;
        matcher.find() ;
        String filename = matcher.group(1) ;
        // 如果名称包含问价夹符号[\]，就只取最后的文件名
        if (filename.contains("\\")){
            return filename.substring(filename.lastIndexOf("\\") +1) ;
        }
        return filename ;
    }


    // 取得文件边界范围
    public static Range fileRange(String content, String contentType){
        Matcher matcher = fileRangeRegex.matcher(content) ;
        matcher.find() ;
        int start = matcher.start(1);
        String boundary = contentType.substring(contentType.lastIndexOf("=") + 1);
        int end = content.indexOf(boundary, start) -4 ;
        return new Range(start, end) ;
    }

    public static void write(byte [] content, int start, int end, String file) throws IOException {
        try (FileOutputStream fileOutputStream = new FileOutputStream(file)){
            fileOutputStream.write(content, start, (end- start));
        }
    }

    // 封装范围起始与结束
    @Getter
    @ToString
    public static class Range{
        final int start ;
        final int end ;
        private Range(int start, int end) {
            this.start = start;
            this.end = end;
        }
    }
}
