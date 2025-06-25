package com.miyu.module.mcc.framework.dynamic;

import org.springframework.cloud.openfeign.FeignClientBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Description: FeignClient工厂类,根据服务名称创建FeignClient对象(代理对象)
 */
@Component
public class DynamicFeignClientFactory <T>{
    private FeignClientBuilder feignClientBuilder;
    public DynamicFeignClientFactory(ApplicationContext applicationContext){
        this.feignClientBuilder = new FeignClientBuilder(applicationContext);
    }
    //动态生成feignClient对象(代理对象)
    public T getFeignClient(final Class<T> type,String ServiceID){
        return this.feignClientBuilder.forType(type,ServiceID).build();
    }
}