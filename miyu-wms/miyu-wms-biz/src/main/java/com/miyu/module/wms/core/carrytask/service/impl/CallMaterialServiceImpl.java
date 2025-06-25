package com.miyu.module.wms.core.carrytask.service.impl;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.outwarehousedetail.OutWarehouseDetailDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.outwarehousedetail.OutWarehouseDetailService;
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
import static com.miyu.module.wms.enums.ErrorCodeConstants.CARRYING_TASK_TRAY_NOT_FOUND;

@Service
@Slf4j
@Transactional(rollbackFor = Exception.class)
public class CallMaterialServiceImpl extends DispatchCarryTaskLogicService {

    @Resource
    @Lazy // 循环依赖，需要懒加载
    private CarryTaskService carryTaskService;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private OutWarehouseDetailService outWarehouseDetailService;

    @Override
    public void createCarryTaskLogic(CarryTaskDO carryTask, MaterialStockDO materialStock, String targetLocationId) {
        MaterialStockDO carryContainerStock = materialStockService.getMaterialAtLocationByMaterialStock(materialStock);
        CommonResult<CarryTaskDO> commonResult = generateCarryTaskByTargetLocationId(carryTask, carryContainerStock, targetLocationId);
        if(commonResult.isSuccess()){
            carryTask = commonResult.getData();
            carryTask.setPriority(1);
            carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_OUT);
            carryTaskService.saveCarryTask(carryTask);
        }else {
            throw exception(commonResult.getCode(), commonResult.getMsg());
        }

    }

    // 根据物料ids 校验其任务单
    @Override
    public Set<String> checkOrderTask(String targetWarehouseId, Map<String, MaterialStockDO> allMaterialStockMap) {
        List<OutWarehouseDetailDO> outWarehouseDetailDOS = outWarehouseDetailService.checkOutWarehouseDetail(targetWarehouseId, allMaterialStockMap);
        return CollectionUtils.convertSet(outWarehouseDetailDOS, OutWarehouseDetailDO::getId);
    }

    // 更新任务单状态
    @Override
    public boolean updateOrderTask(String containerStockId,Integer updateState) {
        return outWarehouseDetailService.updateBatchOutWarehouseDetailStateByMaterialStockId(containerStockId, updateState);
    }

    // 订单填入操作人
    @Override
    public int setOperatorInOrderTask(Set<String> orderIds) {
        return outWarehouseDetailService.setOperatorInBatchOutWarehouseDetail(orderIds);
    }


    /// 重写方法↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓///
    // 如果目标位置 是接驳位  将根据目标位置生成出库搬运任务
    // 如果目标位置 是其他库位，将根据目标仓库生成出库搬运任务
    // 如果目标位置 是null ，将根据目标仓库生成出库搬运任务  如果目标仓库为null 将根据物料的默认存放仓库生成入库搬运任务
    public boolean createCarryTaskLogic(MaterialStockDO materialStock, String targetLocationId, String targetWarehouseId) {
        MaterialStockDO carryContainerStock = materialStockService.getMaterialAtLocationByMaterialStock(materialStock);

        if(targetLocationId != null){
            WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(targetLocationId);
            if(warehouseArea == null){
                throw exception(WAREHOUSE_AREA_NOT_EXISTS);
            }
            // 看看目标库位是接驳位 还是别的
            if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(warehouseArea.getAreaType())){
                CommonResult<CarryTaskDO> commonResult = generateCarryTaskByTargetLocationId(null, carryContainerStock, targetLocationId);
                CarryTaskDO carryTask = commonResult.getData();
                if(carryTask != null && carryTask.getCarrySubTask() != null){
                    carryTask.setPriority(1);
                    carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_OUT);
                    carryTaskService.saveCarryTask(carryTask);
                    return true;
                }
                return false;
            }
        }

        CommonResult<CarryTaskDO> commonResult = generateCarryTaskByTargetWarehouseId(null, carryContainerStock, targetWarehouseId);
        CarryTaskDO carryTask = commonResult.getData();
        if(carryTask != null && carryTask.getCarrySubTask() != null){
            carryTask.setPriority(1);
            carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_OUT);
            carryTaskService.saveCarryTask(carryTask);
            return true;
        }
        return false;
    }


}


