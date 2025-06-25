package com.miyu.cloud.mpc.controller.admin;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.controller.admin.failurerecord.vo.FailureRecordSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import com.miyu.cloud.dms.dal.dataobject.failurerecord.FailureRecordDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.service.devicetype.DeviceTypeService;
import com.miyu.cloud.dms.service.failurerecord.FailureRecordService;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.dms.service.linestationgroup.LineStationGroupService;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecordstep.BatchRecordStepDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dto.externalManufacture.*;
import com.miyu.cloud.mcs.dto.manufacture.McsBatchRecordDTO;
import com.miyu.cloud.mcs.dto.manufacture.McsBatchRecordEventDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsPlanPartDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsPlanProcessDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsPlanStepNcDTO;
import com.miyu.cloud.mcs.dto.productionProcess.ProcedureFileRespDTO;
import com.miyu.cloud.mcs.dto.resource.McsDeviceDTO;
import com.miyu.cloud.mcs.dto.resource.McsMaterialDeliveryDTO;
import com.miyu.cloud.mcs.restServer.api.ManufacturingService;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcessPlanDetailRespDTO;
import com.miyu.cloud.mcs.restServer.service.technology.dto.StepRespDTO;
import com.miyu.cloud.mcs.restServer.service.warehouse.WarehouseRestService;
import com.miyu.cloud.mcs.service.batchorder.BatchOrderService;
import com.miyu.cloud.mcs.service.batchrecord.BatchRecordService;
import com.miyu.cloud.mcs.service.batchrecordstep.BatchRecordStepService;
import com.miyu.cloud.mcs.service.orderform.OrderFormService;
import com.miyu.cloud.mcs.service.taskAdditionalInformation.TaskAdditionalInfoService;
import com.miyu.cloud.mpc.service.InstructionExecuteService;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.miyu.cloud.dms.enums.DictConstants.*;
import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.module.wms.enums.DictConstants.WMS_ORDER_DETAIL_STATUS_2;
import static com.miyu.module.wms.enums.DictConstants.WMS_ORDER_DETAIL_STATUS_3;

@Tag(name = "生产-外部接口")
@RestController
@RequestMapping("/mcs/rest")
public class ManufacturingProcessControl{

    @Resource
    private LineStationGroupService lineStationGroupService;
    @Resource
    private LedgerService ledgerService;
    @Resource
    private DeviceTypeService deviceTypeService;
    @Resource
    private BatchRecordService batchRecordService;
    @Resource
    private OrderFormService orderFormService;
    @Resource
    private BatchOrderService batchOrderService;
    @Resource
    private BatchRecordStepService batchRecordStepService;
    @Resource
    private TechnologyRestService technologyRestService;
    @Resource
    private ManufacturingService manufacturingService;
    @Resource
    private FailureRecordService failureRecordService;
    @Resource
    private WarehouseRestService warehouseRestService;
    @Resource
    private InstructionExecuteService instructionExecuteService;
    @Resource
    private TaskAdditionalInfoService taskAdditionalInfoService;

    @Operation(summary = "三坐标检测-根据物料编码获取任务")
    @GetMapping(value = "/WorkOrder/findMeasureByMaterialCode")
    @Parameter(name = "materialCode", description = "物料编码", required = true, example = "BC-1234")
    public CommonResult<McsExternalPlanDTO> getMeasurePlanByBarCode(@RequestParam("materialCode") String materialCode){
        return this.getPlanByBarCode(materialCode, "QCL");
    }

    @Operation(summary = "蒙皮拉伸机-检验任务编码")
    @GetMapping(value = "/WorkOrder/checkStretchPlanCode")
    public CommonResult<List<Map<String,String>>> checkStretchPlanCode(){
        LineStationGroupDO line = lineStationGroupService.getOneByCode("SSPL");
        if (line == null) return CommonResult.success(new ArrayList<>());
        LambdaQueryWrapper<BatchRecordDO> queryWrapperBR = new LambdaQueryWrapper<>();
        queryWrapperBR.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        queryWrapperBR.eq(BatchRecordDO::getProcessingUnitId, line.getId());
        queryWrapperBR.orderByAsc(BatchRecordDO::getPlanStartTime);
        List<BatchRecordDO> recordList = batchRecordService.list(queryWrapperBR);
        if (recordList.size() == 0) return CommonResult.success(new ArrayList<>());
        BatchRecordDO batchRecordDO = recordList.get(0);
        OrderFormDO orderForm = orderFormService.getOrderForm(batchRecordDO.getOrderId());
        List<Map<String,String>> result = new ArrayList<>();
        Map<String,String> map = new HashMap<>();
        result.add(map);
        map.put("orderNumber", orderForm.getOrderNumber());
        return CommonResult.success(result);
    }

    @Operation(summary = "蒙皮拉伸机-根据物料编码获取任务")
    @GetMapping(value = "/WorkOrder/findStretchByMaterialCode")
    public CommonResult<List<McsExternalSSPLPlanDTO>> getStretchPlanByBarCode(){
        LineStationGroupDO line = lineStationGroupService.getOneByCode("SSPL");
        if (line == null) return CommonResult.success(new ArrayList<>());
        LambdaQueryWrapper<BatchRecordDO> queryWrapperBR = new LambdaQueryWrapper<>();
        queryWrapperBR.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        queryWrapperBR.eq(BatchRecordDO::getProcessingUnitId, line.getId());
        queryWrapperBR.orderByAsc(BatchRecordDO::getPlanStartTime);
        List<BatchRecordDO> recordList = batchRecordService.list(queryWrapperBR);
        if (recordList.size() == 0) return CommonResult.success(new ArrayList<>());
        BatchRecordDO batchRecordDO = recordList.get(0);
        OrderFormDO orderForm = orderFormService.getOrderForm(batchRecordDO.getOrderId());
        ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderForm.getId(), orderForm.getTechnologyId());
        Optional<ProcedureRespDTO> first = technology.getProcedureList().stream().filter(item -> item.getId().equals(batchRecordDO.getProcessId())).findFirst();
        if (!first.isPresent()) throw new ServiceException(5004,"未找到相应工序");
        ProcedureRespDTO procedureRespDTO = first.get();
        StepRespDTO stepRespDTO = procedureRespDTO.getStepList().get(0);
        List<McsExternalSSPLPlanDTO> result = new ArrayList<>();
        McsExternalSSPLPlanDTO planDTO = new McsExternalSSPLPlanDTO();
        result.add(planDTO);
        LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchRecordDO::getOrderId, orderForm.getId());
        queryWrapper.eq(BatchRecordDO::getProcessId, batchRecordDO.getProcessId());
        queryWrapper.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_NEW, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        List<BatchRecordDO> list = batchRecordService.list(queryWrapper);
        Set<String> collect = list.stream().map(BatchRecordDO::getBatchId).collect(Collectors.toSet());
        List<BatchOrderDO> list1 = batchOrderService.list(new LambdaQueryWrapper<BatchOrderDO>().in(BatchOrderDO::getId, collect));
        Set<String> barCodeList = list1.stream().map(BatchOrderDO::getBarCode).collect(Collectors.toSet());
        planDTO.setMaterialCode(new ArrayList<>(barCodeList));
        planDTO.setOrderNumber(orderForm.getOrderNumber());
        planDTO.setProcessNumber(procedureRespDTO.getProcedureNum());
        planDTO.setProcessName(procedureRespDTO.getProcedureName());
        planDTO.setStepNumber(stepRespDTO.getStepNum());
        planDTO.setStepName(stepRespDTO.getStepName());
        String deviceId = batchRecordDO.getDeviceId();
        LedgerDO ledger = ledgerService.getLedger(deviceId);
        planDTO.setDeviceNumber(ledger.getCode());
        planDTO.setProcessCode(technology.getProcessCode());
        planDTO.setProcessSpecificationName(technology.getProcessName());
        planDTO.setProcessSchemeCode(technology.getProcessSchemeCode());
        planDTO.setProcessVersion(technology.getProcessVersion());
        planDTO.setPartNumber(technology.getPartNumber());
        planDTO.setPartName(technology.getPartName());
        planDTO.setDescription(procedureRespDTO.getDescription());
        planDTO.setDescriptionPreview(procedureRespDTO.getDescriptionPreview());
        planDTO.setFileList(BeanUtils.toBean(procedureRespDTO.getFileList(), ProcedureFileRespDTO.class));
        planDTO.setSingleSize(technology.getSingleSize());
        planDTO.setGroupSize(technology.getGroupSize());
        planDTO.setMaterialNumber(technology.getMaterialNumber());
        planDTO.setMaterialSpecification(technology.getMaterialSpecification());
        // TODO: 2025/2/21 临时加工文件内容
        try {
            byte[] encoded = Files.readAllBytes(Paths.get("E:/cache/ceshi.hyzz"));
            // 检测并处理BOM
            if (encoded.length >= 3 && encoded[0] == (byte)0xEF && encoded[1] == (byte)0xBB && encoded[2] == (byte)0xBF) {
                encoded = Arrays.copyOfRange(encoded, 3, encoded.length);
            }
            String content = new String(encoded, StandardCharsets.UTF_8);
//                    String json = "{\"aaa\":\"bbb\"}";
            planDTO.setProcessingJson(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (ProcedureDetailRespDTO procedureDetailRespDTO : procedureRespDTO.getResourceList()) {
            if (procedureDetailRespDTO.getResourcesType() == PROCESS_RESOURCES_TYPE_TOOL) {
                planDTO.setToolTypeNumber(procedureDetailRespDTO.getMaterialNumber());
                planDTO.setToolName(procedureDetailRespDTO.getName());
                planDTO.setToolSpecification(procedureDetailRespDTO.getSpecification());
            }
        }
        return CommonResult.success(result);
    }

    @Operation(summary = "蒙皮拉伸机-任务开工")
    @PostMapping(value = "/WorkOrder/stretchPlanStart")
    public CommonResult<?> stretchPlanStart(@RequestBody McsExternalPlanStartDTO externalPlanStartDTO){
        LineStationGroupDO line = lineStationGroupService.getOneByCode("SSPL");
        if (line == null) return CommonResult.success(new ArrayList<>());
        List<OrderFormDO> list2 = orderFormService.list(new LambdaQueryWrapper<OrderFormDO>().eq(OrderFormDO::getOrderNumber, externalPlanStartDTO.getBatchNumber()));
        OrderFormDO orderFormDO = list2.get(0);
        List<LedgerDO> list1 = ledgerService.list(new LambdaQueryWrapper<LedgerDO>().eq(LedgerDO::getLintStationGroup, line.getId()));
        LedgerDO ledgerDO = list1.get(0);
        String barCode = externalPlanStartDTO.getMaterialCode();
        if (StringUtils.isBlank(barCode)) {
            barCode = ledgerDO.getTechnicalParameter3();
        }
        if (StringUtils.isBlank(barCode)) throw new ServiceException(5004, "当前设备不存在物料");
        LambdaQueryWrapper<BatchOrderDO> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(BatchOrderDO::getOrderId, orderFormDO.getId());
        queryWrapper1.eq(BatchOrderDO::getBarCode, barCode);
        List<BatchOrderDO> list3 = batchOrderService.list(queryWrapper1);
        if (list3.size() == 0) throw new ServiceException(5004, "未找到任务,或任务与物料不符" + externalPlanStartDTO.getBatchNumber() + "-" + barCode);
        BatchOrderDO batchOrderDO = list3.get(0);
        LambdaQueryWrapper<BatchRecordDO> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(BatchRecordDO::getBatchId, batchOrderDO.getId());
        queryWrapper2.eq(BatchRecordDO::getProcedureNum, externalPlanStartDTO.getProcessNumber());
        List<BatchRecordDO> list4 = batchRecordService.list(queryWrapper2);
        BatchRecordDO batchRecordDO = list4.get(0);
        McsBatchRecordEventDTO eventDTO = new McsBatchRecordEventDTO();
        eventDTO.setBatchRecordId(batchRecordDO.getId());
        eventDTO.setBarCode(barCode);
        eventDTO.setDeviceUnitId(ledgerDO.getId());
        //todo 维护设备上登录任务信息
        eventDTO.setOperatorId("1");
        eventDTO.setProgress(externalPlanStartDTO.getProgress());
        eventDTO.setOperatingTime(externalPlanStartDTO.getRealStart());
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
        queryWrapper.orderByAsc(BatchRecordStepDO::getStepOrder);
        List<BatchRecordStepDO> list = batchRecordStepService.list(queryWrapper);
        if (list.size() > 0) {
            BatchRecordStepDO batchRecordStepDO = list.get(0);
            eventDTO.setStepId(batchRecordStepDO.getStepId());
        }
        return manufacturingService.batchRecordStart(eventDTO);
    }

    @Operation(summary = "蒙皮拉伸机-任务完工")
    @PostMapping(value = "/WorkOrder/stretchPlanEnd")
    @Parameter(name = "materialCode", description = "物料编码", required = true, example = "BC-1234")
    public CommonResult<?> stretchPlanEnd(@RequestBody McsExternalPlanEndDTO externalPlanEndDTO){
        LineStationGroupDO line = lineStationGroupService.getOneByCode("SSPL");
        if (line == null) return CommonResult.success(new ArrayList<>());
        List<OrderFormDO> list2 = orderFormService.list(new LambdaQueryWrapper<OrderFormDO>().eq(OrderFormDO::getOrderNumber, externalPlanEndDTO.getBatchNumber()));
        OrderFormDO orderFormDO = list2.get(0);
        List<LedgerDO> list1 = ledgerService.list(new LambdaQueryWrapper<LedgerDO>().eq(LedgerDO::getLintStationGroup, line.getId()));
        LedgerDO ledgerDO = list1.get(0);
        String barCode = externalPlanEndDTO.getMaterialCode();
        if (StringUtils.isBlank(barCode)) {
            barCode = ledgerDO.getTechnicalParameter3();
        }
        LambdaQueryWrapper<BatchOrderDO> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(BatchOrderDO::getOrderId, orderFormDO.getId());
        queryWrapper1.eq(BatchOrderDO::getBarCode, barCode);
        List<BatchOrderDO> list3 = batchOrderService.list(queryWrapper1);
        if (list3.size() == 0) throw new ServiceException(5004, "未找到任务,或任务与物料不符" + externalPlanEndDTO.getBatchNumber() + "-" + barCode);
        BatchOrderDO batchOrderDO = list3.get(0);
        LambdaQueryWrapper<BatchRecordDO> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(BatchRecordDO::getBatchId, batchOrderDO.getId());
        queryWrapper2.eq(BatchRecordDO::getProcedureNum, externalPlanEndDTO.getProcessNumber());
        List<BatchRecordDO> list4 = batchRecordService.list(queryWrapper2);
        BatchRecordDO batchRecordDO = list4.get(0);
        McsBatchRecordEventDTO eventDTO = new McsBatchRecordEventDTO();
        eventDTO.setBatchRecordId(batchRecordDO.getId());
        eventDTO.setBarCode(barCode);
        eventDTO.setDeviceUnitId(ledgerDO.getId());
        //todo 维护设备上登录任务信息
        eventDTO.setOperatorId("1");
        eventDTO.setProgress(externalPlanEndDTO.getProgress());
        eventDTO.setOperatingTime(externalPlanEndDTO.getRealEnd());
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
        queryWrapper.eq(BatchRecordStepDO::getStatus, MCS_STEP_STATUS_ONGOING);
        queryWrapper.orderByAsc(BatchRecordStepDO::getStepOrder);
        List<BatchRecordStepDO> list = batchRecordStepService.list(queryWrapper);
        if (list.size() > 0) {
            BatchRecordStepDO batchRecordStepDO = list.get(0);
            eventDTO.setStepId(batchRecordStepDO.getStepId());
        }
        return manufacturingService.batchRecordEnd(eventDTO);
    }

    @Operation(summary = "荧光检测-根据物料编码获取任务")
    @GetMapping(value = "/WorkOrder/findDefectsDetectionByMaterialCode")
    public CommonResult<List<McsExternalSSPLPlanDTO>> getDefectsDetectionPlanByBarCode(){
        LineStationGroupDO line = lineStationGroupService.getLineStationGroupByCode("FAPL");
        List<McsExternalSSPLPlanDTO> result = new ArrayList<>();
        if (line == null) return CommonResult.success(new ArrayList<>());
        LambdaQueryWrapper<BatchRecordDO> queryWrapperBR = new LambdaQueryWrapper<>();
        queryWrapperBR.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_NEW, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        queryWrapperBR.eq(BatchRecordDO::getProcessingUnitId, line.getId());
        queryWrapperBR.orderByAsc(BatchRecordDO::getPlanStartTime);
        List<BatchRecordDO> recordList = batchRecordService.list(queryWrapperBR);
        Set<String> orderIdSet = recordList.stream().map(BatchRecordDO::getOrderId).collect(Collectors.toSet());
        Set<String> processIdSet = recordList.stream().map(BatchRecordDO::getProcessId).collect(Collectors.toSet());
        for (String orderId : orderIdSet) {
            OrderFormDO orderForm = orderFormService.getOrderForm(orderId);
            ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderForm.getId(), orderForm.getTechnologyId());
            Set<ProcedureRespDTO> processSet = technology.getProcedureList().stream().filter(item -> processIdSet.contains(item.getId())).collect(Collectors.toSet());
            for (ProcedureRespDTO process : processSet) {
                StepRespDTO stepRespDTO = process.getStepList().get(0);//todo 只有一步?
                McsExternalSSPLPlanDTO planDTO = new McsExternalSSPLPlanDTO();
                result.add(planDTO);
                LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(BatchRecordDO::getOrderId, orderForm.getId());
                queryWrapper.eq(BatchRecordDO::getProcessId, process.getId());
                queryWrapper.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_NEW, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
                List<BatchRecordDO> list = batchRecordService.list(queryWrapper);
                ArrayList<McsMaterialPlanDTO> materialCodeList = new ArrayList<>();
                planDTO.setMaterialPlan(materialCodeList);
                for (BatchRecordDO batchRecordDO : list) {
                    McsMaterialPlanDTO materialPlanDTO = new McsMaterialPlanDTO();
                    materialCodeList.add(materialPlanDTO);
                    BatchOrderDO batchOrder = batchOrderService.getBatchOrder(batchRecordDO.getBatchId());
                    materialPlanDTO.setBarCode(batchOrder.getBarCode());
                    materialPlanDTO.setRecordNumber(batchRecordDO.getNumber());
                    LedgerDO ledger = ledgerService.getLedger(batchRecordDO.getDeviceId());
                    materialPlanDTO.setDeviceNumber(ledger.getCode());
                    materialPlanDTO.setPlanStartTime(batchRecordDO.getPlanStartTime());
                    materialPlanDTO.setPlanEndTime(batchRecordDO.getPlanEndTime());
                }
                Set<String> collect = list.stream().map(BatchRecordDO::getBatchId).collect(Collectors.toSet());
                List<BatchOrderDO> list1 = batchOrderService.list(new LambdaQueryWrapper<BatchOrderDO>().in(BatchOrderDO::getId, collect));
                Set<String> barCodeList = list1.stream().map(BatchOrderDO::getBarCode).collect(Collectors.toSet());
                planDTO.setMaterialCode(new ArrayList<>(barCodeList));
                planDTO.setOrderNumber(orderForm.getOrderNumber());
                planDTO.setProcessNumber(process.getProcedureNum());
                planDTO.setProcessName(process.getProcedureName());
                planDTO.setStepNumber(stepRespDTO.getStepNum());
                planDTO.setStepName(stepRespDTO.getStepName());
                planDTO.setProcessCode(technology.getProcessCode());
                planDTO.setProcessSpecificationName(technology.getProcessName());
                planDTO.setProcessSchemeCode(technology.getProcessSchemeCode());
                planDTO.setProcessVersion(technology.getProcessVersion());
                planDTO.setPartNumber(technology.getPartNumber());
                planDTO.setPartName(technology.getPartName());
                planDTO.setDescription(process.getDescription());
                planDTO.setDescriptionPreview(process.getDescriptionPreview());
                planDTO.setFileList(BeanUtils.toBean(process.getFileList(), ProcedureFileRespDTO.class));
                planDTO.setSingleSize(technology.getSingleSize());
                planDTO.setGroupSize(technology.getGroupSize());
                planDTO.setMaterialNumber(technology.getMaterialNumber());
                planDTO.setMaterialSpecification(technology.getMaterialSpecification());
                // TODO: 2025/2/21 临时加工文件内容
                try {
                    byte[] encoded = Files.readAllBytes(Paths.get("E:/cache/ceshi.hyzz"));
                    // 检测并处理BOM
                    if (encoded.length >= 3 && encoded[0] == (byte)0xEF && encoded[1] == (byte)0xBB && encoded[2] == (byte)0xBF) {
                        encoded = Arrays.copyOfRange(encoded, 3, encoded.length);
                    }
                    String content = new String(encoded, StandardCharsets.UTF_8);
//                    String json = "{\"aaa\":\"bbb\"}";
                    planDTO.setProcessingJson(content);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                for (ProcedureDetailRespDTO procedureDetailRespDTO : process.getResourceList()) {
                    if (procedureDetailRespDTO.getResourcesType() == PROCESS_RESOURCES_TYPE_TOOL) {
                        planDTO.setToolTypeNumber(procedureDetailRespDTO.getMaterialNumber());
                        planDTO.setToolName(procedureDetailRespDTO.getName());
                        planDTO.setToolSpecification(procedureDetailRespDTO.getSpecification());
                    }
                }
            }
        }
        return CommonResult.success(result);
    }

    @Operation(summary = "荧光检测-任务开工")
    @PostMapping(value = "/WorkOrder/defectsDetectionPlanStart")
    public CommonResult<?> defectsDetectionPlanStart(@RequestParam("orderNumber") String orderNumber) {
        String[] split = orderNumber.split(",");
        String order = split[0];
        String stepNum = split[1];
        List<OrderFormDO> list = orderFormService.list(new LambdaQueryWrapper<OrderFormDO>().eq(OrderFormDO::getOrderNumber, order));
        if (list.size() != 1) return CommonResult.error(5004, "任务编码不存在");
        List<BatchRecordDO> recordDOList = new ArrayList<>();
        Set<String> recordIdSet = new HashSet<>();
        for (int i = 2; i < split.length; i++) {
            String recordNumber = order + "_" + split[i];
            BatchRecordDO batchRecord = batchRecordService.getBatchRecordByNumber(recordNumber);
            if (batchRecord == null) CommonResult.error(5004, "'" + recordNumber + "'工序任务异常不存在");
            recordDOList.add(batchRecord);
            recordIdSet.add(batchRecord.getId());
        }
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BatchRecordStepDO::getBatchRecordId, recordIdSet);
        queryWrapper.eq(BatchRecordStepDO::getStepOrder, stepNum);
        List<BatchRecordStepDO> stepList = batchRecordStepService.list(queryWrapper);
        BatchRecordStepDO batchRecordStepDO = stepList.get(0);
        String defineDeviceId = batchRecordStepDO.getDefineDeviceId();
        String stepId = batchRecordStepDO.getStepId();
        LedgerDO ledger = ledgerService.getLedger(defineDeviceId);
        //记录开工
        List<McsBatchRecordEventDTO> recordEventDTOList = new ArrayList<>();
        for (BatchRecordDO batchRecordDO : recordDOList) {
            McsBatchRecordEventDTO eventDTO = new McsBatchRecordEventDTO();
            eventDTO.setBatchRecordId(batchRecordDO.getId());
            eventDTO.setBarCode(batchRecordDO.getBarCode());
            eventDTO.setDeviceUnitId(ledger.getId());
            //todo 维护操作者
            eventDTO.setOperatorId("1");
            eventDTO.setProgress(0);
            eventDTO.setOperatingTime(LocalDateTime.now());
            eventDTO.setStepId(stepId);
            recordEventDTOList.add(eventDTO);
        }
        manufacturingService.batchRecordStartBatch(recordEventDTOList);
        return CommonResult.success("开工事件接收成功");
    }

    @Operation(summary = "荧光检测-任务完工")
    @PostMapping(value = "/WorkOrder/defectsDetectionPlanEnd")
    public CommonResult<?> defectsDetectionPlanEnd(@RequestParam("orderNumber") String orderNumber) {
        String[] split = orderNumber.split(",");
        String order = split[0];
        String stepNum = split[1];
        List<OrderFormDO> list = orderFormService.list(new LambdaQueryWrapper<OrderFormDO>().eq(OrderFormDO::getOrderNumber, order));
        if (list.size() != 1) return CommonResult.error(5004, "任务编码不存在");
        List<BatchRecordDO> recordDOList = new ArrayList<>();
        Set<String> recordIdSet = new HashSet<>();
        for (int i = 2; i < split.length; i++) {
            String recordNumber = order + "_" + split[i];
            BatchRecordDO batchRecord = batchRecordService.getBatchRecordByNumber(recordNumber);
            if (batchRecord == null) CommonResult.error(5004, "'" + recordNumber + "'工序任务异常不存在");
            recordDOList.add(batchRecord);
            recordIdSet.add(batchRecord.getId());
        }
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BatchRecordStepDO::getBatchRecordId, recordIdSet);
        queryWrapper.eq(BatchRecordStepDO::getStepOrder, stepNum);
        List<BatchRecordStepDO> stepList = batchRecordStepService.list(queryWrapper);
        BatchRecordStepDO batchRecordStepDO = stepList.get(0);
        String defineDeviceId = batchRecordStepDO.getDefineDeviceId();
        String stepId = batchRecordStepDO.getStepId();
        LedgerDO ledger = ledgerService.getLedger(defineDeviceId);
        List<McsBatchRecordEventDTO> recordEventDTOList = new ArrayList<>();
        for (BatchRecordDO batchRecordDO : recordDOList) {
            McsBatchRecordEventDTO eventDTO = new McsBatchRecordEventDTO();
            eventDTO.setBatchRecordId(batchRecordDO.getId());
            eventDTO.setBarCode(batchRecordDO.getBarCode());
            eventDTO.setDeviceUnitId(ledger.getId());
            //todo 维护操作者
            eventDTO.setOperatorId("1");
            eventDTO.setProgress(100);
            eventDTO.setOperatingTime(LocalDateTime.now());
            eventDTO.setStepId(stepId);
            recordEventDTOList.add(eventDTO);
        }
        return manufacturingService.batchRecordEndBatch(recordEventDTOList);
    }

    @Operation(summary = "通用-获取当前产线任务列表")
    @GetMapping(value = "/WorkOrder/findPlanListByLine")
    @Parameter(name = "lineCode", description = "产线编码", required = true, example = "ABC")
    public CommonResult<List<McsBatchRecordDTO>> getPlanByLine(@RequestParam("lineCode") String lineCode) {
        LineStationGroupDO line = lineStationGroupService.getLineStationGroupByCode(lineCode);
        return manufacturingService.getBatchPlanByWorkstation(line.getId());
    }

    @Operation(summary = "通用-获取当前任务的工序")
    @GetMapping(value = "/WorkOrder/findProcessByPlan")
    @Parameter(name = "orderNumber", description = "工序任务编码", required = true, example = "ABC")
    public CommonResult<McsPlanProcessDTO> findProcessByPlan(@RequestParam("orderNumber") String orderNumber) {
        List<BatchRecordDO> list = batchRecordService.list(new LambdaQueryWrapper<BatchRecordDO>().eq(BatchRecordDO::getNumber, orderNumber));
        if (list.size() != 1) return CommonResult.error(5004, "未找到任务");
        return manufacturingService.getProcessByRecordId(list.get(0).getId());
    }

    @Operation(summary = "通用-根据物料编码与产线编码获取任务")
    @GetMapping(value = "/WorkOrder/findPlanByMaterialCode")
    @Parameter(name = "materialCode", description = "物料编码", required = true, example = "BC-1234")
    @Parameter(name = "lineCode", description = "产线编码", required = true, example = "ABC")
    public CommonResult<McsExternalPlanDTO> getPlanByBarCode(@RequestParam("materialCode")String materialCode, @RequestParam("lineCode") String lineCode) {
        LineStationGroupDO line = lineStationGroupService.getOneByCode(lineCode);
        if (line == null) return CommonResult.success(null);
        LambdaQueryWrapper<BatchRecordDO> queryWrapperBR = new LambdaQueryWrapper<>();
        queryWrapperBR.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        queryWrapperBR.eq(BatchRecordDO::getProcessingUnitId, line.getId());
        queryWrapperBR.eq(BatchRecordDO::getBarCode, materialCode);
        List<BatchRecordDO> recordList = batchRecordService.list(queryWrapperBR);
        if (recordList.size() != 1) return CommonResult.success(null);
        BatchRecordDO batchRecord = recordList.get(0);
        OrderFormDO orderFormDO = orderFormService.getOrderForm(batchRecord.getOrderId());
        ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderFormDO.getId(), orderFormDO.getTechnologyId());
        Optional<ProcedureRespDTO> first = technology.getProcedureList().stream().filter(item -> item.getId().equals(batchRecord.getProcessId())).findFirst();
        if (!first.isPresent()) throw new ServiceException(5004,"未找到相应工序");
        ProcedureRespDTO procedureRespDTO = first.get();
        //根据工序任务查所有工步 step_order排序正序
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapperBRS = new LambdaQueryWrapper<>();
        queryWrapperBRS.eq(BatchRecordStepDO::getBatchRecordId, batchRecord.getId());
        queryWrapperBRS.orderByAsc(BatchRecordStepDO::getStepOrder);
        List<BatchRecordStepDO> stepDOS = batchRecordStepService.list(queryWrapperBRS);
        BatchRecordStepDO stepDO = null;
        McsExternalPlanDTO result = new McsExternalPlanDTO();
        LedgerDO ledger = null;
        if (stepDOS.size() > 0) {
            for (BatchRecordStepDO step : stepDOS) {
                if (step.getStatus() == MCS_STEP_STATUS_NEW || step.getStatus() == MCS_STEP_STATUS_ONGOING) {
                    stepDO = step;
                    break;
                }
            }
            if (stepDO == null) throw new ServiceException(5004, "未找到相应可加工工步");
            String stepId = stepDO.getStepId();
            ledger = ledgerService.getLedger(stepDO.getDefineDeviceId());
            StepRespDTO stepRespDTO = procedureRespDTO.getStepList().stream().filter(item -> stepId.equals(item.getId())).findFirst().get();
            result.setStepNumber(stepRespDTO.getStepNum());
            result.setStepName(stepRespDTO.getStepName());
            result.setStatus(stepDO.getStatus());
            result.setNcList(BeanUtils.toBean(stepRespDTO.getNcList(), McsPlanStepNcDTO.class));
        } else {
            result.setStepNumber("0");
            result.setStepName(procedureRespDTO.getProcedureName());
            ledger = ledgerService.getLedger(batchRecord.getDeviceId());
            int status = batchRecord.getStatus();
            switch (status) {
                case MCS_BATCH_RECORD_STATUS_ISSUED:
                case MCS_BATCH_RECORD_STATUS_NEW: result.setStatus(MCS_STEP_STATUS_NEW);
                case MCS_BATCH_RECORD_STATUS_ONGOING: result.setStatus(MCS_STEP_STATUS_ONGOING);
                case MCS_BATCH_RECORD_STATUS_COMPLETED: result.setStatus(MCS_STEP_STATUS_COMPLETED);
                case MCS_BATCH_RECORD_STATUS_RESCINDED: result.setStatus(MCS_STEP_STATUS_RESCINDED);
            }
            result.setStatus(status);
        }
        result.setDeviceNumber(ledger.getCode());
        result.setMaterialCode(materialCode);
        result.setOrderId(batchRecord.getId());
        result.setOrderNumber(batchRecord.getNumber());
        result.setProcessNumber(procedureRespDTO.getProcedureNum());
        result.setProcessName(procedureRespDTO.getProcedureName());
        result.setReportType(line.getTechnicalParameter1());
        result.setReportPath(line.getTechnicalParameter2());
        result.setTrayModel(ledger.getTechnicalParameter1());
        McsPlanPartDTO mcsPlanPartDTO = BeanUtils.toBean(technology, McsPlanPartDTO.class);
        McsPlanProcessDTO processDTO = BeanUtils.toBean(procedureRespDTO, McsPlanProcessDTO.class);
        processDTO.setPart(mcsPlanPartDTO);
        result.setProcessDTO(processDTO);

        return CommonResult.success(result);
    }

    @Operation(summary = "通用-任务开工")
    @PostMapping(value = "/workOrder/start")
    public CommonResult<?> workOrderStart(@RequestBody McsExternalPlanStartDTO externalPlanStartDTO) {
        if (StringUtils.isBlank(externalPlanStartDTO.getStepNumber())) {
            throw new ServiceException(5005, "参数stepNumber为空");
        }
        BatchRecordDO batchRecordDO = null;
        if (StringUtils.isNotBlank(externalPlanStartDTO.getOrderId())) {
            batchRecordDO = batchRecordService.getBatchRecord(externalPlanStartDTO.getOrderId());
        } else if (StringUtils.isNotBlank(externalPlanStartDTO.getOrderNumber())) {
            batchRecordDO = batchRecordService.getBatchRecordByNumber(externalPlanStartDTO.getOrderNumber());
        } else {
            throw new ServiceException(5004, "未找到任务");
        }
        if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_NEW) {
            throw new ServiceException(5003, "当前任务未下发");
        } else if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_COMPLETED) {
            throw new ServiceException(5003, "当前任务已完成");
        } else if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_RESCINDED) {
            throw new ServiceException(5003, "当前任务已撤销");
        } else if (batchRecordDO.getStatus() != MCS_BATCH_RECORD_STATUS_ISSUED && batchRecordDO.getStatus() != MCS_BATCH_RECORD_STATUS_ONGOING) {
            throw new ServiceException(5005, "任务编码异常");
        }
        String deviceNumber = externalPlanStartDTO.getDeviceNumber();
        LedgerDO ledgerDO = ledgerService.getLedgerByNumber(deviceNumber);
        McsBatchRecordEventDTO eventDTO = new McsBatchRecordEventDTO();
        eventDTO.setBatchRecordId(batchRecordDO.getId());
        eventDTO.setBarCode(externalPlanStartDTO.getMaterialCode());
        eventDTO.setDeviceUnitId(ledgerDO.getId());
        eventDTO.setOperatorId(externalPlanStartDTO.getRealStartBy());
        eventDTO.setProgress(externalPlanStartDTO.getProgress());
        eventDTO.setOperatingTime(externalPlanStartDTO.getRealStart());
        if (!externalPlanStartDTO.getStepNumber().equals("0")) {
            LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
            queryWrapper.eq(BatchRecordStepDO::getStepOrder, externalPlanStartDTO.getStepNumber());
            List<BatchRecordStepDO> list = batchRecordStepService.list(queryWrapper);
            if (list.size() != 1) throw new ServiceException(5005, "工步任务编码异常");
            BatchRecordStepDO batchRecordStepDO = list.get(0);
            eventDTO.setStepId(batchRecordStepDO.getStepId());
        }
        return manufacturingService.batchRecordStart(eventDTO);
    }

    @Operation(summary = "通用-任务完工")
    @PostMapping(value = "/MeasureWorkOrder/end")
    public CommonResult<?> workOrderEnd(@RequestBody McsExternalPlanEndDTO externalPlanEndDTO) {
        if (StringUtils.isBlank(externalPlanEndDTO.getStepNumber())) {
            throw new ServiceException(5005, "参数stepNumber为空");
        }
        BatchRecordDO batchRecordDO = null;
        if (StringUtils.isNotBlank(externalPlanEndDTO.getOrderId())) {
            batchRecordDO = batchRecordService.getBatchRecord(externalPlanEndDTO.getOrderId());
        } else if (StringUtils.isNotBlank(externalPlanEndDTO.getOrderNumber())) {
            batchRecordDO = batchRecordService.getBatchRecordByNumber(externalPlanEndDTO.getOrderNumber());
        } else {
            throw new ServiceException(5004, "未找到任务");
        }
        String deviceNumber = externalPlanEndDTO.getDeviceNumber();
        LedgerDO ledgerDO = ledgerService.getLedgerByNumber(deviceNumber);
        McsMeasureResultDataDTO measureData = externalPlanEndDTO.getMeasureData();
        if (measureData != null) {
            List<McsMeasureResultPointDataDTO> items = measureData.getItems();
            if (items != null && items.size() > 0) {
                boolean qualified = items.stream().noneMatch(item -> "2".equals(item.getResult()));
                manufacturingService.setInspectionSheetResult(batchRecordDO.getId(), qualified);
            }
            taskAdditionalInfoService.addTaskRecordInfoMeasureInfo(batchRecordDO, measureData);
        }
        if (externalPlanEndDTO.getReportFileName() != null) {
            String prePath = "";
            String lineStationGroupId = ledgerDO.getLintStationGroup();
            if (lineStationGroupId != null) {
                LineStationGroupDO lineStationGroup = lineStationGroupService.getLineStationGroup(lineStationGroupId);
                if (lineStationGroup != null) {
                    prePath += lineStationGroup.getTechnicalParameter2();
                }
            }
            taskAdditionalInfoService.addTaskRecordInfoMeasureFile(batchRecordDO, externalPlanEndDTO.getReportFileName(), prePath);
        }
        if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_ISSUED) {
            throw new ServiceException(5003, "当前任务未开始");
        } else if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_NEW) {
            throw new ServiceException(5003, "当前任务未开始");
        } else if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_COMPLETED) {
            throw new ServiceException(5003, "当前任务已完成");
        } else if (batchRecordDO.getStatus() == MCS_BATCH_RECORD_STATUS_RESCINDED) {
            throw new ServiceException(5003, "当前任务已撤销");
        } else if (batchRecordDO.getStatus() != MCS_BATCH_RECORD_STATUS_ONGOING) {
            throw new ServiceException(5005, "任务编码异常");
        }
        McsBatchRecordEventDTO eventDTO = new McsBatchRecordEventDTO();
        eventDTO.setBatchRecordId(batchRecordDO.getId());
        eventDTO.setBarCode(externalPlanEndDTO.getMaterialCode());
        eventDTO.setDeviceUnitId(ledgerDO.getId());
        eventDTO.setOperatorId(externalPlanEndDTO.getRealEndBy());
        eventDTO.setProgress(externalPlanEndDTO.getProgress());
        eventDTO.setOperatingTime(externalPlanEndDTO.getRealEnd());
        if (!externalPlanEndDTO.getStepNumber().equals("0")) {
            LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
            queryWrapper.eq(BatchRecordStepDO::getStepOrder, externalPlanEndDTO.getStepNumber());
            List<BatchRecordStepDO> list = batchRecordStepService.list(queryWrapper);
            if (list.size() != 1) throw new ServiceException(5005, "工步任务编码异常");
            BatchRecordStepDO batchRecordStepDO = list.get(0);
            eventDTO.setStepId(batchRecordStepDO.getStepId());
        }
        return manufacturingService.batchRecordEnd(eventDTO);
    }

    @Operation(summary = "通用-设备信息更新/新增")
    @PostMapping(value = "/deviceData/update")
    public CommonResult<?> deviceInfoUpdate(@RequestBody McsExternalLedgerUpdateDO externalLedgerUpdateDO) {
        if (StringUtils.isBlank(externalLedgerUpdateDO.getCode())) {
            throw new ServiceException(5005, "设备编码异常");
        }
        //产线
        LineStationGroupDO lineStationGroupDO = lineStationGroupService.getLineStationGroupByCode(externalLedgerUpdateDO.getLineCode());
        LedgerDO ledgerByNumber = ledgerService.getLedgerByNumber(externalLedgerUpdateDO.getCode());
        //当设备不存在时新增
        boolean newFlag = ledgerByNumber == null;
        LedgerDO ledgerDO = BeanUtils.toBean(externalLedgerUpdateDO, LedgerDO.class);
        ledgerDO.setNeedMaterials(externalLedgerUpdateDO.getDeliverable());
        if (newFlag) {
            ledgerDO.setLintStationGroup(lineStationGroupDO.getId());
            if (externalLedgerUpdateDO.getDeviceTypeCode() == null) {
                throw new ServiceException(5005, "设备类型编码异常");
            }
            DeviceTypeDO deviceTypeDO = deviceTypeService.getDeviceTypeListByCode(externalLedgerUpdateDO.getDeviceTypeCode());
            ledgerDO.setEquipmentStationType(deviceTypeDO.getId());
            ledgerService.save(ledgerDO);
        } else {
            if (!lineStationGroupDO.getId().equals(ledgerByNumber.getLintStationGroup())) {
                throw new ServiceException(5005, "设备编码重复");
            }
            if (externalLedgerUpdateDO.getId() == null) {
                ledgerDO.setId(ledgerByNumber.getId());
            }
            if (externalLedgerUpdateDO.getDeviceTypeCode() != null) {
                DeviceTypeDO deviceTypeDO = deviceTypeService.getDeviceTypeListByCode(externalLedgerUpdateDO.getDeviceTypeCode());
                ledgerDO.setEquipmentStationType(deviceTypeDO.getId());
            }
            ledgerService.UpdateById(ledgerDO);
        }
        return CommonResult.success("更新成功");
    }

    @Operation(summary = "通用-设备状态更新")
    @PostMapping(value = "/deviceStatus/update")
    public CommonResult<?> deviceStatusUpdate(@RequestBody McsExternalLedgerStatusDO externalLedgerStatusDO) {
        LedgerDO ledgerDO = new LedgerDO();
        ledgerDO.setStatus(externalLedgerStatusDO.getStatus());
        ledgerDO.setRunStatus(externalLedgerStatusDO.getRunStatus());
        ledgerDO.setOnlineStatus(externalLedgerStatusDO.getOnlineStatus());
        ledgerDO.setNeedMaterials(externalLedgerStatusDO.getDeliverable());
        if (externalLedgerStatusDO.getId() == null) {
            LedgerDO ledgerByNumber = ledgerService.getLedgerByNumber(externalLedgerStatusDO.getCode());
            ledgerDO.setId(ledgerByNumber.getId());
        }
        ledgerService.UpdateById(ledgerDO);
        return CommonResult.success("更新成功");
    }

    @Operation(summary = "通用-物料签收")
    @PostMapping(value = "/delivery/check")
    public CommonResult<?> deliveryCheck(@RequestBody McsExternalDeliveryCheckDTO deliveryCheckDTO) {
        String materialCode = deliveryCheckDTO.getMaterialCode();
        String deviceCode = deliveryCheckDTO.getDeviceCode();
        String operator = deliveryCheckDTO.getOperator();
        LedgerDO ledgerByNumber = ledgerService.getLedgerByNumber(deviceCode);
        String toolLocationByDevice = ledgerService.getToolLocationByDevice(ledgerByNumber.getId());
        warehouseRestService.deliveryCheck(materialCode, toolLocationByDevice, operator);
        return CommonResult.success("签收成功");
    }

    @Operation(summary = "通用-异常上报")
    @PostMapping(value = "/exception/raise")
    public CommonResult<?> exceptionRaise(@RequestBody McsExternalFailureDTO externalFailureDTO) {
        String deviceCode = externalFailureDTO.getDeviceCode();
        String code = externalFailureDTO.getCode();
        LedgerDO ledgerDO = ledgerService.getLedgerByNumber(deviceCode);
        LambdaQueryWrapper<FailureRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(FailureRecordDO::getCode, code);
        queryWrapper.eq(FailureRecordDO::getDevice, ledgerDO.getId());
        queryWrapper.in(FailureRecordDO::getFaultState, DMS_FAULT_STATE_0, DMS_FAULT_STATE_1);
        List<FailureRecordDO> failureRecordDOS = failureRecordService.list(queryWrapper);
        if (failureRecordDOS.size() > 0) {
            FailureRecordDO failureRecordDO = failureRecordDOS.get(0);
            if (failureRecordDO.getFaultState().equals(externalFailureDTO.getFaultState() + "")) return CommonResult.success("当前报警已记录");
            FailureRecordSaveReqVO failureRecordSaveReqVO = BeanUtils.toBean(externalFailureDTO,  FailureRecordSaveReqVO.class);
            failureRecordSaveReqVO.setId(failureRecordDO.getId());
            failureRecordSaveReqVO.setDevice(ledgerDO.getId());
            failureRecordService.updateFailureRecord(failureRecordSaveReqVO);
        } else {
            if (externalFailureDTO.getFaultState() == DMS_FAULT_STATE_2.intValue()) return CommonResult.success("当前报警已处理");
            FailureRecordSaveReqVO failureRecordSaveReqVO = BeanUtils.toBean(externalFailureDTO,  FailureRecordSaveReqVO.class);
            failureRecordSaveReqVO.setDevice(ledgerDO.getId());
            failureRecordService.createFailureRecord(failureRecordSaveReqVO);
        }
        return CommonResult.success("上传成功");
    }

    @Operation(summary = "通用-根据设备编码查询物料配送(入线配送)")
    @GetMapping(value = "/carryOrder/listByDevice")
    @Parameter(name = "deviceCode", description = "设备编码", required = true, example = "abc")
    public CommonResult<McsMaterialDeliveryDTO> carryOrderListByDeviceNumber(@RequestParam("deviceCode") String deviceCode) {
        LedgerDO ledgerByNumber = ledgerService.getLedgerByNumber(deviceCode);
        List<OrderReqDTO> carryList = manufacturingService.getDistributionByDeviceId(ledgerByNumber.getId());
        if (carryList.size() > 0) {
            OrderReqDTO orderReqDTO = carryList.get(0);
            McsMaterialDeliveryDTO deliveryDTO = new McsMaterialDeliveryDTO();
            deliveryDTO.setMaterialCode(orderReqDTO.getRealBarCode());
            int orderStatus = orderReqDTO.getOrderStatus();
            if (orderStatus == WMS_ORDER_DETAIL_STATUS_2) {
                deliveryDTO.setStatus(1);
                return CommonResult.success(deliveryDTO);
            } else if (orderStatus == WMS_ORDER_DETAIL_STATUS_3) {
                deliveryDTO.setStatus(2);
                return CommonResult.success(deliveryDTO);
            }
        }
        return CommonResult.success(null);
    }

    @Operation(summary = "通用-根据产线编码查询物料配送(出入线配送)")
    @GetMapping(value = "/carryOrder/listByLine")
    @Parameter(name = "lineCode", description = "产线编码", required = true, example = "ABC")
    public CommonResult<McsMaterialDeliveryDTO> carryOrderListByLineNumber(@RequestParam("lineCode") String lineCode) {
        LineStationGroupDO line = lineStationGroupService.getLineStationGroupByCode(lineCode);
        List<OrderReqDTO> carryList = manufacturingService.getCarryByWarehouseLocation(line.getLocation());
        if (carryList.size() > 0) {
            OrderReqDTO orderReqDTO = carryList.get(0);
            int orderStatus = orderReqDTO.getOrderStatus();
            McsMaterialDeliveryDTO deliveryDTO = new McsMaterialDeliveryDTO();
            deliveryDTO.setMaterialCode(orderReqDTO.getRealBarCode());
            if (orderStatus == WMS_ORDER_DETAIL_STATUS_2) {
                deliveryDTO.setStatus(1);
                return CommonResult.success(deliveryDTO);
            } else if (orderStatus == WMS_ORDER_DETAIL_STATUS_3) {
                deliveryDTO.setStatus(2);
                return CommonResult.success(deliveryDTO);
            }
        }
        McsMaterialDeliveryDTO carryReadyMap = manufacturingService.getCarryReadyByWarehouseLocation(line.getLocation());
        return CommonResult.success(carryReadyMap);
    }

    @Operation(summary = "固熔时效炉-物料编码查询任务")
    @GetMapping(value = "/WorkOrder/findFurnacePlanByMaterialCode")
    @Parameter(name = "materialCode", description = "物料编码", required = true)
    public CommonResult<McsFurnacePlanDTO> getFurnacePlanByMaterialCode(@RequestParam("materialCode") String materialCode) {
        LineStationGroupDO line = lineStationGroupService.getLineStationGroupByCode("HTL");
        LambdaQueryWrapper<BatchRecordDO> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(BatchRecordDO::getBarCode, materialCode);
        queryWrapper1.eq(BatchRecordDO::getProcessingUnitId, line.getId());
        queryWrapper1.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        List<BatchRecordDO> batchRecordDOList = batchRecordService.list(queryWrapper1);
        if (batchRecordDOList.size() != 1) return CommonResult.success(null);
        BatchRecordDO batchRecord = batchRecordDOList.get(0);
        OrderFormDO orderForm = orderFormService.getOrderForm(batchRecord.getOrderId());
        LambdaQueryWrapper<BatchRecordDO> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(BatchRecordDO::getOrderId, orderForm.getId());
        queryWrapper2.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_NEW, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        List<BatchRecordDO> allRecordList = batchRecordService.list(queryWrapper2);
        Map<String, String> stringMap = allRecordList.stream().filter(item -> StringUtils.isNotBlank(item.getBarCode())).collect(Collectors.toMap(BatchRecordDO::getId, BatchRecordDO::getBarCode, (a, b) -> b));
        Set<String> recordIdSet = stringMap.keySet();
        ProcessPlanDetailRespDTO technology = technologyRestService.getTechnologyByIdCache(orderForm.getId(), orderForm.getTechnologyId());
        Optional<ProcedureRespDTO> first = technology.getProcedureList().stream().filter(item -> item.getId().equals(batchRecord.getProcessId())).findFirst();
        if (!first.isPresent()) throw new ServiceException(5004,"未找到相应工序");
        ProcedureRespDTO procedureRespDTO = first.get();
        BatchRecordStepDO stepDO = null;
        //根据工序任务查所有工步 step_order排序正序
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapperBRS = new LambdaQueryWrapper<>();
        queryWrapperBRS.eq(BatchRecordStepDO::getBatchRecordId, batchRecord.getId());
        queryWrapperBRS.orderByAsc(BatchRecordStepDO::getStepOrder);
        List<BatchRecordStepDO> stepDOS = batchRecordStepService.list(queryWrapperBRS);
        McsFurnacePlanDTO result = new McsFurnacePlanDTO();
        LedgerDO ledger;
        if (stepDOS.size() > 0) {
            for (BatchRecordStepDO step : stepDOS) {
                if (step.getStatus() == MCS_STEP_STATUS_NEW || step.getStatus() == MCS_STEP_STATUS_ONGOING) {
                    stepDO = step;
                    break;
                }
            }
            if (stepDO == null) throw new ServiceException(5004, "未找到相应可加工工步");
            String stepId = stepDO.getStepId();
            StepRespDTO stepRespDTO = procedureRespDTO.getStepList().stream().filter(item -> stepId.equals(item.getId())).findFirst().get();
            result.setStepNumber(stepRespDTO.getStepNum());
            result.setStepName(stepRespDTO.getStepName());
            ledger = ledgerService.getLedger(stepDO.getDefineDeviceId());
            result.setDeviceNumber(ledger.getCode());
            result.setDeviceName(ledger.getName());
            result.setStatus(stepDO.getStatus());
            result.setNcList(BeanUtils.toBean(stepRespDTO.getNcList(), McsPlanStepNcDTO.class));

            LambdaQueryWrapper<BatchRecordStepDO> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(BatchRecordStepDO::getStepId,stepId);
            queryWrapper3.in(BatchRecordStepDO::getBatchRecordId,recordIdSet);
            queryWrapper3.in(BatchRecordStepDO::getStatus,MCS_STEP_STATUS_NEW,MCS_STEP_STATUS_ONGOING);
            List<BatchRecordStepDO> list = batchRecordStepService.list(queryWrapper3);
            for (BatchRecordStepDO batchRecordStepDO : list) {
                result.addBatchOrderList(stringMap.get(batchRecordStepDO.getBatchRecordId()), stepDO.getDefineDeviceNumber(), batchRecordStepDO.getPlanStartTime());
            }
        } else {
            result.setStepNumber("0");
            result.setStepName(procedureRespDTO.getProcedureName());
            ledger = ledgerService.getLedger(batchRecord.getDeviceId());
            result.setDeviceNumber(ledger.getCode());
            result.setDeviceName(ledger.getName());
            int status = batchRecord.getStatus();
            switch (status) {
                case MCS_BATCH_RECORD_STATUS_ISSUED:
                case MCS_BATCH_RECORD_STATUS_NEW: result.setStatus(MCS_STEP_STATUS_NEW);
                case MCS_BATCH_RECORD_STATUS_ONGOING: result.setStatus(MCS_STEP_STATUS_ONGOING);
                case MCS_BATCH_RECORD_STATUS_COMPLETED: result.setStatus(MCS_STEP_STATUS_COMPLETED);
                case MCS_BATCH_RECORD_STATUS_RESCINDED: result.setStatus(MCS_STEP_STATUS_RESCINDED);
            }
            result.setStatus(status);
            for (BatchRecordDO batchRecordDO : allRecordList) {
                if (batchRecordDO.getBarCode() == null) continue;
                String batchRecordKey = batchRecordDO.getId();
                String deviceId = batchRecordDO.getDeviceId();
                LedgerDO ledger1 = ledgerService.getLedger(deviceId);
                result.addBatchOrderList(stringMap.get(batchRecordKey), ledger1.getCode(), batchRecordDO.getPlanStartTime());
            }
        }
        result.setMaterialCode(materialCode);
        result.setOrderId(batchRecord.getId());
        result.setOrderNumber(batchRecord.getNumber());
        result.setProcessNumber(procedureRespDTO.getProcedureNum());
        result.setProcessName(procedureRespDTO.getProcedureName());
        return CommonResult.success(result);
    }

    @Operation(summary = "固熔时效炉-任务下发")
    @PostMapping(value = "/WorkOrder/furnacePlanIssued")
    public CommonResult<?> furnacePlanIssued(@RequestBody McsFurnacePlanStartDTO furnacePlanStartDTO) {
        String deviceNumber = furnacePlanStartDTO.getDeviceNumber();
        LedgerDO ledgerByNumber = ledgerService.getLedgerByNumber(deviceNumber);
        List<String> materialCodeList = furnacePlanStartDTO.getMaterialCodeList();
        if (materialCodeList == null || materialCodeList.size() == 0) return CommonResult.error(500,"物料数量为0");
        LambdaQueryWrapper<BatchRecordDO> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.in(BatchRecordDO::getBarCode, materialCodeList);
        queryWrapper1.in(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ISSUED, MCS_BATCH_RECORD_STATUS_ONGOING);
        List<BatchRecordDO> batchRecordDOList = batchRecordService.list(queryWrapper1);
        if (materialCodeList.size() > batchRecordDOList.size()) {
            return CommonResult.error(5005, "部分物料未找到任务,或当前任务不可开工");
        }
        if (materialCodeList.size() < batchRecordDOList.size()) {
            return CommonResult.error(5005, "物料绑定任务异常");
        }
        Set<String> recordIdSet = batchRecordDOList.stream().map(BatchRecordDO::getId).collect(Collectors.toSet());
        Set<String> collect = batchRecordDOList.stream().map(BatchRecordDO::getProcessId).collect(Collectors.toSet());
        if (collect.size() > 1) return CommonResult.error(500, "所选物料不属于同一工序");
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.in(BatchRecordStepDO::getBatchRecordId, recordIdSet);
        queryWrapper2.eq(BatchRecordStepDO::getDefineDeviceId, ledgerByNumber.getId());
        List<BatchRecordStepDO> list = batchRecordStepService.list(queryWrapper2);
        //需要加工文件,默认含有工步
        Set<String> collect1 = list.stream().map(BatchRecordStepDO::getStepId).collect(Collectors.toSet());
        if (collect1.size() > 1) return CommonResult.error(5005, "所选物料不属于同一工步,不可一起加工");
        if (collect1.size() == 0) return CommonResult.error(5004, "未找到当前设备任务");
        String stepId = list.get(0).getStepId();
        BatchRecordDO recordDO = batchRecordDOList.get(0);
        OrderFormDO orderForm = orderFormService.getOrderForm(recordDO.getOrderId());
        ProcedureRespDTO processCache = technologyRestService.getProcessCache(orderForm.getId(), orderForm.getTechnologyId(), recordDO.getProcessId());
        StepRespDTO step = null;
        for (StepRespDTO stepRespDTO : processCache.getStepList()) {
            if (stepId.equals(stepRespDTO.getId())) {
                step = stepRespDTO;
                break;
            }
        }
        if (step == null) {
            return CommonResult.error(5005, "未找到当前任务的工步信息");
        }
        //执行开工动作
        instructionExecuteService.sendFurnacePlanStart(orderForm, batchRecordDOList, step, ledgerByNumber);
        return CommonResult.success("开工事件接收成功");
    }

    @Operation(summary = "固熔时效炉-任务开工")
    @PostMapping(value = "/WorkOrder/furnacePlanStart")
    public CommonResult<?> furnacePlanStart(@RequestParam("orderNumber") String orderNumber) {
        String[] split = orderNumber.split(",");
        String order = split[0];
        String stepNum = split[1];
        List<OrderFormDO> list = orderFormService.list(new LambdaQueryWrapper<OrderFormDO>().eq(OrderFormDO::getOrderNumber, order));
        if (list.size() != 1) return CommonResult.error(5004, "任务编码不存在");
        List<BatchRecordDO> recordDOList = new ArrayList<>();
        Set<String> recordIdSet = new HashSet<>();
        for (int i = 2; i < split.length; i++) {
            String recordNumber = order + "_" + split[i];
            BatchRecordDO batchRecord = batchRecordService.getBatchRecordByNumber(recordNumber);
            if (batchRecord == null) CommonResult.error(5004, "'" + recordNumber + "'工序任务异常不存在");
            recordDOList.add(batchRecord);
            recordIdSet.add(batchRecord.getId());
        }
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BatchRecordStepDO::getBatchRecordId, recordIdSet);
        queryWrapper.eq(BatchRecordStepDO::getStepOrder, stepNum);
        List<BatchRecordStepDO> stepList = batchRecordStepService.list(queryWrapper);
        BatchRecordStepDO batchRecordStepDO = stepList.get(0);
        if (batchRecordStepDO.getStatus() == MCS_STEP_STATUS_COMPLETED) {
            LambdaQueryWrapper<BatchRecordStepDO> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(BatchRecordStepDO::getBatchRecordId, batchRecordStepDO.getBatchRecordId());
            queryWrapper1.orderByAsc(BatchRecordStepDO::getStepOrder);
            queryWrapper1.gt(BatchRecordStepDO::getStepOrder, batchRecordStepDO.getStepOrder());
            List<BatchRecordStepDO> stepList1 = batchRecordStepService.list(queryWrapper1);
            boolean flag = true;
            for (BatchRecordStepDO recordStepDO : stepList1) {
                if (!recordStepDO.getDefineDeviceId().equals(batchRecordStepDO.getDefineDeviceId())) break;
                if (recordStepDO.getStatus() == MCS_STEP_STATUS_COMPLETED || recordStepDO.getStatus() == MCS_STEP_STATUS_RESCINDED) break;
                batchRecordStepDO = recordStepDO;
                flag = false;
            }
            if (flag) {
                return CommonResult.error(5004, "任务已报工");
            }
        }
        String defineDeviceId = batchRecordStepDO.getDefineDeviceId();
        String stepId = batchRecordStepDO.getStepId();
        LedgerDO ledger = ledgerService.getLedger(defineDeviceId);
        //记录开工
        List<McsBatchRecordEventDTO> recordEventDTOList = new ArrayList<>();
        for (BatchRecordDO batchRecordDO : recordDOList) {
            McsBatchRecordEventDTO eventDTO = new McsBatchRecordEventDTO();
            eventDTO.setBatchRecordId(batchRecordDO.getId());
            eventDTO.setBarCode(batchRecordDO.getBarCode());
            eventDTO.setDeviceUnitId(ledger.getId());
            //todo 维护操作者
            eventDTO.setOperatorId("1");
            eventDTO.setProgress(0);
            eventDTO.setOperatingTime(LocalDateTime.now());
            eventDTO.setStepId(stepId);
            recordEventDTOList.add(eventDTO);
        }
        manufacturingService.batchRecordStartBatch(recordEventDTOList);
        return CommonResult.success("开工事件接收成功");
    }

    @Operation(summary = "固熔时效炉-任务完工")
    @PostMapping(value = "/WorkOrder/furnacePlanEnd")
    @Parameter(name = "orderNumber", description = "产线编码", required = true)
    public CommonResult<?> furnacePlanEnd(@RequestParam("orderNumber") String orderNumber) {
        String[] split = orderNumber.split(",");
        String order = split[0];
        String stepNum = split[1];
        List<OrderFormDO> list = orderFormService.list(new LambdaQueryWrapper<OrderFormDO>().eq(OrderFormDO::getOrderNumber, order));
        if (list.size() != 1) return CommonResult.error(5004, "任务编码不存在");
        List<BatchRecordDO> recordDOList = new ArrayList<>();
        Set<String> recordIdSet = new HashSet<>();
        for (int i = 2; i < split.length; i++) {
            String recordNumber = order + "_" + split[i];
            BatchRecordDO batchRecord = batchRecordService.getBatchRecordByNumber(recordNumber);
            if (batchRecord == null) CommonResult.error(5004, "'" + recordNumber + "'工序任务异常不存在");
            recordDOList.add(batchRecord);
            recordIdSet.add(batchRecord.getId());
        }
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BatchRecordStepDO::getBatchRecordId, recordIdSet);
        queryWrapper.eq(BatchRecordStepDO::getStepOrder, stepNum);
        List<BatchRecordStepDO> stepList = batchRecordStepService.list(queryWrapper);
        BatchRecordStepDO batchRecordStepDO = stepList.get(0);
        if (batchRecordStepDO.getStatus() == MCS_STEP_STATUS_COMPLETED) {
            LambdaQueryWrapper<BatchRecordStepDO> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(BatchRecordStepDO::getBatchRecordId, batchRecordStepDO.getBatchRecordId());
            queryWrapper1.orderByAsc(BatchRecordStepDO::getStepOrder);
            queryWrapper1.gt(BatchRecordStepDO::getStepOrder, batchRecordStepDO.getStepOrder());
            List<BatchRecordStepDO> stepList1 = batchRecordStepService.list(queryWrapper1);
            boolean flag = true;
            for (BatchRecordStepDO recordStepDO : stepList1) {
                if (!recordStepDO.getDefineDeviceId().equals(batchRecordStepDO.getDefineDeviceId())) break;
                if (recordStepDO.getStatus() == MCS_STEP_STATUS_COMPLETED || recordStepDO.getStatus() == MCS_STEP_STATUS_RESCINDED) break;
                batchRecordStepDO = recordStepDO;
                flag = false;
            }
            if (flag) {
                return CommonResult.error(5004, "任务已报工");
            }
        }
        String defineDeviceId = batchRecordStepDO.getDefineDeviceId();
        String stepId = batchRecordStepDO.getStepId();
        LedgerDO ledger = ledgerService.getLedger(defineDeviceId);
        List<McsBatchRecordEventDTO> recordEventDTOList = new ArrayList<>();
        for (BatchRecordDO batchRecordDO : recordDOList) {
            McsBatchRecordEventDTO eventDTO = new McsBatchRecordEventDTO();
            eventDTO.setBatchRecordId(batchRecordDO.getId());
            eventDTO.setBarCode(batchRecordDO.getBarCode());
            eventDTO.setDeviceUnitId(ledger.getId());
            //todo 维护操作者
            eventDTO.setOperatorId("1");
            eventDTO.setProgress(100);
            eventDTO.setOperatingTime(LocalDateTime.now());
            eventDTO.setStepId(stepId);
            recordEventDTOList.add(eventDTO);
        }
        return manufacturingService.batchRecordEndBatch(recordEventDTOList);
    }

    @Operation(summary = "固熔时效炉-工步任务完工")
    @PostMapping(value = "/WorkOrder/furnaceStepPlanEnd")
    @Parameter(name = "orderNumber", description = "产线编码", required = true)
    public CommonResult<?> furnaceStepPlanEnd(@RequestParam("orderNumber") String orderNumber) {
        String[] split = orderNumber.split(",");
        String order = split[0];
        String stepNum = split[1];
        List<OrderFormDO> list = orderFormService.list(new LambdaQueryWrapper<OrderFormDO>().eq(OrderFormDO::getOrderNumber, order));
        if (list.size() != 1) return CommonResult.error(5004, "任务编码不存在");
        List<BatchRecordDO> recordDOList = new ArrayList<>();
        Set<String> recordIdSet = new HashSet<>();
        for (int i = 2; i < split.length; i++) {
            String recordNumber = order + "_" + split[i];
            BatchRecordDO batchRecord = batchRecordService.getBatchRecordByNumber(recordNumber);
            if (batchRecord == null) CommonResult.error(5004, "'" + recordNumber + "'工序任务异常不存在");
            recordDOList.add(batchRecord);
            recordIdSet.add(batchRecord.getId());
        }
        LambdaQueryWrapper<BatchRecordStepDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(BatchRecordStepDO::getBatchRecordId, recordIdSet);
        queryWrapper.eq(BatchRecordStepDO::getStepOrder, stepNum);
        List<BatchRecordStepDO> stepList = batchRecordStepService.list(queryWrapper);
        BatchRecordStepDO batchRecordStepDO = stepList.get(0);
        if (batchRecordStepDO.getStatus() == MCS_STEP_STATUS_COMPLETED) {
            LambdaQueryWrapper<BatchRecordStepDO> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(BatchRecordStepDO::getBatchRecordId, batchRecordStepDO.getBatchRecordId());
            queryWrapper1.orderByAsc(BatchRecordStepDO::getStepOrder);
            queryWrapper1.gt(BatchRecordStepDO::getStepOrder, batchRecordStepDO.getStepOrder());
            List<BatchRecordStepDO> stepList1 = batchRecordStepService.list(queryWrapper1);
            boolean flag = true;
            for (BatchRecordStepDO recordStepDO : stepList1) {
                if (!recordStepDO.getDefineDeviceId().equals(batchRecordStepDO.getDefineDeviceId())) break;
                if (recordStepDO.getStatus() == MCS_STEP_STATUS_COMPLETED || recordStepDO.getStatus() == MCS_STEP_STATUS_RESCINDED) break;
                batchRecordStepDO = recordStepDO;
                flag = false;
            }
            if (flag) {
                return CommonResult.error(5004, "任务已报工");
            }
        }
        String defineDeviceId = batchRecordStepDO.getDefineDeviceId();
        String stepId = batchRecordStepDO.getStepId();
        LedgerDO ledger = ledgerService.getLedger(defineDeviceId);
        List<McsBatchRecordEventDTO> recordEventDTOList = new ArrayList<>();
        for (BatchRecordDO batchRecordDO : recordDOList) {
            McsBatchRecordEventDTO eventDTO = new McsBatchRecordEventDTO();
            eventDTO.setBatchRecordId(batchRecordDO.getId());
            eventDTO.setBarCode(batchRecordDO.getBarCode());
            eventDTO.setDeviceUnitId(ledger.getId());
            //todo 维护操作者
            eventDTO.setOperatorId("1");
            eventDTO.setProgress(100);
            eventDTO.setOperatingTime(LocalDateTime.now());
            eventDTO.setStepId(stepId);
            recordEventDTOList.add(eventDTO);
        }
        return manufacturingService.batchRecordEndBatch(recordEventDTOList);
    }

    @Operation(summary = "根据产线编码获取设备")
    @GetMapping(value = "/deviceData/getDeviceListByLineCode")
    @Parameter(name = "lineCode", description = "产线编码", required = true)
    public CommonResult<List<McsDeviceDTO>> getDeviceListByLineCode(@RequestParam("lineCode") String lineCode) {
        LineStationGroupDO line = lineStationGroupService.getOneByCode(lineCode);
        if (line == null) return CommonResult.success(new ArrayList<>());
        List<LedgerDO> list = ledgerService.list(new LambdaQueryWrapper<LedgerDO>().eq(LedgerDO::getLintStationGroup, line.getId()));
        return CommonResult.success(BeanUtils.toBean(list, McsDeviceDTO.class));
    }

}
