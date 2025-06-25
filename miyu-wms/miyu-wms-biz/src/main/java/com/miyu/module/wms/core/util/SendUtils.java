package com.miyu.module.wms.core.util;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * 请求方法抽取
 **/
@Slf4j
public class SendUtils {

    /**
     * 同步get请求 方法抽取
     * 无参数 无日志
     */
    public static JSONObject getJSONObjectRequestSend(String url){

        //1.发起请求
        RestTemplate restTemplate = new RestTemplate();
        String forObject = null;
        try {
            forObject = restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            log.error("rest请求调用失败,失败原因"+e.getMessage());
            e.printStackTrace();
        }
        return JSONObject.parseObject(forObject);
    }

    public static JSONArray getJSONArrayRequestSend(String url){
        //1.发起请求
        RestTemplate restTemplate = new RestTemplate();
        String forObject = null;
        try {
            forObject = restTemplate.getForObject(url, String.class);
        } catch (RestClientException e) {
            log.error("rest请求调用失败,失败原因"+e.getMessage());
            e.printStackTrace();
        }
        return JSONArray.parseArray(forObject);
    }

    /**
     * 同步get请求 方法抽取
     * 无参数 无日志
     */
    public static CommonResult<?> getRequestSendForByte(String url){

        //1.发起请求
        RestTemplate restTemplate = new RestTemplate();

        byte[] forObject = new byte[0];
        try {
            forObject = restTemplate.getForObject(
                    url,
                    byte[].class
            );
        } catch (Exception e) {
            e.getStackTrace();
            log.error(e.getMessage(), e);
            return CommonResult.error(500,"文件流获取失败:" + e.getMessage());
        }
        return CommonResult.success(forObject);
    }

    /**
     * 同步get请求 方法抽取
     */
    public static JSONObject getJSONObjectRequestSend(String url,Map<String,Object> params){

        //1.发起请求
        RestTemplate restTemplate = new RestTemplate();
        String forObject = null;
        try {
            forObject = restTemplate.getForObject(url, String.class,params);
        } catch (RestClientException e) {
            e.printStackTrace();
        }
        return JSON.parseObject(forObject);
    }

    public static JSONObject postJSONObjectRequestSend(String url){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.ALL_VALUE);
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(null, headers);
        RestTemplate restTemplate = new RestTemplate();
        //异步调用任务设定
        ResponseEntity<JSONObject> resEntity = null;
        try {
            resEntity = restTemplate.postForEntity(
                    url,
                    httpEntity,
                    JSONObject.class
            );
        } catch (RestClientException e) {
            log.error("rest请求调用失败,失败原因"+e.getMessage());
            return null;
        }
        log.info("响应值=="+resEntity);
        if(resEntity.getStatusCode().is2xxSuccessful()) {
            JSONObject j = resEntity.getBody();
            return j;
        }
        log.error("rest请求调用失败,失败状态字："+ resEntity.getStatusCodeValue());
        return null;
    }

    /**
     * 同步请求 方法抽取
     *
     * @param url
     * @param jsonObject
     */
    public static CommonResult<?> postStringRequestSend(String url, JSONObject jsonObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.ALL_VALUE);
        log.info("rest请求参数==========={}===========", jsonObject.toJSONString());
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(jsonObject, headers);
        RestTemplate restTemplate = new RestTemplate();
        //异步调用任务设定
        ResponseEntity<String> resEntity = null;
        try {
            resEntity = restTemplate.postForEntity(
                    url,
                    httpEntity,
                    String.class
            );
        } catch (HttpServerErrorException e){
            log.error("rest请求调用失败,失败原因:==========={}===========",e.getResponseBodyAsString());
            return CommonResult.error(500,"rest请求调用失败,失败原因" + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("rest请求调用失败,失败原因:==========={}===========",e.getMessage());
            return CommonResult.error(500,"rest请求调用失败,失败原因" + e.getMessage());
        }
        log.info("rest请求响应值==========={}===========", resEntity);
        if(resEntity.getStatusCode().is2xxSuccessful()) {
            return CommonResult.success(resEntity.getBody());
        }
        log.error("rest请求调用失败,失败状态字：{} ", resEntity.getStatusCodeValue());
        return CommonResult.error(resEntity.getStatusCodeValue(), "rest请求调用失败");
    }

    /**
     * 同步请求 方法抽取
     *
     * @param url
     * @param jsonObject
     */
    public static CommonResult<?> postRequestSend(String url, JSONObject jsonObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.ALL_VALUE);
        log.info("rest请求参数==========={}===========", jsonObject.toJSONString());
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(jsonObject, headers);
        RestTemplate restTemplate = new RestTemplate();
        //异步调用任务设定
        ResponseEntity<JSONObject> resEntity = null;
        try {
            resEntity = restTemplate.postForEntity(
                    url,
                    httpEntity,
                    JSONObject.class
            );
        } catch (HttpServerErrorException e){
            log.error("rest请求调用失败,失败原因:==========={}===========",e.getResponseBodyAsString());
            return CommonResult.error(500,"rest请求调用失败,失败原因" + e.getResponseBodyAsString());
        } catch (Exception e) {
            log.error("rest请求调用失败,失败原因:==========={}===========",e.getMessage());
            return CommonResult.error(500,"rest请求调用失败,失败原因" + e.getMessage());
        }
        log.info("rest请求响应值==========={}===========", resEntity);
        if(resEntity.getStatusCode().is2xxSuccessful()) {
            return CommonResult.success(resEntity.getBody());
        }
        log.error("rest请求调用失败,失败状态字：{} ", resEntity.getStatusCodeValue());
        return CommonResult.error(resEntity.getStatusCodeValue(), "rest请求调用失败");
    }

    /**
     * 同步请求 方法抽取
     *
     * @param url
     * @param jsonObject
     */
    public static JSONObject postRequestSend(String url,String assessToken, JSONObject jsonObject) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Accept", MediaType.ALL_VALUE);
        headers.setBearerAuth(assessToken);
        log.info("请求参数=="+jsonObject.toJSONString());
        HttpEntity<JSONObject> httpEntity = new HttpEntity<>(jsonObject, headers);
        RestTemplate restTemplate = new RestTemplate();
        //异步调用任务设定
        ResponseEntity<JSONObject> resEntity = null;
        try {
            resEntity = restTemplate.postForEntity(
                    url,
                    httpEntity,
                    JSONObject.class
            );
        } catch (RestClientException e) {
            log.error("rest请求调用失败,失败原因"+e.getMessage());
            return null;
        }
        log.info("响应值=="+resEntity);
        if(resEntity.getStatusCode().is2xxSuccessful()) {
            JSONObject j = resEntity.getBody();
            return j;
        }
        log.error("rest请求调用失败,失败状态字："+ resEntity.getStatusCodeValue());
        return null;
    }

    public static JSONObject getRequestSend(String url,String assessToken) {
        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_JSON);
//        headers.add("Accept", MediaType.ALL_VALUE);
//        headers.setBearerAuth(assessToken);
        headers.set("token",assessToken);
        HttpEntity<MultiValueMap<String,Object>> httpEntity = new HttpEntity<>(headers);
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(50000);// 设置超时
        requestFactory.setReadTimeout(50000);
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        //异步调用任务设定
        ResponseEntity<JSONObject> resEntity = null;
        try {
            resEntity = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    JSONObject.class
            );
        } catch (RestClientException e) {
            log.error("rest请求调用失败,失败原因"+e.getMessage());
            return null;
        }
        log.info("响应值=="+resEntity);
        if(resEntity.getStatusCode().is2xxSuccessful()) {
            JSONObject j = resEntity.getBody();
            return j;
        }
        log.error("rest请求调用失败,失败状态字："+ resEntity.getStatusCodeValue());
        return null;
    }

    /**
     * 同步请求 方法抽取
     *
     * @param url
     * @param headers
     * @param reqMap
     */
//    @AutoLog(value = "同步POST请求", logType = CommonConstant.LOG_TYPE_4, operateType = CommonConstant.OPERATE_TYPE_12)
    public static CommonResult<?> postRequestSend(String url, HttpHeaders headers, Map<String, Object> reqMap) {
        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(reqMap, headers);
        //复杂构造函数的使用
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(50000);// 设置超时
        requestFactory.setReadTimeout(50000);

        //请求对象初始化
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        //异步调用任务设定
        ResponseEntity<String> resEntity = null;
        try {
            resEntity = restTemplate.postForEntity(
                    url,
                    httpEntity,
                    String.class
            );
        } catch (RestClientException e) {
          //  RetryUtil.setRetryTimes(2).retry(new SendUtils(), url,headers,reqMap,time);
            log.error("rest请求调用失败,失败原因"+e.getMessage());
            return CommonResult.error(500,"rest请求调用失败,失败原因" + e.getMessage());
        }
        System.out.println(resEntity);
        if(200 == resEntity.getStatusCodeValue()) {
            JSONObject jsonObject = JSON.parseObject(resEntity.getBody());
            return CommonResult.success(jsonObject);
        }
        log.error("rest请求调用失败,失败状态字："+ resEntity.getStatusCodeValue());
        return CommonResult.error(resEntity.getStatusCodeValue(), "rest请求调用失败");
    }

    /**
     * 同步请求 方法抽取
     * @param url
     * @param params
     * @return
     */
    public static CommonResult<?> postRequestSend(String url, MultiValueMap<String, Object> params) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.add("Accept", MediaType.ALL_VALUE);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<MultiValueMap<String, Object>>(params, headers);
        //复杂构造函数的使用
        /*SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(50000);// 设置超时
        requestFactory.setReadTimeout(50000);
        //请求对象初始化
        RestTemplate restTemplate = new RestTemplate(requestFactory);*/
        RestTemplate restTemplate = new RestTemplate();
        //异步调用任务设定
        ResponseEntity<String> resEntity = null;
        try {
            resEntity = restTemplate.postForEntity(
                    url,
                    httpEntity,
                    String.class
            );
        } catch (RestClientException e) {
            //  RetryUtil.setRetryTimes(2).retry(new SendUtils(), url,headers,reqMap,time);
            log.error("rest请求调用失败,失败原因"+e.getMessage());
            return CommonResult.error(500,"rest请求调用失败,失败原因" + e.getMessage());
        }
        System.out.println(resEntity);
        if(200 == resEntity.getStatusCodeValue()) {
            return CommonResult.success(resEntity.getBody());
        }
        log.error("rest请求调用失败,失败状态字："+ resEntity.getStatusCodeValue());
        return CommonResult.error(resEntity.getStatusCodeValue(), "rest请求调用失败");
    }

//    URL ur = null;
//        try {
//        ur = new URL("http://www.yhfund.com.cn");
//        HttpURLConnection urlcon = (HttpURLConnection)ur.openConnection();
//        urlcon.setRequestProperty(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
//        String assessToken = "eyJhbGciOiJIUzUxMiIsImlhdCI6MTY1NzU5MDM3NCwiZXhwIjoxNjczMTQyMzc0fQ.eyJOYW1lIjoiYWRtaW4ifQ.OPqD0uIe75623YZPGC3Kzz6aWdlk5OQ1KAGsubfyyys6UcVfSRgGUq6_Ko2v_MB83_D_FofBRwJDPJZI1ffHtA";
//        urlcon.setRequestProperty(HttpHeaders.AUTHORIZATION, "Bearer " + assessToken);
//        urlcon.connect();
//        BufferedReader in = new BufferedReader(new InputStreamReader(urlcon.getInputStream()));
//        String line;
//        while ((line = in.readLine()) != null){
//            result +=line;
//        }
//        in.close();
//    } catch (Exception e) {
//        e.printStackTrace();
//    }


//    public static JSONObject postRequestInstructSend(String url, HttpHeaders headers, Map<String, Object> reqMap, Integer time) {
//
//        HttpEntity<Map<String, Object>> httpEntity = new HttpEntity<Map<String, Object>>(reqMap, headers);
//        //复杂构造函数的使用
//        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
//        requestFactory.setConnectTimeout(50000);// 设置超时
//        requestFactory.setReadTimeout(50000);
//
//        //请求对象初始化
//        RestTemplate restTemplate = new RestTemplate(requestFactory);
//        //异步调用任务设定
//        ResponseEntity<String> resEntity = null;
//        try {
//            resEntity = restTemplate.postForEntity(
//                    url,
//                    httpEntity,
//                    String.class
//            );
//        } catch (RestClientException e) {
//            //  RetryUtil.setRetryTimes(2).retry(new SendUtils(), url,headers,reqMap,time);
//            log.error("rest请求调用失败,失败原因"+e.getMessage());
//            return null;
//        }
//        log.info(resEntity.toString());
//        if(200 == resEntity.getStatusCodeValue() ){
//            JSONObject jsonObject =  JSON.parseObject(resEntity.getBody());
//            return jsonObject;
//        }else {return null;}
//
//    }

    /**
     * 异步Post
     **/
   /* public static <T> T webClientPost(String url, Object param, Class<T> tClass) {
        WebClient webClient = WebClient.create();
        Mono<T> mono = webClient.post().uri(url).syncBody(param).retrieve().bodyToMono(tClass);
        return mono.block();
    }*/

}
