package com.miyu.module.ppm.api.contractconsignment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.service.contractconsignment.ContractConsignmentService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;


@RestController
@Validated
public class ContractConsignmentApiImpl implements ContractConsignmentApi {

    @Resource
    private ContractConsignmentService contractConsignmentService;



    @Override
    public CommonResult<String> updateContractConsignmentStatus(String businessKey, Integer status) {
        contractConsignmentService.updateContractConsignmentStatus(businessKey,status);
        return CommonResult.success("ok");
    }
}
