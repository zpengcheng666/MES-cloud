package com.miyu.module.pdm.api.processChange;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 工艺更改单流程")
public interface ProcessChangeApi {

    String PREFIX = ApiConstants.PREFIX + "/process-plan-detail";

    @PostMapping(PREFIX + "/updateProcessChangeInstanceStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "businessKey", description = "任务id", required = true, example = "1")
    @Parameter(name = "status", description = "审批状态", required = true, example = "1")
    CommonResult<String> updateProcessChangeInstanceStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status")  Integer status);
}
