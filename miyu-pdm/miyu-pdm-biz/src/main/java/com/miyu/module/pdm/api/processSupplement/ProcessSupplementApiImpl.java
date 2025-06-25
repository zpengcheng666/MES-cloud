package com.miyu.module.pdm.api.processSupplement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.service.processSupplement.ProcessSupplementService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Validated
public class ProcessSupplementApiImpl implements ProcessSupplementApi {
    @Resource
    private ProcessSupplementService processSupplementService;

    @Override
    public CommonResult<String> updateProcessSupplementInstanceStatus(String businessKey, Integer status) {
        processSupplementService.updateProcessSupplementInstanceStatus(businessKey, status);
        return CommonResult.success("ok");
    }
}
