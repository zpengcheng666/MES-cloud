package com.miyu.cloud.mcs.controller.admin.batchrecord;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.controller.admin.ledger.vo.LedgerRespVO;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.service.devicetype.DeviceTypeService;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.dms.service.linestationgroup.LineStationGroupService;
import com.miyu.cloud.mcs.restServer.service.technology.dto.ProcedureRespDTO;
import org.apache.commons.lang3.StringUtils;
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

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.error;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.cloud.mcs.controller.admin.batchrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.service.batchrecord.BatchRecordService;

@Tag(name = "管理后台 - 批次工序任务")
@RestController
@RequestMapping("/mcs/batch-record")
@Validated
public class BatchRecordController {

    @Resource
    private BatchRecordService batchRecordService;
    @Resource
    private LedgerService ledgerService;
    @Resource
    private LineStationGroupService lineStationGroupService;
    @Resource
    private DeviceTypeService deviceTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建批次工序任务")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record:create')")
    public CommonResult<String> createBatchRecord(@Valid @RequestBody BatchRecordSaveReqVO createReqVO) {
        return success(batchRecordService.createBatchRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新批次工序任务")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record:update')")
    public CommonResult<Boolean> updateBatchRecord(@Valid @RequestBody BatchRecordSaveReqVO updateReqVO) {
        batchRecordService.updateBatchRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除批次工序任务")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:batch-record:delete')")
    public CommonResult<Boolean> deleteBatchRecord(@RequestParam("id") String id) {
        batchRecordService.deleteBatchRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得批次工序任务")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record:query')")
    public CommonResult<BatchRecordRespVO> getBatchRecord(@RequestParam("id") String id) {
        BatchRecordDO batchRecord = batchRecordService.getBatchRecord(id);
        return success(BeanUtils.toBean(batchRecord, BatchRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得批次工序任务分页")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record:query')")
    public CommonResult<PageResult<BatchRecordRespVO>> getBatchRecordPage(@Valid BatchRecordPageReqVO pageReqVO) {
        PageResult<BatchRecordDO> pageResult = batchRecordService.getBatchRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BatchRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出批次工序任务 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchRecordExcel(@Valid BatchRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchRecordDO> list = batchRecordService.getBatchRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "批次工序任务.xls", "数据", BatchRecordRespVO.class,
                        BeanUtils.toBean(list, BatchRecordRespVO.class));
    }

    @GetMapping("/listByBatchId")
    @Operation(summary = "根据批次查询任务")
    public CommonResult<List<BatchRecordRespVO>> getListByBatchId(@RequestParam("batchId") String batchId) {
        List<BatchRecordDO> list = batchRecordService.getListByBatchId(batchId);
        if (list.size() == 0) return success(new ArrayList<>());
        Set<String> unitIds = list.stream().map(BatchRecordDO::getProcessingUnitId).collect(Collectors.toSet());
        Set<String> deviceIds = list.stream().map(BatchRecordDO::getDeviceId).filter(Objects::nonNull).flatMap(deviceId -> Arrays.stream(deviceId.split(","))).collect(Collectors.toSet());
        List<BatchRecordRespVO> data = BeanUtils.toBean(list, BatchRecordRespVO.class);
        List<CommonDevice> line = BeanUtils.toBean(lineStationGroupService.getLineStationGroupBatch(unitIds),CommonDevice.class);
        line.addAll(BeanUtils.toBean(deviceTypeService.getDeviceTypeListByIds(unitIds),CommonDevice.class));
        Map<String, String> lineMap = line.stream().collect(Collectors.toMap(CommonDevice::getId, CommonDevice::getName, (a, b) -> b));
        List<LedgerDO> deviceList = deviceIds.size() > 0 ? ledgerService.getLedgerListByIds(deviceIds) : new ArrayList<>();
        Map<String, String> stringMap = deviceList.stream().collect(Collectors.toMap(LedgerDO::getId, LedgerDO::getCode, (a, b) -> b));
        for (BatchRecordRespVO recordRespVO : data) {
            recordRespVO.setUnitName(lineMap.get(recordRespVO.getProcessingUnitId()));
            String deviceIds1 = recordRespVO.getDeviceId();
            if (deviceIds1 != null) {
                String[] deviceId = deviceIds1.split(",");
                Set<String> deviceNameSet = Arrays.stream(deviceId).map(stringMap::get).filter(Objects::nonNull).collect(Collectors.toSet());
                recordRespVO.setDeviceNumber(String.join(",", deviceNameSet));
            }
        }
        return success(data);
    }

    @GetMapping("/list")
    @Operation(summary = "查询集合")
    public CommonResult<List<BatchRecordRespVO>> list(BatchRecordDO batchRecordDO) {
        QueryWrapper<BatchRecordDO> queryWrapper = new QueryWrapper<>(batchRecordDO);
        return success(BeanUtils.toBean(batchRecordService.list(queryWrapper), BatchRecordRespVO.class));
    }

    @GetMapping("/getUnitListByRecordId")
    @Operation(summary = "根据批次任务查询可用产线")
    public CommonResult<List<CommonDevice>> getUnitListByRecordId(@RequestParam("recordId") String recordId) {
        BatchRecordDO batchRecordDO = batchRecordService.getBatchRecord(recordId);
        String[] unitTypeIds = batchRecordDO.getUnitTypeIds().split(",");
        List<CommonDevice> list = deviceTypeService.getUnitListByLineType(Arrays.asList(unitTypeIds));
        return success(list);
    }

    @GetMapping("/getDeviceListByUnitId")
    @Operation(summary = "根据产线查询可用设备")
    public CommonResult<List<LedgerRespVO>> getDeviceListByUnitId(@RequestParam("unitId") String unitId) {
        List<LedgerDO> list = deviceTypeService.getLedgerListByLineList(Collections.singletonList(unitId));
        return success(BeanUtils.toBean(list, LedgerRespVO.class));
    }

    @GetMapping("/getBeforeProcessList")
    @Operation(summary = "根据任务查询工序集合")
    public CommonResult<?> getBeforeProcessListByRecordId(@RequestParam("recordId") String recordId) {
        List<ProcedureRespDTO> list = batchRecordService.getBeforeProcessListByRecordId(recordId);
        return success(list);
    }

    @GetMapping("/getDeviceByOrderId")
    @Operation(summary = "根据订单获取所有可用设备")
    public CommonResult<?> getDeviceByOrderId(@RequestParam("orderIds") String orderIds) {
        try {
            if (StringUtils.isBlank(orderIds)) return success(new ArrayList<>());
            List<String> idList = Arrays.asList(orderIds.split(","));
            return success(batchRecordService.getDeviceByOrderId(idList));
        } catch (ServiceException e) {
            return error(e);
        }
    }
}
