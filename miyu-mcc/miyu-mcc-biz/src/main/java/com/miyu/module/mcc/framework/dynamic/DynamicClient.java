package com.miyu.module.mcc.framework.dynamic;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Description: 通过FeignClient工厂获取到的FeignClient对象通过指定的请求去调用生产者方法!
 */
@Component
public class DynamicClient {
    @Autowired
    private DynamicFeignClientFactory<DynamicService> dynamicDynamicFeignClientFactory;
    public CommonResult<String> executePostApi(String feignName, String url, Object params){
        DynamicService dynamicService = dynamicDynamicFeignClientFactory.getFeignClient(DynamicService.class,feignName);
        return dynamicService.executePostRequest(url,params);
    }
    public CommonResult<String> executeGetApi(String feignName,String url,Object params){
        DynamicService dynamicService = dynamicDynamicFeignClientFactory.getFeignClient(DynamicService.class,feignName);
        return dynamicService.executeGetRequest(url, params);
    }

    public CommonResult<String> executePutApi(String feignName,String url,Object params){
        DynamicService dynamicService = dynamicDynamicFeignClientFactory.getFeignClient(DynamicService.class,feignName);
        return dynamicService.executePutRequest(url, params);
    }

    public CommonResult<String> executeDeleteApi(String feignName,String url,Object params){
        DynamicService dynamicService = dynamicDynamicFeignClientFactory.getFeignClient(DynamicService.class,feignName);
        return dynamicService.executeDeleteRequest(url, params);
    }
}