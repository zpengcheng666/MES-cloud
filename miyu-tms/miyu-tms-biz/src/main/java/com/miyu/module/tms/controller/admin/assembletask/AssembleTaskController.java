package com.miyu.module.tms.controller.admin.assembletask;

import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils;
import com.miyu.module.tms.convert.assemblerecord.AssembleRecordConvert;
import com.miyu.module.tms.dal.dataobject.toolinfo.ToolInfoDO;
import com.miyu.module.tms.service.toolinfo.ToolInfoService;
import com.miyu.module.tms.util.CustomPageUtil;
import com.miyu.module.wms.api.mateiral.MaterialConfigApi;
import com.miyu.module.wms.api.mateiral.MaterialStockApi;
import com.miyu.module.wms.api.mateiral.dto.MaterialConfigRespDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.warehouse.WarehouseLocationApi;
import com.miyu.module.wms.api.warehouse.dto.WarehouseLocationRespDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
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
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.tms.controller.admin.assembletask.vo.*;
import com.miyu.module.tms.dal.dataobject.assembletask.AssembleTaskDO;
import com.miyu.module.tms.dal.dataobject.toolinfo.AssembleRecordDO;
import com.miyu.module.tms.service.assembletask.AssembleTaskService;

@Tag(name = "管理后台 - 刀具装配任务")
@RestController
@RequestMapping("/tms/assemble-task")
@Validated
public class AssembleTaskController {

    @Resource
    private AssembleTaskService assembleTaskService;
    @Resource
    private ToolInfoService toolInfoService;
    @Resource
    private MaterialStockApi materialStockApi;
    @Resource
    private MaterialConfigApi materialConfigApi;
    @Resource
    private WarehouseLocationApi warehouseLocationApi;


    @PostMapping("/create")
    @Operation(summary = "创建刀具装配任务")
    @PreAuthorize("@ss.hasPermission('tms:assemble-task:create')")
    public CommonResult<String> createAssembleTask(@Valid @RequestBody AssembleTaskSaveReqVO createReqVO) {
        return success(assembleTaskService.createAssembleTask(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新刀具装配任务")
    @PreAuthorize("@ss.hasPermission('tms:assemble-task:update')")
    public CommonResult<Boolean> updateAssembleTask(@Valid @RequestBody AssembleTaskSaveReqVO updateReqVO) {
        assembleTaskService.updateAssembleTask(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除刀具装配任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:assemble-task:delete')")
    public CommonResult<Boolean> deleteAssembleTask(@RequestParam("id") String id) {
        assembleTaskService.deleteAssembleTask(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得刀具装配任务")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:assemble-task:query')")
    public CommonResult<AssembleTaskRespVO> getAssembleTask(@RequestParam("id") String id) {
        AssembleTaskDO assembleTask = assembleTaskService.getAssembleTask(id);
        return success(BeanUtils.toBean(assembleTask, AssembleTaskRespVO.class));
    }
    @GetMapping("/getListByOrderNumbers")
    @Operation(summary = "获得刀具装配任务")
    @Parameter(name = "orderNumber", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:assemble-task:query')")
    public CommonResult<List<AssembleTaskRespVO>> getAssembleTaskListByOrderNumbers(@RequestParam("orderNumbers") List<String> orderNumbers) {
        List<AssembleTaskDO> assembleTaskS = assembleTaskService.getAssembleTaskListByOrderNumbers(orderNumbers);
        return success(BeanUtils.toBean(assembleTaskS, AssembleTaskRespVO.class));
    }


    @GetMapping("/page")
    @Operation(summary = "获得刀具装配任务分页")
    @PreAuthorize("@ss.hasPermission('tms:assemble-task:query')")
    public CommonResult<PageResult<AssembleTaskRespVO>> getAssembleTaskPage(@Valid AssembleTaskPageReqVO pageReqVO) {
        PageResult<AssembleTaskDO> pageResult = assembleTaskService.getAssembleTaskPage(pageReqVO);
        HashSet<String> materialConfigIds = new HashSet<>();
        HashSet<String> locationIds = new HashSet<>();
        pageResult.getList().forEach(item -> {
            materialConfigIds.add(item.getMaterialConfigId());
            locationIds.add(item.getTargetLocation());
        });
        //获取物料类型信息
        Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(materialConfigIds);
        //获取库位信息
        CommonResult<List<WarehouseLocationRespDTO>> warehouseLocationResult = warehouseLocationApi.getWarehouseLocationByIds(locationIds);
        Map<String, WarehouseLocationRespDTO> warehouseLocationMap;
        if(warehouseLocationResult.isSuccess() && warehouseLocationResult.getData() != null){
            warehouseLocationMap = CollectionUtils.convertMap(warehouseLocationResult.getData(), WarehouseLocationRespDTO::getId);
        } else {
            warehouseLocationMap = null;
        }
        pageResult.getList().forEach(item -> {
            if(materialConfigMap != null && materialConfigMap.containsKey(item.getMaterialConfigId())){
                item.setMaterialNumber(materialConfigMap.get(item.getMaterialConfigId()).getMaterialNumber());
            }
            if(warehouseLocationMap != null && warehouseLocationMap.containsKey(item.getTargetLocation())){
                item.setTargetLocationCode(warehouseLocationMap.get(item.getTargetLocation()).getLocationCode());
            }
        });
        return success(BeanUtils.toBean(pageResult, AssembleTaskRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出刀具装配任务 Excel")
    @PreAuthorize("@ss.hasPermission('tms:assemble-task:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAssembleTaskExcel(@Valid AssembleTaskPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AssembleTaskDO> list = assembleTaskService.getAssembleTaskPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "刀具装配任务.xls", "数据", AssembleTaskRespVO.class,
                        BeanUtils.toBean(list, AssembleTaskRespVO.class));
    }

    // ==================== 子表（刀具装配记录） ====================

    @GetMapping("/assemble-record/list-by-assemble-task-id")
    @Operation(summary = "获得刀具装配记录列表")
    @Parameter(name = "assembleTaskId", description = "刀具装配任务id")
    @PreAuthorize("@ss.hasPermission('tms:assemble-task:query')")
    public CommonResult<List<AssembleRecordRespVO>> getAssembleRecordListByAssembleTaskId(@RequestParam("assembleTaskId") String assembleTaskId) {
        List<AssembleRecordDO> assembleRecordList = toolInfoService.getAssembleRecordListByAssembleTaskId(assembleTaskId);
        HashSet<String> materialConfigIds = new HashSet<>();
        HashSet<String> materialStockIds = new HashSet<>();
        if(assembleRecordList!= null && !assembleRecordList.isEmpty())assembleRecordList.forEach(item -> {
            materialStockIds.add(item.getMaterialStockId());
        });
        //获取物料库存信息
        CommonResult<List<MaterialStockRespDTO>> materialResult = materialStockApi.getMaterialsByIds(materialStockIds);
        Map<String, MaterialStockRespDTO> materialMap;
        if(materialResult.isSuccess() && materialResult.getData() != null){
            materialMap = new HashMap<>();
            materialResult.getData().forEach(item ->{
                materialMap.put(item.getId(), item);
                materialConfigIds.add(item.getMaterialConfigId());
            });
        } else {
            materialMap = null;
        }
        //获取物料类型信息
        Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(materialConfigIds);

        return success(AssembleRecordConvert.INSTANCE.toAssembleRecordRespVO(assembleRecordList, materialMap, materialConfigMap));
    }

    @GetMapping("/assemble-record/page")
    @Operation(summary = "获得刀具装配记录列表分页")
    @PreAuthorize("@ss.hasPermission('tms:assemble-task:query')")
    public CommonResult<PageResult<ToolInfoDO>> getAssembleRecordPage(@Valid AssembleTaskPageReqVO pageReqVO) {
//        if(StringUtils.isBlank(pageReqVO.getCreator())){
//            Long loginUserId = SecurityFrameworkUtils.getLoginUserId();
//            if(loginUserId!= null)pageReqVO.setCreator(loginUserId.toString());
//        }
        PageResult<ToolInfoDO> pageResult = toolInfoService.getAssembleTaskRecordPage(pageReqVO);
//        List<ToolInfoDO> toolInfoDOS = toolInfoService.getAssembleTaskRecordPage2(pageReqVO);
//        PageResult<ToolInfoDO> pageResult = CustomPageUtil.listToPage(pageReqVO, toolInfoDOS);

        HashSet<String> materialConfigIds = new HashSet<>();
        HashSet<String> materialStockIds = new HashSet<>();
        HashSet<String> locationIds = new HashSet<>();
        pageResult.getList().forEach(item -> {
            materialConfigIds.add(item.getMaterialConfigId());
            materialStockIds.add(item.getMaterialStockId());
            locationIds.add(item.getTargetLocation());
        });
        //获取物料类型信息
        Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(materialConfigIds);
        //获取物料库存信息
        CommonResult<List<MaterialStockRespDTO>> materialResult = materialStockApi.getMaterialsAndLocationByIds(materialStockIds);
        Map<String, MaterialStockRespDTO> materialMap;
        if(materialResult.isSuccess() && materialResult.getData() != null){
            materialMap = CollectionUtils.convertMap(materialResult.getData(), MaterialStockRespDTO::getId);
        } else {
            materialMap = null;
        }
        //获取库位信息
        CommonResult<List<WarehouseLocationRespDTO>> warehouseLocationResult = warehouseLocationApi.getWarehouseLocationByIds(locationIds);
        Map<String, WarehouseLocationRespDTO> warehouseLocationMap;
        if(warehouseLocationResult.isSuccess() && warehouseLocationResult.getData() != null){
            warehouseLocationMap = CollectionUtils.convertMap(warehouseLocationResult.getData(), WarehouseLocationRespDTO::getId);
        } else {
            warehouseLocationMap = null;
        }
        pageResult.getList().forEach(item -> {
            if(materialConfigMap != null && materialConfigMap.containsKey(item.getMaterialConfigId())){
                item.setMaterialNumber(materialConfigMap.get(item.getMaterialConfigId()).getMaterialNumber());
            }
            if(materialMap != null && materialMap.containsKey(item.getMaterialStockId())){
                item.setBarCode(materialMap.get(item.getMaterialStockId()).getBarCode());
                item.setStorageCode(materialMap.get(item.getMaterialStockId()).getStorageCode());
                item.setRootLocationCode(materialMap.get(item.getMaterialStockId()).getRootLocationCode());
            }
            if(warehouseLocationMap != null && warehouseLocationMap.containsKey(item.getTargetLocation())){
                item.setTargetLocationCode(warehouseLocationMap.get(item.getTargetLocation()).getLocationCode());
            }
        });
        return success(pageResult);
    }


    // ==================== 刀具配送需求表 ====================
    @GetMapping("/getToolNeedTaskPage")
    @Operation(summary = "获得刀具装配任务分页")
    @PreAuthorize("@ss.hasPermission('tms:toolneed-task:query')")
    public CommonResult<PageResult<AssembleTaskRespVO>> getToolNeedTaskPage(@Valid AssembleTaskPageReqVO pageReqVO) {
        PageResult<AssembleTaskDO> pageResult = assembleTaskService.getAssembleTaskPage(pageReqVO);
        HashSet<String> materialConfigIds = new HashSet<>();
        HashSet<String> locationIds = new HashSet<>();
        pageResult.getList().forEach(item -> {
            materialConfigIds.add(item.getMaterialConfigId());
            locationIds.add(item.getTargetLocation());
        });
        //获取物料类型信息
        Map<String, MaterialConfigRespDTO> materialConfigMap = materialConfigApi.getMaterialConfigMap(materialConfigIds);
        //获取库位信息
        CommonResult<List<WarehouseLocationRespDTO>> warehouseLocationResult = warehouseLocationApi.getWarehouseLocationByIds(locationIds);
        Map<String, WarehouseLocationRespDTO> warehouseLocationMap;
        if(warehouseLocationResult.isSuccess() && warehouseLocationResult.getData() != null){
            warehouseLocationMap = CollectionUtils.convertMap(warehouseLocationResult.getData(), WarehouseLocationRespDTO::getId);
        } else {
            warehouseLocationMap = null;
        }
        pageResult.getList().forEach(item -> {
            if(materialConfigMap != null && materialConfigMap.containsKey(item.getMaterialConfigId())){
                item.setMaterialNumber(materialConfigMap.get(item.getMaterialConfigId()).getMaterialNumber());
            }
            if(warehouseLocationMap != null && warehouseLocationMap.containsKey(item.getTargetLocation())){
                item.setTargetLocationCode(warehouseLocationMap.get(item.getTargetLocation()).getLocationCode());
            }
        });
        return success(BeanUtils.toBean(pageResult, AssembleTaskRespVO.class));
    }

}