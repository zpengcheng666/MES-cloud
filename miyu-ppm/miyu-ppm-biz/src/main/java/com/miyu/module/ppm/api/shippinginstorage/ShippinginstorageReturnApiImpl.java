package com.miyu.module.ppm.api.shippinginstorage;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippinginstorage.ShippingInstorageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@Validated
public class ShippinginstorageReturnApiImpl implements ShippingInstorageReturnApi {

    @Resource
    private ShippingInstorageService shippingInstorageService;

    @Override
    public CommonResult<String> updateShippingInstorageReturnStatus(String businessKey, Integer status) {
        shippingInstorageService.updateShippingProcessInstanceStatus(businessKey,status);

        return CommonResult.success("ok");
    }
}
