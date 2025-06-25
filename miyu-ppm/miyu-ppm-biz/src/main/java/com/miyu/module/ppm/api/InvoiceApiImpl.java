package com.miyu.module.ppm.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.api.contract.InvoiceApi;
import com.miyu.module.ppm.service.contractinvoice.ContractInvoiceService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController // 提供 RESTful API 接口，给 Feign 调用
@Validated
public class InvoiceApiImpl implements InvoiceApi {

    @Resource
    private ContractInvoiceService invoiceService;

    @Override
    public CommonResult<String> updateInvoiceAuditStatus(String bussinessKey, Integer status) {
        invoiceService.updateContractInvoiceAuditStatus(bussinessKey, status);
        return null;
    }
}
