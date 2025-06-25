package com.miyu.module.es.service.http;

import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class HttpReadUtils {

    /**
     * http请求流
     * @param client
     * @param httpPost
     * @return
     */
    public static String httpRead(CloseableHttpClient client , HttpPost httpPost) {
        //结果返回response
        CloseableHttpResponse response = null;
        //请求流返回内容读取
        BufferedReader reader = null;
        //返回值格式化
        StringBuffer responseString = null;
        try {
            //发起请求
            response = client.execute(httpPost);
            //判断识别码200说明请求连接成功
            if (response.getStatusLine().getStatusCode() == 200) {
                reader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));
                String inputLine;
                responseString = new StringBuffer();
                while ((inputLine = reader.readLine()) != null) {
                    responseString.append(inputLine);
                }
            }
        } catch (Exception e) {
        } finally {
            if (client != null) {
                try {
                    client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (response != null) {
                try {
                    response.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //返回结果转换为字符串
        if (responseString != null) {
            return responseString.toString();
        }else {
            return null;
        }
    }

}
