package com.miyu.cloud.mcs.restServer.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.mysql.devicetype.DeviceTypeMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.cloud.mcs.dto.distribution.McsDistributionLocationDTO;
import com.miyu.cloud.mcs.dto.manufacture.*;
import com.miyu.cloud.mcs.dto.orderForm.McsOrderFormCreateDTO;
import com.miyu.cloud.mcs.dto.outsourcing.McsOutsourcingPlanDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsPlanProcessDTO;
import com.miyu.cloud.mcs.dto.productionProcess.McsProjectOrderDTO;
import com.miyu.module.wms.api.order.dto.ProductionOrderReqDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;

@RestController
@Transactional
public class ManufacturingControlApiImpl implements McsManufacturingControlApi {

    @Resource
    private ManufacturingService manufacturingService;
    @Resource
    private LedgerService ledgerService;
    @Autowired
    private LedgerMapper ledgerMapper;
    @Autowired
    private DeviceTypeMapper deviceTypeMapper;

    @Override
    public CommonResult<?> orderFormCreate(McsOrderFormCreateDTO orderFormCreate) {
        return manufacturingService.orderFormCreate(orderFormCreate);
    }

    @Override
    public CommonResult<?> orderFormCancel(String orderNumber) {
        return manufacturingService.orderFormCancel(orderNumber);
    }

    //批次任务 开工
    @Override
    public CommonResult<?> batchRecordStart(McsBatchRecordEventDTO batchRecord) {
        return manufacturingService.batchRecordStart(batchRecord);
    }

    //批次任务 完工
    @Override
    public CommonResult<?> batchRecordEnd(McsBatchRecordEventDTO batchRecord) {
        return manufacturingService.batchRecordEnd(batchRecord);
    }

    //工位事件 工位暂停/恢复
    @Override
    public CommonResult<?> stepWorkstationEvent(McsWorkstationDTO workstationDTO) {
        return manufacturingService.stepWorkstationEvent(workstationDTO);
    }

    //入库
    @Override
    public CommonResult<?> putInStorage(String deviceId, List<McsBatchResourceDTO> resourceList) {
        return manufacturingService.putInStorage(deviceId, resourceList);
    }

    //移交(下一工位)
//    @Override
//    public CommonResult<?> transfer(String deviceId, List<McsBatchResourceDTO> resourceList) {
//        return manufacturingService.transfer(deviceId, resourceList);
//    }

    //根据工位获取当前任务(工序任务)
    @Override
    public CommonResult<List<McsBatchRecordDTO>> getBatchPlanByWorkstation(String workstationId) {
        return manufacturingService.getBatchPlanByWorkstation(workstationId);
    }

    //根据工位Ip获取当前任务(工序任务)
    @Override
    public CommonResult<List<McsBatchRecordDTO>> getBatchPlanByWorkstationIp(String workstationIp) {
        return manufacturingService.getBatchPlanByWorkstationIp(workstationIp);
    }

    //根据产线(自带管理系统)获取当前任务(工序任务)
    @Override
    public CommonResult<List<McsOrderFormDTO>> getBatchPlanByProductionLine(String productionLine) {
        return manufacturingService.getBatchPlanByProductionLine(productionLine);
    }

    //根据任务(批次)id 查询物料需求
    @Override
    public CommonResult<List<McsBatchResourceDTO>> getBatchResourceByRecordId(String recordId, String workstationId) {
        return manufacturingService.getBatchResourceByRecordId(recordId,workstationId);
    }

    //根据任务(批次)id 查询详细任务(零件)
    @Override
    public CommonResult<McsBatchRecordDTO> getBatchDetailByRecordId(String recordId) {
        return manufacturingService.getBatchDetailByRecordId(recordId);
    }

    //根据批次任务id 查询工序(包含工步)
    @Override
    public CommonResult<McsPlanProcessDTO> getProcessByRecordId(String recordId) {
        return manufacturingService.getProcessByRecordId(recordId);
    }

    //根据工位是否暂停 暂停true
    @Override
    public CommonResult<Boolean> getWorkstationPause(String workstationId) {
        return manufacturingService.getWorkstationPause(workstationId);
    }

    @Override
    public CommonResult<List<McsProjectOrderDTO>> getPartRecordByProjectNumber(String projectNumber) {
        return manufacturingService.getPartRecordByProjectNumber(Collections.singletonList(projectNumber));
    }

    //根据项目号(批量) 查询零件加工情况
    @Override
    public CommonResult<List<McsProjectOrderDTO>> getPartRecordByProjectNumber(List<String> projectNumbers) {
        return manufacturingService.getPartRecordByProjectNumber(projectNumbers);
    }

    //根据物料id集合 查询零件当前任务
    @Override
    public CommonResult<Map<String,McsBatchRecordDTO>> getCurrentPlanByMaterialIds(List<String> materialIds) {
        return manufacturingService.getCurrentPlanByMaterialIds(materialIds);
    }

    //根据物料id 查询加工记录
    @Override
    public CommonResult<Map<String, List<McsBatchRecordDTO>>> getRecordsByBarCodeList(List<String> barCodeList) {
        return manufacturingService.getRecordsByBarCodeList(barCodeList);
    }

    /**
     * 检验单生成
     * @param recordId 批次任务id
     * @param schemeId 检验方案ID
     */
    @Override
    public CommonResult<?> createInspectionSheet(String recordId, String barCode, String schemeId, String userId) {
        return manufacturingService.createInspectionSheet(recordId, barCode, schemeId, userId);
    }

    @Override
    public CommonResult<List<McsDistributionLocationDTO>> getOrderReqLocation(List<String> applicationNumbers) {
        return manufacturingService.getOrderReqLocation(applicationNumbers);
    }

    @Override
    public CommonResult<List<String>> getLocationByUser(String userId) {
        return manufacturingService.getLocationByUser(userId);
    }

    @Override
    public CommonResult<?> outsourcingStart(McsOutsourcingPlanDTO outsourcingPlanDTO) {
        return manufacturingService.outsourcingStart(outsourcingPlanDTO);
    }

    @Override
    public CommonResult<?> outsourcingFinish(McsOutsourcingPlanDTO outsourcingPlanDTO) {
        return manufacturingService.outsourcingFinish(outsourcingPlanDTO);
    }

    /**
     * 根据条码查询 当前零件 当前工序目标位置
     * @param barCode 物料条码
     */
    @Override
    public CommonResult<List<ProductionOrderReqDTO>> getTargetLocationListByBarCode(String barCode) {
        return manufacturingService.getTargetLocationListByBarCode(barCode);
    }

    @Override
    public CommonResult<?> createNewMaterialInManufacture(McsSplitProcessingProcedure splitProcessingProcedure) {
        return manufacturingService.createNewMaterialInManufacture(splitProcessingProcedure);
    }

    @Override
    public CommonResult<?> getDeviceListByLineCode(String lineCode) {
        return manufacturingService.getDeviceListByLineCode(lineCode);
    }

    @Override
    public CommonResult<String[]> getEgLedger() {
        List<LedgerDO> list =ledgerMapper.getEgLedger(deviceTypeMapper.getEgId().getId());
        return CommonResult.success(list.stream().map(LedgerDO::getIp).toArray(String[]::new));
    }

    @Override
    public CommonResult<String> getDeviceCodeByLocationId(String locationId) {
        return manufacturingService.getDeviceCodeByLocationId(locationId);
    }
}
