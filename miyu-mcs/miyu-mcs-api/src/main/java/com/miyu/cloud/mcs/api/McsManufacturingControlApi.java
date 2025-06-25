package com.miyu.cloud.mcs.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.mcs.dto.distribution.McsDistributionLocationDTO;
import com.miyu.cloud.mcs.dto.manufacture.*;
import com.miyu.cloud.mcs.dto.orderForm.McsOrderFormCreateDTO;
import com.miyu.cloud.mcs.dto.outsourcing.McsOutsourcingPlanDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsPlanProcessDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsProjectOrderDTO;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Component
@FeignClient(name = "mcs-server")
public interface McsManufacturingControlApi {

    //子订单接收
    @PostMapping(value = "/mcs/rest/orderFormCreate")
    CommonResult<?> orderFormCreate(@RequestBody McsOrderFormCreateDTO orderFormCreate);

    @PostMapping(value = "/mcs/rest/orderFormCancel")
    CommonResult<?> orderFormCancel(@RequestParam("orderNumber") String orderNumber);

    //工序任务 任务开工
    @PostMapping(value = "/mcs/rest/batchRecordStart")
    CommonResult<?> batchRecordStart(@RequestBody McsBatchRecordEventDTO batchRecord);

    //工序任务 任务完工
    @PostMapping(value = "/mcs/rest/batchRecordEnd")
    CommonResult<?> batchRecordEnd(@RequestBody McsBatchRecordEventDTO batchRecord);

    //工位/设备 暂停/恢复
    @PostMapping(value = "/mcs/rest/workstationEvent")
    CommonResult<?> stepWorkstationEvent(@RequestBody McsWorkstationDTO workstationDTO);

    //入库
    @PostMapping(value = "/mcs/rest/putInStorage/{deviceId}")
    CommonResult<?> putInStorage(@PathVariable("deviceId") String deviceId, @RequestBody List<McsBatchResourceDTO> resourceList);

    //移交(下一工位)(废弃)
//    @PostMapping(value = "/mcs/rest/transfer/{deviceId}")
//    CommonResult<?> transfer(@PathVariable("deviceId") String deviceId, @RequestBody List<McsBatchResourceDTO> resourceList);

    //根据工位获取当前任务(工序任务)
    @GetMapping(value = "/mcs/rest/getBatchPlan")
    CommonResult<List<McsBatchRecordDTO>> getBatchPlanByWorkstation(@RequestParam("workstationId") String workstationId);

    //根据工位Ip获取当前任务(工序任务)
    @GetMapping(value = "/mcs/rest/getBatchPlanByIp")
    CommonResult<List<McsBatchRecordDTO>> getBatchPlanByWorkstationIp(@RequestParam("workstationIp") String workstationIp);

    //根据产线(自带管理系统)获取当前任务(工序任务)
    @GetMapping(value = "/mcs/rest/getBatchPlanByProductionLine")
    CommonResult<List<McsOrderFormDTO>> getBatchPlanByProductionLine(@RequestParam("workstationId") String workstationId);

    //根据任务(批次)id,工位/产线id 查询物料需求
    @GetMapping(value = "/mcs/rest/getBatchResource")
    CommonResult<List<McsBatchResourceDTO>> getBatchResourceByRecordId(@RequestParam("recordId") String recordId, @RequestParam("workstationId") String workstationId);

    //根据任务(批次)id 查询详细任务(零件)
    @GetMapping(value = "/mcs/rest/getBatchDetail")
    CommonResult<McsBatchRecordDTO> getBatchDetailByRecordId(@RequestParam("recordId") String recordId);

    //根据工序任务id 查询工序(包含工步)
    @GetMapping(value = "/mcs/rest/getProcess")
    CommonResult<McsPlanProcessDTO> getProcessByRecordId(@RequestParam("recordId") String recordId);

    //根据工位查询是否暂停 暂停true
    @GetMapping(value = "/mcs/rest/getWorkstationPause")
    CommonResult<Boolean> getWorkstationPause(@RequestParam("workstationId") String workstationId);

    //根据项目号 查询零件加工情况 (项目)
    @GetMapping(value = "/mcs/rest/getPartRecordByProject")
    CommonResult<List<McsProjectOrderDTO>> getPartRecordByProjectNumber(@RequestParam("projectNumber") String projectNumber);

    //根据项目号(批量) 查询零件加工情况 (项目)
    @GetMapping(value = "/mcs/rest/getPartRecordByProjectList")
    CommonResult<List<McsProjectOrderDTO>> getPartRecordByProjectNumber(@RequestParam("projectNumbers") List<String> projectNumbers);

    /**
     * 根据物料id集合 查询零件当前任务
     * @return map key: 物料id, value: 批次任务
     */
    @GetMapping(value = "/mcs/rest/getCurrentPlanByMaterialIds")
    CommonResult<Map<String,McsBatchRecordDTO>> getCurrentPlanByMaterialIds(@RequestParam("materialIds") List<String> materialIds);

    /**
     * 根据物料条码 查询加工记录 (质量)
     * @return map key: 物料id, value: 工序加工记录->工步加工记录
     */
    @GetMapping(value = "/mcs/rest/getRecordsByMaterialIds")
    CommonResult<Map<String,List<McsBatchRecordDTO>>> getRecordsByBarCodeList(@RequestParam("barCodeList") List<String> barCodeList);

    /**
     * 检验单生成
     * @param recordId 批次任务id
     * @param schemeId 检验方案ID
     */
    @PostMapping(value = "/mcs/rest/createInspectionSheet")
    CommonResult<?> createInspectionSheet(@RequestParam("recordId") String recordId,
                                          @RequestParam("barCode") String barCode,
                                          @RequestParam("recordId") String schemeId,
                                          @RequestParam("userId") String userId);

    /**
     * 根据配送申请单号 查询配送位置
     */
    @GetMapping(value = "/mcs/rest/getOrderReqLocation")
    CommonResult<List<McsDistributionLocationDTO>> getOrderReqLocation(@RequestParam("applicationNumbers") List<String> applicationNumbers);

    @GetMapping(value = "/mcs/rest/getLocationByUser")
    CommonResult<List<String>> getLocationByUser(@RequestParam("userId") String userId);

    /**
     * 接收外协发起信息
     */
    @PostMapping(value = "/mcs/rest/outsourcingStart")
    CommonResult<?> outsourcingStart(@RequestBody McsOutsourcingPlanDTO outsourcingPlanDTO);

    /**
     * 接收工序外协完成信息
     */
    @PostMapping(value = "/mcs/rest/outsourcingFinish")
    CommonResult<?> outsourcingFinish(@RequestBody McsOutsourcingPlanDTO outsourcingPlanDTO);

    /**
     * 根据条码查询 当前零件 当前工序目标位置
     * @param barCode 物料条码
     */
    @GetMapping(value = "/mcs/rest/getTargetLocationListByBarCode")
    CommonResult<?> getTargetLocationListByBarCode(@RequestParam("barCode") String barCode);

    @PostMapping(value = "/mcs/rest/createNewMaterialInManufacture")
    CommonResult<?> createNewMaterialInManufacture(McsSplitProcessingProcedure splitProcessingProcedure);

    @GetMapping(value = "/mcs/rest/getDeviceListByLineCode")
    CommonResult<?> getDeviceListByLineCode(@RequestParam("lineCode") String lineCode);

    @GetMapping(value = "/mcs/rest/getEgLedger")
    CommonResult<String[]> getEgLedger();

    /**
     * 根据库位id查询设备编码
     * @return
     */
    @GetMapping(value = "/mcs/rest/getDeviceCodeByLocationId")
    CommonResult<String> getDeviceCodeByLocationId(@RequestParam("locationId") String locationId);

}
