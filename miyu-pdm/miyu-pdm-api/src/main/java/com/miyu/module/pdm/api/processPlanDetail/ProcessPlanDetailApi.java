package com.miyu.module.pdm.api.processPlanDetail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcedureRespDTO;
import com.miyu.module.pdm.api.processPlanDetail.dto.ProcessPlanDetailRespDTO;
import com.miyu.module.pdm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 工艺规程")
public interface ProcessPlanDetailApi {

    String PREFIX = ApiConstants.PREFIX + "/process-plan-detail";

    @PostMapping(PREFIX + "/updateProcessStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "businessKey", description = "任务id", required = true, example = "1")
    @Parameter(name = "status", description = "审批状态", required = true, example = "1")
    CommonResult<String> updateProcessStatus(@RequestParam("businessKey") String businessKey,@RequestParam("status")  Integer status);

    @GetMapping(PREFIX + "/getProcessPlanDetail")
    @Operation(summary = "根据工艺规程版本id获取工艺规程详细信息")
    @Parameter(name = "processVersionId", description = "工艺规程版本id", required = true, example = "1")
    CommonResult<String> getProcessPlanDetail(@RequestParam("processVersionId") String processVersionId);

    @GetMapping(PREFIX + "/getProcedureListByProcessVersionId")
    @Operation(summary = "根据工艺规程版本id获取工序列表")
    @Parameter(name = "processVersionId", description = "工艺规程版本id", required = true, example = "1")
    CommonResult<List<ProcedureRespDTO>> getProcedureListByProcessVersionId(@RequestParam("processVersionId") String processVersionId);

    /**
     * 过滤capp_process_version中status=3且is_valid=0；按零件图号模糊检索
     * @param partNumber
     * @return
     */
    //这个是API接口
    @GetMapping(PREFIX + "/getProcessPlanList")
    @Operation(summary = "获取工艺规程列表")
    @Parameter(name = "partNumber", description = "零件图号", required = true, example = "1")
    CommonResult<List<ProcessPlanDetailRespDTO>> getProcessPlanList(@RequestParam("partNumber") String partNumber);



    @GetMapping(PREFIX + "/getProcedureListByIds")
    @Operation(summary = "工序ID获取工序信息")
    @Parameter(name = "ids", description = "工序ID集合", required = true, example = "1")
    CommonResult<List<ProcedureRespDTO>> getProcedureListByIds(@RequestParam("ids") Collection<String> ids);


    default Map<String,ProcedureRespDTO> getProcedureMapByIds(@RequestParam("ids") Collection<String> ids){
        List<ProcedureRespDTO> checkedData = getProcedureListByIds(ids).getCheckedData();
        return CollectionUtils.convertMap(checkedData, ProcedureRespDTO::getId);
    }

}
