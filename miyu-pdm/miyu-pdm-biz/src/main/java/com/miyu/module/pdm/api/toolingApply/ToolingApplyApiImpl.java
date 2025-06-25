package com.miyu.module.pdm.api.toolingApply;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.service.toolingApply.ToolingApplyService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@Validated
public class ToolingApplyApiImpl implements ToolingApplyApi {

    @Resource
    private ToolingApplyService toolingApplyService;

    @Override
    public CommonResult<String> updateToolingApplyStatus(String businessKey, Integer status) {
        toolingApplyService.updateApplyInstanceStatus(businessKey, status);
        return CommonResult.success("ok");
    }
}
