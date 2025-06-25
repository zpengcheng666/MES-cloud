package com.miyu.module.ppm.api.contractconsignment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.service.contractconsignment.ContractConsignmentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;


@RestController
@Validated
public class ContractConsignmentReturnApiImpl implements ContractConsignmentReturnApi {

    @Resource
    private ContractConsignmentService contractConsignmentService;




    @Override
    public CommonResult<String> updateContractConsignmentReturnStatus(String businessKey, Integer status) {
        contractConsignmentService.updateContractConsignmentReturnStatus(businessKey,status);
        return CommonResult.success("ok");
    }
}
