package com.miyu.module.pdm.controller.admin.processPlanDetail;



import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigReqDTO;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ResourceSelectedReqVO;

import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.*;

import com.miyu.module.pdm.controller.admin.processTask.vo.ProcedureRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessRespVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureFileDO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.ProcessChangeDO;
import com.miyu.module.pdm.service.process.processService;
import com.miyu.module.pdm.service.processPlanDetail.ProcessPlanDetailService;

import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertSet;
import static com.miyu.module.pdm.enums.ErrorCodeConstants.*;

import com.miyu.module.qms.api.dto.InspectionSchemeRespDTO;
import com.miyu.module.qms.api.dto.InspectionSchemeSaveReqDTO;
import com.miyu.module.qms.api.inspectionscheme.InspectionSchemeApi;
import com.miyu.module.qms.enums.InspectionSchemeTypeEnum;
import io.swagger.v3.oas.annotations.Operation;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.ibatis.annotations.Param;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "PDM - 技术评估任务")
@RestController
@RequestMapping("pdm/process-plan-detail")
@Validated
public class ProcessPlanDetailController {

    @Resource
    private ProcessPlanDetailService processPlanDetailService;

    @Resource
    private processService processService;

    @Resource
    private InspectionSchemeApi inspectionSchemeApi;

    @Resource
    private MaterialMCCApi materialMCCApi;

    //工艺更改单
    @GetMapping("/getChangeOrderList")
    @Operation(summary = "获得工艺更改单列表")
    public CommonResult<List<ProcessChangeRespVO>> getChangeOrderList(@Valid ProcessChangeReqVO reqVO) {
        List<ProcessChangeRespVO> list = processPlanDetailService.getChangeOrderList(reqVO);
        return success(list);
    }

    @GetMapping("/getChangeDetailList")
    @Operation(summary = "获得ChangeDetail列表")
    public CommonResult<List<ProcessChangeRespVO>> getChangeDetailList(@RequestParam("processChangeId") String processChangeId) {
        List<ProcessChangeRespVO> list = processPlanDetailService.getChangeDetailList(processChangeId);
        return success(list);
    }

    @GetMapping("/getChangeOrderById")
    @Operation(summary = "获得工艺更改单")
    public CommonResult<ProcessChangeRespVO> getChangeOrderById(@RequestParam("id") String id) {
        ProcessChangeDO change = processPlanDetailService.getChangeOrderById(id);
        return success(BeanUtils.toBean(change, ProcessChangeRespVO.class));
    }

    @DeleteMapping("/deleteChangeOrderById")
    @Operation(summary = "删除工艺更改单")
    public CommonResult<Boolean> deleteChangeOrderById(@RequestParam("id") String id) {
        processPlanDetailService.deleteChangeOrderById(id);
        return success(true);
    }

    @DeleteMapping("/deleteOrderDetailById")
    @Operation(summary = "删除工艺更改单详细")
    public CommonResult<Boolean> deleteOrderDetailById(@RequestParam("id") String id) {
        processPlanDetailService.deleteOrderDetailById(id);
        return success(true);
    }

    //获取更改单详情动态项
    @GetMapping("/getChangeDetailItemList")
    @Operation(summary = "获得工艺更改单详细")
    public CommonResult<List<ProcessChangeRespVO>> getChangeDetailItemList(@Valid ProcessChangeReqVO reqVO) {
        List<ProcessChangeRespVO> list = processPlanDetailService.getChangeDetailItem(reqVO);
        return success(list);
    }

    @GetMapping("/getProcessChangeById")
    @Operation(summary = "获取更改单进度页数据")
    public CommonResult<ProcessChangeRespVO> getProcessChangeById(@RequestParam("id") String id) {
        ProcessChangeRespVO respVO = processPlanDetailService.getProcessChangeById(id);
        return success(respVO);
    }

    @GetMapping("/getProcessChangeDetailById")
    @Operation(summary = "获取更改单进度页列表数据")
    public CommonResult<List<ProcessChangeRespVO>> getProcessChangeDetailById(@Valid ProcessChangeReqVO reqVO) {
        List<ProcessChangeRespVO> list = processPlanDetailService.getProcessChangeDetailById(reqVO);
        return success(list);
    }

    @PostMapping("/saveOrderItem")
    @Operation(summary = "添加更改单")
    public CommonResult<String> saveOrderItem(@Valid @RequestBody ProcessChangeReqVO createReqVO) {
        processPlanDetailService.saveOrderItem(createReqVO);
        return success("保存成功");
    }

    @PutMapping("/updateOrderItem")
    @Operation(summary = "修改更改单")
    public CommonResult<Boolean> updateOrderItem(@Valid @RequestBody ProcessChangeReqVO updateReqVO) {
        processPlanDetailService.updateOrderItem(updateReqVO);
        return success(true);
    }

    @PutMapping("/startProcessChangeInstance")
    @Operation(summary = "工艺更改单审批")
    public CommonResult<Boolean> startProcessChangeInstance(@Valid @RequestBody ProcessChangeReqVO updateReqVO) {
        processPlanDetailService.startProcessChangeInstance(updateReqVO);
        return success(true);
    }
    @DeleteMapping("/deleteOrderItem")
    @Operation(summary = "删除更改单")
    @Parameter(name = "id", description = "Id", required = true, example = "1024")
    public CommonResult<Boolean> deleteOrderItem(@RequestParam("id") String id) {
        processPlanDetailService.deleteOrderItem(id);
        return success(true);
    }

    //数控程序
    @GetMapping("/getNC")
    @Operation(summary = "获得数控程序信息")
    public CommonResult<List<NcRespVO>> getNc(@Valid ProcessVersionNcReqVO req1VO) {
        List<NcRespVO> list = processPlanDetailService.getNc(req1VO);
        return success(list);
    }
    @PostMapping("/saveNC")
    @Operation(summary = "保存数控程序信息")
    public CommonResult<String> saveNc(@Valid @RequestBody NcReqVO reqVO,@Valid @RequestBody ProcessVersionNcReqVO req1VO) {
        processPlanDetailService.saveNc(reqVO,req1VO);
        return success("保存成功");
    }
    @DeleteMapping("/deleteNC")
    @Operation(summary = "删除数控程序信息")
    public CommonResult<Boolean> deleteNc(@Valid NcReqVO reqVO,@Valid  ProcessVersionNcReqVO req1VO) {
        processPlanDetailService.deleteNc(reqVO,req1VO);
        return success(true);
    }

    @PutMapping("/updateStep")
    @Operation(summary = "修改工步")
    public CommonResult<Boolean> updateStepById(@Valid @RequestBody ProcessPlanDetailReqVO updateReqVO) {
        processPlanDetailService.updateStep(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteStep")
    @Operation(summary = "删除工步")
    public CommonResult<Boolean> deleteProcessStepById(@RequestParam("id") String id) {
        processPlanDetailService.deleteStep(id);
        return success(true);
    }

    @PostMapping("/updateCutternum")
    @Operation(summary = "更改设备预估工时")
    public CommonResult<Boolean> updateCutternum(@Valid StepDetailReqVO reqVO) {
        processPlanDetailService.updateCutternum(reqVO);
        return success(true);
    }

    @PostMapping("/addProcedure")
    @Operation(summary = "添加工序")
    public CommonResult<String> addProcedure(@Valid @RequestBody ProcessPlanDetailReqVO addReqVO) {
        return success(processPlanDetailService.addProcedure(addReqVO));
    }

    @PostMapping("/addStep")
    @Operation(summary = "添加工步")
    public CommonResult<String> addStep(@Valid @RequestBody ProcessPlanDetailReqVO addReqVO) {
        return success(processPlanDetailService.addStep(addReqVO));
    }
    @PutMapping("/updateProcedure")
    @Operation(summary = "修改工序")
    public CommonResult<Boolean> updateProcedureById(@Valid @RequestBody ProcessPlanDetailReqVO updateReqVO) {
        processPlanDetailService.updateProcedure(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteProcedure")
    @Operation(summary = "删除工序")
    public CommonResult<Boolean> updateProcedureById(@RequestParam("id") String id) {
        processPlanDetailService.deleteProcedure(id);
        return success(true);
    }
    @GetMapping("getPartListByProcessPlanDetailId")
    @Operation(summary = "获得当前项目关联零件目录列表", description = "根据选中项目id")
    public CommonResult<List<ProcessPlanDetailRespVO>> getPartListByProcessPlanDetailId(@RequestParam("projectCode") String projectCode) {
        List<ProcessPlanDetailRespVO> list = processPlanDetailService.getPartListByProcessPlanDetailId(projectCode);
        return success(BeanUtils.toBean(list,ProcessPlanDetailRespVO.class));
    }
//    @GetMapping({"/list-all-simple", "/simple-list"})
//    @Operation(summary = "获取用户精简信息列表", description ="主要用于前端的下拉选项")
//    public CommonResult<List<ProcessPlanDetailRespVO>> getSimpleUserList() {
//        // 拼接数据
//        Map<Long, StepDO> deptMap = processPlanDetailService.getStepMap(
//                convertList(ProcessPlanDetailRespVO::getProcessVersionId));
//        return success(UserConvert.INSTANCE.convertSimpleList(list, deptMap));
//    }
    @GetMapping("getProjPartBomTreeList")
    @Operation(summary = "获得工艺详细设计结构树")
    public CommonResult<List<ProjPartBomTreeRespVO>> getProjPartBomTreeList(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomTreeRespVO> list = processPlanDetailService.getProjPartBomTreeList(reqVO);
        return success(list);
    }

    @GetMapping("getProjPartBomTreeByTaskId")
    @Operation(summary = "审批详情页-获取单个工序工艺详细设计结构树")
    public CommonResult<List<ProjPartBomTreeRespVO>> getProjPartBomTreeByTaskId(@Valid ProjPartBomReqVO reqVO) {
        List<ProjPartBomTreeRespVO> list = processPlanDetailService.getProjPartBomTreeByTaskId(reqVO);
        return success(list);
    }

    @GetMapping("getProcessPlanDetailList")
    @Operation(summary = "获得工艺编制列表", description = "根据projectCode和partVersionId筛选")
    public CommonResult<List<ProcessPlanDetailRespVO>> selectProcessPlanDetailList(@Valid ProcessPlanDetailReqVO reqVO) {
        List<ProcessPlanDetailRespVO> list = processPlanDetailService.selectProcessPlanDetailList(reqVO);
        return success(list);
    }
    @GetMapping("getProcessPlanDetail")
    @Operation(summary = "获取零件工艺规程详细信息")
    public CommonResult<ProcessPlanDetailRespVO> getProcessPlanDetail(@RequestParam("id") String id) {
        ProcessPlanDetailRespVO respVO = processPlanDetailService.getProcessPlanDetail(id);
        return success(respVO);
    }
    @GetMapping("getProcessPlanDetailTreeList")
    @Operation(summary = "获得当前选中项目的零件目录结构树")
    public CommonResult<List<ProcessPlanDetailRespVO>> getProcessPlanDetailTreeList(@Valid ProcessPlanDetailReqVO reqVO) {
        List<ProcessPlanDetailRespVO> list = processPlanDetailService.getProcessPlanDetailTreeList(reqVO);
        return success(list);
    }

    @GetMapping("getProcess")
    @Operation(summary = "获得工艺规程数据")
    public CommonResult<ProcessPlanDetailRespVO> getProcess(@RequestParam("id") String id){
        ProcessPlanDetailRespVO respVO = processPlanDetailService.getProcess(id);
        return success(respVO);
    }

//    @GetMapping("getProcedure")
//    @Operation(summary = "获得工序数据")
//    public CommonResult<ProcessPlanDetailRespVO> getProcedure(@Param("id") String id){
//        ProcessPlanDetailRespVO respVO = processPlanDetailService.getProcedure(id);
//        return success(respVO);
//    }
    @GetMapping("getProcedure")
    @Operation(summary = "获得工序数据")
    public CommonResult<ProcedureRespVO> getProcedure(@Param("id") String id){
        ProcedureRespVO respVO = processPlanDetailService.getProcedure(id);
        return success(respVO);
    }
//    @GetMapping("getStep")
//    @Operation(summary = "获得工步数据")
//    public CommonResult<ProcessPlanDetailRespVO> getStep(@Param("id") String id){
//        ProcessPlanDetailRespVO respVO = processPlanDetailService.getStep(id);
//        return success(respVO);
//    }

    @GetMapping("getStep")
    @Operation(summary = "获得工步数据")
    public CommonResult<StepRespVO> getStep(@Param("id") String id){
        StepRespVO respVO = processPlanDetailService.getStep(id);
        return success(respVO);
    }

    @GetMapping("/getResourceListByProcedure")
    @Operation(summary = "工序-获取关联制造资源列表")
    public CommonResult<List<ProcedureDetailRespVO>> getResourceListByProcedure(ResourceSelectedReqVO reqVO) {
        List<ProcedureDetailRespVO> list = processPlanDetailService.getResourceListByProcedure(reqVO);
        return success(BeanUtils.toBean(list, ProcedureDetailRespVO.class));
    }

    @PostMapping("/deleteSelectedResourceProcedure")
    @Operation(summary = "工序-删除选择资源(设备、刀具、工装)")
    public CommonResult<Boolean> deleteSelectedDeviceProcedure(@Valid ProcedureDetailReqVO reqVO) {
        processPlanDetailService.deleteSelectedDeviceProcedure(reqVO);
        return success(true);
    }

    @PostMapping("/saveSelectedResourceProcedure")
    @Operation(summary = "工序-保存选择资源(设备、刀具、工装)")
    public CommonResult<Boolean> saveSelectedResourceProcedure(@Valid ResourceSelectedReqVO reqVO) {
        processPlanDetailService.saveSelectedResourceProcedure(reqVO);
        return success(true);
    }

    @GetMapping("/getResourceListByStep")
    @Operation(summary = "工序-获取关联制造资源列表")
    public CommonResult<List<StepDetailRespVO>> getResourceListByStep(ResourceSelectedReqVO reqVO) {
        List<StepDetailRespVO> list = processPlanDetailService.getResourceListByStep(reqVO);
        return success(BeanUtils.toBean(list, StepDetailRespVO.class));
    }

    @PostMapping("/deleteSelectedResourceStep")
    @Operation(summary = "工序-删除选择资源(设备、刀具、工装)")
    public CommonResult<Boolean> deleteSelectedDeviceStep(@Valid StepDetailReqVO reqVO) {
        processPlanDetailService.deleteSelectedDeviceStep(reqVO);
        return success(true);
    }

    @PostMapping("/saveSelectedResourceStep")
    @Operation(summary = "工序-保存选择资源(设备、刀具、工装)")
    public CommonResult<Boolean> saveSelectedResourceStep(@Valid ResourceSelectedReqVO reqVO) {
        processPlanDetailService.saveSelectedResourceStep(reqVO);
        return success(true);
    }
    @GetMapping("/getProcedureFiles")
    @Operation(summary = "获得工序草图列表")
    public CommonResult<List<ProcedureFileRespVO>> getProcedureFiles(@Valid ProcedureFileSaveReqVO reqVO) {
        List<ProcedureFileRespVO> list = processPlanDetailService.getProcedureFiles(reqVO);
        return success(list);
    }
    @GetMapping("/getProcedureFileById")
    @Operation(summary = "获得工序草图")
    public CommonResult<ProcedureFileRespVO> getProcedureFileById(@RequestParam("id") String id) {
        ProcedureFileDO procedureFileDO = processPlanDetailService.getProcedureFileById(id);
        return success(BeanUtils.toBean(procedureFileDO, ProcedureFileRespVO.class));
    }
    @PostMapping("/saveProcedureFile")
    @Operation(summary = "保存工艺草图")
    public CommonResult<String> saveSelectedResource(@Valid @RequestBody ProcedureFileSaveReqVO reqVO) {
        processPlanDetailService.saveSelectedResource(reqVO);
        return success("保存成功");
    }
    @DeleteMapping("/deleteProcedureFile")
    @Parameter(name = "id",required = true)
    @Operation(summary = "删除工艺草图")
    public CommonResult<Boolean> deleteSelectedResourc(@RequestParam("id") String id) {
        processPlanDetailService.deleteSelectedResource(id);
        return success(true);
    }

    @GetMapping("/getStepFiles")
    @Operation(summary = "获得工步草图")
    public CommonResult<List<StepFileRespVO>> getProcedureFiles(@Valid StepFileSaveReqVO reqVO) {
        List<StepFileRespVO> list = processPlanDetailService.getStepFiles(reqVO);
        return success(list);
    }
    @PostMapping("/saveStepFile")
    @Operation(summary = "保存工步草图")
    public CommonResult<String> saveSelectedResource(@Valid @RequestBody StepFileSaveReqVO reqVO) {
        processPlanDetailService.saveSelectedStepFile(reqVO);
        return success("保存成功");
    }
    @DeleteMapping("/deleteStepFile")
    @Operation(summary = "删除工步草图")
    @Parameter(name = "id",required = true)
    public CommonResult<Boolean> deleteSelectedResource(@RequestParam("id") String id) {
        processPlanDetailService.deleteSelectedStepFile(id);
        return success(true);
    }

    @PutMapping("/startProcessInstance")
    @Operation(summary = "工艺规程发起流程")
    public CommonResult<Boolean> startProcessInstance(@Valid @RequestBody ProcessDetailTaskReqVO updateReqVO) {
        processPlanDetailService.startProcessInstance(updateReqVO);
        return success(true);
    }

    @PutMapping("/updateProcessDetailTaskStatus")
    @Operation(summary = "更新工艺详细设计任务状态为2编制中")
    public CommonResult<Boolean> updateProcessDetailTaskStatus(@Valid @RequestBody ProcessDetailTaskReqVO updateReqVO) {
        processPlanDetailService.updateProcessDetailTaskStatus(updateReqVO);
        return success(true);
    }

    @GetMapping("/getProcedureSchemaItemList")
    @Operation(summary = "获取检测项")
    public CommonResult<ProcedureSchemaItemNewRespVO> getProcedureSchemaItemList(@Valid ProcedureSchemaItemReqVO reqVO) {
        ProcedureSchemaItemNewRespVO respVO = new ProcedureSchemaItemNewRespVO();
        CommonResult<InspectionSchemeRespDTO> inspectionSchemeDto = inspectionSchemeApi.getInspectionSchemeByProcessId(reqVO.getProcessVersionId(), reqVO.getProcedureId());
        if(inspectionSchemeDto != null && inspectionSchemeDto.getData() != null) {
            respVO.setInspectionSchemeId(inspectionSchemeDto.getData().getId());
            List<ProcedureSchemaItemRespVO> list = BeanUtils.toBean(inspectionSchemeDto.getData().getItems(), ProcedureSchemaItemRespVO.class);
            if(list != null && list.size() > 0) {
                for (ProcedureSchemaItemRespVO procedureSchemaItemRespVO : list) {
                    procedureSchemaItemRespVO.setInspectionSchemeId(inspectionSchemeDto.getData().getId());
                    procedureSchemaItemRespVO.setProcessVersionId(reqVO.getProcessVersionId());
                    procedureSchemaItemRespVO.setProcedureId(reqVO.getProcedureId());
                }
                respVO.setItemList(list);
            }
        }
        return success(respVO);
    }

    @PostMapping("/saveProcedureSchemaItem")
    @Operation(summary = "添加检测项")
    public CommonResult<Boolean> saveProcedureSchemaItem(@Valid @RequestBody List<ProcedureSchemaItemReqVO> createReqVO) {
        if(createReqVO != null && createReqVO.size() > 0) {
            ProcedureSchemaItemReqVO req = createReqVO.get(0);
            String processVersionId = req.getProcessVersionId();
            String procedureId = req.getProcedureId();
            ProcessRespVO process = processService.selectProcessById(processVersionId);
            ProcedureRespVO procedure = processPlanDetailService.getProcedure(procedureId);
            InspectionSchemeSaveReqDTO schemeSaveReqDTO = new InspectionSchemeSaveReqDTO();
            if(req.getInspectionSchemeId() != null && !req.getInspectionSchemeId().equals("")) {
                schemeSaveReqDTO.setId(req.getInspectionSchemeId());
            }
            schemeSaveReqDTO.setTechnologyId(processVersionId);
            schemeSaveReqDTO.setProcessId(procedureId);
            schemeSaveReqDTO.setSchemeName(process.getProcessName()+"-"+process.getProcessVersion()+"-"+procedure.getProcedureName());
            schemeSaveReqDTO.setSchemeNo(process.getProcessCode()+"-"+process.getProcessVersion()+"-"+procedure.getProcedureNum());
            schemeSaveReqDTO.setSchemeType(InspectionSchemeTypeEnum.PRODUCTTYPE.getStatus());
            // 区分不同的物料类型：第一道工序(来料检验)时传毛坯材料对应的物料id，其他工序时通过零件图号查物料类型id
            String materialConfigId = "";
            String materialNumber = "";
            String materialName = "";
            if(procedure.getProcedureNum().equals("05") || procedure.getProcedureName().equals("来料检验")) {
                materialConfigId = process.getMaterialId();
                materialNumber = process.getMaterialNumber();
                materialName = process.getMaterialName();
            } else {
                MaterialConfigReqDTO dto = new MaterialConfigReqDTO();
                String partNumber = process.getPartNumber();
                List<MaterialConfigRespDTO> materialConfigList = materialMCCApi.getMaterialConfigListByTypeCode(dto.setMaterialNumber(partNumber)).getCheckedData();
                if(materialConfigList != null && materialConfigList.size() > 0) {
                    MaterialConfigRespDTO materialConfigRespDTO = materialConfigList.get(0);
                    materialConfigId = materialConfigRespDTO.getId();
                    materialNumber = materialConfigRespDTO.getMaterialNumber();
                    materialName = materialConfigRespDTO.getMaterialName();
                }
            }
            schemeSaveReqDTO.setMaterialConfigId(materialConfigId);
            schemeSaveReqDTO.setMaterialNumber(materialNumber);
            schemeSaveReqDTO.setMaterialName(materialName);
            schemeSaveReqDTO.setIsInspect(procedure.getIsInspect().toString());
            List<InspectionSchemeSaveReqDTO.Item> itemsList = BeanUtils.toBean(createReqVO,InspectionSchemeSaveReqDTO.Item.class);
            schemeSaveReqDTO.setItems(itemsList);
            inspectionSchemeApi.createInspectionScheme(schemeSaveReqDTO);
        }
        return success(true);
    }

    @PutMapping("/updateProcedureSchemaItem")
    @Operation(summary = "修改检测项")
    public CommonResult<Boolean> updateProcedureSchemaItem(@Valid @RequestBody ProcedureSchemaItemReqVO updateReqVO) {
        processPlanDetailService.updateProcedureSchemaItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/deleteProcedureSchemaItem")
    @Operation(summary = "删除检测项")
    @Parameter(name = "id", description = "采购意见Id", required = true, example = "1024")
    public CommonResult<Boolean> deleteProcedureSchemaItem(@RequestParam("id") String id) {
        inspectionSchemeApi.deleteInspectionSchemeItemById(id);
        return success(true);
    }

    @GetMapping("/getStepAttributeValList")
    @Operation(summary = "获得工步自定义属性")
    public CommonResult<List<CustomizedAttributeValRespVO>> getStepAttributeValList(@RequestParam("objectId") String objectId){
        List<CustomizedAttributeValRespVO> list = processPlanDetailService.getStepAttributeValList(objectId);
        return success(list);
    }
}
