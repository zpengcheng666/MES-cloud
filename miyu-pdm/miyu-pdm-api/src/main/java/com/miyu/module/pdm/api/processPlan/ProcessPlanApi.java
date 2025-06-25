package com.miyu.module.pdm.api.processPlan;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.api.processPlan.dto.PartRespDTO;
import com.miyu.module.pdm.api.processPlan.dto.ProcessRespDTO;
import com.miyu.module.pdm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 工艺方案")
public interface ProcessPlanApi {

    String PREFIX = ApiConstants.PREFIX + "/process-plan";

    @PostMapping(PREFIX + "/updateProcessTaskStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "businessKey", description = "任务id", required = true, example = "1")
    @Parameter(name = "status", description = "审批状态", required = true, example = "1")
    CommonResult<String> updateProcessTaskStatus(@RequestParam("businessKey") String businessKey,@RequestParam("status")  Integer status);

    @PostMapping(PREFIX + "/pushProcess")
    @Operation(summary = "项目评审审批通过后，推送工艺准备信息(生成工艺任务)")
    @Parameter(name = "projectCode", description = "项目号", required = true, example = "1")
    @Parameter(name = "partNumber", description = "零件图号", required = true, example = "1")
    @Parameter(name = "partName", description = "零件名称", required = true, example = "1")
    @Parameter(name = "processCondition", description = "加工状态", required = true, example = "1")
    @Parameter(name = "processPreparationTime", description = "工艺准备完成时间", required = true, example = "1")
    CommonResult<String> pushProcess(
            @RequestParam("projectCode") String projectCode,
            @RequestParam("partNumber") String partNumber,
            @RequestParam("partName") String partName,
            @RequestParam("processCondition") String processCondition,
            @RequestParam("processPreparationTime") LocalDateTime processPreparationTime
    );

    @GetMapping(PREFIX + "/getInformationList")
    @Operation(summary = "工艺方案详情")
    @Parameter(name = "processSchemes", description = "工艺方案id", required = true)
    CommonResult<List<ProcessRespDTO>> getInformationList(@RequestParam("processSchemes") List<String> processSchemes);

    @GetMapping(PREFIX + "/getProcessConditionList")
    @Operation(summary = "根据零件图号查加工状态列表")
    @Parameter(name = "partNumber", description = "零件图号", required = true)
    CommonResult<List<PartRespDTO>> getProcessConditionList(@RequestParam("partNumber") String partNumber);
}
