package com.miyu.module.wms.core.carrytask.service.impl;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.inwarehousedetail.InWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.inwarehousedetail.InWarehouseDetailService;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class MaterialInServiceImpl extends DispatchCarryTaskLogicService {

    @Resource
    @Lazy
    private CarryTaskService carryTaskService;
    @Resource
    private InWarehouseDetailService inWarehouseDetailService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private WarehouseLocationService warehouseLocationService;

    // 入库搬运
    @Override
    public void createCarryTaskLogic(CarryTaskDO carryTask, MaterialStockDO trayStock, String targetWarehouseId){
        CommonResult<CarryTaskDO> commonResult = generateCarryTaskByTargetWarehouseId(carryTask, trayStock, targetWarehouseId);
        carryTask = commonResult.getData();
        if(carryTask != null && carryTask.getCarrySubTask() != null){// 不能被挂起
            carryTask.setPriority(1);
            carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_IN);
            carryTaskService.saveCarryTask(carryTask);
        }

    }

    // 根据物料ids 校验其任务单
    @Override
    public Set<String> checkOrderTask(String targetWarehouseId, Map<String, MaterialStockDO> allMaterialStockMap) {
        List<InWarehouseDetailDO> inWarehouseDetailDOS = inWarehouseDetailService.checkInWarehouseDetail(allMaterialStockMap);
        return CollectionUtils.convertSet(inWarehouseDetailDOS, InWarehouseDetailDO::getId);
    }

    // 根据物料ids 校验其任务单
    public List<InWarehouseDetailDO> checkOrderTask2(Map<String, MaterialStockDO> allMaterialStockMap) {
        return inWarehouseDetailService.checkInWarehouseDetail(allMaterialStockMap);
    }

    // 更新任务单状态
    @Override
    public boolean updateOrderTask(String containerStockId,Integer updateState) {
        return inWarehouseDetailService.updateBatchInWarehouseDetailStateByMaterialStockId(containerStockId, updateState);
    }

    @Override
    public int setOperatorInOrderTask(Set<String> orderIds) {
        return inWarehouseDetailService.setOperatorInBatchInWarehouseDetail(orderIds);
    }

    public void handleIdleTray() {
        // 查询所有自动或半自动接驳的接驳库位
        List<WarehouseLocationDO> availableTransitLocation = warehouseLocationService.getAvailableTransitLocation();
        Map<String, WarehouseLocationDO> locationMap = CollectionUtils.convertMap(availableTransitLocation, WarehouseLocationDO::getId);
        List<MaterialStockDO> trayStockList = materialStockService.getMaterialStockListByLocationIds(locationMap.keySet());
        for (MaterialStockDO trayStock : trayStockList) {
            WarehouseLocationDO warehouseLocationDO = locationMap.get(trayStock.getLocationId());
            try{
                boolean success = this.handleIdleTrayInWarehouse(trayStock, warehouseLocationDO.getWarehouseId());
                if(success){
                    break;
                }
            }catch (Exception ignored){}

        }
    }

    // 空闲托盘入库
    public boolean handleIdleTrayInWarehouse(MaterialStockDO trayStock, String atWarehouseId) {
        if(atWarehouseId == null){
            throw exception(PARAM_NOT_NULL);
        }
        // 先看是不是已经在默认存放仓库了
        if(atWarehouseId.equals(trayStock.getDefaultWarehouseId())){
            return false;
        }
        // 先看托盘上有没有物料
        List<MaterialStockDO> materialStockOnTray = materialStockService.getMaterialStockListByContainerId(trayStock.getId());
        if(CollectionUtils.isAnyEmpty(materialStockOnTray)){
            // 再看是否已存在任务
            carryTaskService.checkCarryTask(trayStock.getId());
            createCarryTaskLogic(null, trayStock, trayStock.getDefaultWarehouseId());
            return true;
        }
        return false;
    }



    public boolean createCarryTaskLogic(MaterialStockDO materialStock, String targetWarehouseId){
        CommonResult<CarryTaskDO> commonResult = generateCarryTaskByTargetWarehouseId(null, materialStock, targetWarehouseId);
        CarryTaskDO carryTask = commonResult.getData();
        if(carryTask != null && carryTask.getCarrySubTask() != null){
            carryTask.setPriority(1);
            carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_IN);
            carryTaskService.saveCarryTask(carryTask);
            return true;
        }
        return false;

    }


}