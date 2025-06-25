package com.miyu.cloud.mcs.restServer.service.warehouse;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.dto.resource.McsMaterialConfigDTO;
import com.miyu.cloud.mcs.restServer.api.WmsOrderApiMapping;
import com.miyu.cloud.mcs.restServer.service.encoding.EncodingService;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.CarryTrayStatusDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockLocationTypeDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.OrderApi;
import com.miyu.module.wms.api.order.dto.*;
import com.miyu.module.wms.api.warehouse.WarehouseLocationApi;
import com.miyu.module.wms.api.warehouse.dto.WarehouseLocationRespDTO;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.miyu.cloud.mcs.enums.DictConstants.*;
import static com.miyu.module.wms.enums.DictConstants.*;

@Service
@Validated
public class WarehouseRestServiceImpl implements WarehouseRestService {

    @Resource
    private OrderApi orderApi;
    @Resource
    private WmsOrderApiMapping orderApiMapping;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private EncodingService encodingService;
    @Resource
    private WmsOrderApiMapping wmsOrderApiMapping;
    @Resource
    private MaterialConfigApi materialConfigApi;
    @Resource
    private WarehouseLocationApi warehouseLocationApi;

    //校验配送完成
    @Override
    public List<DistributionRecordDO> checkDeliveryComplete(List<DistributionRecordDO> recordList) {
        List<OrderReqDTO> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        for (DistributionRecordDO distributionRecordDO : recordList) {
            String applicationId = distributionRecordDO.getNumber();
            int type = distributionRecordDO.getType();
            if (!set.contains(applicationId)) {
                OrderReqDTO orderReqDTO = new OrderReqDTO();
                list.add(orderReqDTO);
                orderReqDTO.setOrderNumber(applicationId);
                orderReqDTO.setOrderType(type);
                set.add(applicationId);
            }
        }
        CommonResult<List<OrderReqDTO>> listCommonResult = orderApiMapping.orderList(list, "1");
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
        List<DistributionRecordDO> result = new ArrayList<>();
        for (OrderReqDTO orderReqDTO : listCommonResult.getData()) {
            Optional<DistributionRecordDO> first = recordList.stream().filter(item -> orderReqDTO.getOrderNumber().equals(item.getNumber())
                    && orderReqDTO.getMaterialStockId().equals(item.getMaterialUid())).findFirst();
            if (first.isPresent()) {
                DistributionRecordDO distributionRecordDO = first.get();
                if (orderReqDTO.getOrderStatus().equals(WMS_ORDER_DETAIL_STATUS_4)) {
                    distributionRecordDO.setStatus(MCS_DELIVERY_RECORD_STATUS_COMPLETED);
                    distributionRecordDO.setOperatorId(orderReqDTO.getOperator());
                    distributionRecordDO.setTime(orderReqDTO.getOperateTime());
                    result.add(distributionRecordDO);
                } else if (orderReqDTO.getOrderStatus().equals(WMS_ORDER_DETAIL_STATUS_5)) {
                    distributionRecordDO.setStatus(MCS_DELIVERY_RECORD_STATUS_CLOSE);
                    result.add(distributionRecordDO);
                } else if (orderReqDTO.getOrderStatus().equals(WMS_ORDER_DETAIL_STATUS_3)) {
                    if (distributionRecordDO.getStatus() != MCS_DELIVERY_RECORD_STATUS_READY) {
                        distributionRecordDO.setStatus(MCS_DELIVERY_RECORD_STATUS_READY);
                        result.add(distributionRecordDO);
                    }
                }
            }
        }
        return result;
    }

    //出库
    @Override
    public void outboundApplication(List<DistributionRecordDO> recordList) {
        List<OrderReqDTO> orderReqDTOS = generateOrderReqDTO(recordList);
        orderReqDTOS.forEach(item -> item.setOrderStatus(WMS_ORDER_DETAIL_STATUS_6));
        CommonResult<List<String>> listCommonResult = orderApi.orderDistribute(orderReqDTOS);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
    }

    //入库
    @Override
    public void putInStorage(List<DistributionRecordDO> recordList) {
        List<OrderReqDTO> orderReqDTOS = generateOrderReqDTO(recordList);
        orderReqDTOS.forEach(item -> item.setOrderStatus(WMS_ORDER_DETAIL_STATUS_1));
        CommonResult<List<String>> listCommonResult = orderApi.orderDistribute(orderReqDTOS);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
    }

    //移交
    @Override
    public void materialTransfer(List<DistributionRecordDO> recordList) {
        List<OrderReqDTO> orderReqDTOS = generateOrderReqDTO(recordList);
        orderReqDTOS.forEach(item -> item.setOrderStatus(WMS_ORDER_DETAIL_STATUS_1));
        CommonResult<List<String>> listCommonResult = orderApi.orderDistribute(orderReqDTOS);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
    }

    private List<OrderReqDTO> generateOrderReqDTO(List<DistributionRecordDO> recordList) {
        if (recordList.size() == 0) throw new ServiceException(5002, "配送数量为0");
        ArrayList<OrderReqDTO> orderReqDTOS = new ArrayList<>();
        for (DistributionRecordDO distributionRecordDO : recordList) {
            OrderReqDTO orderReqDTO = new OrderReqDTO();
            orderReqDTOS.add(orderReqDTO);
            orderReqDTO.setOrderType(distributionRecordDO.getType());
            orderReqDTO.setOrderNumber(distributionRecordDO.getNumber());
            orderReqDTO.setBatchNumber(distributionRecordDO.getBatchNumber());
            orderReqDTO.setMaterialConfigId(distributionRecordDO.getMaterialConfigId());
            orderReqDTO.setQuantity(distributionRecordDO.getCount());
            orderReqDTO.setChooseStockId(distributionRecordDO.getMaterialUid());
            orderReqDTO.setStartWarehouseId(distributionRecordDO.getStartLocationId());
            orderReqDTO.setTargetWarehouseId(distributionRecordDO.getTargetLocationId());
            orderReqDTO.setNeedTime(distributionRecordDO.getPlanStartTime());
        }
        return orderReqDTOS;
    }

    //加工后变更物料信息
    @Override
    public List<MaterialStockRespDTO> updateMaterialsByPlan(List<String> oldMaterialIdList, String partNumber, String procedureNum, String batchNumber) {
        List<MaterialStockRespDTO> result = new ArrayList<>();
        McsMaterialConfigDTO materialConfigDTO = new McsMaterialConfigDTO();
        materialConfigDTO.setPartNumber(partNumber);
        if (procedureNum == null) {
            materialConfigDTO.setMaterialConfigNumber(partNumber);
        } else {
            materialConfigDTO.setMaterialConfigNumber(partNumber + "_" + procedureNum);
        }
        //获取类型
        materialConfigDTO = encodingService.getMaterialConfig(materialConfigDTO);
        for (String oldMaterialId : oldMaterialIdList) {
            CommonResult<MaterialStockRespDTO> materialResult = materialStockApi.getById(oldMaterialId);
            if (!materialResult.isSuccess()) throw new ServiceException(materialResult.getCode(), materialResult.getMsg());
            MaterialStockRespDTO oldMaterial = materialResult.getData();
            MaterialStockRespDTO material = new MaterialStockRespDTO();
            if (procedureNum == null) {
                material.setMaterialConfigId(materialConfigDTO.getPartTypeId());
            } else {
                material.setMaterialConfigId(materialConfigDTO.getMaterialConfigId());
            }
            material.setMaterialNumber(materialConfigDTO.getMaterialConfigNumber());
            material.setBarCode(oldMaterial.getBarCode());
            material.setMaterialManage(oldMaterial.getMaterialManage());
            material.setBatchNumber(oldMaterial.getBatchNumber());
            material.setTotality(oldMaterial.getTotality());
            CommonResult<Boolean> booleanCommonResult = materialStockApi.updateMaterialStockConfig(oldMaterialId, material.getMaterialConfigId());
            if (!booleanCommonResult.isSuccess()) throw new ServiceException(materialResult.getCode(), materialResult.getMsg());
            if (!booleanCommonResult.getData()) throw new ServiceException(5005, "物料类型变更失败:"+oldMaterialId);
            result.add(material);
        }
        return result;
    }

    @Override
    public MaterialStockRespDTO updateMaterialsByPlan(String oldMaterialIdList, String partNumber, String procedureNum, String batchNumber) {
        return updateMaterialsByPlan(Collections.singletonList(oldMaterialIdList), partNumber, procedureNum, batchNumber).get(0);
    }

    @Override
    public String getMaterialLocation(String materialUid) {
        CommonResult<MaterialStockRespDTO> materialStockResult = materialStockApi.getById(materialUid);
        if (!materialStockResult.isSuccess()) throw new ServiceException(materialStockResult.getCode(), materialStockResult.getMsg());
        MaterialStockRespDTO stockResult = materialStockResult.getData();
        return stockResult.getLocationId();
    }

    @Override
    public List<MaterialStockRespDTO> getOutOrderMaterialsByConfigIds(Collection<String> singletonList) {
        CommonResult<List<MaterialStockRespDTO>> listCommonResult = materialStockApi.getOutOrderMaterialsByConfigIds(singletonList);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
        return listCommonResult.getData();
    }

    @Override
    public List<MaterialStockLocationTypeDTO> getMaterialsByConfigIdsWithWarehouse(Collection<String> singletonList) {
        CommonResult<List<MaterialStockLocationTypeDTO>> listCommonResult = materialStockApi.getMaterialsAndLocationInfoByConfigIds(singletonList);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
        return listCommonResult.getData();
    }

    @Override
    public List<MaterialStockRespDTO> getByIds(Collection<String> materialIds) {
        return materialIds.stream().map(materialId -> materialStockApi.getById(materialId)).filter(CommonResult::isSuccess).map(CommonResult::getData).filter(Objects::nonNull).collect(Collectors.toList());
    }

    @Override
    public MaterialStockRespDTO getById(String materialId) {
        CommonResult<MaterialStockRespDTO> commonResult = materialStockApi.getById(materialId);
        if (!commonResult.isSuccess()) throw new ServiceException(commonResult.getCode(), commonResult.getMsg());
        return commonResult.getData();
    }

    @Override
    public MaterialStockRespDTO splitBatch(String materialId, Integer count1) {
        // TODO: 2024/10/8 暂时 无此功能
        return null;
    }

    @Override
    public void adoptApplication(List<DistributionRecordDO> recordDOS) {
        List<OrderReqDTO> orderReqDTOS = generateOrderReqDTO(recordDOS);
        orderReqDTOS.forEach(item -> item.setOrderStatus(WMS_ORDER_DETAIL_STATUS_1));
        CommonResult<List<String>> listCommonResult = orderApi.orderUpdateStatus(orderReqDTOS);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());

    }

    @Override
    public MaterialStockRespDTO getByBarCode(String barCode) {
        CommonResult<List<MaterialStockRespDTO>> listCommonResult = materialStockApi.getMaterialsByBarCode(barCode);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
        return listCommonResult.getData().get(0);
    }

    @Override
    public MaterialStockRespDTO getByBarCodeIgnoreTenantId(String barCode) {
        CommonResult<List<MaterialStockRespDTO>> listCommonResult = orderApiMapping.getMaterialsByBarCode(barCode, "1");
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
        return listCommonResult.getData().get(0);
    }

    @Override
    public MaterialStockRespDTO getByBarCodeLocationIgnoreTId(String barCode) {
        CommonResult<MaterialStockRespDTO> listCommonResult = orderApiMapping.getMaterialAtLocationByBarCode(barCode, "1");
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
        return listCommonResult.getData();
    }

    @Override
    public void updateDistributionWarehouse(Collection<DistributionRecordDO> otherSet) {
        List<OrderUpdateDTO> updateList = new ArrayList<>();
        for (DistributionRecordDO distributionRecordDO : otherSet) {
            OrderUpdateDTO updateDTO = new OrderUpdateDTO();
            updateDTO.setMaterialStockId(distributionRecordDO.getMaterialUid());
            updateDTO.setTargetWarehouseId(distributionRecordDO.getTargetLocationId());
            updateDTO.setOrderStatus(WMS_ORDER_DETAIL_STATUS_6);
            updateDTO.setOrderType(WMS_ORDER_TYPE_PRODUCE_OUT);
            updateList.add(updateDTO);
        }
        CommonResult<List<String>> listCommonResult = orderApi.updateTargetWarehouse(updateList);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
    }

    @Override
    public void distributionRecordRevoke(List<DistributionRecordDO> recordList) {
        List<OrderReqDTO> orderReqDTOS = generateOrderReqDTO(recordList);
        orderReqDTOS.forEach(item -> item.setOrderStatus(WMS_ORDER_DETAIL_STATUS_5));
        CommonResult<List<String>> listCommonResult = orderApi.orderUpdateStatus(orderReqDTOS);
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
    }

    @Override
    public List<ProductionOrderRespDTO> autoProductionDispatch(List<ProductionOrderReqDTO> reqDTOList) {
        CommonResult<List<ProductionOrderRespDTO>> listCommonResult = orderApiMapping.autoProductionDispatch(reqDTOList, "1");
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
        return listCommonResult.getData();
    }

    @Override
    public List<SpecifiedTransportationRespDTO> detectionDeviceCarry(List<SpecifiedTransportationReqDTO> reqDTOList) {
        CommonResult<List<SpecifiedTransportationRespDTO>> listCommonResult = orderApiMapping.specifiedStorageSpaceTransportation(reqDTOList, "1");
        if (!listCommonResult.isSuccess()) throw new ServiceException(listCommonResult.getCode(), listCommonResult.getMsg());
        return listCommonResult.getData();
    }

    @Override
    public void createNewMaterialInManufacture(String barCode, String location) {

    }

    @Override
    public Map<String, CarryTrayStatusDTO> getMaterialCarryReadyStatusList() {
        CommonResult<Map<String, CarryTrayStatusDTO>> callTrayStatus = orderApi.getCallTrayStatus();
        if (!callTrayStatus.isSuccess()) throw new ServiceException(callTrayStatus.getCode(), callTrayStatus.getMsg());
        return callTrayStatus.getData();
    }

    @Override
    public void deliveryCheck(String barCode, String location, String operator) {
        Map<String,Object> map = new HashMap<>();
        map.put("barCode", barCode);
        map.put("locationCode", location);
        CommonResult<List<OrderReqDTO>> result = wmsOrderApiMapping.wmsCheckOut(map, "1");
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
    }

    @Override
    public List<OrderReqDTO> getNotCompleteOrder() {
        CommonResult<List<OrderReqDTO>> result = orderApiMapping.getNotCompleteOrder("1");
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
        return result.getData();
    }

    @Override
    public Map<String, MaterialConfigRespDTO> getMaterialConfigById(Collection<String> materialConfigIds) {
        return materialConfigApi.getMaterialConfigMap(materialConfigIds);
    }

    @Override
    public List<WarehouseLocationRespDTO> getWarehouseLocationByIds(Collection<String> locationIds) {
        CommonResult<List<WarehouseLocationRespDTO>> result = warehouseLocationApi.getWarehouseLocationByIds(locationIds);
        if (!result.isSuccess()) throw new ServiceException(result.getCode(), result.getMsg());
        return result.getData();
    }
}

