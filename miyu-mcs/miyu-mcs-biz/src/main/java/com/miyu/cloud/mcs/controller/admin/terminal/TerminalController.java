package com.miyu.cloud.mcs.controller.admin.terminal;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.dto.manufacture.*;
import com.miyu.cloud.mcs.restServer.api.ManufacturingService;
import com.miyu.module.wms.api.order.dto.OrderReqDTO;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.miyu.cloud.mcs.enums.DictConstants.MCS_DISTRIBUTION_STATUS_REDIS_DEVICE;


@Tag(name = "管理后台 - 生产终端操作")
////@RestController
@RequestMapping("/mcs/terminal")
@Validated
public class TerminalController {

    @Resource
    private ManufacturingService manufacturingService;
    @Resource
    private LedgerService ledgerService;
    @Resource
    private RedisTemplate<String,Object> redisTemplate;

    //根据工位/产线获取当前任务(批次)
    @GetMapping("/getBatchPlan")
    @Parameter(name = "workstationId", description = "工位Id", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:query')")
    public CommonResult<?> getBatchPlanByWorkstation(@RequestParam("workstationId") String workstationId) {
        return manufacturingService.getBatchPlanByWorkstation(workstationId);
    }

    //根据任务(批次)id 查询物料需求
    @GetMapping("/getBatchResourceByRecordId")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:query')")
    public CommonResult<?> getBatchResourceByRecordId(@RequestParam("recordId") String recordId, @RequestParam("workstationId") String workstationId) {
        return manufacturingService.getBatchResourceByRecordId(recordId, workstationId);
    }

    //根据任务(批次)id 查询详细任务(零件)
    @GetMapping(value = "/getBatchDetail")
    @Parameter(name = "recordId", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:query')")
    public CommonResult<?> getBatchDetailByRecordId(@RequestParam("recordId") String recordId) {
        return manufacturingService.getBatchDetailByRecordId(recordId);
    }

    //根据批次任务id 查询工序(包含工步)
    @GetMapping(value = "/getProcess")
    @Parameter(name = "recordId", description = "任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:query')")
    public CommonResult<?> getProcessByRecordId(@RequestParam("recordId") String recordId) {
        return manufacturingService.getProcessByRecordId(recordId);
    }

    //批次 任务开工
    @PostMapping(value = "/batchRecordStart")
    @Parameter(name = "batchRecord", description = "任务开工信息", required = true)
    public CommonResult<?> batchRecordStart(@RequestBody McsBatchRecordEventDTO batchRecord) {
        return manufacturingService.batchRecordStart(batchRecord);
    }

    //批次 任务完工
    @PostMapping(value = "/batchRecordEnd")
    @Parameter(name = "batchRecord", description = "任务完工信息", required = true)
    public CommonResult<?> batchRecordEnd(@RequestBody McsBatchRecordEventDTO batchRecord) {
        return manufacturingService.batchRecordEnd(batchRecord);
    }

    //工位/设备 暂停/恢复
    @PostMapping(value = "/workstationEvent")
    @Parameter(name = "workstationDTO", description = "工位/设备 暂停/恢复信息", required = true)
    public CommonResult<Boolean> stepWorkstationEvent(@RequestBody McsWorkstationDTO workstationDTO) {
        manufacturingService.stepWorkstationEvent(workstationDTO);
        return CommonResult.success(true);
    }

    //根据工位查询是否暂停 暂停true
    @GetMapping(value = "/getWorkstationPause")
    @Parameter(name = "workstationId", description = "工位ID", required = true)
    public CommonResult<?> getWorkstationPause(@RequestParam("workstationId") String workstationId) {
        return manufacturingService.getWorkstationPause(workstationId);
    }

    //入库
    @PostMapping(value = "/putInStorage/{deviceId}")
    public CommonResult<Boolean> putInStorage(@PathVariable("deviceId") String deviceId, @RequestBody List<McsBatchResourceDTO> resourceList) {
        manufacturingService.putInStorage(deviceId, resourceList);
        return CommonResult.success(true);
    }

    //根据物料id集合 查询零件当前任务
    @GetMapping(value = "/getCurrentPlanByMaterialIds")
    @Parameter(name = "materialIds", description = "物料id集合", required = true)
    public CommonResult<Map<String, McsBatchRecordDTO>> getCurrentPlanByMaterialIds(@RequestParam("materialIds") List<String> materialIds) {
        return manufacturingService.getCurrentPlanByMaterialIds(materialIds);
    }

    //检验单生成
    @PostMapping(value = "/createInspectionSheet")
    public CommonResult<?> createInspectionSheet(@RequestBody Map<String, String> data) {
        return manufacturingService.createInspectionSheet(data.get("recordId"), data.get("barCode"), data.get("schemeId"), data.get("userId"));
    }

    //根据用户id查询 绑定位置
    @GetMapping(value = "/getLocationByUser")
    public CommonResult<List<String>> getLocationByUser(@RequestParam("userId") String userId) {
        return manufacturingService.getLocationByUser(userId);
    }

    @GetMapping(value = "/getMaterialCarryReadyStatusMap")
    public CommonResult<Map<String, Boolean>> getMaterialCarryReadyStatusMap(@RequestParam("barCodeList") List<String> barCodeList) {
        return manufacturingService.getMaterialCarryReadyStatusMap(barCodeList);
    }

    @GetMapping(value = "/getDistributionByDeviceId")
    public CommonResult<List<OrderReqDTO>> getDistributionByDeviceId(@RequestParam("deviceId") String deviceId) {
        List<OrderReqDTO> list = manufacturingService.getDistributionByDeviceId(deviceId);
        return CommonResult.success(list);
    }
}

