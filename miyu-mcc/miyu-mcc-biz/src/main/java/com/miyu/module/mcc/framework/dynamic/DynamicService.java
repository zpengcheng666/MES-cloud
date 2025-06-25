package com.miyu.module.mcc.framework.dynamic;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.cloud.openfeign.SpringQueryMap;
import org.springframework.web.bind.annotation.*;

/**
 * Description: 通用接口,里面定义通用方法
 *  注意: 由于服务生产者所有的接口的返回值都是json格式的字符串,
 *         所以这里的通用接口的返回值最好使用String类型!!!
 */
public interface DynamicService {
    /**
     * post方法对应的方法
     * @param url 服务生产者Handler方法请求映射地址
     * @param params 服务生产者Handler方法参数
     * @return
     */
    @PostMapping(value = "{url}")
    CommonResult<String> executePostRequest(@PathVariable("url") String url, @RequestBody Object params);
    @GetMapping(value = "{url}")
    CommonResult<String> executeGetRequest(@PathVariable("url") String url, @SpringQueryMap Object params);
    @PutMapping(value = "{url}")
    CommonResult<String> executePutRequest(@PathVariable("url") String url, @RequestBody Object params);
    @DeleteMapping(value = "{url}")
    CommonResult<String> executeDeleteRequest(@PathVariable("url") String url, @RequestBody Object params);
}
