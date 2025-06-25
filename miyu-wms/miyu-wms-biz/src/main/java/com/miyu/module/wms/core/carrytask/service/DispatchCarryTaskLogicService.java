package com.miyu.module.wms.core.carrytask.service;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import com.miyu.module.wms.util.StringListUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;

import javax.annotation.Resource;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;
import static com.miyu.module.wms.enums.ErrorCodeConstants.CARRYING_TASK_TRAY_NOT_CONFIG_CARRYING_AREA;


@Slf4j
public abstract class DispatchCarryTaskLogicService {

    @Resource
    @Lazy // 循环依赖，需要懒加载
    private CarryTaskService carryTaskService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private MaterialConfigAreaService materialConfigAreaService;
    @Resource
    private WarehouseAreaService warehouseAreaService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private WarehouseService warehouseService;

    // ==================== 抽象方法 ====================

    // 创建搬运任务
    public abstract void createCarryTaskLogic(CarryTaskDO carryTask, MaterialStockDO toolingStock, String targetLocationId);

    // 更新任务单状态
    public abstract boolean updateOrderTask(@NotEmpty String containerStockId, @NotNull Integer updateState);

    // ==================== 通用方法 ==========

    // 挂起的任务尝试恢复
    public void handleHoldCarryTaskLogic(CarryTaskDO carryTask) {
        try {
            Class<? extends DispatchCarryTaskLogicService> clazz = this.getClass();
            Method createCarryTaskLogic = clazz.getDeclaredMethod("createCarryTaskLogic", CarryTaskDO.class, MaterialStockDO.class, String.class);
            String reflectStockId = carryTask.getReflectStockId();
            String reflectLocationId = carryTask.getReflectLocationId();
            String reflectWarehouseId = carryTask.getReflectWarehouseId();
            MaterialStockDO materialStock = materialStockService.getMaterialStockById(reflectStockId);
            createCarryTaskLogic.invoke(this, carryTask, materialStock, reflectLocationId==null?reflectWarehouseId:reflectLocationId);
        }catch (Exception e){
            carryTask.setTaskDescription(e.getMessage() + " 处理失败");
            carryTaskService.updateCarryTask(carryTask);
        }
    }


    // 下发搬运任务  有异常由调用者 捕获处理 下发等待指令
    /*@Transactional(rollbackFor = Exception.class)
    public String dispatchCarryTaskLogic(CarryTaskDO carryTask) {
//        CarryTaskDO carryTask = carryTaskService.getCarryTaskWithSubTask(parentId);
        if(carryTask != null &&
            (
                carryTask.getTaskStatus() == DictConstants.WMS_CARRY_TASK_STATUS_NOT_START
                || carryTask.getTaskStatus() == DictConstants.WMS_CARRY_TASK_STATUS_RUNNING
            )
        ){
            List<CarrySubTaskDO> carrySubTaskList = carryTask.getCarrySubTask();
            String containerStockId = null;
            // 排序过的子任务
            if(!CollectionUtils.isAnyEmpty(carrySubTaskList)){
                containerStockId = carrySubTaskList.get(carrySubTaskList.size() - 1).getMaterialStockId();

                for (int i = 0; i < carrySubTaskList.size(); i++) {
                    CarrySubTaskDO carrySubTask = carrySubTaskList.get(i);
                    if(carrySubTask.getTaskStatus() == DictConstants.WMS_CARRY_SUB_TASK_STATUS_HOLD
                            ||carrySubTask.getTaskStatus() == DictConstants.WMS_CARRY_SUB_TASK_STATUS_NOT_START){
                        // 如果是挂起 或者 未开始 那就下发
                        // 获取搬运任务 并更新 子任务状态
                       *//* AtomicReference<Boolean> success = new AtomicReference<>(true);
                        String ex = transactionTemplate.execute(status -> {
                            try {*//*
                                return preDispatchCarrTaskLogic(carryTask, carrySubTask);
                          *//*  }catch (Exception e){
                                success.set(false);
                                status.setRollbackOnly();
                                return e.getMessage();
                            }
                        });

                        if(!success.get()){
                            carrySubTask.setTaskDescription(ex + " ; 子任务下发失败");
                            carrySubTask.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_HOLD);
                            carryTaskService.updateCarrySubTask(carrySubTask);
                            return "挂起等待";
                        }*//*
                    }else if(carrySubTask.getTaskStatus() == DictConstants.WMS_CARRY_SUB_TASK_STATUS_ISSUED) {
                        // 如果是已下发那就抛异常
                        throw exception(CARRYING_TASK_HAS_DISPATCH);
                    }else if(carrySubTask.getTaskStatus() == DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING) {
                        // 如果是运行 那就更新为 已完成
                        updateCarryTaskLogic(carryTask, carrySubTask.getId());
                        // 更新物料库位
                        if(carrySubTask.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_TAKE){
                            String agvLocationId = "1796371233231605761";
                            if (!materialStockService.updateMaterialStock(carrySubTask.getMaterialStockId(), agvLocationId)) {
                                // 物料库位更新失败
                                throw exception(MATERIAL_STOCK_UPDATE_LOCATION_ERROR);
                            }
                        }else if(carrySubTask.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_PUT){
                            if (!materialStockService.updateMaterialStock(carrySubTask.getMaterialStockId(), carrySubTask.getLocationId())) {
                                // 物料库位更新失败
                                throw exception(MATERIAL_STOCK_UPDATE_LOCATION_ERROR);
                            }
                        }
                    }else if(carrySubTask.getTaskStatus() == DictConstants.WMS_CARRY_TASK_CALL_BACK_STATUS_FINISHED) {
                        // 如果是已完成 那就跳过
                        continue;
                    }
                }
            }
            // 没任务了 就更新任务单状态：已完成
            if(carryTask.getTaskType() == DictConstants.WMS_CARRY_TASK_TYPE_IN){
                // 如果是入库搬运 更新任务单状态：已完成
                updateOrderTask(containerStockId, DictConstants.WMS_ORDER_DETAIL_STATUS_4);
            }else {
                // 更新任务单状态为 待签收
                updateOrderTask(containerStockId, DictConstants.WMS_ORDER_DETAIL_STATUS_3);
            }
            // 没任务了 更新搬运任务状态： 任务完成
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_FINISHED);
            carryTaskService.updateCarryTask(carryTask);
            return "没了";
        }
       return "错误的任务ID";
    }

    // 更新搬运任务状态
    @Transactional(rollbackFor = Exception.class)
    public void updateCarryTaskLogic(CarryTaskDO carryTask, String subId){
        List<CarrySubTaskDO> carrySubTaskList = carryTask.getCarrySubTask();
        if(CollectionUtils.isAnyEmpty(carrySubTaskList)){
            throw exception(CARRYING_TASK_NOT_EXISTS);
        }
        for (int i = 0; i < carrySubTaskList.size(); i++) {
            CarrySubTaskDO carrySubTask = carrySubTaskList.get(i);
            if (carrySubTask.getId().equals(subId)){
                // 更新 子任务状态
                Integer taskStatus = carrySubTask.getTaskStatus();
                if (taskStatus == DictConstants.WMS_CARRY_SUB_TASK_STATUS_HOLD
                        || taskStatus == DictConstants.WMS_CARRY_SUB_TASK_STATUS_NOT_START) {
                    // 挂起或者未开始 就更新 子任务状态 已下发
                    carrySubTask.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_ISSUED);
                    carryTaskService.updateCarrySubTask(carrySubTask);
                }else if (taskStatus == DictConstants.WMS_CARRY_SUB_TASK_STATUS_ISSUED) {
                    // 已下发 就更新 子任务状态 为运行中
                    carrySubTask.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING);
                    carryTaskService.updateCarrySubTask(carrySubTask);
                } else if (taskStatus == DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING) {
                    // 运行中 就更新 子任务状态 为已完成
                    carrySubTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_CALL_BACK_STATUS_FINISHED);
                    carryTaskService.updateCarrySubTask(carrySubTask);

                    // 更新 任务单 状态

                    // 如果下个子任务是最后一个任务 并且为入库搬运任务  而且还是上架  就更新任务单状态 为待上架
                    if(i+1 < carrySubTaskList.size()
                            && carrySubTaskList.get(i + 1).getExecuteOrder() == carrySubTaskList.size()
                            && carryTask.getTaskType() == DictConstants.WMS_CARRY_TASK_TYPE_IN
                            && carrySubTaskList.get(i+1).getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_ON) {
                        String containerStockId = carrySubTask.getMaterialStockId();
                        updateOrderTask(containerStockId, DictConstants.WMS_ORDER_DETAIL_STATUS_3);
                    }
                }
            }
        }
    }*/


    //==================== 子类重写方法 ====================

    // 根据物料ids 校验其任务单  返回任务单id
    public Set<String> checkOrderTask(String targetWarehouseId, Map<String, MaterialStockDO> allMaterialStockMap) {
        // 默认啥也不干，子类去重写
        return new HashSet<>();
    }

    // 根据容器查询其上所有物料（工装的话 包括工装本身） 注意：要传入库位上的物料
    public List<MaterialStockDO> getAllMaterialStock(MaterialStockDO containerStock){
        // 1. 校验此库位上的物料容器 是否已存在任务
        carryTaskService.checkCarryTask(containerStock.getId());
        // 2. 默认查询其上所有物料  容器上无物料则会抛出异常
        return getMaterialStockListByContainer(containerStock);
    }


    // 订单填入操作人---------签收人直接在更新任务状态为已完成时设置
    public int setOperatorInOrderTask(Set<String> orderIds) {
        return 0;
    }

    // ==================== 辅助方法 ====================

    // 下发搬运任务前的逻辑  有异常由调用者 捕获处理 下发等待指令
    /*private String preDispatchCarrTaskLogic(CarryTaskDO carryTask,CarrySubTaskDO carrySubTask){
        // 下发前更新主任务状态
        if(carrySubTask.getExecuteOrder() == 1){
            // 如果要下发的是 第一个子任务 就更新主搬运任务状态： 进行中
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_RUNNING);
            carryTaskService.updateCarryTask(carryTask);
            // 更新任务单状态 待送达
            updateOrderTask(carrySubTask.getMaterialStockId(), DictConstants.WMS_ORDER_DETAIL_STATUS_2);
        }

        // 再更新 子任务状态  挂起或者未开始状态 更新为： 已下发
        updateCarryTaskLogic(carryTask, carrySubTask.getId());

        // 如果是上下架 就生成上下架指令
        if(carrySubTask.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_ON){
            {
                String containerStockId = carrySubTask.getMaterialStockId();
                // 起始库位 是上一个任务的 结束库位
                String startLocationId = carryTask.getCarrySubTask().get(carrySubTask.getExecuteOrder() - 2).getLocationId();
                String targetLocationId = carrySubTask.getLocationId();
                instructionService.onShelfInstruction(carryTask.getId(),containerStockId, startLocationId, targetLocationId);
            }
            if(carrySubTask.getExecuteOrder() == carryTask.getCarrySubTask().size())return "没了";
            return "上架等待";
        }else if(carrySubTask.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_OFF){
            {
                String containerStockId = carrySubTask.getMaterialStockId();
                MaterialStockDO containerStock = materialStockService.getMaterialStock(containerStockId);
                String startLocationId = containerStock.getLocationId();
                String targetLocationId = carrySubTask.getLocationId();
                instructionService.offShelfInstruction(carryTask.getId(),containerStockId, startLocationId, targetLocationId);
            }
            if(carrySubTask.getExecuteOrder() == carryTask.getCarrySubTask().size())return "没了";
            return "下架等待";
        }
        return "下发搬运任务";
    }*/


    // 根据托盘 获取其下架接驳库区
    public List<String> getTrayConfigTransitAreaIds(String trayStockId,String trayConfigId){
        // 获取托盘库区
        WarehouseAreaDO containerAtWarehouseArea = warehouseAreaService.getWarehouseAreaByMaterialStockId(trayStockId);
        // 1.校验托盘所在库区  库区属性：自动库区 库区类型： 存储位 的容器物料
        if (DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO != containerAtWarehouseArea.getAreaProperty()
                || DictConstants.WMS_WAREHOUSE_AREA_TYPE_1 != containerAtWarehouseArea.getAreaType()) {
            throw exception(MATERIAL_STOCK_NOT_SUPPORT_CARRYING);
        }

        // 2. 校验托盘是否存在 下架库区
        List<MaterialConfigAreaDO> materialConfigTransitAreaList =materialConfigAreaService.getMaterialConfigTransitAreaByMaterialConfigIdAndWarehouseId(trayConfigId, containerAtWarehouseArea.getWarehouseId());
        if(CollectionUtils.isAnyEmpty(materialConfigTransitAreaList)){
            throw exception(CARRYING_TASK_TRAY_NOT_CONFIG_CARRYING_AREA);
        }
        return CollectionUtils.convertList(materialConfigTransitAreaList, MaterialConfigAreaDO::getWarehouseAreaId);
    }


    // 根据接驳库区 获取空闲的接驳库位  没有返回null
    public WarehouseLocationDO getFreeEmptyDownLocationByAreaIds(List<String> areaIds){
        {
            // 在下架库区里 找一个空的接驳库位
            List<WarehouseLocationDO> emptyLocation = warehouseLocationService.getEmptyLocationByAreaIds(areaIds);
            // 获取所有未完成的搬运任务
            List<CarrySubTaskDO> unfinishedCarrySubTask = carryTaskService.getUnfinishedCarrySubTask();
            Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarrySubTask, CarrySubTaskDO::getLocationId);
            emptyLocation = emptyLocation.stream().filter(l -> !haveTaskLocationIdSet.contains(l.getId())).collect(Collectors.toList());
            if(CollectionUtils.isAnyEmpty(emptyLocation)){
                return null;
            }else {
                // 创建一个Random对象
                Random random = new Random();
                // 从列表中随机取出一个值
               return emptyLocation.get(random.nextInt(emptyLocation.size()));
            }

        }
    }


    // 根据接驳库区 获取空闲的接驳库位  没有返回null  此方法不可抛出异常
    public WarehouseLocationDO getFreeEmptyUpLocationByAreaIds(List<String> areaIds){
        try {
            // 在下架库区里 找一个空的接驳库位
            List<WarehouseLocationDO> emptyLocation = warehouseLocationService.getEmptyLocationByAreaIds(areaIds);
            if(CollectionUtils.isAnyEmpty(emptyLocation)){
                return null;
            }
            // 获取所有未完成的搬运任务
            List<CarrySubTaskDO> unfinishedCarrySubTask = carryTaskService.getUnfinishedCarrySubTask();
            Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarrySubTask, CarrySubTaskDO::getLocationId);

            if(CollectionUtils.isAnyEmpty(haveTaskLocationIdSet)){
                return emptyLocation.stream().findFirst().orElse(null);
            }

            // 过滤掉有任务的上架库位
            return emptyLocation.stream().filter(l -> !haveTaskLocationIdSet.contains(l.getId())).findFirst().orElse(null);
        }catch (Exception e){
            log.error("获取空闲上架库位失败: {}",e.getMessage());
        }
        return null;
    }

    public Collection<WarehouseLocationDO> getFreeEmptyUpLocationListByAreaIds(Collection<String> areaIds){
        try {
            // 在下架库区里 找一个空的接驳库位
            List<WarehouseLocationDO> emptyLocation = warehouseLocationService.getEmptyLocationByAreaIds(areaIds);
            if(CollectionUtils.isAnyEmpty(emptyLocation)){
                return null;
            }
            // 获取所有未完成的搬运任务
            List<CarrySubTaskDO> unfinishedCarrySubTask = carryTaskService.getUnfinishedCarrySubTask();
            Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarrySubTask, CarrySubTaskDO::getLocationId);

            if(CollectionUtils.isAnyEmpty(haveTaskLocationIdSet)){
                Map<String, WarehouseLocationDO> stringWarehouseLocationDOMap = CollectionUtils.convertMap(emptyLocation, WarehouseLocationDO::getWarehouseAreaId);
                // 一个库区类型返回一个库位就行了
                return stringWarehouseLocationDOMap.values();
            }

            // 过滤掉有任务的上架库位
            Map<String, WarehouseLocationDO> filterWarehouseLocationDOMap = emptyLocation.stream().filter(l -> !haveTaskLocationIdSet.contains(l.getId())).collect(Collectors.toMap(WarehouseLocationDO::getWarehouseAreaId, Function.identity(), (a, b) -> a));
            return !filterWarehouseLocationDOMap.values().isEmpty() ? filterWarehouseLocationDOMap.values() : null;
        }catch (Exception e){
            log.error("获取空闲上架库位失败: {}",e.getMessage());
        }
        return null;
    }

    //根据物料id 获取其绑定在库位上的容器
    public MaterialStockDO getContainerStockByMaterialStockId(String materialStockId) {
        MaterialStockDO materialStock = materialStockService.getMaterialStock(materialStockId);
        Map<String, MaterialStockDO> warehouseLocationListByMaterialStockList = materialStockService.getWarehouseLocationListByMaterialStockList(Arrays.asList(materialStock));
        MaterialStockDO containerStock = warehouseLocationListByMaterialStockList.get(materialStockId);
        if(containerStock == null){
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        return containerStock;
    }

    // 根据容器 查询其上所有物料（工装的话 包括工装本身）  容器上无物料则会抛出异常
    public List<MaterialStockDO> getMaterialStockListByContainer(MaterialStockDO containerStock) {
        List<MaterialStockDO> resultAllMaterialStockList = new ArrayList<>();
        // 根据容器类物料查询其上所有物料
        List<MaterialStockDO> materialStockList = materialStockService.getAllMaterialStockListByMaterialStockId(containerStock.getId());
        resultAllMaterialStockList.addAll(materialStockList);

        //如果不是托盘
        if(!DictConstants.WMS_MATERIAL_TYPE_TP.equals(containerStock.getMaterialType())){
            // 则工装也加入结果集中
            resultAllMaterialStockList.add(containerStock);
        }

        // 如果容器上无物料，则抛出异常
        if (CollectionUtils.isAnyEmpty(resultAllMaterialStockList)) {
            throw exception(MATERIAL_STOCK_NOT_EXISTS);
        }
        return resultAllMaterialStockList;
    }


    /**
     * 校验库位是否可用 搬运
     */
    public boolean checkStartLocationAvailable(String startLocationId){
        if (StringUtils.isBlank(startLocationId)) return false;
        WarehouseLocationDO warehouseLocation = warehouseLocationService.getWarehouseLocation(startLocationId);
        // 判断库位是否 锁定 有效
        if(/*warehouseLocation.getLocked() == DictConstants.INFRA_BOOLEAN_TINYINT_YES
                || */warehouseLocation.getValid() == DictConstants.INFRA_BOOLEAN_TINYINT_NO){
            return false;
        }
        // 判断库位是否有物料
        /*List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByLocationId(locationId);
        if(!CollectionUtils.isAnyEmpty(materialStockList)){
            return false;
        }*/
        // 获取所有未完成的搬运任务
        List<CarrySubTaskDO> unfinishedCarrySubTask = carryTaskService.getUnfinishedCarrySubTask();
        Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarrySubTask, CarrySubTaskDO::getLocationId);
        // 判断库位是否有任务
        return !haveTaskLocationIdSet.contains(startLocationId);
    }

    /**
     * 校验库位是否可用 搬运
     */
    public boolean checkTargetLocationAvailable(String targetLocationId){
        if (StringUtils.isBlank(targetLocationId)) return false;
        WarehouseLocationDO warehouseLocation = warehouseLocationService.getWarehouseLocation(targetLocationId);
        // 判断库位是否 锁定 有效
        if(/*warehouseLocation.getLocked() == DictConstants.INFRA_BOOLEAN_TINYINT_YES
                || */warehouseLocation.getValid() == DictConstants.INFRA_BOOLEAN_TINYINT_NO){
            return false;
        }
        // 判断库位是否有物料
        List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByLocationId(targetLocationId);
        if(!CollectionUtils.isAnyEmpty(materialStockList)){
            return false;
        }
        // 获取所有未完成的搬运任务
        List<CarrySubTaskDO> unfinishedCarrySubTask = carryTaskService.getUnfinishedCarrySubTask();
        Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarrySubTask, CarrySubTaskDO::getLocationId);
        // 判断库位是否有任务
        return !haveTaskLocationIdSet.contains(targetLocationId);
    }


    // 校验库位是否被锁定
    public void checkLocationLocked(String locationId){
        WarehouseLocationDO warehouseLocation = warehouseLocationService.getWarehouseLocation(locationId);
        if(warehouseLocation.getLocked() == DictConstants.INFRA_BOOLEAN_TINYINT_YES
                || warehouseLocation.getValid() == DictConstants.INFRA_BOOLEAN_TINYINT_NO){
            throw exception(WAREHOUSE_LOCATION_LOCKED);
        }
    }


    /**
     * 根据目标库位生成搬运任务
     * @param carryTask  搬运任务 反射专用 正常调用时传null
     * @param carryMaterial 要搬运的物料所在库位上的 物料
     * @param targetLocationId 目标库位
     * @return
     */
    public CommonResult<CarryTaskDO> generateCarryTaskByTargetLocationId(CarryTaskDO carryTask, MaterialStockDO carryMaterial, String targetLocationId){
        carryMaterial = materialStockService.getMaterialAtLocationByMaterialStock(carryMaterial);
        // 获取物料所在库位上的 容器物料  托盘或工装
        if(carryMaterial == null
                || StringUtils.isBlank(carryMaterial.getLocationId())){
            return CommonResult.error(CARRYING_TASK_MATERIAL_NOT_CONTAINER);
        }
        if (carryTask== null){
            // 校验是否已经存在任务
            try {
                carryTaskService.checkCarryTask(carryMaterial.getId());
            }catch (Exception e){return CommonResult.error(CARRYING_TASK_MATERIAL_HAS_GENERATE_TASK);}
            carryTask = new CarryTaskDO();
        }
        carryTask.setReflectStockId(carryMaterial.getId());
        carryTask.setReflectLocationId(targetLocationId);
        CommonResult<List<CarrySubTaskDO>> subTaskResult = generateCarrySubTaskByTargetLocationId(carryMaterial, targetLocationId);
        if(subTaskResult.isSuccess()){
            carryTask.setCarrySubTask(subTaskResult.getData());
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_NOT_START);
            return CommonResult.success(carryTask);
        }else {
            log.error("生成搬运子任务失败: {}",subTaskResult.getMsg());
        }
        carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_HOLD);
        return CommonResult.success(carryTask);
    }

    /**
     * 根据目标仓库生成搬运任务
     * @param carryTask  搬运任务 反射专用 正常调用时传null
     * @param carryMaterial 要搬运的物料所在库位上的 物料
     * @param targetWarehouseId 目标仓库
     * @return
     */
    public CommonResult<CarryTaskDO> generateCarryTaskByTargetWarehouseId(CarryTaskDO carryTask, MaterialStockDO carryMaterial, String targetWarehouseId){
        carryMaterial = materialStockService.getMaterialAtLocationByMaterialStock(carryMaterial);
        // 物料所在库位上的 物料
        if(carryMaterial == null
                || StringUtils.isBlank(carryMaterial.getLocationId())){
            return CommonResult.error(CARRYING_TASK_MATERIAL_NOT_CONTAINER);
        }
        if (carryTask== null){
            // 校验是否已经存在任务
            try {
                carryTaskService.checkCarryTask(carryMaterial.getId());
            }catch (Exception e){return CommonResult.error(CARRYING_TASK_MATERIAL_HAS_GENERATE_TASK);}
            carryTask = new CarryTaskDO();
        }
        carryTask.setReflectStockId(carryMaterial.getId());
        carryTask.setReflectWarehouseId(targetWarehouseId);
        CommonResult<List<CarrySubTaskDO>> subTaskResult = generateCarrySubTaskByTargetWarehouseId(carryMaterial, targetWarehouseId);
        if(subTaskResult.isSuccess()){
            carryTask.setCarrySubTask(subTaskResult.getData());
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_NOT_START);
            return CommonResult.success(carryTask);
        }else {
            log.error("生成搬运子任务失败: {}",subTaskResult.getMsg());
        }
        carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_HOLD);
        return CommonResult.success(carryTask);
    }


    public CommonResult<CarryTaskDO> generateCarryTaskByStartLocationIdAndTargetLocationId(CarryTaskDO carryTask, MaterialStockDO carryMaterial, String startLocationId, String targetLocationId){
        carryMaterial = materialStockService.getMaterialAtLocationByMaterialStock(carryMaterial);
        // 获取物料所在库位上的 容器物料  托盘或工装
        if(carryMaterial == null
                || StringUtils.isBlank(carryMaterial.getLocationId())){
            return CommonResult.error(CARRYING_TASK_MATERIAL_NOT_CONTAINER);
        }
        if (carryTask== null){
            // 校验是否已经存在任务
            try {
                carryTaskService.checkCarryTask(carryMaterial.getId());
            }catch (Exception e){return CommonResult.error(CARRYING_TASK_MATERIAL_HAS_GENERATE_TASK);}
            carryTask = new CarryTaskDO();
        }
        carryTask.setReflectStockId(carryMaterial.getId());
        carryTask.setReflectLocationId(targetLocationId);
        CommonResult<List<CarrySubTaskDO>> subTaskResult = generateCarrySubTaskByStartLocationIdAndTargetLocationId(carryMaterial, startLocationId, targetLocationId);
        if(subTaskResult.isSuccess()){
            carryTask.setCarrySubTask(subTaskResult.getData());
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_NOT_START);
            return CommonResult.success(carryTask);
        }
        carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_HOLD);
        return CommonResult.success(carryTask);
    }


    /**
     * 根据目标库位生成搬运子任务
     * @param carryMaterial 物料所在库位上的 物料
     * @param targetLocationId 目标库位
     * @return
     */
    private CommonResult<List<CarrySubTaskDO>> generateCarrySubTaskByTargetLocationId(MaterialStockDO carryMaterial, String targetLocationId){
        // 物料所在库区
        WarehouseAreaDO carryMaterialAtArea = warehouseAreaService.getWarehouseAreaByMaterialStock(carryMaterial);
        if(carryMaterialAtArea == null){
            throw exception(WAREHOUSE_NOT_EXISTS);
        }

        if(!checkTargetLocationAvailable(targetLocationId)){
            return CommonResult.error(CARRYING_TASK_TARGET_LOCATION_NOT_FREE_CALL);
        }
        WarehouseLocationDO targetLocation = warehouseLocationService.getWarehouseLocation(targetLocationId);

        // 起始接驳库区 集合
        Set<String> startTransitAreaIds = null;
        // 起始接驳库位初始化
        WarehouseLocationDO freeStartLocation = null;

        // step1 解析出 起始接驳库区 和 目标接驳库区
        // 物料只能在三个位置 1.存储区 2.接驳区 3.收获区
        // 物料在存储区
        if (DictConstants.WMS_WAREHOUSE_AREA_TYPE_1.equals(carryMaterialAtArea.getAreaType())){
            // 物料在存储区 那就是呼叫物料
            // 物料在存储区 那起始库区就是物料所在仓库的接驳库区
            // 获取物料的起始接驳库区集合   基本上就一个
            List<MaterialConfigAreaDO> startTransitConfigAreaList = materialConfigAreaService.getMaterialConfigTransitAreaByWarehouseId(carryMaterialAtArea.getWarehouseId());
            if(CollectionUtils.isAnyEmpty(startTransitConfigAreaList)){
                throw exception(WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA);
            }
            startTransitAreaIds = CollectionUtils.convertSet(startTransitConfigAreaList, MaterialConfigAreaDO::getWarehouseAreaId);
            if(DictConstants.WMS_MATERIAL_TYPE_GZ.equals(carryMaterial.getMaterialType())){// 工装在存储区
                return generateCarrySubTaskByTooling(carryMaterial, startTransitAreaIds, targetLocation);
            }else if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(carryMaterial.getMaterialType())){// 托盘在存储区
                for (String startTransitAreaId : startTransitAreaIds) {
                    // 获取一个空闲的起始库位--用来下架
                    freeStartLocation = getFreeEmptyDownLocationByAreaIds(Collections.singletonList(startTransitAreaId));
                    if(freeStartLocation != null){
                        break;
                    }
                }
                if (freeStartLocation == null) {
                    return CommonResult.error(CARRYING_TASK_START_LOCATION_NOT_FREE);
                }

                return generateCarrySubTaskByTray(carryMaterial, freeStartLocation.getId(), targetLocation);
            }else {// 其他物料在存储区
                throw exception(MATERIAL_STOCK_NOT_CONTAINER_MATERIAL_CANNOT_BIND_BIN);
            }
        }else if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(carryMaterialAtArea.getAreaType())
                || DictConstants.WMS_WAREHOUSE_AREA_TYPE_11.equals(carryMaterialAtArea.getAreaType())){
            // 在接驳区 只能是托盘
            if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(carryMaterial.getMaterialType())){// 托盘搬运
                return generateCarrySubTaskByTray(carryMaterial, carryMaterial.getLocationId(), targetLocation);
            }else {// 非托盘物料在接驳区
                WarehouseLocationDO materialAtLocation = warehouseLocationService.getWarehouseLocation(carryMaterial.getLocationId());
                // 此库位可以存放托盘  或者 此位置不可以存放托盘 但是库位上不是工装
                if(materialAtLocation.getIsTray() || !DictConstants.WMS_MATERIAL_TYPE_GZ.equals(carryMaterial.getMaterialType())){
                    // 有些不能存放托盘的库位可以放工装
                    throw exception(CARRYING_TASK_CARRYING_LOCATION_HAS_MATERIAL);
                }
                // 非托盘物料在接驳区 那就呼叫托盘直接取走
                return generateCarrySubTaskByTooling(carryMaterial, materialAtLocation, targetLocation);
            }
        }else if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_5.equals(carryMaterialAtArea.getAreaType())){// 在收获区
            if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(carryMaterial.getMaterialType())){// 非托盘才能在收获区
                return CommonResult.error(CARRYING_TASK_DELIVERY_LOCATION_HAS_TRAY);
            }
            // 物料在收获区 托盘不会在收货区   那就还是入库或者移库
            List<MaterialConfigAreaDO> startTransitConfigAreaList = materialConfigAreaService.getMaterialConfigTransitAreaByWarehouseId(carryMaterialAtArea.getWarehouseId());
            if(CollectionUtils.isAnyEmpty(startTransitConfigAreaList)){
                throw exception(WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA);
            }

            startTransitAreaIds = CollectionUtils.convertSet(startTransitConfigAreaList, MaterialConfigAreaDO::getWarehouseAreaId);

            // 呼叫托盘
            return this.simpleCallTrayByMaterial(carryMaterial, startTransitAreaIds, targetLocation);
        }else {
            throw exception(UNKNOWN_TYPE);
        }

    }


    /**
     * 根据目标仓库生产搬运子任务
     * @param carryMaterial 物料所在库位上的 物料
     * @param targetWarehouseId 目标仓库
     * @return
     */
    private CommonResult<List<CarrySubTaskDO>> generateCarrySubTaskByTargetWarehouseId(MaterialStockDO carryMaterial, String targetWarehouseId){
        // 物料所在库区
        WarehouseAreaDO carryMaterialAtArea = warehouseAreaService.getWarehouseAreaByMaterialStock(carryMaterial);
        if(carryMaterialAtArea == null){
            throw exception(WAREHOUSE_NOT_EXISTS);
        }

        // 起始接驳库区 集合
        Set<String> startTransitAreaIds = null;
        // 目标接驳库区 集合
        Set<String> targetTransitAreaIds = null;
        // 起始接驳库位初始化
        WarehouseLocationDO freeStartLocation = null;
        // step1 解析出 起始接驳库区 和 目标接驳库区
        // 物料只能在三个位置 1.存储区 2.接驳区 3.收获区
        // 物料在存储区
        if (DictConstants.WMS_WAREHOUSE_AREA_TYPE_1.equals(carryMaterialAtArea.getAreaType())){
            // 物料在存储区 那就是呼叫物料
            // 物料在存储区 那起始库区就是物料所在仓库的接驳库区
            // 获取物料的起始接驳库区集合   基本上就一个
            List<MaterialConfigAreaDO> startTransitConfigAreaList = materialConfigAreaService.getMaterialConfigTransitAreaByWarehouseId(carryMaterialAtArea.getWarehouseId());
            if(CollectionUtils.isAnyEmpty(startTransitConfigAreaList)){
                throw exception(WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA);
            }

            List<MaterialConfigAreaDO>targetTransitConfigAreaList = materialConfigAreaService.getMaterialConfigTransitAreaByWarehouseId(targetWarehouseId);
            if(CollectionUtils.isAnyEmpty(targetTransitConfigAreaList)){
                throw exception(WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA);
            }
            startTransitAreaIds = CollectionUtils.convertSet(startTransitConfigAreaList, MaterialConfigAreaDO::getWarehouseAreaId);
            targetTransitAreaIds = CollectionUtils.convertSet(targetTransitConfigAreaList, MaterialConfigAreaDO::getWarehouseAreaId);

            // 目标接驳库位初始化
            WarehouseLocationDO freeTargetLocation = null;
            for (String targetTransitAreaId : targetTransitAreaIds) {
                // 获取一个空闲的目标库位
                freeTargetLocation = getFreeEmptyDownLocationByAreaIds(Collections.singletonList(targetTransitAreaId));
                if(freeTargetLocation != null){
                    break;
                }
            }
            if (freeTargetLocation == null) {
                return CommonResult.error(CARRYING_TASK_TARGET_LOCATION_NOT_FREE);
            }

            if(DictConstants.WMS_MATERIAL_TYPE_GZ.equals(carryMaterial.getMaterialType())){// 工装在存储区
                // 物料在存储区，用他的下架库区 ，因为下架的接驳库区可能会有符合条件的托盘(但是下架库区可能被占满了所以不一定会有空闲的起始库位)
                return generateCarrySubTaskByTooling(carryMaterial, startTransitAreaIds, freeTargetLocation);
            }else if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(carryMaterial.getMaterialType())){//托盘在存储区
                for (String startTransitAreaId : startTransitAreaIds) {
                    // 获取一个空闲的起始库位--用来下架
                    freeStartLocation = getFreeEmptyDownLocationByAreaIds(Collections.singletonList(startTransitAreaId));
                    if(freeStartLocation != null){
                        break;
                    }
                }
                if (freeStartLocation == null) {
                    return CommonResult.error(CARRYING_TASK_START_LOCATION_NOT_FREE);
                }

                return generateCarrySubTaskByTray(carryMaterial, freeStartLocation.getId(), freeTargetLocation);
            }else {// 其他物料在存储区
                throw exception(MATERIAL_STOCK_NOT_CONTAINER_MATERIAL_CANNOT_BIND_BIN);
            }
        }else if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(carryMaterialAtArea.getAreaType())
                 || DictConstants.WMS_WAREHOUSE_AREA_TYPE_11.equals(carryMaterialAtArea.getAreaType())){
            List<MaterialConfigAreaDO> targetTransitConfigAreaList = null;
            if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(carryMaterialAtArea.getAreaType())){
                // 获取目标接驳库区集合
                targetTransitConfigAreaList = materialConfigAreaService.getMaterialConfigTransitAreaByWarehouseId(targetWarehouseId);
            }else{
                targetTransitConfigAreaList = materialConfigAreaService.getMaterialConfigCutterTransitAreaByWarehouseId(targetWarehouseId);
            }

            if(CollectionUtils.isAnyEmpty(targetTransitConfigAreaList)){
                throw exception(WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA);
            }

            // 在接驳区 只能是托盘
            if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(carryMaterial.getMaterialType())){// 托盘搬运

                targetTransitAreaIds = CollectionUtils.convertSet(targetTransitConfigAreaList, MaterialConfigAreaDO::getWarehouseAreaId);
                // 目标接驳库位初始化
                WarehouseLocationDO freeTargetLocation = null;
                for (String targetTransitAreaId : targetTransitAreaIds) {
                    // 获取一个空闲的目标库位
                    freeTargetLocation = getFreeEmptyDownLocationByAreaIds(Collections.singletonList(targetTransitAreaId));
                    if(freeTargetLocation != null){
                        break;
                    }
                }
                if (freeTargetLocation == null) {
                    return CommonResult.error(CARRYING_TASK_TARGET_LOCATION_NOT_FREE);
                }

                return generateCarrySubTaskByTray(carryMaterial, carryMaterial.getLocationId(), freeTargetLocation);
            }else { // 非托盘物料在接驳区
                WarehouseLocationDO materialAtLocation = warehouseLocationService.getWarehouseLocation(carryMaterial.getLocationId());
                // 此库位可以存放托盘  或者 此位置不可以存放托盘 但是库位上不是工装
                if(materialAtLocation.getIsTray() || !DictConstants.WMS_MATERIAL_TYPE_GZ.equals(carryMaterial.getMaterialType())){
                    // 有些不能存放托盘的库位可以放工装
                    throw exception(CARRYING_TASK_CARRYING_LOCATION_HAS_MATERIAL);
                }

                targetTransitAreaIds = CollectionUtils.convertSet(targetTransitConfigAreaList, MaterialConfigAreaDO::getWarehouseAreaId);
                // 目标接驳库位初始化
                WarehouseLocationDO freeTargetLocation = null;
                for (String targetTransitAreaId : targetTransitAreaIds) {
                    // 获取一个空闲的目标库位
                    freeTargetLocation = getFreeEmptyDownLocationByAreaIds(Collections.singletonList(targetTransitAreaId));
                    if(freeTargetLocation != null){
                        break;
                    }
                }
                if (freeTargetLocation == null) {
                    return CommonResult.error(CARRYING_TASK_TARGET_LOCATION_NOT_FREE);
                }
                // 非托盘物料在接驳区 那就呼叫托盘直接取走
                return generateCarrySubTaskByTooling(carryMaterial, materialAtLocation, freeTargetLocation);
            }
        }else if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_5.equals(carryMaterialAtArea.getAreaType())){// 在收货区
            if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(carryMaterial.getMaterialType())){// 非托盘才能在收货区
                return CommonResult.error(CARRYING_TASK_DELIVERY_LOCATION_HAS_TRAY);
            }
            // 物料在收获区 托盘不会在收货区   那就还是入库或者移库
            List<MaterialConfigAreaDO> startTransitConfigAreaList = materialConfigAreaService.getMaterialConfigTransitAreaByWarehouseId(carryMaterialAtArea.getWarehouseId());
            if(CollectionUtils.isAnyEmpty(startTransitConfigAreaList)){
                throw exception(WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA);
            }

            List<MaterialConfigAreaDO>targetTransitConfigAreaList = materialConfigAreaService.getMaterialConfigTransitAreaByWarehouseId(targetWarehouseId);
            if(CollectionUtils.isAnyEmpty(targetTransitConfigAreaList)){
                throw exception(WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA);
            }

            startTransitAreaIds = CollectionUtils.convertSet(startTransitConfigAreaList, MaterialConfigAreaDO::getWarehouseAreaId);
            targetTransitAreaIds = CollectionUtils.convertSet(targetTransitConfigAreaList, MaterialConfigAreaDO::getWarehouseAreaId);

            // 目标接驳库位初始化
            WarehouseLocationDO freeTargetLocation = null;
            for (String targetTransitAreaId : targetTransitAreaIds) {
                // 获取一个空闲的目标库位
                freeTargetLocation = getFreeEmptyDownLocationByAreaIds(Collections.singletonList(targetTransitAreaId));
                if(freeTargetLocation != null){
                    break;
                }
            }
            if (freeTargetLocation == null) {
                return CommonResult.error(CARRYING_TASK_TARGET_LOCATION_NOT_FREE);
            }
            return this.simpleCallTrayByMaterial(carryMaterial, startTransitAreaIds, freeTargetLocation);
        }else {
            throw exception(UNKNOWN_TYPE);
        }
    }

    /**
     * 根据起始库位和目标库位生成搬运子任务
     * @param carryContainerStock
     * @param startLocationId
     * @param targetLocationId
     * @return
     */
    private CommonResult<List<CarrySubTaskDO>> generateCarrySubTaskByStartLocationIdAndTargetLocationId(MaterialStockDO carryContainerStock, String startLocationId, String targetLocationId){
        // 物料所在库区
        WarehouseAreaDO carryContainerAtArea = warehouseAreaService.getWarehouseAreaByMaterialStock(carryContainerStock);
        if(carryContainerAtArea == null){
            throw exception(WAREHOUSE_NOT_EXISTS);
        }

        WarehouseAreaDO startArea = warehouseAreaService.getWarehouseAreaByLocationId(startLocationId);
        if(!DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(startArea.getAreaType())){
            throw exception(CARRYING_TASK_TARGET_LOCATION_NOT_SUPPORT_CARRYING);
        }
        if(!checkStartLocationAvailable(startLocationId)){
            return CommonResult.error(CARRYING_TASK_TARGET_LOCATION_NOT_FREE_CALL);
        }


        WarehouseAreaDO targetArea = warehouseAreaService.getWarehouseAreaByLocationId(targetLocationId);
        if(!DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(targetArea.getAreaType())){
            throw exception(CARRYING_TASK_TARGET_LOCATION_NOT_SUPPORT_CARRYING);
        }
        if(!checkTargetLocationAvailable(targetLocationId)){
            return CommonResult.error(CARRYING_TASK_TARGET_LOCATION_NOT_FREE_CALL);
        }
        WarehouseLocationDO startLocation = warehouseLocationService.getWarehouseLocation(startLocationId);
        WarehouseLocationDO targetLocation = warehouseLocationService.getWarehouseLocation(targetLocationId);

        if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(carryContainerStock.getMaterialType())) {// 托盘搬运
            return generateCarrySubTaskByTray(carryContainerStock, startLocationId, targetLocation);
        }else if(DictConstants.WMS_MATERIAL_TYPE_GZ.equals(carryContainerStock.getMaterialType())){
            return generateCarrySubTaskByTooling(carryContainerStock, startLocation, targetLocation);
        }else {
            throw exception(CARRYING_TASK_CARRYING_LOCATION_HAS_MATERIAL);
        }
    }

    private CommonResult<List<CarrySubTaskDO>> generateCarrySubTaskByTray(MaterialStockDO tray, String startLocationId, WarehouseLocationDO targetLocation){
        // 搬运子任务初始化
        List<CarrySubTaskDO> carrySubTaskDOS = new ArrayList<>();
        // 配送物料
        CommonResult<List<CarrySubTaskDO>> materialDistributionResult = materialDistribution(carrySubTaskDOS, tray, startLocationId, targetLocation);
        if(!materialDistributionResult.isSuccess()){
            return materialDistributionResult;
        }
        // 托盘回库
        return trayReturn(carrySubTaskDOS, tray, targetLocation, startLocationId);
    }

    // 根据起始库区和目标库位生成搬运子任务
    private CommonResult<List<CarrySubTaskDO>> generateCarrySubTaskByTooling(MaterialStockDO tooling, Collection<String> startTransitAreaIds, WarehouseLocationDO freeTargetLocation){
        // 搬运子任务初始化
        List<CarrySubTaskDO> carrySubTaskDOS = new ArrayList<>();
        // 指定的托盘类型
        List<String> appointTrayConfigIds = StringListUtils.stringToArrayList(tooling.getContainerConfigIds());
        CommonResult<List<CarrySubTaskDO>> callTrayResult = null;
        MaterialStockDO canCarryTray = null;
        WarehouseLocationDO freeStartLocation = null;
        one:for (String startTransitAreaId : startTransitAreaIds) {
            // 获取一个空闲的起始库位---获取不到也没事 因为可能有托盘就在起始接驳库区
            freeStartLocation = getFreeEmptyDownLocationByAreaIds(Collections.singletonList(startTransitAreaId));
            List<MaterialStockDO> carryTrayList = getCanCarryTray(appointTrayConfigIds, startTransitAreaId, freeStartLocation, freeTargetLocation);
            if(carryTrayList == null){
                return CommonResult.error(CARRYING_TASK_TRAY_TYPE_NOT_FREE);
            }
            for (MaterialStockDO carryTray : carryTrayList) {
                // 托盘所在库区
                String trayAtAreaId = carryTray.getAtAreaId();
                // 如果 起始接驳库区集合不为空  并且 托盘所在库区 就在起始接驳库区集合中  那么就不用呼叫托盘搬运了 直接结束
                if(!CollectionUtils.isAnyEmpty(startTransitAreaIds)
                        && startTransitAreaIds.contains(trayAtAreaId)){
                    canCarryTray = carryTray;
                    // 更改起始库位为托盘所在库位
                    freeStartLocation = warehouseLocationService.getWarehouseLocation(carryTray.getLocationId());
                    break one;
                }
                //呼叫托盘搬运
                callTrayResult = callTray(carrySubTaskDOS, carryTray, freeStartLocation);
                if(callTrayResult.isSuccess()){
                    canCarryTray = carryTray;
                    break one;
                }
            }

            return callTrayResult;
        }

        // 配送物料
        CommonResult<List<CarrySubTaskDO>> materialDistributionResult = materialDistribution(carrySubTaskDOS, tooling, freeStartLocation.getId(), freeTargetLocation);
        if(!materialDistributionResult.isSuccess()){
            return materialDistributionResult;
        }
        // 托盘回库
        return trayReturn(carrySubTaskDOS, canCarryTray, freeTargetLocation,freeStartLocation.getId());
    }

    // 根据起始库位和目标库位生成搬运子任务
    private CommonResult<List<CarrySubTaskDO>> generateCarrySubTaskByTooling(MaterialStockDO tooling, WarehouseLocationDO freeStartLocation, WarehouseLocationDO freeTargetLocation){
        // 搬运子任务初始化
        List<CarrySubTaskDO> carrySubTaskDOS = new ArrayList<>();
        // 指定的托盘类型
        List<String> appointTrayConfigIds = StringListUtils.stringToArrayList(tooling.getContainerConfigIds());
        CommonResult<List<CarrySubTaskDO>> callTrayResult = null;
        MaterialStockDO canCarryTray = null;

        List<MaterialStockDO> carryTrayList = getCanCarryTray(appointTrayConfigIds, null, freeStartLocation, freeTargetLocation);
        if(carryTrayList == null){
            return CommonResult.error(CARRYING_TASK_TRAY_TYPE_NOT_FREE);
        }
        for (MaterialStockDO carryTray : carryTrayList) {
            //呼叫托盘搬运
            callTrayResult = callTray(carrySubTaskDOS, carryTray, freeStartLocation);
            if(callTrayResult.isSuccess()){
                canCarryTray = carryTray;
                break;
            }
        }

        if(canCarryTray == null){
            return callTrayResult;
        }

        // 配送物料
        CommonResult<List<CarrySubTaskDO>> materialDistributionResult = materialDistribution(carrySubTaskDOS, tooling, freeStartLocation.getId(), freeTargetLocation);
        if(!materialDistributionResult.isSuccess()){
            return materialDistributionResult;
        }
        // 托盘回库
        return trayReturn(carrySubTaskDOS, canCarryTray, freeTargetLocation,freeStartLocation.getId());
    }

    // 生成搬运子任务
//    private CommonResult<List<CarrySubTaskDO>> generateCarrySubTask(MaterialStockDO carryContainerStock, List<String> allStartTransitAreaIds, WarehouseLocationDO freeStartLocation, WarehouseLocationDO freeTargetLocation){
//        // 搬运子任务初始化
//        List<CarrySubTaskDO> carrySubTaskDOS = new ArrayList<>();
//        if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(carryContainerStock.getMaterialType())){// 托盘搬运
//            MaterialStockDO tray = carryContainerStock;
//            // 配送物料
//            CommonResult<List<CarrySubTaskDO>> materialDistributionResult = materialDistribution(carrySubTaskDOS, tray, freeStartLocation.getId(), freeTargetLocation);
//            if(!materialDistributionResult.isSuccess()){
//                return materialDistributionResult;
//            }
//            // 托盘回库
//            return trayReturn(carrySubTaskDOS, tray, freeTargetLocation);
//        }else if(DictConstants.WMS_MATERIAL_TYPE_GZ.equals(carryContainerStock.getMaterialType())){// 工装搬运
//            MaterialStockDO tooling = carryContainerStock;
//            // 指定的托盘类型
//            List<String> appointTrayConfigIds = StringListUtils.stringToArrayList(tooling.getContainerConfigIds());
//            CommonResult<List<CarrySubTaskDO>> callTrayResult = null;
//            MaterialStockDO canCarryTray = null;
//            one:for (String startTransitAreaId : allStartTransitAreaIds) {
//                if(freeStartLocation == null){
//                    // 获取一个空闲的起始库位
//                    freeStartLocation = getFreeEmptyDownLocationByAreaIds(Collections.singletonList(startTransitAreaId));
//                }
//                List<MaterialStockDO> carryTrayList = getCanCarryTray(appointTrayConfigIds, startTransitAreaId, freeStartLocation, freeTargetLocation);
//                if(carryTrayList == null){
//                    return CommonResult.error(CARRYING_TASK_TRAY_TYPE_NOT_FREE);
//                }
//                for (MaterialStockDO carryTray : carryTrayList) {
//                    // 托盘所在库区
//                    String trayAtAreaId = carryTray.getAtAreaId();
//                    // 如果 起始接驳库区集合不为空  并且 托盘所在库区 就在起始接驳库区集合中  那么就不用呼叫托盘搬运了 直接结束
//                    if(!CollectionUtils.isAnyEmpty(allStartTransitAreaIds)
//                            && allStartTransitAreaIds.contains(trayAtAreaId)){
//                        canCarryTray = carryTray;
//                        // 更改起始库位为托盘所在库位
//                        freeStartLocation = warehouseLocationService.getWarehouseLocation(carryTray.getLocationId());
//                        break one;
//                    }
//                    //呼叫托盘搬运
//                    callTrayResult = callTray(carrySubTaskDOS, carryTray, freeStartLocation.getId());
//                    if(callTrayResult.isSuccess()){
//                        canCarryTray = carryTray;
//                        break one;
//                    }
//                }
//
//                return callTrayResult;
//            }
//
//            // 配送物料
//            CommonResult<List<CarrySubTaskDO>> materialDistributionResult = materialDistribution(carrySubTaskDOS, tooling, freeStartLocation.getId(), freeTargetLocation);
//            if(!materialDistributionResult.isSuccess()){
//                return materialDistributionResult;
//            }
//            // 托盘回库
//            return trayReturn(carrySubTaskDOS, canCarryTray, freeTargetLocation);
//
//
//        }else {
//            throw exception(CARRYING_TASK_MATERIAL_NOT_CONTAINER);
//        }
//    }

    /**
     * 根据要搬运的物料 匹配并呼叫托盘
     *  仅用于呼叫的物料在    收货区
     * @param carryMaterial
     * @param allStartTransitAreaIds
     * @return
     */
    private CommonResult<List<CarrySubTaskDO>> simpleCallTrayByMaterial(MaterialStockDO carryMaterial, Set<String> allStartTransitAreaIds, WarehouseLocationDO targetLocation){
        // 指定的托盘类型
        List<String> appointTrayConfigIds = StringListUtils.stringToArrayList(carryMaterial.getContainerConfigIds());
        CommonResult<List<CarrySubTaskDO>> callTrayResult = CommonResult.success(null);
        for (String startTransitAreaId : allStartTransitAreaIds) {
            // 获取一个空闲的起始库位
            WarehouseLocationDO freeStartLocation = getFreeEmptyDownLocationByAreaIds(Collections.singletonList(startTransitAreaId));
            List<MaterialStockDO> carryTrayList = getCanCarryTray(appointTrayConfigIds, startTransitAreaId, freeStartLocation, targetLocation);
            if(carryTrayList == null){
                return CommonResult.error(CARRYING_TASK_TRAY_TYPE_NOT_FREE);
            }
            for (MaterialStockDO carryTray : carryTrayList) {
                // 托盘所在库区
                String trayAtAreaId = carryTray.getAtAreaId();
                // 搬运子任务初始化
                List<CarrySubTaskDO> carrySubTaskDOS = new ArrayList<>();
                // 如果 起始接驳库区集合不为空  并且 托盘所在库区 就在起始接驳库区集合中  那么就不用呼叫托盘搬运了 直接结束
                if(!CollectionUtils.isAnyEmpty(allStartTransitAreaIds)
                        && allStartTransitAreaIds.contains(trayAtAreaId)){
                    // 呼叫托盘就在起始接驳库区 那就直接用这个托盘了
                    CarrySubTaskDO carrySubTaskDO = new CarrySubTaskDO();
                    carrySubTaskDO.setMaterialStockId(carryTray.getId());
                    carrySubTaskDO.setLocationId(carryTray.getLocationId());
                    carrySubTaskDOS.add(carrySubTaskDO);
                    return  CommonResult.success(carrySubTaskDOS);
                }
                // 呼叫托盘搬运
                callTrayResult = callTray(carrySubTaskDOS, carryTray, freeStartLocation);
                if(callTrayResult.isSuccess()){
                    return callTrayResult;
                }
            }
        }

        return CommonResult.error(WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA);
    }

    /**
     * 呼叫托盘搬运
     * @param carrySubTaskList 子任务集合
     * @param tray 托盘
     * @param targetLocation 目标库位
     * @return
     */
    private CommonResult<List<CarrySubTaskDO>> callTray(List<CarrySubTaskDO> carrySubTaskList, MaterialStockDO tray, WarehouseLocationDO targetLocation) {
        if(tray == null || targetLocation == null){
            return CommonResult.error(PARAM_NOT_NULL);
        }
        // 1. 看托盘是否需要下架 ，如需下架获取托盘的下架库位
        WarehouseLocationDO trayDownLocation = null;
        {
            WarehouseAreaDO canTrayAtArea = warehouseAreaService.getWarehouseAreaByLocationId(tray.getLocationId());
            // 托盘在存储区，所以托盘要先下架
            if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_1.equals(canTrayAtArea.getAreaType())){
                List<MaterialConfigAreaDO> trayTransitConfigAreaList = materialConfigAreaService.getMaterialConfigTransitAreaByWarehouseId(canTrayAtArea.getWarehouseId());

                trayDownLocation = getFreeEmptyDownLocationByAreaIds(trayTransitConfigAreaList.stream().map(MaterialConfigAreaDO::getWarehouseAreaId).collect(Collectors.toList()));

                if(trayDownLocation == null){
                    return CommonResult.error(IN_WAREHOUSE_CONTAINER_NO_AVAILABLE_DOWN_LOCATION);
                }
            }
        }

        carryTaskService.createCallTrayCarrySubTask(carrySubTaskList, tray, trayDownLocation, targetLocation);

        return CommonResult.success(carrySubTaskList);
    }


    /**
     * 配送物料
     * @param carrySubTaskList 子任务集合
     * @param containerStock 容器物料  只能传递绑定库位上的容器 是工装就传工装本身 不是工装就传托盘
     * @param startLocationId 起始库位id
     * @param targetLocation 目标库位
     * @return
     */
    private CommonResult<List<CarrySubTaskDO>> materialDistribution(List<CarrySubTaskDO> carrySubTaskList, MaterialStockDO containerStock, String startLocationId , WarehouseLocationDO targetLocation) {
        if(containerStock == null || startLocationId == null || targetLocation == null){
            return CommonResult.error(PARAM_NOT_NULL);

        }
        // 物料所在库区
        WarehouseAreaDO materialAtArea = warehouseAreaService.getWarehouseAreaByMaterialStock(containerStock);
        if(materialAtArea == null){
            throw exception(WAREHOUSE_NOT_EXISTS);
        }

        // 托盘初始化
        MaterialStockDO tray = null;
        //工装初始化
        MaterialStockDO tooling = null;
        if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(containerStock.getMaterialType())){
            tray = containerStock;
            List<MaterialStockDO> toolingStockList = materialStockService.getMaterialStockListByContainerId(tray.getId());
            if(toolingStockList.size() == 1 && DictConstants.WMS_MATERIAL_TYPE_GZ.equals(toolingStockList.get(0).getMaterialType())){
                tooling = toolingStockList.get(0);
            }
        }else if(DictConstants.WMS_MATERIAL_TYPE_GZ.equals(containerStock.getMaterialType())){
            tooling = containerStock;
            if(carrySubTaskList.isEmpty()){
                // 前边没有子任务，说明托盘就在工装的起始下架接驳库位，直接获取库位上的托盘
                List<MaterialStockDO> trayStockList = materialStockService.getMaterialStockListByLocationId(startLocationId);
                if(trayStockList.size() == 1 && DictConstants.WMS_MATERIAL_TYPE_TP.equals(trayStockList.get(0).getMaterialType())){
                    tray = trayStockList.get(0);
                }else {
                    return CommonResult.error(PARAM_NOT_NULL);
                }
            }else {
                // 从前边的子任务中获取托盘
                tray = materialStockService.getMaterialStockById(carrySubTaskList.get(carrySubTaskList.size() - 1).getMaterialStockId());
            }
        }else {
            throw exception(CARRYING_TASK_MATERIAL_NOT_CONTAINER);
        }

        // 上架
        WarehouseLocationDO upEmptyLocation = null;
        String upMaterialId = null;
        WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseArea(targetLocation.getWarehouseAreaId());
        WarehouseDO warehouse = warehouseService.getWarehouse(warehouseArea.getWarehouseId());
        // 如果终点库位是 自动化立体库 并且 是 自动化库区 那么他需要上架
        if(DictConstants.WMS_WAREHOUSE_TYPE_1.equals(warehouse.getWarehouseType())
                && DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO.equals( warehouseArea.getAreaProperty())){
            List<String> upAreaIds = null;
            upMaterialId = tray.getId();
            // 获取托盘的上架库区
            List<MaterialConfigAreaDO> trayStorageConfigAreaList = materialConfigAreaService.getMaterialConfigStorageAreaByMaterialConfigIdAndWarehouseId(tray.getMaterialConfigId(), targetLocation.getWarehouseId());
            if(trayStorageConfigAreaList.isEmpty() && tooling != null){
                // 托盘的上架库区不存在 就找工装的上架库区
                List<MaterialConfigAreaDO> materialStorageConfigAreaList = materialConfigAreaService.getMaterialConfigStorageAreaByMaterialConfigIdAndWarehouseId(tooling.getMaterialConfigId(), targetLocation.getWarehouseId());
                if(materialStorageConfigAreaList.isEmpty()){
                    throw exception(CARRYING_TASK_MATERIAL_NOT_FOUND_STORAGE_AREA);
                }
                upMaterialId = tooling.getId();
                upAreaIds = materialStorageConfigAreaList.stream().map(MaterialConfigAreaDO::getWarehouseAreaId).collect(Collectors.toList());
            }else {
                upAreaIds = trayStorageConfigAreaList.stream().map(MaterialConfigAreaDO::getWarehouseAreaId).collect(Collectors.toList());
            }
            if(CollectionUtils.isAnyEmpty(upAreaIds)){
                throw exception(CARRYING_TASK_MATERIAL_NOT_FOUND_STORAGE_AREA);
            }

            upEmptyLocation = getFreeEmptyUpLocationByAreaIds(upAreaIds);
            if(upEmptyLocation == null){
                return CommonResult.error(CARRYING_TASK_STORAGE_LOCATION_NOT_FREE);
            }
        }

        carryTaskService.createMaterialDistributionCarrySubTask(carrySubTaskList, containerStock.getId(), tray.getId(), startLocationId, targetLocation, materialAtArea.getAreaType(), upMaterialId, upEmptyLocation);
        return CommonResult.success(carrySubTaskList);
    }

    /**
     * 托盘回库
     * @param carrySubTaskList 子任务集合
     * @param tray 托盘
     * @param currentLocation 任务完成后的终点接驳库位
     * @param takeLocationId 托盘回库之前取物料的库位id （用来将托盘回库前，将托盘上的物料更新到新的库位上）
     * @return
     */
    private CommonResult<List<CarrySubTaskDO>> trayReturn(List<CarrySubTaskDO> carrySubTaskList, MaterialStockDO tray, WarehouseLocationDO currentLocation, String takeLocationId) {
        if(currentLocation == null){
            return CommonResult.error(PARAM_NOT_NULL);
        }
        if(!currentLocation.getIsTray()){
            if (tray == null) {
                return CommonResult.error(PARAM_NOT_NULL);
            }

            tray = materialStockService.getMaterialStockById(tray.getId());
            String defaultWarehouseId = tray.getDefaultWarehouseId();
            // 获取托盘默认仓库的接驳库区
            List<MaterialConfigAreaDO> trayTransitConfigAreaList = materialConfigAreaService.getMaterialConfigTransitAreaByWarehouseId(defaultWarehouseId);
            if(CollectionUtils.isAnyEmpty(trayTransitConfigAreaList)){
                throw exception(WAREHOUSE_NOT_CONFIG_TRANSPORT_AREA);
            }
            // 根据接驳库区获取空闲的接驳库位
            WarehouseLocationDO returnLocation = getFreeEmptyUpLocationByAreaIds(trayTransitConfigAreaList.stream().map(MaterialConfigAreaDO::getWarehouseAreaId).collect(Collectors.toList()));
            if(returnLocation == null){
                return CommonResult.error(CARRYING_TASK_CARRYING_LOCATION_NOT_FREE);
            }

            WarehouseLocationDO upLocation = null;
            WarehouseAreaDO warehouseArea = warehouseAreaService.getWarehouseArea(returnLocation.getWarehouseAreaId());
            WarehouseDO warehouse = warehouseService.getWarehouse(warehouseArea.getWarehouseId());
            // 如果他是 自动化立体库 并且 是 自动化库区 那么他需要上架
            if(DictConstants.WMS_WAREHOUSE_TYPE_1.equals(warehouse.getWarehouseType()) && DictConstants.WMS_WAREHOUSE_AREA_PROPERTY_AUTO.equals( warehouseArea.getAreaProperty())) {
                // 看看在自己的默认存放仓库是否存在空闲的上架库位
                List<MaterialConfigAreaDO> trayStorageConfigAreaList = materialConfigAreaService.getMaterialConfigStorageAreaByMaterialConfigIdAndWarehouseId(tray.getMaterialConfigId(), defaultWarehouseId);
                if (!trayStorageConfigAreaList.isEmpty()) {
                    upLocation = getFreeEmptyUpLocationByAreaIds(trayStorageConfigAreaList.stream().map(MaterialConfigAreaDO::getWarehouseAreaId).collect(Collectors.toList()));
                }
                if (upLocation == null) {
                    return CommonResult.error(CARRYING_TASK_STORAGE_LOCATION_NOT_FREE);
                }
            }
            carryTaskService.createTrayReturnCarrySubTask(carrySubTaskList, tray, returnLocation.getId(), upLocation, takeLocationId);
        }

        return CommonResult.success(carrySubTaskList);
    }

    // 找一个闲置托盘
    public List<MaterialStockDO> getCanCarryTray(List<String> appointTrayConfigIds, String startTransitAreaId, WarehouseLocationDO startLocation, WarehouseLocationDO targetLocation){
        String targetAreaId =  targetLocation != null? targetLocation.getWarehouseAreaId(): null;
        // 初始化库区
        Set<String>  initStartTransitAreaIds = new HashSet<>();
        Set<String> appointTrayConfigIdsFilter = new HashSet<>();
        // 如果 起始接驳库区集合为空   就按照给定的起始库位找托盘
        if(StringUtils.isBlank(startTransitAreaId)){
            // 找一个能来这的托盘类型
            appointTrayConfigIdsFilter.addAll(materialConfigAreaService.getMaterialConfigAreaByStartAreaIdAndEndAreaId(startLocation.getWarehouseAreaId(), targetAreaId, appointTrayConfigIds));
            if(CollectionUtils.isAnyEmpty(appointTrayConfigIdsFilter)){
                return null;
            }
            initStartTransitAreaIds.add(startLocation.getWarehouseAreaId());
        }else {
            initStartTransitAreaIds.add(startTransitAreaId);
            if(startLocation == null || startLocation.getWarehouseAreaId().equals(startTransitAreaId)){
                // 可用的托盘类型id
                List<String> availableTrayConfigIds = materialConfigAreaService.getMaterialConfigAreaByStartAreaIdAndEndAreaId(startTransitAreaId, targetAreaId, appointTrayConfigIds);
                appointTrayConfigIdsFilter.addAll(availableTrayConfigIds);
            }
        }


        // 类型有了 那就找到闲置的托盘
        List<MaterialStockDO> allEmptyTray = materialStockService.getEmptyTrayStockListByMaterialConfigIds(appointTrayConfigIdsFilter,startTransitAreaId);
        List<MaterialStockDO> resuleTrayList = new ArrayList<>();
        for (MaterialStockDO emptyTray : allEmptyTray) {
            try {
                // 校验托盘是否已存在任务
                carryTaskService.checkSubCarryTask(emptyTray.getId());
                // 校验托盘所在位置是否被锁定
                checkLocationLocked(emptyTray.getLocationId());
                resuleTrayList.add(emptyTray);
            } catch (Exception e) {
                log.error("托盘已被占用或不存在任务:{}", e.getMessage());
                continue;
            }
        }

        if(resuleTrayList.isEmpty()){
            return null;
        }

        // 根据库存组成map
        Map<String, MaterialStockDO> locationAndTrayMap = CollectionUtils.convertMap(resuleTrayList, MaterialStockDO::getLocationId);

        // 获取托盘所在的库位
        List<WarehouseLocationDO> warehouseLocationList = warehouseLocationService.getWarehouseLocationListByIds(locationAndTrayMap.keySet());


        // 在接驳位的托盘优先级最高
        LinkedList<MaterialStockDO> queueList = new LinkedList<>();
        Set<String> areaSet = new HashSet<>();
        // 就在起始库区的托盘
        List<MaterialStockDO> trayAtStartAreaList = new ArrayList<>();
        warehouseLocationList.forEach(warehouseLocation -> {
            if(!areaSet.contains(warehouseLocation.getWarehouseAreaId())){
                if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_3.equals(warehouseLocation.getAreaType())){
                    // 获取此库区的托盘
                    MaterialStockDO currTray = locationAndTrayMap.get(warehouseLocation.getId());
                    // 如果此托盘在起始库区 就先放到起始库区的托盘列表里
                    if(initStartTransitAreaIds.contains(warehouseLocation.getWarehouseAreaId())){
                        trayAtStartAreaList.add(currTray);
                    }else {
                        // 如果起始库位为空 说明所有库位都被占用了，那就没必要拿不在此仓库的托盘了
                        if(startLocation != null)queueList.addFirst(currTray);
                    }
                }else {
                    // 将在同一个存储库区上的托盘分组 一个存储库区一个托盘
                    areaSet.add(warehouseLocation.getWarehouseAreaId());
                    // 如果起始库位为空 说明所有库位都被占用了，那就没必要拿不在此仓库的托盘了
                    if(startLocation != null)queueList.addLast(locationAndTrayMap.get(warehouseLocation.getId()));
                }
            }
        });
        trayAtStartAreaList.forEach(queueList::addFirst);
        if(queueList.isEmpty()){
            return null;
        }
        return queueList;
    }
}
