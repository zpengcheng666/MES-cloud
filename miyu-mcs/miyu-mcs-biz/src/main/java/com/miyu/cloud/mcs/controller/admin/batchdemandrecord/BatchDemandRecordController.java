package com.miyu.cloud.mcs.controller.admin.batchdemandrecord;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.dal.dataobject.ledger.LedgerDO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.dms.service.linestationgroup.LineStationGroupService;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mcs.service.batchrecord.BatchRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
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
import static com.miyu.cloud.mcs.enums.DictConstants.MCS_DEMAND_RECORD_STATUS_COMPLETED;

import com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord.BatchDemandRecordDO;
import com.miyu.cloud.mcs.service.batchdemandrecord.BatchDemandRecordService;

@Tag(name = "管理后台 - 需求分拣详情")
@RestController
@RequestMapping("/mcs/batch-demand-record")
@Validated
public class BatchDemandRecordController {

    @Resource
    private BatchDemandRecordService batchDemandRecordService;
    @Resource
    private LineStationGroupService lineStationGroupService;
    @Resource
    private LedgerService ledgerService;
    @Resource
    private BatchRecordService batchRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建需求分拣详情")
    @PreAuthorize("@ss.hasPermission('mcs:batch-demand-record:create')")
    public CommonResult<String> createBatchDemandRecord(@Valid @RequestBody BatchDemandRecordSaveReqVO createReqVO) {
        return success(batchDemandRecordService.createBatchDemandRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新需求分拣详情")
    @PreAuthorize("@ss.hasPermission('mcs:batch-demand-record:update')")
    public CommonResult<Boolean> updateBatchDemandRecord(@Valid @RequestBody BatchDemandRecordSaveReqVO updateReqVO) {
        batchDemandRecordService.updateBatchDemandRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除需求分拣详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:batch-demand-record:delete')")
    public CommonResult<Boolean> deleteBatchDemandRecord(@RequestParam("id") String id) {
        batchDemandRecordService.deleteBatchDemandRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得需求分拣详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:batch-demand-record:query')")
    public CommonResult<BatchDemandRecordRespVO> getBatchDemandRecord(@RequestParam("id") String id) {
        BatchDemandRecordDO batchDemandRecord = batchDemandRecordService.getBatchDemandRecord(id);
        return success(BeanUtils.toBean(batchDemandRecord, BatchDemandRecordRespVO.class));
    }

    @PostMapping("/demandRecordRevoke")
    @Operation(summary = "获得需求分拣详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:batch-demand-record:query')")
    public CommonResult<String> demandRecordRevoke(@RequestParam("id") String id) {
        batchDemandRecordService.demandRecordRevoke(id);
        return success("操作成功");
    }

    @GetMapping("/page")
    @Operation(summary = "获得需求分拣详情分页")
    @PreAuthorize("@ss.hasPermission('mcs:batch-demand-record:query')")
    public CommonResult<PageResult<BatchDemandRecordRespVO>> getBatchDemandRecordPage(@Valid BatchDemandRecordPageReqVO pageReqVO) {
        PageResult<BatchDemandRecordDO> pageResult = batchDemandRecordService.getBatchDemandRecordPage(pageReqVO);
        PageResult<BatchDemandRecordRespVO> result = BeanUtils.toBean(pageResult, BatchDemandRecordRespVO.class);
        List<BatchDemandRecordRespVO> list = result.getList();
        List<String> batchRecordIds = new ArrayList<>();
        List<String> unitIds = new ArrayList<>();
        List<String> deviceIds = new ArrayList<>();
        for (BatchDemandRecordRespVO demandRecordDO : list) {
            batchRecordIds.add(demandRecordDO.getBatchRecordId());
            unitIds.add(demandRecordDO.getProcessingUnitId());
            if (demandRecordDO.getDeviceId().contains(",")) {
                deviceIds.addAll(Arrays.asList(demandRecordDO.getDeviceId().split(",")));
            } else {
                deviceIds.add(demandRecordDO.getDeviceId());
            }
        }
        List<BatchRecordDO> recordDOS = batchRecordService.listByIds(batchRecordIds);
        List<LineStationGroupDO> units = lineStationGroupService.getLineStationGroupBatch(unitIds);
        List<LedgerDO> devices = ledgerService.listByIds(deviceIds);
        Map<String, String> recordDOMap = recordDOS.stream().collect(Collectors.toMap(BatchRecordDO::getId, BatchRecordDO::getNumber, (a, b) -> b));
        Map<String, String> unitMap = units.stream().collect(Collectors.toMap(LineStationGroupDO::getId, LineStationGroupDO::getName, (a, b) -> b));
        Map<String, String> deviceMap = devices.stream().collect(Collectors.toMap(LedgerDO::getId, LedgerDO::getName, (a, b) -> b));
        for (BatchDemandRecordRespVO demandRecordDO : list) {
            demandRecordDO.setBatchRecordNumber(recordDOMap.get(demandRecordDO.getBatchRecordId()));
            demandRecordDO.setUnitNumber(unitMap.get(demandRecordDO.getProcessingUnitId()));
            if (demandRecordDO.getDeviceId().contains(",")) {
                StringBuilder deviceName = null;
                for (String deviceId : demandRecordDO.getDeviceId().split(",")) {
                    String deviceN = deviceMap.get(deviceId);
                    if (StringUtils.isBlank(deviceN)) continue;
                    if (deviceName == null) {
                        deviceName = new StringBuilder();
                    } else {
                        deviceName.append(",");
                    }
                    deviceName.append(deviceN);
                }
                if (deviceName != null) {
                    demandRecordDO.setDeviceNumber(deviceName.toString());
                }
            } else {
                demandRecordDO.setDeviceNumber(deviceMap.get(demandRecordDO.getDeviceId()));
            }
        }
        return success(result);
    }

    @GetMapping("/listByDemandId")
    @Operation(summary = "获得需求分拣详情分页")
    @PreAuthorize("@ss.hasPermission('mcs:batch-demand-record:query')")
    public CommonResult<List<BatchDemandRecordRespVO>> listByDemandId(@RequestParam("demandId") String demandId) {
        LambdaQueryWrapper<BatchDemandRecordDO> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(BatchDemandRecordDO::getDemandId, demandId);
        List<BatchDemandRecordDO> list = batchDemandRecordService.list(queryWrapper);
        List<BatchDemandRecordRespVO> result = BeanUtils.toBean(list, BatchDemandRecordRespVO.class);
        List<String> batchRecordIds = new ArrayList<>();
        List<String> unitIds = new ArrayList<>();
        List<String> deviceIds = new ArrayList<>();
        for (BatchDemandRecordRespVO demandRecordDO : result) {
            batchRecordIds.add(demandRecordDO.getBatchRecordId());
            unitIds.add(demandRecordDO.getProcessingUnitId());
            if (demandRecordDO.getDeviceId().contains(",")) {
                deviceIds.addAll(Arrays.asList(demandRecordDO.getDeviceId().split(",")));
            } else {
                deviceIds.add(demandRecordDO.getDeviceId());
            }
        }
        List<BatchRecordDO> recordDOS = batchRecordService.listByIds(batchRecordIds);
        List<LineStationGroupDO> units = lineStationGroupService.getLineStationGroupBatch(unitIds);
        List<LedgerDO> devices = ledgerService.listByIds(deviceIds);
        Map<String, String> recordDOMap = recordDOS.stream().collect(Collectors.toMap(BatchRecordDO::getId, BatchRecordDO::getNumber, (a, b) -> b));
        Map<String, String> unitMap = units.stream().collect(Collectors.toMap(LineStationGroupDO::getId, LineStationGroupDO::getName, (a, b) -> b));
        Map<String, String> deviceMap = devices.stream().collect(Collectors.toMap(LedgerDO::getId, LedgerDO::getName, (a, b) -> b));
        for (BatchDemandRecordRespVO demandRecordDO : result) {
            demandRecordDO.setBatchRecordNumber(recordDOMap.get(demandRecordDO.getBatchRecordId()));
            demandRecordDO.setUnitNumber(unitMap.get(demandRecordDO.getProcessingUnitId()));
            if (demandRecordDO.getDeviceId().contains(",")) {
                StringBuilder deviceName = null;
                for (String deviceId : demandRecordDO.getDeviceId().split(",")) {
                    String deviceN = deviceMap.get(deviceId);
                    if (StringUtils.isBlank(deviceN)) continue;
                    if (deviceName == null) {
                        deviceName = new StringBuilder();
                    } else {
                        deviceName.append(",");
                    }
                    deviceName.append(deviceN);
                }
                if (deviceName != null) {
                    demandRecordDO.setDeviceNumber(deviceName.toString());
                }
            } else {
                demandRecordDO.setDeviceNumber(deviceMap.get(demandRecordDO.getDeviceId()));
            }
        }
        return success(result);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出需求分拣详情 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:batch-demand-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchDemandRecordExcel(@Valid BatchDemandRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchDemandRecordDO> list = batchDemandRecordService.getBatchDemandRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "需求分拣详情.xls", "数据", BatchDemandRecordRespVO.class,
                        BeanUtils.toBean(list, BatchDemandRecordRespVO.class));
    }

    @GetMapping("/getSelectedMaterialIdByDemandId")
    @Operation(summary = "根据需求id,查询已使用的位置")
    public CommonResult<List<BatchDemandRecordDO>> getSelectedMaterialIdByDemandId(@RequestParam("demandId") String demandId) {
        QueryWrapper<BatchDemandRecordDO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("demand_id", demandId);
        List<BatchDemandRecordDO> list = batchDemandRecordService.list(queryWrapper);
        return success(list);
    }
}
