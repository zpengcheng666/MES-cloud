package com.miyu.module.ppm.api.shippinginstorage;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingDetailRetraceDTO;
import com.miyu.module.ppm.api.shipping.dto.ShippingOutboundReqDTO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippinginstorage.ShippingInstorageService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.ppm.enums.ErrorCodeConstants.SHIPPING_NOT_EXISTS;


@RestController
@Validated
public class ShippinginstorageApiImpl implements ShippingInstorageApi {

    @Resource
    private ShippingInstorageService shippingInstorageService;

    @Override
    public CommonResult<String> updateShippingInstorageStatus(String businessKey, Integer status) {
        shippingInstorageService.updateShippingInstorgerProcessInstanceStatus(businessKey,status);

        return CommonResult.success("ok");
    }
}
