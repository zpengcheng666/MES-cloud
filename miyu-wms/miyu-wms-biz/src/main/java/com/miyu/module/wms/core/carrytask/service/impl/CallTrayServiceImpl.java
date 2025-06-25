package com.miyu.module.wms.core.carrytask.service.impl;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;


@Service
@Transactional(rollbackFor = Exception.class)
public class CallTrayServiceImpl extends DispatchCarryTaskLogicService {

    @Resource
    @Lazy // 循环依赖，需要懒加载
    private CarryTaskService carryTaskService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private WarehouseAreaService warehouseAreaService;


    // 呼叫托盘   移动（下架库位） 下架（下架库位）  取（下架库位） 移动（目标库位）放（目标库位）
    @Override
    public void createCarryTaskLogic(CarryTaskDO carryTask, MaterialStockDO trayStock, String targetLocationId) {
        CommonResult<CarryTaskDO> commonResult = generateCarryTaskByTargetLocationId(carryTask, trayStock, targetLocationId);
        if(commonResult.isSuccess()){
            carryTask = commonResult.getData();
            carryTask.setPriority(1);
            carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_TRAY);
            carryTaskService.saveCarryTask(carryTask);
            // 呼叫托盘任务生成成功得把库位锁上
            List<CarrySubTaskDO> carrySubTask = commonResult.getData().getCarrySubTask();
            if(!CollectionUtils.isAnyEmpty(carrySubTask))warehouseLocationService.lockLocation(carrySubTask.get(carrySubTask.size() - 1).getLocationId());
        }else {
            throw exception(commonResult.getCode(), commonResult.getMsg());
        }

    }

    // 收货库位 -> 自动呼叫托盘
    public CommonResult<String> autoCallTrayLogic(MaterialStockDO toolingStock, String targetWarehouseId, String targetLocationId) {
        CarryTaskDO carryTask = new CarryTaskDO();
        toolingStock = materialStockService.getMaterialAtLocationByMaterialStock(toolingStock);
        CommonResult<CarryTaskDO> commonResult = null;
        if(targetLocationId != null){
            WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseAreaByLocationId(targetLocationId);
            if(warehouseArea == null){
                throw exception(WAREHOUSE_AREA_NOT_EXISTS);
            }
            // 看看目标库位是接驳位 还是别的
            if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(warehouseArea.getAreaType())){
                commonResult = generateCarryTaskByTargetLocationId(carryTask, toolingStock, targetLocationId);
            }
        }
        if(commonResult ==null)commonResult = generateCarryTaskByTargetWarehouseId(carryTask, toolingStock, targetWarehouseId);

        if(commonResult.getData() == null || commonResult.getData().getCarrySubTask() == null){
            return CommonResult.error(CARRYING_TASK_GENERATE_ERROR);
        }

        // 呼叫托盘任务生成成功得把库位锁上
        List<CarrySubTaskDO> carrySubTask = commonResult.getData().getCarrySubTask();
        // 说明托盘就在目标库位上
        if(carrySubTask.size() == 1){
            // 再查一下 是否此托盘已被占用
            List<CarryTaskDO> carryTaskList =  carryTaskService.getUnfinishedCarryTaskByReflectStockId(carryTask.getReflectStockId());
            if(!CollectionUtils.isAnyEmpty(carryTaskList)){
                return CommonResult.error(CARRYING_TASK_GENERATE_ERROR);
            }
            // 把目标库位锁上
            warehouseLocationService.lockLocation(carrySubTask.get(0).getLocationId());
            return CommonResult.success(carrySubTask.get(0).getMaterialStockId());
        }


        carryTask = commonResult.getData();
        carryTask.setPriority(1);
        carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_TRAY);
        carryTaskService.saveCarryTask(carryTask);

        String trayStockId = null;
        for (CarrySubTaskDO carrySubTaskDO : carryTask.getCarrySubTask()) {
            if(carrySubTaskDO.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_TAKE){
                trayStockId = carrySubTaskDO.getMaterialStockId();
            }
        }
        if(!CollectionUtils.isAnyEmpty(carrySubTask))warehouseLocationService.lockLocation(carrySubTask.get(carrySubTask.size() - 1).getLocationId());
        return CommonResult.success(trayStockId);
    }
    // 重写父类方法
    @Override
    public List<MaterialStockDO> getAllMaterialStock(MaterialStockDO containerStock) {
        // 1. 校验此库位上的物料容器 是否已存在任务
        carryTaskService.checkCarryTask(containerStock.getId());
        // 2. 查询托盘上的 物料
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByContainerId(containerStock.getId());
        if(!CollectionUtils.isAnyEmpty(materialStockList)){
            // 只能呼叫空托盘
            throw exception(CARRYING_TASK_MATERIAL_NOT_TRAY_CALL);
        }
        return materialStockList;
    }

    public boolean updateOrderTask(String containerStockId,Integer updateState) {
        return true;
    }



}
