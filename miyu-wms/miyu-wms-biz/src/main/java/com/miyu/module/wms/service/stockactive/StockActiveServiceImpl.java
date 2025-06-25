package com.miyu.module.wms.service.stockactive;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.api.order.OrderDistributeApiImpl;
import com.miyu.module.wms.controller.admin.materialstock.vo.MaterialStockPageReqVO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.stockactive.StockActiveMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;
import com.miyu.module.wms.service.movewarehousedetail.MoveWarehouseDetailService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.FilterMaterialUtils;
import com.miyu.module.wms.util.StringListUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

@Service
@Validated
public class StockActiveServiceImpl implements StockActiveService{

    @Resource
    private StockActiveMapper stockActiveMapper;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private OutWarehouseDetailService outWarehouseDetailService;
    @Resource
    private MoveWarehouseDetailService moveWarehouseDetailService;
    @Resource
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    private MaterialStorageService materialStorageService;
    @Resource
    private WarehouseService warehouseService;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    @Lazy
    private OrderDistributeApiImpl orderDistributeApi;


    @Override
    public PageResult<MaterialStockDO> getOnshelfPage(MaterialStockPageReqVO pageReqVO) {
        return stockActiveMapper.getOnshelfPage(pageReqVO);
    }

    @Override
    public PageResult<MaterialStockDO> getOffshelfPage(MaterialStockPageReqVO pageReqVO) {
        return stockActiveMapper.getOffshelfPage(pageReqVO);
    }


    /**
     * 出库签出
     * @param signForMaterialStock 签收的物料id
     * @param signForLocationId 签收的物料的库位id
     */
    @Override
    @Transactional
    public void signForAllWaitSignForMaterial(MaterialStockDO signForMaterialStock, String signForLocationId) {
        //根据容器id（托盘或工装） 查询其上所有物料
        List<MaterialStockDO> allMaterialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(signForMaterialStock.getId());

        if(!DictConstants.WMS_MATERIAL_TYPE_TP.equals(signForMaterialStock.getMaterialType())){
            allMaterialStockList.add(signForMaterialStock);
        }

        if(CollectionUtils.isAnyEmpty(allMaterialStockList)){
            throw exception(MATERIAL_STOCK_CANNOT_SIGN_OUT_MATERIAL);
        }
        // 仅托盘上的物料
        Set<String> materialIds = CollectionUtils.convertSet(allMaterialStockList, MaterialStockDO::getId);

        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(signForLocationId);
        if(warehouseArea == null){
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }

        List<MoveWarehouseDetailDO> allMoveWarehouseDetailList = moveWarehouseDetailService.getWaitSignForMoveWarehouseDetailListByMaterialStockIds(materialIds);
        if(!CollectionUtils.isAnyEmpty(allMoveWarehouseDetailList)){
            // 出入库单详情数量与物料数量不一致 则报错
            if (CollectionUtils.isAnyEmpty(allMoveWarehouseDetailList) ||
                    (allMoveWarehouseDetailList.size() < FilterMaterialUtils.filter_Tp_Gz(allMaterialStockList).size())) {
                throw exception(IN_WAREHOUSE_MATERIAL_NOT_IN_IN_WAREHOUSE_ORDER);
            }
            if(!warehouseArea.getWarehouseId().equals(allMoveWarehouseDetailList.get(0).getTargetWarehouseId())){
                throw exception(MATERIAL_STOCK_SIGN_OUT_WAREHOUSE_NOT_MATCH_OUT_WAREHOUSE);
            }
            allMoveWarehouseDetailList.forEach(moveWarehouseDetailDO -> {
               if(StringUtils.isNotBlank(moveWarehouseDetailDO.getSignLocationId()) && !signForLocationId.equals(moveWarehouseDetailDO.getSignLocationId())){
                   throw exception(MATERIAL_STOCK_SIGN_OUT_NOT_MATCH_TARGET_LOCATION);
               }
            });
            if(!moveWarehouseDetailService.updateBatchMoveWarehouseDetailStateByMaterialStockId(signForMaterialStock.getId(), DictConstants.WMS_ORDER_DETAIL_STATUS_4)){
                throw exception(MATERIAL_STOCK_SIGN_OUT_UPDATE_STOCK_IN_OUT_ORDER_ERROR);
            }
        }else {

            List<OutWarehouseDetailDO> allOutWarehouseDetailList = outWarehouseDetailService.getWaitSignForOutWarehouseDetailListByMaterialStockIds(materialIds);
            if(CollectionUtils.isAnyEmpty(allOutWarehouseDetailList)){
                throw exception(OUT_WAREHOUSE_MATERIAL_NOT_OUT_OUT_WAREHOUSE_ORDER);
            }
            // 出入库单详情数量与物料数量不一致 则报错
            if (CollectionUtils.isAnyEmpty(allOutWarehouseDetailList) ||
                    (allOutWarehouseDetailList.size() < FilterMaterialUtils.filter_Tp_Gz(allMaterialStockList).size())) {
                throw exception(IN_WAREHOUSE_MATERIAL_NOT_IN_IN_WAREHOUSE_ORDER);
            }
            if(!warehouseArea.getWarehouseId().equals(allOutWarehouseDetailList.get(0).getTargetWarehouseId())){
                throw exception(MATERIAL_STOCK_SIGN_OUT_WAREHOUSE_NOT_MATCH_OUT_WAREHOUSE);
            }
            allOutWarehouseDetailList.forEach(moveWarehouseDetailDO -> {
                if(StringUtils.isNotBlank(moveWarehouseDetailDO.getSignLocationId()) && !signForLocationId.equals(moveWarehouseDetailDO.getSignLocationId())){
                    throw exception(MATERIAL_STOCK_SIGN_OUT_NOT_MATCH_TARGET_LOCATION);
                }
            });
            if(!outWarehouseDetailService.updateBatchOutWarehouseDetailStateByMaterialStockId(signForMaterialStock.getId(), DictConstants.WMS_ORDER_DETAIL_STATUS_4)){
                throw exception(MATERIAL_STOCK_SIGN_OUT_UPDATE_STOCK_IN_OUT_ORDER_ERROR);
            }
        }

        if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(signForMaterialStock.getMaterialType())){
            List<MaterialStockDO> materialStockListAtTray = materialStockService.getMaterialStockListByContainerId(signForMaterialStock.getId());
            // 更新物料库位
            if(!materialStockService.updateBatchMaterialStock(materialStockListAtTray.stream().map(MaterialStockDO::getId).collect(Collectors.toList()), signForLocationId)){
                // 物料库位更新失败
                throw exception(IN_WAREHOUSE_LOCATION_UPDATE_ERROR);
            }
        }else {
            // 更新物料库位
            if(!materialStockService.updateBatchMaterialStock(Collections.singletonList(signForMaterialStock.getId()), signForLocationId)){
                // 物料库位更新失败
                throw exception(IN_WAREHOUSE_LOCATION_UPDATE_ERROR);
            }
        }

    }

    /**
     * 待出库刀具签收   出库单签收
     * @param cutterStock
     * @param locationCode
     */
    @Override
    @Transactional
    public void signForAllWaitSignForCutter(MaterialStockDO cutterStock, String locationCode) {

        WarehouseLocationDO warehouseLocation = warehouseLocationService.getWarehouseLocationByLocationCode(locationCode);
        if (warehouseLocation == null) {
            throw exception(WAREHOUSE_LOCATION_NOT_EXISTS);
        }

        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(warehouseLocation.getId());
        if(warehouseArea == null){
            throw exception(WAREHOUSE_AREA_NOT_EXISTS);
        }

        // 查询待签收的 移库单 --包括待出库
        List<MoveWarehouseDetailDO> moveWarehouseDetailList = moveWarehouseDetailService.getWaitSignForMoveWarehouseDetailListByMaterialStockIds(Collections.singletonList(cutterStock.getId()));
        if(!moveWarehouseDetailList.isEmpty()) {

            if(!warehouseArea.getWarehouseId().equals(moveWarehouseDetailList.get(0).getTargetWarehouseId())){
                throw exception(MATERIAL_STOCK_SIGN_OUT_WAREHOUSE_NOT_MATCH_OUT_WAREHOUSE);
            }
            if(!moveWarehouseDetailService.updateBatchMoveWarehouseDetailStateByMaterialStockId(cutterStock.getId(), DictConstants.WMS_ORDER_DETAIL_STATUS_4)){
                throw exception(MATERIAL_STOCK_SIGN_OUT_UPDATE_STOCK_IN_OUT_ORDER_ERROR);
            }
        }else {
            // 查询待签收的 出库单 --包括待出库
            List<OutWarehouseDetailDO> outWarehouseDetailList = outWarehouseDetailService.getWaitSignForOutWarehouseDetailListByMaterialStockIds(Collections.singletonList(cutterStock.getId()));

            if(outWarehouseDetailList.isEmpty()){
                throw exception(OUT_WAREHOUSE_MATERIAL_NOT_FOUND_DELIVERY_ORDER);
            }
            if(!warehouseArea.getWarehouseId().equals(outWarehouseDetailList.get(0).getTargetWarehouseId())){
                throw exception(MATERIAL_STOCK_SIGN_OUT_WAREHOUSE_NOT_MATCH_OUT_WAREHOUSE);
            }
            if(!outWarehouseDetailService.updateBatchOutWarehouseDetailStateByMaterialStockId(cutterStock.getId(), DictConstants.WMS_ORDER_DETAIL_STATUS_4)){
                throw exception(MATERIAL_STOCK_SIGN_OUT_UPDATE_STOCK_IN_OUT_ORDER_ERROR);
            }
        }


        // 更新物料库位
        if(!materialStockService.updateBatchMaterialStock(Collections.singletonList(cutterStock.getId()), warehouseLocation.getId())){
            // 物料库位更新失败
            throw exception(IN_WAREHOUSE_LOCATION_UPDATE_ERROR);
        }

    }

    /*@Override
    public void signAheadAutoSignForMaterial(String materialStockId, String signForLocationId) {
        MaterialStockDO signForMaterialStock = materialStockService.getMaterialStockById(materialStockId);
        List<MaterialStockDO> allMaterialStockList = new ArrayList<>();
        if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(signForMaterialStock.getMaterialType())) {
            // 如果是托盘，仅获得其上边的物料库存
            List<MaterialStockDO> materialStockListOnTrays = materialStockService.getMaterialStockListByContainerId(signForMaterialStock.getId());
            allMaterialStockList.addAll(materialStockListOnTrays);
        }else {
            allMaterialStockList.add(signForMaterialStock);
        }

        if(CollectionUtils.isAnyEmpty(allMaterialStockList)){
            throw exception(MATERIAL_STOCK_CANNOT_SIGN_OUT_MATERIAL);
        }
        // 仅托盘上的物料
        Set<String> materialIds = CollectionUtils.convertSet(allMaterialStockList, MaterialStockDO::getId);

        // 获取待送达的移库单
        List<MoveWarehouseDetailDO> allMoveWarehouseDetailList = moveWarehouseDetailService.getWaitArriveMoveWarehouseDetailListByMaterialStockIds(materialIds);
        if(CollectionUtils.isAnyEmpty(allMoveWarehouseDetailList)){
            if(!outWarehouseDetailService.updateBatchOutWarehouseDetailStateByMaterialStockId(signForMaterialStock.getId(), DictConstants.WMS_ORDER_DETAIL_STATUS_4)){
                throw exception(MATERIAL_STOCK_SIGN_OUT_UPDATE_STOCK_IN_OUT_ORDER_ERROR);
            }
        }else {
            if(!moveWarehouseDetailService.updateBatchMoveWarehouseDetailStateByMaterialStockId(signForMaterialStock.getId(), DictConstants.WMS_ORDER_DETAIL_STATUS_4)){
                throw exception(MATERIAL_STOCK_SIGN_OUT_UPDATE_STOCK_IN_OUT_ORDER_ERROR);
            }
        }

        Set<String> materialStockIds = CollectionUtils.convertSet(allMaterialStockList, MaterialStockDO::getId);
        // 更新物料库位
        if(!materialStockService.updateBatchMaterialStock(materialStockIds, signForLocationId)){
            // 物料库位更新失败
            throw exception(IN_WAREHOUSE_LOCATION_UPDATE_ERROR);
        }
    }*/


    /**
     * 入库签入
     * @param checkInMaterialStock
     * @param storageCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkInMaterial(MaterialStockDO checkInMaterialStock, String storageCode) {
        MaterialStorageDO materialStorage = materialStorageService.getMaterialStorageByStorageCode(storageCode);
        if (materialStorage == null) {
            throw exception(MATERIAL_STORAGE_NOT_EXISTS);
        }
        MaterialStockDO containerStock = materialStockService.getMaterialStockById(materialStorage.getMaterialStockId());
        if (containerStock == null) {
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        List<String> appointConfigIds = StringListUtils.stringToArrayList(checkInMaterialStock.getContainerConfigIds());

        // 校验指定的容器类型 是否符合预设的配置
        if (!CollectionUtils.isAnyEmpty(appointConfigIds)) {
            if (appointConfigIds.stream().noneMatch(configId -> containerStock.getMaterialConfigId().contains(configId))) {
                throw exception(STOCK_CHECK_CONTAINER_NOT_MATCH_MATERIAL);
            }
        }

        // 1. 不知道签入的是啥 反正就是看他上边都有啥
        List<MaterialStockDO> allMaterialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(checkInMaterialStock.getId());
        allMaterialStockList.add(checkInMaterialStock);

        // 2. 要签入的物料库存集合
        Map<String, MaterialStockDO> allMaterialStockMap = CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);
        List<MoveWarehouseDetailDO> allMoveWarehouseDetailList = moveWarehouseDetailService.getWaitOutMoveWarehouseDetailListByMaterialStockIds(allMaterialStockMap.keySet());
/*        // 将托盘和工装去掉
        allMaterialStockMap = FilterMaterialUtils.filter_Tp_Gz(allMaterialStockMap);*/
        // 3. 校验任务单
        if (CollectionUtils.isAnyEmpty(allMoveWarehouseDetailList)) {
            // 先判断是否是入库操作
//                try{
            List<InWarehouseDetailDO> inWarehouseDetailDOS = inWarehouseDetailService.checkInWarehouseDetail(allMaterialStockMap);

            inWarehouseDetailDOS.forEach(inWarehouseDetailDO -> {
                if (StringUtils.isNotBlank(inWarehouseDetailDO.getCarryTrayId()) && !inWarehouseDetailDO.getCarryTrayId().equals(containerStock.getId())) {
                    throw exception(CARRYING_TASK_TRAY_NOT_MATCH_MATERIAL);
                }
            });

            // 填入操作人
            inWarehouseDetailService.setOperatorInBatchInWarehouseDetail(CollectionUtils.convertSet(inWarehouseDetailDOS, InWarehouseDetailDO::getId));
//                }catch (Exception ignored){ }
        } else {
//                try{
            // 不是入库操作 则是移库操作
            List<MoveWarehouseDetailDO> moveWarehouseDetailDOS = moveWarehouseDetailService.checkMoveWarehouseDetail(null, allMaterialStockMap);

            moveWarehouseDetailDOS.forEach(moveWarehouseDetailDO -> {
                if (StringUtils.isNotBlank(moveWarehouseDetailDO.getCarryTrayId()) && !moveWarehouseDetailDO.getCarryTrayId().equals(containerStock.getId())) {
                    throw exception(CARRYING_TASK_TRAY_NOT_MATCH_MATERIAL);
                }
            });
            // 填入操作人
            moveWarehouseDetailService.setOperatorInBatchMoveWarehouseDetail(CollectionUtils.convertSet(moveWarehouseDetailDOS, MoveWarehouseDetailDO::getId));
//                }catch (Exception ignored){ }
        }

        // 4. 更新物料位置
        materialStockService.updateMaterialStorage(checkInMaterialStock.getId(), materialStorage.getId());

        String locationId = materialStockService.getLocationIdByMaterialStock(containerStock);
        WarehouseDO warehouse = warehouseService.getWarehouse(warehouseAreaService.getWarehouseAreaByLocationId(locationId).getWarehouseId());
        // 自动化线体库 物料签入 自动解锁库位
        if(DictConstants.WMS_WAREHOUSE_TYPE_5.equals(warehouse.getWarehouseType())){
            warehouseLocationService.unlockLocation(locationId);
        }
    }


    /**
     * 入库签入-刀具
     * @param checkInMaterialStock
     * @param storageCode
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkInCutter(MaterialStockDO checkInMaterialStock, String storageCode, String trayId) {
        MaterialStorageDO materialStorage = materialStorageService.getMaterialStorageByStorageCode(storageCode);
        if(materialStorage == null){
            throw exception(MATERIAL_STORAGE_NOT_EXISTS);
        }
        MaterialStockDO containerStock = materialStockService.getMaterialStock(trayId);
        if(containerStock == null){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }

        this.checkInCutter(checkInMaterialStock, containerStock, trayId, materialStorage);
    }

    /**
     * 入库签入-刀具
     * @param checkInMaterialStock
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkInCutter(MaterialStockDO checkInMaterialStock, String storageId) {
        MaterialStorageDO materialStorage = materialStorageService.getMaterialStorage(storageId);
        if(materialStorage == null){
            throw exception(MATERIAL_STORAGE_NOT_EXISTS);
        }
        String trayId = materialStorage.getMaterialStockId();
        MaterialStockDO containerStock = materialStockService.getMaterialStock(materialStorage.getMaterialStockId());
        if(containerStock == null){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }

        this.checkInCutter(checkInMaterialStock, containerStock, trayId, materialStorage);
    }

    private void checkInCutter(MaterialStockDO checkInMaterialStock, MaterialStockDO containerStock, String trayId, MaterialStorageDO materialStorage){
        // 传入的托盘编码所属托盘必须为指定的托盘
        if(!trayId.equals(materialStorage.getMaterialStockId())){
            throw exception(MATERIAL_STOCK_SCAN_BIN_CODE_ERROR);
        }

        List<String> appointConfigIds = StringListUtils.stringToArrayList(checkInMaterialStock.getContainerConfigIds());

        // 校验指定的容器类型 是否符合预设的配置
        if(!CollectionUtils.isAnyEmpty(appointConfigIds)){
            if(appointConfigIds.stream().noneMatch(configId -> containerStock.getMaterialConfigId().contains(configId))){
                throw exception(STOCK_CHECK_CONTAINER_NOT_MATCH_MATERIAL);
            }
        }

        // 1. 不知道签入的是啥 反正就是看他上边都有啥
        List<MaterialStockDO> allMaterialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(checkInMaterialStock.getId());
        allMaterialStockList.add(checkInMaterialStock);

        // 2. 要签入的物料库存集合
        Map<String, MaterialStockDO> allMaterialStockMap = CollectionUtils.convertMap(allMaterialStockList, MaterialStockDO::getId);
        List<OutWarehouseDetailDO> allOutWarehouseDetailList = outWarehouseDetailService.getWaitOutWarehouseOutWarehouseDetailListByMaterialStockIds(allMaterialStockMap.keySet());
        // 3. 校验任务单
        if(!CollectionUtils.isAnyEmpty(allOutWarehouseDetailList)) {
            // 不是入库操作 则是出库操作
            List<OutWarehouseDetailDO> outWarehouseDetailDOS = outWarehouseDetailService.checkOutWarehouseDetail(null, allMaterialStockMap);

            // 填入操作人
            outWarehouseDetailService.setOperatorInBatchOutWarehouseDetail(CollectionUtils.convertSet(outWarehouseDetailDOS, OutWarehouseDetailDO::getId));
        }else {
            // 先判断是否是入库操作
            List<InWarehouseDetailDO> inWarehouseDetailDOS = inWarehouseDetailService.checkInWarehouseDetail(allMaterialStockMap);

            inWarehouseDetailDOS.forEach(inWarehouseDetailDO -> {
                if(StringUtils.isNotBlank(inWarehouseDetailDO.getCarryTrayId()) && !inWarehouseDetailDO.getCarryTrayId().equals(containerStock.getId())){
                    throw exception(CARRYING_TASK_TRAY_NOT_MATCH_MATERIAL);
                }
            });

            // 填入操作人
            inWarehouseDetailService.setOperatorInBatchInWarehouseDetail(CollectionUtils.convertSet(inWarehouseDetailDOS, InWarehouseDetailDO::getId));

        }

        // 4. 更新物料位置
        materialStockService.updateMaterialStorage(checkInMaterialStock.getId(), materialStorage.getId());
    }
}
