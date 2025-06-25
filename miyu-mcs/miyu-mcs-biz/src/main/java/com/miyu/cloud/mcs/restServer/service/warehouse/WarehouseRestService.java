package com.miyu.cloud.mcs.restServer.service.warehouse;

import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.module.wms.api.mateiral.dto.CarryTrayStatusDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockLocationTypeDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.dto.*;
import com.miyu.module.wms.api.warehouse.dto.WarehouseLocationRespDTO;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface WarehouseRestService {

    //监听配送完成
    List<DistributionRecordDO> checkDeliveryComplete(List<DistributionRecordDO> recordList);

    //出库
    void outboundApplication(List<DistributionRecordDO> recordList);

    //入库
    void putInStorage(List<DistributionRecordDO> recordList);

    //移交
    void materialTransfer(List<DistributionRecordDO> recordList);

    //加工后变更物料信息
    List<MaterialStockRespDTO> updateMaterialsByPlan(List<String> oldMaterialIdList, String partNumber, String procedureNum, String batchNumber);

    MaterialStockRespDTO updateMaterialsByPlan(String oldMaterialId, String partNumber, String procedureNum, String batchNumber);

    //物料在当前位置
    String getMaterialLocation(String materialUid);

    List<MaterialStockRespDTO> getOutOrderMaterialsByConfigIds(Collection<String> singletonList);

    List<MaterialStockLocationTypeDTO> getMaterialsByConfigIdsWithWarehouse(Collection<String> singletonList);

    List<MaterialStockRespDTO> getByIds(Collection<String> materialIds);

    MaterialStockRespDTO getById(String materialId);

    MaterialStockRespDTO splitBatch(String materialId, Integer count1);

    //出库申请通过
    void adoptApplication(List<DistributionRecordDO> recordDOS);

    //根据条码获取物料
    MaterialStockRespDTO getByBarCode(String barCode);

    MaterialStockRespDTO getByBarCodeIgnoreTenantId(String barCode);

    MaterialStockRespDTO getByBarCodeLocationIgnoreTId(String barCode);

    //更新 配送任务目标仓库
    void updateDistributionWarehouse(Collection<DistributionRecordDO> otherSet);

    //撤销 配送任务
    void distributionRecordRevoke(List<DistributionRecordDO> recordList);

    List<ProductionOrderRespDTO> autoProductionDispatch(List<ProductionOrderReqDTO> reqDTOList);

    List<SpecifiedTransportationRespDTO> detectionDeviceCarry(List<SpecifiedTransportationReqDTO> reqDTOList);

    void createNewMaterialInManufacture(String barCode, String location);

    Map<String, CarryTrayStatusDTO> getMaterialCarryReadyStatusList();

    void deliveryCheck(String barCode, String location, String operator);

    List<OrderReqDTO> getNotCompleteOrder();

    Map<String, MaterialConfigRespDTO> getMaterialConfigById(Collection<String> materialConfigIds);


    List<WarehouseLocationRespDTO> getWarehouseLocationByIds(Collection<String> locationIds);
}
