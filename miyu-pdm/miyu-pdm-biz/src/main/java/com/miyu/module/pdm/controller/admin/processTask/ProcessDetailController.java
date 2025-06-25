package com.miyu.module.pdm.controller.admin.processTask;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.*;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteListReqVO;
import com.miyu.module.pdm.controller.admin.processRoute.vo.ProcessRouteRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.*;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.FeasibilityResultDO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.QuotaPerPartDO;
import com.miyu.module.pdm.dal.dataobject.processRoute.ProcessRouteDO;
import com.miyu.module.pdm.service.feasibilityDetail.FeasibilityDetailService;
import com.miyu.module.pdm.service.process.processService;
import com.miyu.module.pdm.service.processTask.ProcessDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "PDM - 工艺方案编制")
@RestController
@RequestMapping("pdm/process-detail")
@Validated
public class ProcessDetailController {
    @Resource
    private ProcessDetailService processDetailService;
    @Resource
    private FeasibilityDetailService feasibilityDetailService;
    @Resource
    private processService processService;
    @GetMapping("getProjPartBomListByProjectCode")
    @Operation(summary = "获得当前选中项目的零件目录", description = "根据选中项目编号获取")
    public CommonResult<List<ProjPartBomRespVO>> getProjPartBomListByProjectCode(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomRespVO> list = processDetailService.getProjPartBomListByProjectCode(reqVO);
        return success(list);
    }
    @GetMapping("getProjPartBomTreeList")
    @Operation(summary = "获得当前选中项目的零件目录结构树")
    public CommonResult<List<ProjPartBomTreeRespVO>> getProjPartBomTreeList(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomTreeRespVO> list = processDetailService.getProjPartBomTreeList(reqVO);
        return success(list);
    }
    @GetMapping("getProjPartBomTreeByPartVersionId")
    @Operation(summary = "审批详情页获取单个零件工艺方案结构树")
    public CommonResult<List<ProjPartBomTreeRespVO>> getProjPartBomTreeByPartVersionId(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomTreeRespVO> list = processDetailService.getProjPartBomTreeByPartVersionId(reqVO);
        return success(list);
    }
    @GetMapping("getPartDetailByTaskId")
    @Operation(summary = "审批详情页获取零件工艺方案详细信息")
    public CommonResult<ProjPartBomRespVO> getPartDetailByTaskId(@RequestParam("id") String id) {
        ProjPartBomRespVO respVO = processDetailService.getPartDetailByTaskId(id);
        return success(respVO);
    }
    @PostMapping("/saveSelectedResource")
    @Operation(summary = "保存选择资源(设备、刀具、工装)")
    public CommonResult<Boolean> saveSelectedResource(@Valid ResourceSelectedReqVO reqVO) {
        processDetailService.saveSelectedResource(reqVO);
        return success(true);
    }

    @PostMapping("/deleteSelectedResource")
    @Operation(summary = "删除选择资源(设备、刀具、工装)")
    public CommonResult<Boolean> deleteSelectedDevice(@Valid ProcessDetailReqVO reqVO) {
        processDetailService.deleteSelectedDevice(reqVO);
        return success(true);
    }
    @GetMapping("/getResourceListByPart")
    @Operation(summary = "获取零件关联制造资源列表")
    public CommonResult<List<ProcessDetailRespVO>> getResourceListByPart(ResourceSelectedReqVO reqVO) {
        List<ProcessDetailRespVO> list = processDetailService.getResourceListByPart(reqVO);
        return success(BeanUtils.toBean(list, ProcessDetailRespVO.class));
    }
    @GetMapping("/getFeasibilityResultInfo")
    @Operation(summary = "获得评估结果信息")
    public CommonResult<FeasibilityResultRespVO> getFeasibilityResultInfo(FeasibilityResultReqVO reqVO) {
        FeasibilityResultDO result = processDetailService.getFeasibilityResult(reqVO);
        return success(BeanUtils.toBean(result, FeasibilityResultRespVO.class));
    }
    @PutMapping("/updateFeasibilityTaskStatus")
    @Operation(summary = "更新技术评估任务状态为2评估中")
    public CommonResult<Boolean> updateProcessTaskStatus(@Valid @RequestBody ProcessTaskReqVO updateReqVO) {
        processDetailService.updateProcessTaskStatus(updateReqVO);
        return success(true);
    }


    @GetMapping("/getQuotaPerPartInfo")
    @Operation(summary = "获得单件定额管理信息")
    public CommonResult<QuotaPerPartRespVO> getQuotaPerPartInfo(QuotaPerPartReqVO reqVO) {
        QuotaPerPartDO result = feasibilityDetailService.getQuotaPerPart(reqVO);
        return success(BeanUtils.toBean(result, QuotaPerPartRespVO.class));
    }

//    @PutMapping("/updateProcessInformation")
//    @Operation(summary = "修改单件定额")
//    public CommonResult<Boolean> updateQuotaPerPart(@Valid @RequestBody ProcessSaveReqVO updateReqVO) {
//        processService.updateProcess(updateReqVO);
//        return success(true);
//    }
    @GetMapping("/getProcessRouteDetailList")
    @Operation(summary = "获得加工路线信息")
    public CommonResult<List<ProcessRouteRespVO>> getProcessRouteDetailList(ProcessRouteListReqVO listReqVO) {
        List<ProcessRouteDO> list = processService.getRouteList(listReqVO);
        return success(BeanUtils.toBean(list, ProcessRouteRespVO.class));
    }

    @GetMapping("/getProcessInformation")
    @Operation(summary = "获得工艺信息")
   public CommonResult<ProcessRespVO> getInformation(@RequestParam("id") String id){
        ProcessRespVO respVO = processService.selectProcessById(id);
        return success(respVO);
    }

    @PostMapping("/createProcessInformation")
    @Operation(summary = "新增工艺信息")
    public CommonResult<String> createQuotaPerPart(@Valid @RequestBody ProcessSaveReqVO createReqVO) {
        return success(processService.createProcess(createReqVO));
    }
    @PutMapping("/updateProcessInformation")
    @Operation(summary = "修改工艺信息")
    public CommonResult<Boolean> updateQuotaPerPart(@Valid @RequestBody ProcessSaveReqVO ReqVO) {
        processService.updateProcess(ReqVO);
        return success(true);
    }
    @DeleteMapping("/deleteProcessInformation")
    @Operation(summary = "删除工艺信息")
    public CommonResult<Boolean> deleteQuotaPerPart(@Valid @RequestBody ProcessSaveReqVO deleteReqVO) {
        processService.deleteProcess(deleteReqVO);
        return success(true);
    }

    @GetMapping("/getProcessCountByPartVersionId")
    @Operation(summary = "获得零件+加工状态下已定版工艺规程数量")
    public CommonResult<Integer> getProcessCountByPartVersionId(@RequestParam("partVersionId") String partVersionId,@RequestParam("processCondition") String processCondition){
        return success(processService.getProcessCountByPartVersionId(partVersionId, processCondition));
    }

    @GetMapping("/getProcessListByPartVersionId")
    @Operation(summary = "获得零件下已定版工艺规程列表")
    public CommonResult<List<ProcessRespVO>> getProcessListByPartVersionId(@RequestParam("partVersionId") String partVersionId,@RequestParam("processCondition") String processCondition){
        List<ProcessRespVO> list = processService.getProcessListByPartVersionId(partVersionId, processCondition);
        return success(list);
    }

    @PostMapping("/saveSelectedProcess")
    @Operation(summary = "保存选中的零件已定版工艺规程")
    public CommonResult<Boolean> saveSelectedProcess(@Valid ProcessSelectedReqVO reqVO) {
        processService.saveSelectedProcess(reqVO);
        return success(true);
    }

    @PostMapping("/saveProcessUp")
    @Operation(summary = "零件已定版工艺规程升版")
    public CommonResult<Boolean> saveProcessUp(@Valid ProcessSelectedReqVO reqVO) {
        processService.saveProcessUp(reqVO);
        return success(true);
    }

    @PutMapping("/startProcessInstance")
    @Operation(summary = "工艺方案发起流程")
    public CommonResult<Boolean> startProcessInstance(@Valid @RequestBody ProcessTaskReqVO updateReqVO) {
        processService.startProcessInstance(updateReqVO);
        return success(true);
    }

}
