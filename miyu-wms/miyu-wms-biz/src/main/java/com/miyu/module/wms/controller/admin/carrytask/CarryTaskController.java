package com.miyu.module.wms.controller.admin.carrytask;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.wms.core.carrytask.restservice.DispatchAGVService;
import com.miyu.module.wms.core.carrytask.service.DispatchCarryTaskLogicService;
import com.miyu.module.wms.dal.dataobject.carrytask.CarrySubTaskDO;
import com.miyu.module.wms.enums.DictConstants;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicReference;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static com.miyu.module.wms.enums.ErrorCodeConstants.CARRYING_TASK_NOT_EXISTS;

import com.miyu.module.wms.controller.admin.carrytask.vo.*;
import com.miyu.module.wms.dal.dataobject.carrytask.CarryTaskDO;
import com.miyu.module.wms.service.carrytask.CarryTaskService;

@Tag(name = "管理后台 - 搬运任务")
@RestController
@RequestMapping("/wms/carry-task")
@Validated
public class CarryTaskController {

    @Resource
    private CarryTaskService carryTaskService;
    @Resource
    private DispatchAGVService dispatchAGVService;

    @PostMapping("/create")
    @Operation(summary = "创建搬运任务")
    @PreAuthorize("@ss.hasPermission('wms:carry-task:create')")
    public CommonResult<String> createCarryTask(@Valid @RequestBody CarryTaskSaveReqVO createReqVO) {
        return success(carryTaskService.createCarryTask(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新搬运任务")
    @PreAuthorize("@ss.hasPermission('wms:carry-task:update')")
    public CommonResult<Boolean> updateCarryTask(@Valid @RequestBody CarryTaskSaveReqVO updateReqVO) {
        carryTaskService.updateCarryTask(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除搬运任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:carry-task:delete')")
    public CommonResult<Boolean> deleteCarryTask(@RequestParam("id") String id) {
        carryTaskService.deleteCarryTask(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得搬运任务")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:carry-task:query')")
    public CommonResult<CarryTaskRespVO> getCarryTask(@RequestParam("id") String id) {
        CarryTaskDO carryTask = carryTaskService.getCarryTask(id);
        return success(BeanUtils.toBean(carryTask, CarryTaskRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得搬运任务分页")
    @PreAuthorize("@ss.hasPermission('wms:carry-task:query')")
    public CommonResult<PageResult<CarryTaskRespVO>> getCarryTaskPage(@Valid CarryTaskPageReqVO pageReqVO) {
        PageResult<CarryTaskDO> pageResult = carryTaskService.getCarryTaskPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CarryTaskRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出搬运任务 Excel")
    @PreAuthorize("@ss.hasPermission('wms:carry-task:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCarryTaskExcel(@Valid CarryTaskPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CarryTaskDO> list = carryTaskService.getCarryTaskPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "搬运任务.xls", "数据", CarryTaskRespVO.class,
                        BeanUtils.toBean(list, CarryTaskRespVO.class));
    }

    // ==================== 子表（搬运任务子表） ====================

    @GetMapping("/carry-sub-task/list-by-parent-id")
    @Operation(summary = "获得搬运任务子表列表")
    @Parameter(name = "parentId", description = "搬运任务id")
    @PreAuthorize("@ss.hasPermission('wms:carry-task:query')")
    public CommonResult<List<CarrySubTaskDO>> getCarrySubTaskListByParentId(@RequestParam("parentId") String parentId) {
        return success(carryTaskService.getCarrySubTaskListByParentId(parentId));
    }


    /**
     * 搬运任务下发  todo: 待添加幂等性控制 防止防止重复下发  目前重复调用会抛异常
     * @param carryTaskId
     * @return
     */
    @PostMapping("/dispatch-carry-task")
    @Operation(summary = "搬运任务下发")
    public CommonResult<String> dispatchCarryTask(String carryTaskId) {
        CarryTaskDO carryTask = carryTaskService.getCarryTaskWithSubTask(carryTaskId);
        if(carryTask.getTaskStatus() != DictConstants.WMS_CARRY_TASK_STATUS_NOT_START
                && carryTask.getTaskStatus() != DictConstants.WMS_CARRY_TASK_STATUS_EXCEPTION){
            return CommonResult.success("失败：该任务已下发，请勿重复下发！");
        }
        return carryTaskService.dispatchCarryTask(carryTask);
    }


    /**
     * 搬运任务开始执行反馈    测试代码，模拟调用
     * @param carrySubTaskId
     * @return
     */
    @PostMapping("/end-carry-task")
    @Operation(summary = "搬运任务执行完成反馈")
    public CommonResult<Boolean> startCarryTaskCallback(String carrySubTaskId) {
        CarryTaskDO carryTask = carryTaskService.getCarryTaskBySubTaskId(carrySubTaskId);
//        DispatchCarryTaskLogicService dispatchService = dispatchCarryTaskFactory.getDispatchService(carryTask.getTaskType());

        List<CarrySubTaskDO> carrySubTaskList = carryTask.getCarrySubTask();
        if(CollectionUtils.isAnyEmpty(carrySubTaskList)){
            throw exception(CARRYING_TASK_NOT_EXISTS);
        }

        CarrySubTaskDO subTask = carrySubTaskList.stream().filter(item -> item.getId().equals(carrySubTaskId)).findFirst().orElse(null);

        if(subTask != null && (subTask.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_ON || subTask.getInsType() == DictConstants.WMS_CARRY_TASK_INSTRUCTION_TYPE_OFF)){
            return success(false);
        }

        if(subTask != null){
            CarryTaskReceiveVO carryTaskReceiveVO = new CarryTaskReceiveVO();
            carryTaskReceiveVO.setTaskNo(carryTask.getTaskCode());
            carryTaskReceiveVO.setSort(String.valueOf(subTask.getExecuteOrder()));
            carryTaskReceiveVO.setStatus(DictConstants.WMS_CARRY_TASK_CALL_BACK_STATUS_FINISH);
            // 模拟 调用AGV调度接口-任务状态接收接口
            CarryTaskResult<?> carryTaskResult = dispatchAGVService.agvCallbackServiceReceiveTask(carryTaskReceiveVO);
            if(carryTaskResult.isSuccess()) return success(true);
        }

        return success(false);
    }

    /**
     * AGV调度接口-任务状态接收接口
     * @param carryTaskReceive
     * @return
     */
    @PostMapping("/agvCallbackService/SendTask")
    @Operation(summary = " 任务状态接收接口")
    public CarryTaskResult<?> agvCallbackServiceReceiveTask(@RequestBody CarryTaskReceiveVO carryTaskReceive){
        return dispatchAGVService.agvCallbackServiceReceiveTask(carryTaskReceive);
    }


}
