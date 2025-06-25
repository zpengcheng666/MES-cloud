package com.miyu.module.wms.core.carrytask.service.impl;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.cloud.mcs.api.McsManufacturingControlApi;
import com.miyu.cloud.mcs.dto.distribution.McsDistributionLocationDTO;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;


@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class ToolDistributionServiceImpl extends DispatchCarryTaskLogicService {
    @Resource
    private McsManufacturingControlApi mcsManufacturingControlApi;
    @Resource
    @Lazy
    private CarryTaskService carryTaskService;
    @Resource
    private OutWarehouseDetailService outWarehouseDetailService;
    @Resource
    private MaterialConfigAreaService materialConfigAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private MaterialStockService materialStockService;

    // 刀具配送
    @Override
    public void createCarryTaskLogic(CarryTaskDO carryTask, MaterialStockDO trayStock, String needLocationIdsStr){
        String trayConfigId = trayStock.getMaterialConfigId();
        String trayStockId = trayStock.getId();

        if (carryTask== null){
            carryTask = new CarryTaskDO();
        }
        carryTask.setReflectStockId(trayStockId);
        carryTask.setReflectWarehouseId(needLocationIdsStr);

        List<String> targetLocationIdList = Arrays.asList(needLocationIdsStr.split(","));
        // 获取目标库位实体集合
        List<WarehouseLocationDO> targetLocationList = warehouseLocationService.getWarehouseLocationListByIds(targetLocationIdList);
        // 根据物料类获取托盘的所有物料库区配置
        List<MaterialConfigAreaDO> trayMaterialConfigArea = materialConfigAreaService.getTrayMaterialConfigAreaByMaterialConfigId(trayConfigId);
        // 将库区id转为set集合
        Set<String> trayConfigAreaIdSet = CollectionUtils.convertSet(trayMaterialConfigArea, MaterialConfigAreaDO::getWarehouseAreaId);
        // 校验目标库位所属库区 是否都在托盘的物料库区配置中
        targetLocationList.forEach(location -> {
            // 如果托盘的库区配置不存在此目标库区，则抛出异常
            if (!trayConfigAreaIdSet.contains(location.getWarehouseAreaId())) {
                throw exception(MATERIAL_STOCK_TRAY_NOT_CONFIG_DESTINATION);
            }
        });
        String targetLocationIdsStr = null;
        // 校验目标库位是否能生成搬运任务   是否被占用是否存在任务
        if(checkCanCreateCarryTaskByLocationIds(targetLocationIdList)){
            targetLocationIdsStr = String.join(",", targetLocationIdList);
        }

        // 5. 生成搬运任务
        carryTaskService.createToolDistributionCarryTask(carryTask, trayStock, targetLocationIdsStr);

    }

    // 根据物料ids 校验其任务单
    public List<OutWarehouseDetailDO> checkOrderTask(Map<String, MaterialStockDO> allMaterialStockMap) {
        // 刀具出库存在多个目标仓库 所以不用校验目标仓库
        List<OutWarehouseDetailDO> allOutWarehouseDetailList = outWarehouseDetailService.checkOutWarehouseDetail(null, allMaterialStockMap);

        // 根据需求库位的配送顺序排序
        return allOutWarehouseDetailList.stream().sorted(Comparator.comparing(OutWarehouseDetailDO::getDeliverySequence)).collect(Collectors.toList());
    }

    // 填入刀具配送的目标库位
    public void fillInOutWarehouseDetailTargetLocation(String materialId, String orderNumber) {
        MaterialStockDO cutterStock = materialStockService.getMaterialStockById(materialId);
        if(cutterStock == null){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        // 判断是否为刀具
        if(DictConstants.WMS_MATERIAL_TYPE_DJ.equals(cutterStock.getMaterialType())){
            return;
        }
        // 根据单号获取 待审核的出库单
        List<OutWarehouseDetailDO> outWarehouseDetailDOList = outWarehouseDetailService.getOutWarehouseDetailListByOrderNumber(orderNumber,DictConstants.WMS_ORDER_DETAIL_STATUS_6);

        if(outWarehouseDetailDOList.isEmpty() ){
            return;
        }
        OutWarehouseDetailDO outWarehouseDetailDO = outWarehouseDetailDOList.get(0);
        if(materialId.equals(outWarehouseDetailDO.getChooseStockId())){
            return;
        }

        // 获取刀具的需求库位
        CommonResult<List<McsDistributionLocationDTO>> result = mcsManufacturingControlApi.getOrderReqLocation(Collections.singletonList(orderNumber));
        if(!result.isSuccess() || CollectionUtils.isAnyEmpty(result.getData())){
            throw exception(GET_TOOL_DISTRIBUTION_ORDER_ERROR);
        }
        List<McsDistributionLocationDTO> mcsDistributionLocationDTOList = result.getData();

        McsDistributionLocationDTO mcsDistributionLocationDTO = mcsDistributionLocationDTOList.get(0);
        // 校验需求库位是否存在
        if(StringUtils.isBlank(mcsDistributionLocationDTO.getTargetLocation())){
            throw exception(MATERIAL_STOCK_TRAY_NOT_CONFIG_BIN);
        }
        // 获取配送库位
        WarehouseLocationDO warehouseLocation = warehouseLocationService.getWarehouseLocation(mcsDistributionLocationDTO.getTargetLocation());
        if(warehouseLocation == null){
            throw exception(MATERIAL_STOCK_TRAY_NOT_CONFIG_DESTINATION_ERROR);
        }
        // 根据物料获取物料对应的出库单 并更新需求库位
        outWarehouseDetailDO.setNeedLocationId(warehouseLocation.getId());
        // 填入配送顺序
        outWarehouseDetailDO.setDeliverySequence(warehouseLocation.getDeliverySequence());

        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(mcsDistributionLocationDTO.getTargetLocation());
        if(warehouseArea == null){
            throw exception(MATERIAL_STOCK_TRAY_NOT_CONFIG_DESTINATION_ERROR);
        }
        // 更新目标仓库id
        outWarehouseDetailDO.setTargetWarehouseId(warehouseArea.getWarehouseId());

        // 更新需求库位
        outWarehouseDetailService.updateById(outWarehouseDetailDO);
    }


    // 更新任务单状态
    @Override
    public boolean updateOrderTask(String containerStockId,Integer updateState) {
        return outWarehouseDetailService.updateBatchOutWarehouseDetailStateByMaterialStockId(containerStockId, updateState);
    }

    @Override
    public int setOperatorInOrderTask(Set<String> orderIds) {
        return outWarehouseDetailService.setOperatorInBatchOutWarehouseDetail(orderIds);
    }


    // 校验目标库位集合是否能生成搬运任务
    private boolean checkCanCreateCarryTaskByLocationIds(List<String> locationIds){
        // 根据库位查询所有存在库存的物料库存
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByLocationIds(locationIds);
        // 取出有库存的库位id
        Set<String> materialStockLocationIdsSet = CollectionUtils.convertSet(materialStockList, MaterialStockDO::getLocationId);
        // 如果目标库存在库存，则抛出异常
        for (String locationId : locationIds) {
            if(materialStockLocationIdsSet.contains(locationId)){
                log.info("此库位id：{} -已存在刀具托盘，暂时无法生成搬运任务!",locationId);
                return false;
            }
        }

        // 获取所有未完成的搬运任务
        List<CarrySubTaskDO> unfinishedCarrySubTask = carryTaskService.getUnfinishedCarrySubTask();
        Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarrySubTask, CarrySubTaskDO::getLocationId);

        if(CollectionUtils.isAnyEmpty(haveTaskLocationIdSet)){
            return true;
        }

        for (String locationId : locationIds) {
            if(haveTaskLocationIdSet.contains(locationId)){
                log.info("此库位id：{} -已存在搬运任务，暂时无法生成搬运任务!",locationId);
                return false;
            }
        }
        return true;
    }
}