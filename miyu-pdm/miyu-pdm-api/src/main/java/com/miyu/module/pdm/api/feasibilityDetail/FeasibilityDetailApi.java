package com.miyu.module.pdm.api.feasibilityDetail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import com.miyu.module.pdm.enums.ApiConstants;
import feign.Param;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 技术评估")
public interface FeasibilityDetailApi {

    String PREFIX = ApiConstants.PREFIX + "/feasibility-detail";

    @PostMapping(PREFIX + "/updateProjectstatus")
    @Operation(summary = "推送项目关闭状态，更新工艺中记录的项目状态")
    @Parameter(name = "projectCode", description = "项目号", required = true, example = "1")
    CommonResult<String> updateProjectstatus(@RequestParam("projectCode") String projectCode);

    @PostMapping(PREFIX + "/updateFeasilityTaskStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "businessKey", description = "任务id", required = true, example = "1")
    @Parameter(name = "status", description = "审批状态", required = true, example = "1")
    CommonResult<String> updateFeasilityTaskStatus(@RequestParam("businessKey") String businessKey,@RequestParam("status")  Integer status);



    @PostMapping(PREFIX + "/pushFeasibility")
    @Operation(summary = "项目立项审批通过后，推送需要技术评估的零件信息(生成评估任务)")
    @Parameter(name = "projectCode", description = "项目号", required = true, example = "1")
    @Parameter(name = "partNumber", description = "零件图号", required = true, example = "1")
    @Parameter(name = "partName", description = "零件名称", required = true, example = "1")
    @Parameter(name = "processCondition", description = "加工状态", required = true, example = "1")
    CommonResult<String> pushFeasibility(@RequestParam("projectCode") String projectCode, @RequestParam("partNumber")  String partNumber,
            @RequestParam("partName")  String partName, @RequestParam("processCondition") String processCondition);
}


