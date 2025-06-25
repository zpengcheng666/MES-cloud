package cn.iocoder.yudao.module.pms.api.assessment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.pms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 评审")
public interface AssessmentApi {

    String PREFIX = ApiConstants.PREFIX + "/assessment";

    @PostMapping(PREFIX + "/updateProjectStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "businessKey", description = "任务id", required = true, example = "1")
    @Parameter(name = "status", description = "审批状态", required = true, example = "1")
    CommonResult<String> updateProcessStatus(@RequestParam("businessKey") String businessKey,@RequestParam("status")  Integer status);

}
