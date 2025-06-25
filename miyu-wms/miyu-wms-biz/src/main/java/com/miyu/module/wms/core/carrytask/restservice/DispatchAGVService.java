package com.miyu.module.wms.core.carrytask.restservice;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.miyu.module.wms.controller.admin.carrytask.vo.CarryTaskIssueVO;
import com.miyu.module.wms.controller.admin.carrytask.vo.CarryTaskReceiveVO;
import com.miyu.module.wms.controller.admin.carrytask.vo.CarryTaskResult;
import com.miyu.module.wms.controller.admin.carrytask.vo.CarryTaskStatusVO;
import com.miyu.module.wms.convert.carrytask.CarryTaskConvert;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskFactory;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.core.util.SendUtils;
import com.miyu.module.wms.dal.dataobject.agv.AGVDO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.dal.dataobject.instruction.InstructionDO;
import com.miyu.module.wms.dal.dataobject.materialstock.MaterialStockDO;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.dal.dataobject.warehouse.WarehouseDO;
import com.miyu.module.wms.enums.DictConstants;
import com.miyu.module.wms.service.agv.AGVService;
import com.miyu.module.wms.service.carrytask.CarryTaskService;
import com.miyu.module.wms.service.instruction.InstructionService;
import com.miyu.module.wms.service.materialstock.MaterialStockService;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;
import com.miyu.module.wms.service.stockactive.StockActiveService;
import com.miyu.module.wms.service.warehouse.WarehouseService;
import com.miyu.module.wms.service.warehouselocation.WarehouseLocationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.MATERIAL_STOCK_UPDATE_LOCATION_ERROR;

/**
 * 调用AGV调度接口Service
 */
@Component
@Slf4j
@Transactional
public class DispatchAGVService {

    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @Value("${agv.server.host}")
    private String agvServerHost;
    @Resource
    @Lazy
    private CarryTaskService carryTaskService;
    @Resource
    private InstructionService instructionService;
    @Resource
    private MaterialStockService materialStockService;
    @Resource
    private MaterialStorageService materialStorageService;
    @Resource
    private AGVService agvService;
    @Resource
    private DispatchCarryTaskFactory dispatchCarryTaskFactory;
    @Resource
    private WarehouseService warehouseService;

    /**
     * 1.AGV调度接口-创建任务
     * @param carryTaskIssueVO
     * @return
     */
    public CarryTaskResult<?> agvCallbackServiceCreateTask(CarryTaskIssueVO carryTaskIssueVO) {
        JSONObject jsonObject = CarryTaskConvert.INSTANCE.convertIssueVOToJson(carryTaskIssueVO);
        log.info("1.AGV调度接口-创建任务--》{}", jsonObject.toJSONString());
        CarryTaskResult<?> carryTaskResult = CarryTaskResult.success(jsonObject);
//        CarryTaskResult<?> carryTaskResult = sendToAGV(jsonObject, "/agvCallbackService/CreateTask");

        return carryTaskResult;
    }


    /**
     * 2.AGV调度接口-继续任务
     * @param taskNo
     * @param sort
     * @param operationDate
     * @return
     */
    public CarryTaskResult<?> agvCallbackServiceContinueTask(String parentId, String taskNo, Integer sort, LocalDateTime operationDate) {
        CarrySubTaskDO subTask  = carryTaskService.getCarrySubTaskByParentIdAndExecuteOrder(parentId, sort);
        if(subTask == null){
            return CarryTaskResult.error(500,"子任务不存在");
        }
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TaskNo", taskNo);
        jsonObject.put("Sort", String.valueOf(sort));
        jsonObject.put("Operationdate", formatDate(operationDate));
//        CarryTaskResult<?> carryTaskResult = sendToAGV(jsonObject, "/agvCallbackService/ContinueTask");

        // todo: 伪代码 后期删除
        CarryTaskResult<?> carryTaskResult = CarryTaskResult.success(jsonObject);
        if(carryTaskResult.isSuccess()){
            // 更新子任务状态为运行中
            subTask.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING);
            subTask.setStartTime(operationDate);
            carryTaskService.updateCarrySubTask(subTask);
        }else {
            // 更新子任务状态为异常
            subTask.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_EXCEPTION);
            subTask.setTaskDescription(carryTaskResult.getCodeMsg());
            carryTaskService.updateCarrySubTask(subTask);
        }
       return carryTaskResult;
    }

    /**
     * 3.AGV调度接口-查询设备状态
     * @param carNo
     * @return
     */
    public CarryTaskResult<?> agvCallbackServiceQueryCarStatus(String carNo) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("CarNo", carNo);
        CarryTaskResult<?> carryTaskResult =sendToAGV(jsonObject, "/agvCallbackService/QueryCarStatus");
        // 解析返回结果
        if (carryTaskResult.isSuccess() && carryTaskResult.getData() != null && carryTaskResult.getData() instanceof JSONObject) {
            JSONObject data = (JSONObject) carryTaskResult.getData();
            Gson gson = new Gson();
            return carryTaskResult.success(gson.fromJson(data.toJSONString(), CarryTaskStatusVO.class));
        }

        return carryTaskResult;
    }


    /**
     * 4.AGV调度接口-取消任务
     * @param taskCode
     * @return
     */
    public CarryTaskResult<?> agvCallbackServiceCancelTask(String taskCode) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("TaskNo", taskCode);
        return sendToAGV(jsonObject, "/agvCallbackService/CancelTask");
    }

    /**
     * AGV调度接口-任务状态接收接口
     * @param carryTaskReceive
     * @return
     */
    public CarryTaskResult<?> agvCallbackServiceReceiveTask(@RequestBody CarryTaskReceiveVO carryTaskReceive) {
        String taskNo = carryTaskReceive.getTaskNo();
        String carNo = carryTaskReceive.getCarNo();
        String sort = carryTaskReceive.getSort();
        String status = carryTaskReceive.getStatus();
        String statusTime = carryTaskReceive.getStatusTime();
        log.info("任务状态接收接口：{}", carryTaskReceive);

        CarryTaskDO carryTask = carryTaskService.getCarryTaskByTaskCode(taskNo);
        if(carryTask == null){
            log.error("任务不存在");
            return CarryTaskResult.error(500,"任务不存在");
        }

        DispatchCarryTaskLogicService dispatchService = dispatchCarryTaskFactory.getDispatchService(carryTask.getTaskType());

        if(StringUtils.isNotBlank(carNo) && StringUtils.isBlank(carryTask.getAgvId())){
            AGVDO agv = agvService.getAGVByCarNo(carNo);
            carryTask.setAgvId(agv.getId());
            carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_RUNNING);
            carryTask.setStartTime(parseDate(statusTime));
            carryTaskService.updateCarryTask(carryTask);

            // 更新任务单状态 待送达
            dispatchService.updateOrderTask(carryTask.getReflectStockId(), DictConstants.WMS_ORDER_DETAIL_STATUS_2);
        }

        // 如果当前子任务不是已完成状态，则更新为运行中
//        if(StringUtils.isNotBlank(sort)
//               && !DictConstants.WMS_CARRY_TASK_CALL_BACK_STATUS_FINISH.equals(status)){
//            CarrySubTaskDO subTask = carryTaskService.getCarrySubTaskByParentIdAndExecuteOrder(carryTask.getId(), Integer.parseInt(sort));
//            if(subTask != null){
//                // 更新子任务状态运行中
//                subTask.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING);
//                subTask.setStartTime(parseDate(statusTime));
//                carryTaskService.updateCarrySubTask(subTask);
//                return CarryTaskResult.success(carryTask);
//            }
//        }

        // 如果当前子任务是 已完成状态，则更新子任务状态
        if(StringUtils.isNotBlank(sort)
                && DictConstants.WMS_CARRY_TASK_CALL_BACK_STATUS_FINISH.equals(status)){
            CarrySubTaskDO subTask = carryTaskService.getCarrySubTaskByParentIdAndExecuteOrder(carryTask.getId(), Integer.parseInt(sort));
            if(subTask == null){
                log.error("子任务不存在");
                return CarryTaskResult.error(500,"子任务不存在");
            }

            // 如果当前子任务不是运行中状态，则不更新 ? 理论上不会有此情况
            if(!DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING.equals(subTask.getTaskStatus())){
                log.error("子任务状态不是运行中状态");
                return CarryTaskResult.error(500,"子任务状态不是运行中状态");
            }
            // 更新子任务状态已完成
            subTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_CALL_BACK_STATUS_FINISHED);
            subTask.setEndTime(parseDate(statusTime));
            carryTaskService.updateCarrySubTask(subTask);

            CarrySubTaskDO nextSubTask = carryTaskService.getCarrySubTaskByParentIdAndExecuteOrder(carryTask.getId(), Integer.parseInt(sort)+1);

            // 特殊处理 对于特殊标记的子任务--即目标库位不能承载托盘的子任务 需要自动签收，并且托盘回库，但是其实搬运任务已经完成了。
            if(nextSubTask != null && nextSubTask.getSpecialMark() ){
                dispatchService.updateOrderTask(subTask.getMaterialStockId(), DictConstants.WMS_ORDER_DETAIL_STATUS_4);
            }

            // 更新物料库位
            if(Objects.equals(subTask.getInsType(), DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_TAKE)){
                AGVDO agv = agvService.getAGV(carryTask.getAgvId());
                String storageId = materialStorageService.getStorageIdByLocationId(agv.getLocationId());
                if(storageId != null){
                    if (!materialStockService.updateMaterialStorage(subTask.getMaterialStockId(), storageId)) {
                        // 物料库位更新失败
                        throw exception(MATERIAL_STOCK_UPDATE_LOCATION_ERROR);
                    }
                }else {
                    if (!materialStockService.updateMaterialStock(subTask.getMaterialStockId(), agv.getLocationId())) {
                        // 物料库位更新失败
                        throw exception(MATERIAL_STOCK_UPDATE_LOCATION_ERROR);
                    }
                }
            }else if(subTask.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_PUT){
                if (!materialStockService.updateMaterialStock(subTask.getMaterialStockId(), subTask.getLocationId())) {
                    // 物料库位更新失败
                    throw exception(MATERIAL_STOCK_UPDATE_LOCATION_ERROR);
                }
            }
        }
        executeNextSubTask();
        return CarryTaskResult.success(carryTask);
    }

    // 执行一下一个子任务  todo: 定时器调用
    public void executeNextSubTask(){
        // 获取未完成的主任务
        List<CarryTaskDO> unfinishedCarryTask = carryTaskService.getUnfinishedCarryTask();
        for (CarryTaskDO carryTask : unfinishedCarryTask) {
            DispatchCarryTaskLogicService dispatchService = dispatchCarryTaskFactory.getDispatchService(carryTask.getTaskType());
            List<CarrySubTaskDO> carrySubTaskList = carryTaskService.getCarrySubTaskListByParentId(carryTask.getId());
            // 根据执行顺序排序
            carrySubTaskList = carrySubTaskList.stream().sorted(Comparator.comparing(CarrySubTaskDO::getExecuteOrder)).collect(Collectors.toList());
            LocalDateTime now = LocalDateTime.now();
            int i;
            for (i = 0; i < carrySubTaskList.size(); i++) {
                // 如果当前子任务是运行中状态，则不更新
                if(DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING.equals(carrySubTaskList.get(i).getTaskStatus())){
                    break;
                }
                // 如果当前子任务是 未开始状态，则更新子任务状态
                if(DictConstants.WMS_CARRY_SUB_TASK_STATUS_NOT_START.equals(carrySubTaskList.get(i).getTaskStatus())){

                    CarrySubTaskDO currSubTask = carrySubTaskList.get(i);

                    // 如果当前子任务不是上下架 类任务，则更新下一个子任务状态为运行中
                    if(!DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_ON.equals(currSubTask.getInsType())
                            && !DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_OFF.equals(currSubTask.getInsType())){
                        // 如果当前任务是 需要触发  则触发接口
                        if(DictConstants.WMS_CARRY_TASK_TRIGEVENT_YES.equals(currSubTask.getTrigEvent())){
                            this.agvCallbackServiceContinueTask(carryTask.getId(), carryTask.getTaskCode(), currSubTask.getExecuteOrder(), now);
                        }else {
                            currSubTask.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING);
                            currSubTask.setStartTime(now);
                            carryTaskService.updateCarrySubTask(currSubTask);
                        }
                    }else if(i > 0){
                        // 如果当前子任务是上下架类任务，则下发任务
                        CarrySubTaskDO subTask = carrySubTaskList.get(i - 1);
                        CommonResult<?> commonResult = instructionService.createCarrySubTaskInstruction(subTask,currSubTask);
                        if(commonResult.isSuccess()){
                            InstructionDO instruction = (InstructionDO)commonResult.getData();
                            // 更新子任务状态为运行中
                            currSubTask.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_RUNNING);
                            currSubTask.setStartTime(now);
                            currSubTask.setInstructionId(instruction.getId());
                            carryTaskService.updateCarrySubTask(currSubTask);

                            // 如果此任务是入库搬运 并且 当前子任务是上架
                            if(Objects.equals(carryTask.getTaskType(), DictConstants.WMS_CARRY_TASK_TYPE_IN)
                                    && Objects.equals(currSubTask.getInsType(), DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_ON)){
                                // 并且当前子任务是最后一个任务  就更新任务单状态 为待上架
                                if(i+1 == carrySubTaskList.size()) {
                                    String containerStockId = currSubTask.getMaterialStockId();
                                    dispatchService.updateOrderTask(containerStockId, DictConstants.WMS_ORDER_DETAIL_STATUS_3);
                                }
                            }
                        }else {
                            // 更新子任务状态为异常
                            currSubTask.setTaskStatus(DictConstants.WMS_CARRY_SUB_TASK_STATUS_EXCEPTION);
                            currSubTask.setTaskDescription(commonResult.getMsg());
                            carryTaskService.updateCarrySubTask(currSubTask);
                        }
                    }

                    break;
                }
            }
            // 如果没有 未开始的子任务了
            if(i == carrySubTaskList.size()){
                CarrySubTaskDO subTask = carrySubTaskList.get(carrySubTaskList.size() - 1);
                // 所有子任务已完成，更新任务状态为已完成
                carryTask.setTaskStatus(DictConstants.WMS_CARRY_TASK_STATUS_FINISHED);
                carryTask.setEndTime(now);
                carryTaskService.updateCarryTask(carryTask);

                // 没任务了 就更新任务单状态：已完成
                if(carryTask.getTaskType() == DictConstants.WMS_CARRY_TASK_TYPE_IN){
                    // 如果是入库搬运 更新任务单状态：已完成
                    dispatchService.updateOrderTask(subTask.getMaterialStockId(), DictConstants.WMS_ORDER_DETAIL_STATUS_4);
                }else {
                    // 如果目标位置仓库 为 自动化立体库 则直接更新状态为已完成
                    WarehouseDO warehouseByLocationId = warehouseService.getWarehouseByLocationId(subTask.getLocationId());
                    if(DictConstants.WMS_WAREHOUSE_TYPE_1.equals(warehouseByLocationId.getWarehouseType())){
                        // 更新任务单状态为 已完成
                        dispatchService.updateOrderTask(subTask.getMaterialStockId(), DictConstants.WMS_ORDER_DETAIL_STATUS_4);
                    }else {
                        // 更新任务单状态为 待签收
                        dispatchService.updateOrderTask(subTask.getMaterialStockId(), DictConstants.WMS_ORDER_DETAIL_STATUS_3);
                    }
                }
            }
        }


    }

    /**
     * 发送请求到AGV
     * @param jsonObject
     * @param url
     * @return
     */
    private CarryTaskResult<?> sendToAGV(JSONObject jsonObject, String url) {
        CommonResult<?> commonResult = SendUtils.postRequestSend(agvServerHost + url, jsonObject);
        log.info("请求AGV服务成功,请求地址：{},返回结果：{}", url,commonResult);

        if(commonResult.isSuccess()){
            if(commonResult.getData() instanceof JSONObject) {
                JSONObject restBody = (JSONObject) commonResult.getData();
                Gson gson = new Gson();
                return gson.fromJson(restBody.toJSONString(), CarryTaskResult.class);
            }else {
                return CarryTaskResult.error(500,"返回结果不是JSONObject类型");
            }
        }

        return CarryTaskResult.error(commonResult.getCode(),commonResult.getMsg());
    }

    /**
     * String 转 LocalDateTime
     * @param dateStr
     * @return
     */
    private LocalDateTime parseDate(String dateStr) {
        if(StringUtils.isBlank(dateStr)){
            return LocalDateTime.now();
        }
        try {
            return LocalDateTime.parse(dateStr, DateTimeFormatter.ofPattern(DATE_FORMAT));
        } catch (Exception e) {
            log.error("日期格式转换失败：{}", dateStr, e);
            return LocalDateTime.now();
        }
    }
    /**
     * LocalDateTime 转 String
     * @param date
     * @return
     */
    private String formatDate(LocalDateTime date) {
        if(date == null){
            date = LocalDateTime.now();
        }
        return date.format(DateTimeFormatter.ofPattern(DATE_FORMAT));
    }

}
