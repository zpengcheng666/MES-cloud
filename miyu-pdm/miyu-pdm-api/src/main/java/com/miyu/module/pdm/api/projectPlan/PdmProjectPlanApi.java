package com.miyu.module.pdm.api.projectPlan;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.api.projectAssessment.dto.CombinationRespDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProcedureDetailRespDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomReqDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomTreeRespDTO;
import com.miyu.module.pdm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 项目计划所需相关数据获取")
public interface PdmProjectPlanApi {

    String PREFIX = ApiConstants.PREFIX + "/projectPlan";

    @PostMapping(PREFIX + "/getProjPartBomPlanList")
    @Operation(summary = "获取工艺编程列表")
    CommonResult<List<ProjPartBomTreeRespDTO>> getProjPartBomPlanList(@RequestBody ProjPartBomReqDTO req);

    @GetMapping("getProjPartBomTreeList")
    @Operation(summary = "获得工艺详细设计结构树")
    CommonResult<List<ProjPartBomTreeRespDTO>> getProjPartBomProcessDetailDesignList(@RequestParam("projectCode") String projectCode, @RequestParam("viewSelf") boolean viewSelf);

    @GetMapping("/getResourceListByProcedure")
    @Operation(summary = "工序-获取关联制造资源列表")
    public CommonResult<List<ProcedureDetailRespDTO>> getResourceListByProcedure(@RequestParam("processVersionId") String processVersionId, @RequestParam("procedureId") String procedureId, @RequestParam("partVersionId") String partVersionId);

    @GetMapping("/getCombinationListByIds")
    @Operation(summary = "获取刀具资源列表")
    public CommonResult<List<CombinationRespDTO>> getResourceCombinationListByIds(@RequestParam("ids")List<String> ids);

    @GetMapping("getProjPartBomTreeListNew")
    @Operation(summary = "获得未关闭项目的零件目录结构树")
    public CommonResult<List<ProjPartBomTreeRespDTO>> getProjPartBomTreeListNew(@RequestParam("projectCode") String projectCode,@RequestParam("partNumber")String partNumber);

    @GetMapping(PREFIX + "/getProcessListByProjectCodes")
    @Operation(summary = "获取多个项目下的工艺信息")
    public CommonResult<List<ProjPartBomTreeRespDTO>> getProcessListByProjectCodes(@RequestParam("projectCodes") List<String> projectCodes);


}
