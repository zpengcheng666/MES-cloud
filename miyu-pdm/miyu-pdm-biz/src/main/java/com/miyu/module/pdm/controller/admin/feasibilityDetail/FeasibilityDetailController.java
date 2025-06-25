package com.miyu.module.pdm.controller.admin.feasibilityDetail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.*;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureSchemaItemReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureSchemaItemRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.*;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcedureSchemaItemDO;
import com.miyu.module.pdm.service.feasibilityDetail.FeasibilityDetailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "PDM - 技术评估")
@RestController
@RequestMapping("pdm/feasibility-detail")
@Validated
public class FeasibilityDetailController {

    @Resource
    private FeasibilityDetailService feasibilityDetailService;

    @GetMapping("getProjPartBomListByProjectCode")
    @Operation(summary = "获得当前选中项目的零件目录", description = "根据选中项目编号获取")
    public CommonResult<List<ProjPartBomRespVO>> getProjPartBomListByProjectCode(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomRespVO> list = feasibilityDetailService.getProjPartBomListByProjectCode(reqVO);
        return success(list);
    }

    @GetMapping("/getFeasibilityResultInfo")
    @Operation(summary = "获得评估结果信息")
    public CommonResult<FeasibilityResultRespVO> getFeasibilityResultInfo(FeasibilityResultReqVO reqVO) {
        FeasibilityResultDO result = feasibilityDetailService.getFeasibilityResult(reqVO);
        return success(BeanUtils.toBean(result, FeasibilityResultRespVO.class));
    }

    @PostMapping("/createFeasibilityResult")
    @Operation(summary = "新增评估结果")
    public CommonResult<String> createFeasibilityResult(@Valid @RequestBody FeasibilityResultSaveReqVO createReqVO) {
        return success(feasibilityDetailService.createFeasibilityResult(createReqVO));
    }

    @PutMapping("/updateFeasibilityResult")
    @Operation(summary = "修改评估结果")
    public CommonResult<Boolean> updateFeasibilityResult(@Valid @RequestBody FeasibilityResultSaveReqVO updateReqVO) {
        feasibilityDetailService.updateFeasibilityResult(updateReqVO);
        return success(true);
    }

    @GetMapping("/getResourceListByPart")
    @Operation(summary = "获取零件关联制造资源列表")
    public CommonResult<List<FeasibilityDetailRespVO>> getResourceListByPart(ResourceSelectedReqVO reqVO) {
        List<FeasibilityDetailRespVO> list = feasibilityDetailService.getResourceListByPart(reqVO);
        return success(BeanUtils.toBean(list, FeasibilityDetailRespVO.class));
    }

    @PostMapping("/saveSelectedResource")
    @Operation(summary = "保存选择资源(设备、刀具、工装)")
    public CommonResult<Boolean> saveSelectedResource(@Valid ResourceSelectedReqVO reqVO) {
        feasibilityDetailService.saveSelectedResource(reqVO);
        return success(true);
    }

    @PostMapping("/deleteSelectedResource")
    @Operation(summary = "删除选择资源(设备、刀具、工装)")
    public CommonResult<Boolean> deleteSelectedDevice(@Valid FeasibilityDetailReqVO reqVO) {
        feasibilityDetailService.deleteSelectedDevice(reqVO);
        return success(true);
    }

    @PostMapping("/updateProcessingTime")
    @Operation(summary = "更改设备预估工时")
    public CommonResult<Boolean> updateProcessingTime(@Valid FeasibilityDetailReqVO reqVO) {
        feasibilityDetailService.updateProcessingTime(reqVO);
        return success(true);
    }

    @GetMapping("/getDemandDeviceList")
    @Operation(summary = "获取零件关联设备采购意见列表")
    public CommonResult<List<DemandDeviceRespVO>> getDemandDeviceList(DemandDeviceReqVO reqVO) {
        List<DemandDeviceRespVO> list = feasibilityDetailService.getDemandDeviceList(reqVO);
        return success(BeanUtils.toBean(list, DemandDeviceRespVO.class));
    }

    @PostMapping("/createDemandDevice")
    @Operation(summary = "创建设备采购意见")
    public CommonResult<String> createDemandDevice(@Valid @RequestBody DemandDeviceReqVO createReqVO) {
        return success(feasibilityDetailService.createDemandDevcie(createReqVO));
    }

    @PutMapping("/updateDemandDevice")
    @Operation(summary = "修改设备采购意见")
    public CommonResult<Boolean> updateDemandDevice(@Valid @RequestBody DemandDeviceReqVO updateReqVO) {
        feasibilityDetailService.updateDemandDevice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteDemandDevice")
    @Operation(summary = "删除设备采购意见")
    @Parameter(name = "id", description = "采购意见Id", required = true, example = "1024")
    public CommonResult<Boolean> deleteDemandDevice(@RequestParam("id") String id) {
        feasibilityDetailService.deleteDemandDevice(id);
        return success(true);
    }

    @GetMapping("/getDemandDevice")
    @Operation(summary = "获得设备采购意见")
    public CommonResult<DemandDeviceRespVO> getDemandDevice(@RequestParam("id") String id) {
        DemandDeviceDO device = feasibilityDetailService.getDemandDevice(id);
        return success(BeanUtils.toBean(device, DemandDeviceRespVO.class));
    }

    @GetMapping("/getDemandMaterialList")
    @Operation(summary = "获取零件关联工装采购意见列表")
    public CommonResult<List<DemandMaterialRespVO>> getDemandMaterialList(DemandMaterialReqVO reqVO) {
        List<DemandMaterialRespVO> list = feasibilityDetailService.getDemandMaterialList(reqVO);
        return success(BeanUtils.toBean(list, DemandMaterialRespVO.class));
    }

    @PostMapping("/createDemandMaterial")
    @Operation(summary = "创建工装采购意见")
    public CommonResult<String> createDemandMaterial(@Valid @RequestBody DemandMaterialReqVO createReqVO) {
        return success(feasibilityDetailService.createDemandMaterial(createReqVO));
    }

    @PutMapping("/updateDemandMaterial")
    @Operation(summary = "修改工装采购意见")
    public CommonResult<Boolean> updateDemandMaterial(@Valid @RequestBody DemandMaterialReqVO updateReqVO) {
        feasibilityDetailService.updateDemandMaterial(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteDemandMaterial")
    @Operation(summary = "删除工装采购意见")
    @Parameter(name = "id", description = "采购意见Id", required = true, example = "1024")
    public CommonResult<Boolean> deleteDemandMaterial(@RequestParam("id") String id) {
        feasibilityDetailService.deleteDemandMaterial(id);
        return success(true);
    }

    @GetMapping("/getDemandMaterial")
    @Operation(summary = "获得工装采购意见")
    public CommonResult<DemandMaterialRespVO> getDemandMaterial(@RequestParam("id") String id) {
        DemandMaterialDO device = feasibilityDetailService.getDemandMaterial(id);
        return success(BeanUtils.toBean(device, DemandMaterialRespVO.class));
    }

    //
    @GetMapping("/getDemandMeasureList")
    @Operation(summary = "获取零件关联量具采购意见列表")
    public CommonResult<List<DemandMeasureRespVO>> getDemandMeasureList(DemandMeasureReqVO reqVO) {
        List<DemandMeasureRespVO> list = feasibilityDetailService.getDemandMeasureList(reqVO);
        return success(BeanUtils.toBean(list, DemandMeasureRespVO.class));
    }

    @PostMapping("/createDemandMeasure")
    @Operation(summary = "创建量具采购意见")
    public CommonResult<String> createDemandMeasure(@Valid @RequestBody DemandMeasureReqVO createReqVO) {
        return success(feasibilityDetailService.createDemandMeasure(createReqVO));
    }

    @PutMapping("/updateDemandMeasure")
    @Operation(summary = "修改量具采购意见")
    public CommonResult<Boolean> updateDemandMeasure(@Valid @RequestBody DemandMeasureReqVO updateReqVO) {
        feasibilityDetailService.updateDemandMeasure(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteDemandMeasure")
    @Operation(summary = "删除量具采购意见")
    @Parameter(name = "id", description = "采购意见Id", required = true, example = "1024")
    public CommonResult<Boolean> deleteDemandMeasure(@RequestParam("id") String id) {
        feasibilityDetailService.deleteDemandMeasure(id);
        return success(true);
    }

    @GetMapping("/getDemandMeasure")
    @Operation(summary = "获得量具采购意见")
    public CommonResult<DemandMeasureRespVO> getDemandMeasure(@RequestParam("id") String id) {
        DemandMeasureDO deviceMeasure = feasibilityDetailService.getDemandMeasure(id);
        return success(BeanUtils.toBean(deviceMeasure, DemandMeasureRespVO.class));
    }

    @GetMapping("/getDemandCutterList")
    @Operation(summary = "获取零件关联刀具采购意见列表")
    public CommonResult<List<DemandCutterRespVO>> getDemandCutterList(DemandCutterReqVO reqVO) {
        List<DemandCutterRespVO> list = feasibilityDetailService.getDemandCutterList(reqVO);
        return success(BeanUtils.toBean(list, DemandCutterRespVO.class));
    }

    @PostMapping("/createDemandCutter")
    @Operation(summary = "创建刀具采购意见")
    public CommonResult<String> createDemandCutter(@Valid @RequestBody DemandCutterReqVO createReqVO) {
        return success(feasibilityDetailService.createDemandCutter(createReqVO));
    }

    @PutMapping("/updateDemandCutter")
    @Operation(summary = "修改刀具采购意见")
    public CommonResult<Boolean> updateDemandCutter(@Valid @RequestBody DemandCutterReqVO updateReqVO) {
        feasibilityDetailService.updateDemandCutter(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteDemandCutter")
    @Operation(summary = "删除刀具采购意见")
    @Parameter(name = "id", description = "采购意见Id", required = true, example = "1024")
    public CommonResult<Boolean> deleteDemandCutter(@RequestParam("id") String id) {
        feasibilityDetailService.deleteDemandCutter(id);
        return success(true);
    }

    @GetMapping("/getDemandCutter")
    @Operation(summary = "获得刀具采购意见")
    public CommonResult<DemandCutterRespVO> getDemandCutter(@RequestParam("id") String id) {
        DemandCutterDO device = feasibilityDetailService.getDemandCutter(id);
        return success(BeanUtils.toBean(device, DemandCutterRespVO.class));
    }

    @GetMapping("/getDemandHiltList")
    @Operation(summary = "获取零件关联刀柄采购意见列表")
    public CommonResult<List<DemandHiltRespVO>> getDemandHiltList(DemandHiltReqVO reqVO) {
        List<DemandHiltRespVO> list = feasibilityDetailService.getDemandHiltList(reqVO);
        return success(BeanUtils.toBean(list, DemandHiltRespVO.class));
    }

    @PostMapping("/createDemandHilt")
    @Operation(summary = "创建刀柄采购意见")
    public CommonResult<String> createDemandHilt(@Valid @RequestBody DemandHiltReqVO createReqVO) {
        return success(feasibilityDetailService.createDemandHilt(createReqVO));
    }

    @PutMapping("/updateDemandHilt")
    @Operation(summary = "修改刀柄采购意见")
    public CommonResult<Boolean> updateDemandHilt(@Valid @RequestBody DemandHiltReqVO updateReqVO) {
        feasibilityDetailService.updateDemandHilt(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteDemandHilt")
    @Operation(summary = "删除刀柄采购意见")
    @Parameter(name = "id", description = "采购意见Id", required = true, example = "1024")
    public CommonResult<Boolean> deleteDemandHilt(@RequestParam("id") String id) {
        feasibilityDetailService.deleteDemandHilt(id);
        return success(true);
    }

    @GetMapping("/getDemandHilt")
    @Operation(summary = "获得刀柄采购意见")
    public CommonResult<DemandHiltRespVO> getDemandHilt(@RequestParam("id") String id) {
        DemandHiltDO device = feasibilityDetailService.getDemandHilt(id);
        return success(BeanUtils.toBean(device, DemandHiltRespVO.class));
    }

    @GetMapping("/getQuotaPerPartInfo")
    @Operation(summary = "获得单件定额管理信息")
    public CommonResult<QuotaPerPartRespVO> getQuotaPerPartInfo(QuotaPerPartReqVO reqVO) {
        QuotaPerPartDO result = feasibilityDetailService.getQuotaPerPart(reqVO);
        return success(BeanUtils.toBean(result, QuotaPerPartRespVO.class));
    }

    @PostMapping("/createQuotaPerPart")
    @Operation(summary = "新增单件定额")
    public CommonResult<String> createQuotaPerPart(@Valid @RequestBody QuotaPerPartSaveReqVO createReqVO) {
        return success(feasibilityDetailService.createQuotaPerPart(createReqVO));
    }

    @PutMapping("/updateQuotaPerPart")
    @Operation(summary = "修改单件定额")
    public CommonResult<Boolean> updateQuotaPerPart(@Valid @RequestBody QuotaPerPartSaveReqVO updateReqVO) {
        feasibilityDetailService.updateQuotaPerPart(updateReqVO);
        return success(true);
    }

    @PutMapping("/updateFeasibilityTaskStatus")
    @Operation(summary = "更新技术评估任务状态为2评估中")
    public CommonResult<Boolean> updateFeasibilityTaskStatus(@Valid @RequestBody FeasibilityTaskReqVO updateReqVO) {
        feasibilityDetailService.updateFeasibilityTaskStatus(updateReqVO);
        return success(true);
    }

    @PutMapping("/startProcessInstance")
    @Operation(summary = "技术评估发起流程")
    public CommonResult<Boolean> startProcessInstance(@Valid @RequestBody FeasibilityTaskReqVO updateReqVO) {
        feasibilityDetailService.startProcessInstance(updateReqVO);
        return success(true);
    }

    @GetMapping("getPartDetailByTaskId")
    @Operation(summary = "审批详情页获取零件技术评估详细信息")
    public CommonResult<ProjPartBomRespVO> getPartDetailByTaskId(@RequestParam("id") String id) {
        ProjPartBomRespVO respVO = feasibilityDetailService.getPartDetailByTaskId(id);
        return success(respVO);
    }
}
