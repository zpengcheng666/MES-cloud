package com.miyu.module.wms.service.carrytask;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.convert.carrytask.CarryTaskConvert;
import com.miyu.module.wms.core.carrytask.restservice.DispatchAGVService;
import com.miyu.module.wms.core.carrytask.service.impl.MaterialInServiceImpl;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.warehousearea.WarehouseAreaDO;
import com.miyu.module.wms.dal.dataobject.warehouselocation.WarehouseLocationDO;
import com.miyu.module.wms.dal.mysql.carrytask.CarrySubTaskMapper;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.framework.generator.seivice.ICodeGeneratorService;
import com.miyu.module.wms.service.alarm.AlarmService;
import com.miyu.module.wms.service.materialconfig.MaterialConfigService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.warehousearea.WarehouseAreaService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import com.miyu.module.wms.controller.admin.carrytask.vo.*;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.carrytask.CarryTaskMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 搬运任务 Service 实现类
 *
 * @author 技术部长
 */
@Service
@Validated
@Slf4j
public class CarryTaskServiceImpl implements CarryTaskService {
    // 生成搬运任务流水号 前缀
    private final static String CARRYING_TASK_PREFIX = "CT-";

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private CarryTaskMapper carryTaskMapper;
    @Resource
    private CarrySubTaskMapper carrySubTaskMapper;
    @Resource
    private ICodeGeneratorService codeGeneratorService;
    @Resource
    private MaterialConfigService materialConfigService;
    @Resource
    private DispatchAGVService dispatchAGVService;
    @Resource
    private WarehouseLocationService warehouseLocationService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private AlarmService alarmService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createCarryTask(CarryTaskSaveReqVO createReqVO) {
        // 插入
        CarryTaskDO carryTask = BeanUtils.toBean(createReqVO, CarryTaskDO.class);
        carryTaskMapper.insert(carryTask);

        // 插入子表
        createCarrySubTaskList(carryTask.getId(), createReqVO.getCarrySubTasks());
        // 返回
        return carryTask.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateCarryTask(CarryTaskSaveReqVO updateReqVO) {
        // 校验存在
        validateCarryTaskExists(updateReqVO.getId());
        // 更新
        CarryTaskDO updateObj = BeanUtils.toBean(updateReqVO, CarryTaskDO.class);
        carryTaskMapper.updateById(updateObj);

        // 更新子表
        updateCarrySubTaskList(updateReqVO.getId(), updateReqVO.getCarrySubTasks());
    }

    @Override
    public void updateCarryTask(CarryTaskDO carryTaskDO) {
        // 校验存在
        validateCarryTaskExists(carryTaskDO.getId());

        carryTaskMapper.updateById(carryTaskDO);
    }

    @Override
    public void updateCarrySubTask(CarrySubTaskDO carrySubTask) {
        carrySubTaskMapper.updateById(carrySubTask);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteCarryTask(String id) {
        // 校验存在
        validateCarryTaskExists(id);
        // 删除时 解锁目标库位
        List<CarrySubTaskDO> carrySubTask = carryTaskMapper.selectById(id).getCarrySubTask();
        if(!CollectionUtils.isAnyEmpty(carrySubTask))warehouseLocationService.unlockLocation(carrySubTask.get(carrySubTask.size() - 1).getLocationId());

        // 删除
        carryTaskMapper.deleteById(id);

        // 删除子表
        deleteCarrySubTaskByParentId(id);
    }

    private void validateCarryTaskExists(String id) {
        if (carryTaskMapper.selectById(id) == null) {
            throw exception(IN_WAREHOUSE_CARRY_TASK_NOT_EXISTS);
        }
    }

    @Override
    public CarryTaskDO getCarryTask(String id) {
        return carryTaskMapper.selectById(id);
    }

    @Override
    public CarryTaskDO getCarryTaskByTaskCode(String taskCode) {
        return carryTaskMapper.selectOne(CarryTaskDO::getTaskCode, taskCode);
    }

    @Override
    public CarryTaskDO getCarryTaskWithSubTask(String id) {
        CarryTaskDO carryTask = carryTaskMapper.selectById(id);
        List<CarrySubTaskDO> carrySubTaskList = carrySubTaskMapper.selectListByParentIds(Arrays.asList(id));
        carrySubTaskList.sort(Comparator.comparing(CarrySubTaskDO::getExecuteOrder));
        carryTask.setCarrySubTask(carrySubTaskList);
        return carryTask;
    }

    @Override
    public CarryTaskDO getCarryTaskBySubTaskId(String subId) {
        CarrySubTaskDO carrySubTask = carrySubTaskMapper.selectById(subId);
        return getCarryTaskWithSubTask(carrySubTask.getParentId());
    }

    @Override
    public PageResult<CarryTaskDO> getCarryTaskPage(CarryTaskPageReqVO pageReqVO) {
        return carryTaskMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CarryTaskDO> getHangingCarryTask() {
        return carryTaskMapper.selectByTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_HOLD);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveCarryTask(CarryTaskDO carryTask) {
        try {
            // 锁一下 key
            redissonClient.getLock(carryTask.getReflectStockId()).tryLock(60,60, TimeUnit.SECONDS);
            if(StringUtils.isBlank(carryTask.getTaskCode())){
                String taskCode = CARRYING_TASK_PREFIX + codeGeneratorService.generateTimestampSerialNumber();
                carryTask.setTaskCode(taskCode);
            }
            List<CarryTaskDO> carryTaskList =  this.getUnfinishedCarryTaskByReflectStockId(carryTask.getReflectStockId());
            if(!CollectionUtils.isAnyEmpty(carryTaskList)){
                throw exception(CARRYING_TASK_MATERIAL_HAS_GENERATE_TASK);
            }
            if(!carryTaskMapper.insertOrUpdate(carryTask)){
                throw exception(CARRYING_TASK_CREATE_ERROR);
            }
            if(carryTask.getCarrySubTask()!= null)createCarrySubTaskList(carryTask.getId(), carryTask.getCarrySubTask());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            redissonClient.getLock(carryTask.getReflectStockId()).unlock();
        }
    }

    @Override
    public synchronized List<CarryTaskDO> getUnfinishedCarryTaskByReflectStockId(String reflectStockId) {
        return  carryTaskMapper.getUnfinishedCarryTaskByReflectStockId(reflectStockId);
    }


    /**
     * 创建刀具配送搬运任务
     * @param carryTask
     * @param trayStock
     * @param targetLocationIdsStr
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createToolDistributionCarryTask(CarryTaskDO carryTask, MaterialStockDO trayStock, String targetLocationIdsStr) {
        if(carryTask.getId() == null) {
            // 生成搬运任务
            String taskCode = CARRYING_TASK_PREFIX + codeGeneratorService.generateTimestampSerialNumber();
            carryTask.setTaskCode(taskCode);
            carryTask.setPriority(1);
            if (targetLocationIdsStr == null) {
                carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_HOLD);
            } else {
                carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_NOT_START);
            }
            carryTask.setTaskType(DictConstants.WMS_CARRY_TASK_TYPE_OUT);
            carryTaskMapper.insert(carryTask);
        }else if(targetLocationIdsStr != null){
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_NOT_START);
            carryTaskMapper.updateById(carryTask);
        }

        if(targetLocationIdsStr != null) {
            String trayStockId = trayStock.getId();
            String trayLocationId = trayStock.getLocationId();

            List<String> targetLocationIdList = Arrays.asList(targetLocationIdsStr.split(","));

            List<CarrySubTaskDO> carrySubTaskDOS = new ArrayList<>();
            int executeOrder = 0;
            // 生成子任务   // 移动 取 /移动 放   /等待/ 移动 取 移动 放 /等待/
            {// 托盘搬运
                // 取托盘
                takeCarrySubTask(++executeOrder, trayStockId, trayLocationId, false,carrySubTaskDOS);
                // 放到目标地点托盘
                putCarrySubTask(++executeOrder, trayStockId, targetLocationIdList.get(0), carrySubTaskDOS);
            }
            for (int i = 1; i < targetLocationIdList.size(); i++){// 等待 取 放
                // 在目标地点等待取托盘
                takeCarrySubTask(++executeOrder, trayStockId, targetLocationIdList.get(i-1), true,carrySubTaskDOS);
                // 放置到下一个目标地点
                putCarrySubTask(++executeOrder, trayStockId, targetLocationIdList.get(i), carrySubTaskDOS);
            }
            createCarrySubTaskList(carryTask.getId(), carrySubTaskDOS);
        }
    }


    @Override
    public List<CarryTaskDO> getUnfinishedCarryTask() {
        return carryTaskMapper.selectUnfinishedCarryTask();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CommonResult<String> dispatchCarryTask(CarryTaskDO carryTask) {
        if(carryTask.getCarrySubTask() == null || carryTask.getCarrySubTask().isEmpty()){
            log.error("子任务为空");
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_EXCEPTION);
            carryTask.setTaskDescription("子任务为空");
            this.updateCarryTask(carryTask);
            return CommonResult.success("失败：子任务为空");
        }

        MaterialConfigDO materialConfig = null;
        for (CarrySubTaskDO carrySubTaskDO : carryTask.getCarrySubTask()) {
            if(carrySubTaskDO.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_TAKE){
                String trayStockId = carrySubTaskDO.getMaterialStockId();
                materialConfig = materialConfigService.getMaterialConfigByMaterialStockId(trayStockId);
                break;
            }
        }

        if(materialConfig == null || materialConfig.getContainerType() == null){
            log.error("要搬运的托盘类型不存在");
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_EXCEPTION);
            carryTask.setTaskDescription("要搬运的托盘类型不存在");
            this.updateCarryTask(carryTask);
            return CommonResult.success("失败：要搬运的托盘类型不存在");
        }

        // 转换实体
        CarryTaskIssueVO carryTaskIssue = CarryTaskConvert.INSTANCE.convertCarryTaskDOToIssueVO(carryTask, materialConfig.getContainerType());
        // 下发到AGV系统
        CarryTaskResult<?> carryTaskResult = dispatchAGVService.agvCallbackServiceCreateTask(carryTaskIssue);
        if (carryTaskResult.isSuccess()){
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_ISSUED);
//            carryTask.setTaskDescription(carryTaskResult.getData().toString());
            this.updateCarryTask(carryTask);
        }else {
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_EXCEPTION);
            carryTask.setTaskDescription(carryTaskResult.getCodeMsg());
            this.updateCarryTask(carryTask);
        }

        {
            // todo: 伪代码 后期删除
            CarryTaskReceiveVO carryTaskReceiveVO = new CarryTaskReceiveVO();
            carryTaskReceiveVO.setTaskNo(carryTaskIssue.getTaskNo());
            carryTaskReceiveVO.setSort("1");
            carryTaskReceiveVO.setCarNo("NO.1");

            dispatchAGVService.agvCallbackServiceReceiveTask(carryTaskReceiveVO);
        }
        return CommonResult.success("成功："+carryTask.getTaskCode());
    }


    // ==================== 子表（搬运任务子表） ====================

    @Override
    public CarrySubTaskDO getCarrySubTaskByParentIdAndExecuteOrder(String parentId, Integer executeOrder) {
        return carrySubTaskMapper.selectOne(CarrySubTaskDO::getParentId, parentId, CarrySubTaskDO::getExecuteOrder, executeOrder);
    }

    @Override
    public Boolean updateCarrySubTaskByParentIdAndExecuteOrder(String parentId, Integer executeOrder, Integer taskStatus) {
        return carrySubTaskMapper.updateByParentIdAndExecuteOrder(parentId, executeOrder, taskStatus);
    }


    @Override
    public List<CarrySubTaskDO> getCarrySubTaskListByParentId(String parentId) {
        return carrySubTaskMapper.selectListByParentId(parentId);
    }

    @Override
    public List<CarrySubTaskDO> getUnfinishedCarrySubTask() {
        return carrySubTaskMapper.selectUnfinishedCarrySubTask();
    }


    // 校验物料是否已存在任务
    @Override
    public void checkCarryTask(String containerStockId) {
        List<CarryTaskDO> unfinishedCarryTask = this.getUnfinishedCarryTask();
        Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarryTask, CarryTaskDO::getReflectStockId);
        if(haveTaskLocationIdSet.contains(containerStockId)){
            throw exception(CARRYING_TASK_MATERIAL_HAS_GENERATE_TASK);
        }
    }

    // 校验物料是否已存在任务
    @Override
    public void checkSubCarryTask(String materialStockId) {
        List<CarrySubTaskDO> unfinishedCarrySubTask = this.getUnfinishedCarrySubTask();
        Set<String> haveTaskLocationIdSet = CollectionUtils.convertSet(unfinishedCarrySubTask, CarrySubTaskDO::getMaterialStockId);
        if(haveTaskLocationIdSet.contains(materialStockId)){
            throw exception(CARRYING_TASK_MATERIAL_HAS_GENERATE_TASK);
        }
    }

    @Override
    public CarryTaskResult<?> dispatchNextCarryTask(String insId) {
        CarrySubTaskDO carrySubTask = carrySubTaskMapper.selectOne(CarrySubTaskDO::getInstructionId, insId, CarrySubTaskDO::getTaskStatus, DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING);
        if(carrySubTask != null){
            CarryTaskDO carryTask = this.getCarryTaskWithSubTask(carrySubTask.getParentId());
            if(carryTask != null){
                CarryTaskReceiveVO carryTaskReceiveVO = new CarryTaskReceiveVO();
                carryTaskReceiveVO.setTaskNo(carryTask.getTaskCode());
                carryTaskReceiveVO.setSort(String.valueOf(carrySubTask.getExecuteOrder()));
                carryTaskReceiveVO.setStatus(DictConstants.WMS_CARRY_TASK_CALL_BACK_STATUS_FINISH);
                return dispatchAGVService.agvCallbackServiceReceiveTask(carryTaskReceiveVO);
            }
        }

        // 找不也正常，因为可能是手动下发的任务
        return CarryTaskResult.error(500, "未找到下一个任务");
    }

    @Override
    public void moveCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList) {
        commonCarrySubTask(executeOrder, DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_MOVE, materialId, locationId, false, carrySubTaskList);
    }

    @Override
    public void takeCarrySubTask(int executeOrder, String materialId, String locationId, boolean trigEvent, List<CarrySubTaskDO> carrySubTaskList) {
        commonCarrySubTask(executeOrder, DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_TAKE, materialId, locationId, trigEvent, carrySubTaskList);
    }

    @Override
    public void putCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList) {
        commonCarrySubTask(executeOrder, DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_PUT, materialId, locationId, false, carrySubTaskList);
    }

    @Override
    public void putSpecialMarkCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList) {
        CarrySubTaskDO carrySubTaskDO = new CarrySubTaskDO();
        carrySubTaskDO.setInsType(DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_PUT);
        carrySubTaskDO.setExecuteOrder(executeOrder);
        carrySubTaskDO.setMaterialStockId(materialId);
        carrySubTaskDO.setLocationId(locationId);
        carrySubTaskDO.setTrigEvent(DictConstants.WMS_CARRY_TASK_TRIGEVENT_NO);
        carrySubTaskDO.setSpecialMark(true);
        carrySubTaskList.add(carrySubTaskDO);
    }

    @Override
    public void upCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList) {
        commonCarrySubTask(executeOrder, DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_ON, materialId, locationId, false, carrySubTaskList);
    }

    @Override
    public void downCarrySubTask(int executeOrder, String materialId, String locationId, List<CarrySubTaskDO> carrySubTaskList) {
        commonCarrySubTask(executeOrder, DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_OFF, materialId, locationId, false, carrySubTaskList);
    }

    @Override
    public void createCallTrayCarrySubTask(List<CarrySubTaskDO> carrySubTaskList, MaterialStockDO tray, WarehouseLocationDO trayDownLocation, WarehouseLocationDO targetLocation) {
        // 生成搬运子任务
        int executeOrder = 0;
        // 取托盘
        String materialId = tray.getId();

        String targetLocationId = targetLocation.getId();

        if(trayDownLocation != null){
            // 托盘的下架位置不为空 则需要移动至 下架库位
            // 移动+下架+取
            moveCarrySubTask(++executeOrder, materialId, trayDownLocation.getId(), carrySubTaskList);

            downCarrySubTask(++executeOrder, materialId, trayDownLocation.getId(), carrySubTaskList);
            if(!trayDownLocation.getId().equals(targetLocationId)){
                takeCarrySubTask(++executeOrder, materialId, trayDownLocation.getId(), true, carrySubTaskList);

                // 看看目标未知能否存放托盘
                if(targetLocation.getIsTray()){
                    // 放
                    putCarrySubTask(++executeOrder, materialId, targetLocationId, carrySubTaskList);
                }

            }
        }else{
            // 取
            takeCarrySubTask(++executeOrder, materialId, tray.getLocationId(), false, carrySubTaskList);
            // 看看目标未知能否存放托盘
            if(targetLocation.getIsTray()){
                // 放
                putCarrySubTask(++executeOrder, materialId, targetLocationId, carrySubTaskList);
            }
        }

    }

    @Override
    public void createMaterialDistributionCarrySubTask(List<CarrySubTaskDO> carrySubTaskList, String containerStockId,  String trayId, String startLocationId , WarehouseLocationDO targetLocation, Integer materialAtAreaType, String upMaterialId, WarehouseLocationDO upEmptyLocation) {
        int executeOrder = 0;
        if(!CollectionUtils.isAnyEmpty(carrySubTaskList)){
            executeOrder = carrySubTaskList.get(carrySubTaskList.size() - 1).getExecuteOrder();
        }

        if(DictConstants.WMS_WAREHOUSE_AREA_TYPE_1.equals(materialAtAreaType)){
            if(executeOrder == 0){
                // 看看 是否有呼叫托盘的子任务 有了就不需要再生成移动了
                moveCarrySubTask(++executeOrder, containerStockId, startLocationId, carrySubTaskList);
            }
            // 物料在存储区才需要下架
            downCarrySubTask(++executeOrder, containerStockId, startLocationId, carrySubTaskList);
        }

        WarehouseLocationDO warehouseLocation = warehouseLocationService.getWarehouseLocation(startLocationId);

        if(warehouseLocation.getIsTray()){
            //  取
            takeCarrySubTask(++executeOrder, trayId, startLocationId, true, carrySubTaskList);
        }else {
            //  取
            takeCarrySubTask(++executeOrder, containerStockId, startLocationId, true, carrySubTaskList);
        }

        //  放
        putCarrySubTask(++executeOrder, trayId, targetLocation.getId(), carrySubTaskList);

        // 上架
        if(upEmptyLocation != null){
            upCarrySubTask(++executeOrder, upMaterialId, upEmptyLocation.getId(), carrySubTaskList);
        }
    }

    @Override
    public void createTrayReturnCarrySubTask(List<CarrySubTaskDO> carrySubTaskList, MaterialStockDO tray, String returnLocationId, WarehouseLocationDO trayUpLocation, String takeLocationId) {
        int executeOrder = 0;
        if(CollectionUtils.isAnyEmpty(carrySubTaskList)){
            throw exception(BUG);
        }

        CarrySubTaskDO beforeCarrySubTask = carrySubTaskList.get(carrySubTaskList.size() - 1);
        executeOrder = beforeCarrySubTask.getExecuteOrder();
        {
            String materialId = null;
            // 获取任务 取件库位的物料
//            MaterialStockDO containerStock = materialStockService.getContainerStockByLocationId(takeLocationId);
            MaterialStockDO containerStock = tray;
            materialId = containerStock.getId();

            if(DictConstants.WMS_MATERIAL_TYPE_TP.equals(containerStock.getMaterialType())){
                List<MaterialStockDO> materialStockList = materialStockService.getMaterialStockListByContainerId(containerStock.getId());
                // 将上一步放的物料更改成 托盘上的容器
                if(materialStockList.size() != 1){
                    throw exception(CARRYING_TASK_TRAY_HAS_MULTIPLE_MATERIAL);
                }
                materialId = materialStockList.get(0).getId();
            }

            beforeCarrySubTask.setMaterialStockId(materialId);
        }

        // 放
        putSpecialMarkCarrySubTask(++executeOrder, tray.getId(), returnLocationId, carrySubTaskList);
        if(trayUpLocation != null){
            // 上架
            upCarrySubTask(++executeOrder, tray.getId(), trayUpLocation.getId(), carrySubTaskList);
        }
    }

    private void commonCarrySubTask(int executeOrder, Integer insType, String materialId, String locationId, boolean trigEvent, List<CarrySubTaskDO> carrySubTaskList) {
        CarrySubTaskDO carrySubTaskDO = new CarrySubTaskDO();
        carrySubTaskDO.setInsType(insType);
        carrySubTaskDO.setExecuteOrder(executeOrder);
        carrySubTaskDO.setMaterialStockId(materialId);
        carrySubTaskDO.setLocationId(locationId);
        carrySubTaskDO.setTrigEvent(trigEvent? DictConstants.WMS_CARRY_TASK_TRIGEVENT_YES : DictConstants.WMS_CARRY_TASK_TRIGEVENT_NO);
        carrySubTaskList.add(carrySubTaskDO);
    }


    /*private void createCarrySubTaskList(String parentId, List<CarrySubTaskDO> list) {
        list.forEach(o -> o.setParentId(parentId));
        carrySubTaskMapper.insertBatch(list);
    }*/

    private void createCarrySubTaskList(String parentId, List<CarrySubTaskDO> list) {
        list.forEach(o -> {
            o.setParentId(parentId);
            if(o.getTaskStatus() == null)o.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_NOT_START);
        });
//        this.createCarrySubTaskInstruction(list);
        carrySubTaskMapper.insertBatch(list);
    }



    private void updateCarrySubTaskList(String parentId, List<CarrySubTaskDO> list) {
        deleteCarrySubTaskByParentId(parentId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createCarrySubTaskList(parentId, list);
    }

    private void deleteCarrySubTaskByParentId(String parentId) {
        carrySubTaskMapper.deleteByParentId(parentId);
    }

}
