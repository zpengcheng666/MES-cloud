package com.miyu.cloud.mpc.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.mysql.devicetype.DeviceTypeMapper;
import com.miyu.cloud.dms.dal.mysql.ledger.LedgerMapper;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.batchrecordstep.BatchRecordStepDO;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.dal.dataobject.productionrecords.ProductionRecordsDO;
import com.miyu.cloud.mcs.dal.mysql.batchrecord.BatchRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.batchrecordstep.BatchRecordStepMapper;
import com.miyu.cloud.mcs.dal.mysql.distributionrecord.DistributionRecordMapper;
import com.miyu.cloud.mcs.dal.mysql.orderform.OrderFormMapper;
import com.miyu.cloud.mcs.dal.mysql.productionrecords.ProductionRecordsMapper;
import com.miyu.cloud.mcs.restServer.service.inspection.InspectionSheetService;
import com.miyu.cloud.mcs.restServer.service.warehouse.WarehouseRestService;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.dto.SpecifiedTransportationReqDTO;
import com.miyu.module.wms.api.order.dto.SpecifiedTransportationRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;

import static com.miyu.cloud.mcs.enums.DictConstants.*;

@Slf4j
@Service
@Validated
@Transactional
public class EventTriggerServiceImpl implements EventTriggerService {

    @Resource
    private DeviceTypeMapper deviceTypeMapper;
    @Resource
    private LedgerMapper ledgerMapper;
    @Resource
    private BatchRecordStepMapper batchRecordStepMapper;
    @Resource
    private ProductionRecordsMapper productionRecordsMapper;
    @Resource
    private DistributionRecordMapper distributionRecordMapper;
    @Resource
    private BatchRecordMapper batchRecordMapper;
    @Resource
    private OrderFormMapper orderFormMapper;

    @Resource
    private WarehouseRestService warehouseRestService;
    @Resource
    private LedgerService ledgerService;
    @Resource
    private InspectionSheetService inspectionSheetService;

    //三坐标测量 内部搬运
    @Override
    public void checkDetectionDeviceCarry() {
        DeviceTypeDO dcm3 = deviceTypeMapper.selectOne(DeviceTypeDO::getCode, "DCM3");
        DeviceTypeDO dcm6 = deviceTypeMapper.selectOne(DeviceTypeDO::getCode, "DCM6");
        DeviceTypeDO mss3 = deviceTypeMapper.selectOne(DeviceTypeDO::getCode, "DSS3");
        DeviceTypeDO mss6 = deviceTypeMapper.selectOne(DeviceTypeDO::getCode, "DSS6");
        Map<String,String> map = new HashMap<>();
        map.put(dcm3.getId(), mss3.getId());
        map.put(dcm6.getId(), mss6.getId());
        List<SpecifiedTransportationReqDTO> carryTaskList = new ArrayList<>();
        Map<String,Map<String,Object>> messageMap = new HashMap<>();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            //获取测量设备
            List<LedgerDO> detectionList = ledgerMapper.selectList(LedgerDO::getEquipmentStationType, entry.getKey());
            //获取静止设备
            List<LedgerDO> coolingList = ledgerMapper.selectList(LedgerDO::getEquipmentStationType, entry.getValue());
            Set<String> coolingIdSet = new HashSet<>();
            Set<String> coolingLocationSet = new HashSet<>();
            for (LedgerDO aDo : coolingList) {
                String id = aDo.getId();
                coolingIdSet.add(id);
                String toolLocationByDevice = ledgerService.getToolLocationByDevice(id);
                if (toolLocationByDevice != null) {
                    coolingLocationSet.add(toolLocationByDevice);
                }
            }
            //遍历测量设备 寻找可开工的任务
            for (LedgerDO ledgerDO : detectionList) {
                //屏蔽已有未完成搬运任务设备
                LambdaQueryWrapper<DistributionRecordDO> dqw = new LambdaQueryWrapper<>();
                dqw.in(DistributionRecordDO::getStatus, MCS_DELIVERY_RECORD_STATUS_NEW, MCS_DELIVERY_RECORD_STATUS_APPLIED, MCS_DELIVERY_RECORD_STATUS_DELIVERY);
                dqw.eq(DistributionRecordDO::getDeviceId, ledgerDO.getId());
                Long count = distributionRecordMapper.selectCount(dqw);
                if (count > 0) continue;
                //查询任务
                LambdaQueryWrapper<BatchRecordDO> queryWrapper = new LambdaQueryWrapper<>();
                queryWrapper.eq(BatchRecordDO::getStatus, MCS_BATCH_RECORD_STATUS_ONGOING);
                queryWrapper.eq(BatchRecordDO::getProcesStatus, MCS_PROCES_STATUS_CURRENT);
                queryWrapper.like(BatchRecordDO::getDeviceId, ledgerDO.getId());
                List<BatchRecordDO> batchRecordDOList = batchRecordMapper.selectList(queryWrapper);
                for (BatchRecordDO batchRecordDO : batchRecordDOList) {
                    //判断物料是否在静止位
                    MaterialStockRespDTO material = warehouseRestService.getByBarCodeLocationIgnoreTId(batchRecordDO.getBarCode());
                    if (!coolingLocationSet.contains(material.getAtLocationId())) continue;
                    LambdaQueryWrapper<BatchRecordStepDO> queryWrapper1 = new LambdaQueryWrapper<>();
                    queryWrapper1.eq(BatchRecordStepDO::getBatchRecordId, batchRecordDO.getId());
                    queryWrapper1.orderByAsc(BatchRecordStepDO::getStepOrder);
                    List<BatchRecordStepDO> stepDOS = batchRecordStepMapper.selectList(queryWrapper1);
                    BatchRecordStepDO stepPlan = null;
                    for (BatchRecordStepDO stepDO : stepDOS) {
                        if (stepDO.getStatus() == MCS_STEP_STATUS_COMPLETED) continue;
                        if (stepDO.getStatus() == MCS_STEP_STATUS_NEW || stepDO.getStatus() == MCS_STEP_STATUS_ONGOING) {
                            stepPlan = stepDO;
                            break;
                        }
                    }
                    if (stepPlan == null) continue;
                    if (!stepPlan.getDefineDeviceId().equals(ledgerDO.getId())) continue;
                    if (stepPlan.getStatus() == MCS_STEP_STATUS_ONGOING) continue;

                    boolean cooled = false;
                    boolean begin = false;
                    boolean carry = false;
                    //判断下一步是否需要搬运
                    for (BatchRecordStepDO stepDO : stepDOS) {
                        String defineDeviceId = stepDO.getDefineDeviceId();
                        if (begin) {
                            if (cooled) {
                                carry = defineDeviceId.equals(ledgerDO.getId());
                            }
                            break;
                        } else {
                            begin = stepDO.getStatus() == MCS_STEP_STATUS_COMPLETED;
                        }
                        if (coolingIdSet.contains(defineDeviceId)) {
                            cooled = true;
                        }
                    }
                    if (carry) {
                        String location = ledgerService.getToolLocationByDevice(ledgerDO.getId());
                        SpecifiedTransportationReqDTO reqDTO = new SpecifiedTransportationReqDTO();
                        carryTaskList.add(reqDTO);
                        reqDTO.setStartLocationId(material.getAtLocationId());
                        reqDTO.setTargetLocationId(location);
                        reqDTO.setBarCode(material.getBarCode());
                        Map<String,Object> message = new HashMap<>();
                        message.put("device", ledgerDO);
                        message.put("record", batchRecordDO);
                        messageMap.put(location, message);
                    }
                }
            }
        }
        if (carryTaskList.size() == 0) return;
        List<SpecifiedTransportationRespDTO> productionOrderRespDTOS = warehouseRestService.detectionDeviceCarry(carryTaskList);
        creatDistributionRecord(productionOrderRespDTOS, messageMap);
    }

    private void creatDistributionRecord(List<SpecifiedTransportationRespDTO> respDTOS, Map<String,Map<String,Object>> messageMap) {
        List<DistributionRecordDO> list = new ArrayList<>();
        for (SpecifiedTransportationRespDTO respDTO : respDTOS) {
            String barCode = respDTO.getBarCode();
            String targetLocationId = respDTO.getTargetLocationId();
            String orderNumber = respDTO.getOrderNumber();
            int orderType = respDTO.getOrderType();
            Map<String, Object> map = messageMap.get(targetLocationId);
            LedgerDO device = (LedgerDO) map.get("device");
            BatchRecordDO batchRecordDO = (BatchRecordDO) map.get("record");
            MaterialStockRespDTO resourceDTO = warehouseRestService.getByBarCodeIgnoreTenantId(barCode);
            DistributionRecordDO recordDO = new DistributionRecordDO();
            recordDO.setMaterialUid(resourceDTO.getId());
            recordDO.setBatchRecordId(batchRecordDO.getId());
            recordDO.setNumber(orderNumber);
            recordDO.setMaterialConfigId(resourceDTO.getMaterialConfigId());
            recordDO.setProcessingUnitId(device.getLintStationGroup());
            recordDO.setDeviceId(device.getId());
            recordDO.setMaterialNumber(resourceDTO.getMaterialNumber());
            recordDO.setBatchNumber(resourceDTO.getBatchNumber());
            recordDO.setBarCode(resourceDTO.getBarCode());
            recordDO.setStatus(MCS_DELIVERY_RECORD_STATUS_DELIVERY);
            recordDO.setType(orderType);
            recordDO.setResourceType(WMS_MATERIAL_TYPE_WORKPIECE);
            recordDO.setBatch(resourceDTO.getMaterialManage());
            recordDO.setCount(resourceDTO.getTotality());
            list.add(recordDO);
        }
        if (list.size() > 0) {
            distributionRecordMapper.insertBatch(list);
        }
    }

    //质检工序 质检单生产 生成方法 独立事务
    public void createInspectionSheetTask(BatchRecordDO batchRecordDO) {
        OrderFormDO orderFormDO = orderFormMapper.selectById(batchRecordDO.getOrderId());
        MaterialStockRespDTO material = warehouseRestService.getByBarCodeIgnoreTenantId(batchRecordDO.getBarCode());
        batchRecordDO.setInspect(MCS_DETAIL_INSPECT_STATUS_WAITING);
        batchRecordMapper.updateById(batchRecordDO);
        inspectionSheetService.createInspectionSheetTask(batchRecordDO,material,orderFormDO.getTechnologyId());
    }
}
