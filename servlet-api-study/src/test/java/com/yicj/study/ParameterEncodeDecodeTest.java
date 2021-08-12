package com.yicj.study;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

@Slf4j
public class ParameterEncodeDecodeTest {

    @Test
    public void test1() throws UnsupportedEncodingException {
        String info = "%E5%BC%A0%E4%B8%89" ;
        byte[] bytes = info.getBytes("ISO-8859-1");
        String content = new String(bytes, "UTF-8");
        log.info("content : {}", content);//%E5%BC%A0%E4%B8%89
    }

    @Test
    public void test2() throws UnsupportedEncodingException {
        String info = "%E5%BC%A0%E4%B8%89" ;
        String decode = URLDecoder.decode(info, "ISO-8859-1");
        log.info("====>  {}", decode);
        String content = new String(decode.getBytes("ISO-8859-1"), "UTF-8") ;
        log.info("content : {}", content);//张三
    }

    @Test
    public void test21() throws UnsupportedEncodingException {
        String info = "%E5%BC%A0%E4%B8%89" ;// UTF-8
        String decode = URLDecoder.decode(info, "GBK");
        log.info("====>  {}", decode);
        String content = new String(decode.getBytes("GBK"), "UTF-8") ;
        log.info("content : {}", content);//张三
    }

    @Test
    public void test3() throws UnsupportedEncodingException {
        String info = "%E5%BC%A0%E4%B8%89" ;
        byte[] bytes = info.getBytes("ISO-8859-1");
        String content = new String(bytes, "UTF-8");
        log.info("content : {}", content);//%E5%BC%A0%E4%B8%89
    }

    @Test
    public void encode() throws UnsupportedEncodingException {
        String info = "张三" ;
        String decode = URLEncoder.encode(info, "UTF-8");
        System.out.println(decode);
    }


    @Test
    public void decode() throws UnsupportedEncodingException {
        String info = "%E5%BC%A0%E4%B8%89" ;
        String decode = URLDecoder.decode(info, "UTF-8");
        System.out.println(decode);//张三
    }

    @Test
    public void test4() throws Exception {
        String info = "测试中文乱码问题" ;
        byte[] bytes = info.getBytes("ISO-8859-1");
        log.info("content : {}", new String(bytes, "ISO-8859-1"));
    }

    @Test
    public void test5() throws Exception {
        String info = "测试中文乱码问题" ;
        String charset = "ISO-8859-1" ;
        charset = "UTF-8" ;
        String encode = URLEncoder.encode(info, charset);
        String decode = URLDecoder.decode(encode, charset);
        log.info("content : {}", decode);
    }

    @Test
    public void test6() throws UnsupportedEncodingException {
        String info = "中" ;
        String charset = "ISO-8859-1" ;
        charset = "UTF-8" ;
        byte[] bytes = info.getBytes(charset);

        log.info("len : {}", bytes.length);
    }
}
