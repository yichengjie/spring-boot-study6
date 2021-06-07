package com.yicj.study.pure2;

import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;

public class HttpClientUtil {

    public static String httpPostWithJson(String url, String json) {
        String returnValue = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            httpClient = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(url);
            StringEntity requestEntity = new StringEntity(json,"utf-8");
            requestEntity.setContentEncoding("UTF-8");
            httpPost.setHeader("Content-type", "application/json");
            httpPost.setEntity(requestEntity);
            returnValue = httpClient.execute(httpPost,responseHandler); //调接口获取返回值时，必须用此方法
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return returnValue;
    }


    public static String httpGet(String url) {
        String returnValue = "";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        ResponseHandler<String> responseHandler = new BasicResponseHandler();
        try{
            httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);
            returnValue = httpClient.execute(httpGet,responseHandler); //调接口获取返回值时，必须用此方法
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return returnValue;
    }
}
