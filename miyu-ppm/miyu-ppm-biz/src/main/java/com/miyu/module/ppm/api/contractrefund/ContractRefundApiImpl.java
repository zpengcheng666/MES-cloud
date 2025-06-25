package com.miyu.module.ppm.api.contractrefund;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.api.contracrefund.ContractRefundApi;
import com.miyu.module.ppm.api.shippingreturn.ShippingReturnApi;
import com.miyu.module.ppm.service.contractrefund.ContractRefundService;
import com.miyu.module.ppm.service.shippingreturn.ShippingReturnService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@Validated
public class ContractRefundApiImpl implements ContractRefundApi {

    @Resource
    private ContractRefundService contractRefundService;


    @Override
    public CommonResult<String> updateContractRefundStatus(String businessKey, Integer status) {

        contractRefundService.updateRefundProcessInstanceStatus(businessKey,status);
        return CommonResult.success("ok");
    }
}
