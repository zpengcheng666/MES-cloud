package com.miyu.module.wms.core.carrytask.service.impl;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.movewarehousedetail.MoveWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.movewarehousedetail.MoveWarehouseDetailService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.StringListUtils;
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
@Transactional(rollbackFor = Exception.class)
@Slf4j
public class MoveMaterialServiceImpl extends DispatchCarryTaskLogicService {

    @Resource
    @Lazy
    private CarryTaskService carryTaskService;
    @Resource
    private MoveWarehouseDetailService moveWarehouseDetailService;
    @Resource
    private WarehouseAreaService warehouseAreaService;

    @Override
    public void createCarryTaskLogic(CarryTaskDO carryTask, MaterialStockDO trayStock, String targetWarehouseId) {
        CommonResult<CarryTaskDO> commonResult = generateCarryTaskByTargetWarehouseId(carryTask, trayStock, targetWarehouseId);
        carryTask = commonResult.getData();
        if(carryTask != null && carryTask.getCarrySubTask() != null){// 不能被挂起
            carryTask.setPriority(1);
            carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_ADJUST);
            carryTaskService.saveCarryTask(carryTask);
        }
    }

    // 根据物料ids 校验其任务单
    @Override
    public Set<String> checkOrderTask(String targetWarehouseId, Map<String, MaterialStockDO> allMaterialStockMap) {
        List<MoveWarehouseDetailDO> moveWarehouseDetailDOS = moveWarehouseDetailService.checkMoveWarehouseDetail(targetWarehouseId, allMaterialStockMap);
        return CollectionUtils.convertSet(moveWarehouseDetailDOS, MoveWarehouseDetailDO::getId);
    }

    public List<MoveWarehouseDetailDO> checkOrderTask2(Map<String, MaterialStockDO> allMaterialStockMap) {
        return moveWarehouseDetailService.checkMoveWarehouseDetail(null, allMaterialStockMap);
    }

    // 更新任务单状态
    @Override
    public boolean updateOrderTask(String containerStockId,Integer updateState) {
        return moveWarehouseDetailService.updateBatchMoveWarehouseDetailStateByMaterialStockId(containerStockId, updateState);
    }


    // 订单填入操作人
    @Override
    public int setOperatorInOrderTask(Set<String> orderIds) {
        return moveWarehouseDetailService.setOperatorInBatchMoveWarehouseDetail(orderIds);
    }

    public boolean createCarryTaskLogic(MaterialStockDO materialStock, String targetLocationId,String targetWarehouseId) {
        if(targetLocationId != null){
            WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(targetLocationId);
            if(warehouseArea == null){
                throw exception(WAREHOUSE_AREA_NOT_EXISTS);
            }
            // 看看目标库位是接驳位 还是别的
            if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(warehouseArea.getAreaType())){
                CommonResult<CarryTaskDO> commonResult = generateCarryTaskByTargetLocationId(null, materialStock, targetLocationId);
                CarryTaskDO carryTask = commonResult.getData();
                if(carryTask != null && carryTask.getCarrySubTask() != null){
                    carryTask.setPriority(1);
                    carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_ADJUST);
                    carryTaskService.saveCarryTask(carryTask);
                    return true;
                }
                return false;
            }
        }

        // 根据目标仓库生成 搬运任务
        CommonResult<CarryTaskDO> commonResult = generateCarryTaskByTargetWarehouseId(null, materialStock, targetWarehouseId);
        CarryTaskDO carryTask = commonResult.getData();
        if(carryTask != null && carryTask.getCarrySubTask() != null){
            carryTask.setPriority(1);
            carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_ADJUST);
            carryTaskService.saveCarryTask(carryTask);
            return true;
        }
        return false;
    }

    public boolean createCarryTaskLogic2(MaterialStockDO toolingStock, String startLocationId, String targetLocationId) {
        CommonResult<CarryTaskDO> commonResult = generateCarryTaskByStartLocationIdAndTargetLocationId(null, toolingStock, startLocationId, targetLocationId);
        CarryTaskDO carryTask = commonResult.getData();
        if(carryTask != null && carryTask.getCarrySubTask() != null){
            carryTask.setPriority(1);
            carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_ADJUST);
            carryTaskService.saveCarryTask(carryTask);
            return true;
        }
        return false;
    }

}
