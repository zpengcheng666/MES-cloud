package com.miyu.module.pdm.api.processChange;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.service.processPlanDetail.ProcessPlanDetailService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Validated
public class ProcessChangeApiImpl implements ProcessChangeApi {
    @Resource
    private ProcessPlanDetailService processPlanDetailService;

    @Override
    public CommonResult<String> updateProcessChangeInstanceStatus(String businessKey, Integer status) {
        processPlanDetailService.updateProcessChangeInstanceStatus(businessKey, status);
        return CommonResult.success("ok");
    }
}
