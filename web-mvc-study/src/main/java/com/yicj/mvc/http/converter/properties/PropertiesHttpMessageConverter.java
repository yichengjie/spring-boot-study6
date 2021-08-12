package com.yicj.mvc.http.converter.properties;

import org.springframework.http.*;
import org.springframework.http.converter.AbstractGenericHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;

import java.io.*;
import java.lang.reflect.Type;
import java.nio.charset.Charset;
import java.util.Properties;

// 参考MappingJackson2HttpMessageConverter
public class PropertiesHttpMessageConverter extends AbstractGenericHttpMessageConverter<Properties> {

    public PropertiesHttpMessageConverter(){
        super(new MediaType("text", "properties"));
    }

    @Override
    protected void writeInternal(Properties properties, Type type, HttpOutputMessage outputMessage) throws IOException, HttpMessageNotWritableException {
        OutputStream body = outputMessage.getBody();
        Charset charset = this.getCharset(outputMessage) ;
        Writer writer = new OutputStreamWriter(body,charset) ;
        // 这里要使用字符流，以防止中文乱码
        properties.store(writer,"form PropertiesHttpMessageConverter");
    }

    @Override
    protected Properties readInternal(Class<? extends Properties> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        InputStream body = inputMessage.getBody();
        Properties properties = new Properties() ;
        // 从Content-Type解析编码
        Charset charset = getCharset(inputMessage) ;
        InputStreamReader reader = new InputStreamReader(body, charset) ;
        // 注意这里要使用字符流，否则会有中文乱码
        properties.load(reader);
        return properties;
    }


    private Charset getCharset(HttpMessage inputMessage){
        // 从Content-Type解析编码
        HttpHeaders headers = inputMessage.getHeaders();
        MediaType contentType = headers.getContentType();
        Charset charset = contentType.getCharset();
        charset = charset == null ? Charset.forName("UTF-8") : charset ;
        return charset ;
    }

    @Override
    public Properties read(Type type, Class<?> contextClass, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return readInternal(null, inputMessage);
    }
}
