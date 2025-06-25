package com.miyu.module.ppm.api.shippingreturn;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.api.shipping.ShippingApi;
import com.miyu.module.ppm.api.shipping.dto.ShippingOutboundReqDTO;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDTO;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDetailRetraceDTO;
import com.miyu.module.ppm.dal.dataobject.shipping.ShippingDO;
import com.miyu.module.ppm.enums.shipping.ShippingStatusEnum;
import com.miyu.module.ppm.service.shipping.ShippingService;
import com.miyu.module.ppm.service.shippingreturn.ShippingReturnService;
import com.miyu.module.ppm.service.shippingreturndetail.ShippingReturnDetailService;
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
public class ShippingReturnApiImpl implements ShippingReturnApi {

    @Resource
    private ShippingReturnService shippingReturnService;
    @Resource
    private ShippingReturnDetailService shippingReturnDetailService;


    @Override
    public CommonResult<String> updateShippingReturnStatus(String businessKey, Integer status) {

        shippingReturnService.updateShippingProcessInstanceStatus(businessKey,status);
        return CommonResult.success("ok");
    }

    @Override
    public CommonResult<List<ShippingReturnDTO>> getShippingReturnListByContractIds(Collection<String> ids) {
        return CommonResult.success(shippingReturnService.getShippingReturnListByContractIds(ids));
    }

    @Override
    public CommonResult<List<ShippingReturnDetailRetraceDTO>> getShippingReturnListByBarcode(String barCode) {
        return CommonResult.success(shippingReturnDetailService.getShippingReturnListByBarcode(barCode));
    }
}
