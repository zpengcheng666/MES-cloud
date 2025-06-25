package com.miyu.module.ppm.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.api.contract.PaymentApi;
import com.miyu.module.ppm.api.contract.dto.ContractPaymentDTO;
import com.miyu.module.ppm.service.contractpayment.ContractPaymentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class PaymentApiImpl implements PaymentApi {

    @Resource
    private ContractPaymentService paymentService;

    @Override
    public CommonResult<String> updatePaymentAuditStatus(String bussinessKey, Integer status) {
        paymentService.updateContractPaymentAuditStatus(bussinessKey, status);
        return null;
    }

    @Override
    public CommonResult<List<ContractPaymentDTO>> getContractPaymentByContractIds(Collection<String> ids) {
        List<ContractPaymentDTO> contractPaymentByContractIds = paymentService.getContractPaymentByContractIds(ids);
        return success(contractPaymentByContractIds);
    }
}
