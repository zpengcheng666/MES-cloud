package com.miyu.cloud.mcs.controller.admin.batchorderdemand;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.web.core.util.WebFrameworkUtils;
import cn.iocoder.yudao.module.system.api.user.AdminUserApi;
import cn.iocoder.yudao.module.system.api.user.dto.AdminUserRespDTO;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.mcs.dal.dataobject.batchdemandrecord.BatchDemandRecordDO;
import com.miyu.cloud.mcs.dto.resource.McsCuttingToolDemandDTO;
import com.miyu.cloud.mcs.dto.resource.McsCuttingToolDemandUptateDTO;
import com.miyu.cloud.mcs.restServer.service.warehouse.WarehouseRestService;
import com.miyu.cloud.mcs.service.batchdemandrecord.BatchDemandRecordService;
import com.miyu.cloud.mcs.service.distributionapplication.DistributionApplicationService;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockLocationTypeDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
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
import java.time.LocalDateTime;
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
import static com.miyu.cloud.mcs.enums.DictConstants.MCS_READY_STATUS_NOT_FULLY_PREPARED;

import com.miyu.cloud.mcs.controller.admin.batchorderdemand.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchorderdemand.BatchOrderDemandDO;
import com.miyu.cloud.mcs.service.batchorderdemand.BatchOrderDemandService;

@Tag(name = "管理后台 - 批次订单需求")
@RestController
@RequestMapping("/mcs/batch-order-demand")
@Validated
public class BatchOrderDemandController {

    @Resource
    private BatchOrderDemandService batchOrderDemandService;
    @Resource
    private BatchDemandRecordService batchDemandRecordService;

    @Resource
    private AdminUserApi adminUserApi;
    @Resource
    private WarehouseRestService warehouseRestService;
    @Resource
    private LedgerService ledgerService;

    @PostMapping("/create")
    @Operation(summary = "创建批次订单需求")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-demand:create')")
    public CommonResult<String> createBatchOrderDemand(@Valid @RequestBody BatchOrderDemandSaveReqVO createReqVO) {
        return success(batchOrderDemandService.createBatchOrderDemand(createReqVO));
    }

    @PostMapping("/createExtraDemand")
    @Operation(summary = "创建批次订单需求")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-demand:create')")
    public CommonResult<String> createExtraDemand(@Valid @RequestBody BatchOrderDemandSaveReqVO createReqVO) {
        return success(batchOrderDemandService.createExtraDemand(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新批次订单需求")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-demand:update')")
    public CommonResult<Boolean> updateBatchOrderDemand(@Valid @RequestBody BatchOrderDemandSaveReqVO updateReqVO) {
        batchOrderDemandService.updateBatchOrderDemand(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除批次订单需求")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-demand:delete')")
    public CommonResult<Boolean> deleteBatchOrderDemand(@RequestParam("id") String id) {
        batchOrderDemandService.deleteBatchOrderDemand(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得批次订单需求")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-demand:query')")
    public CommonResult<BatchOrderDemandRespVO> getBatchOrderDemand(@RequestParam("id") String id) {
        BatchOrderDemandDO batchOrderDemand = batchOrderDemandService.getBatchOrderDemand(id);
        return success(BeanUtils.toBean(batchOrderDemand, BatchOrderDemandRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得批次订单需求分页")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-demand:query')")
    public CommonResult<PageResult<BatchOrderDemandRespVO>> getBatchOrderDemandPage(@Valid BatchOrderDemandPageReqVO pageReqVO) {
        PageResult<BatchOrderDemandDO> pageResult = batchOrderDemandService.getBatchOrderDemandPage(pageReqVO);
        PageResult<BatchOrderDemandRespVO> data = BeanUtils.toBean(pageResult, BatchOrderDemandRespVO.class);

        Set<Long> userIds = pageResult.getList().stream().filter(item -> item.getConfirmedBy() != null).map(item -> Long.parseLong(item.getConfirmedBy())).collect(Collectors.toSet());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds).getData();
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(item -> item.getId() + "", AdminUserRespDTO::getNickname, (a, b) -> b));

        for (BatchOrderDemandRespVO batchOrderDemand : data.getList()) {
            batchOrderDemand.setConfirmedByName(userMap.get(batchOrderDemand.getConfirmedBy()));
        }
        return success(data);
    }

    @GetMapping("/getUnitList")
    @Operation(summary = "需求新增-查询可用产线")
    public CommonResult<List<LineStationGroupDO>> getUnitList() {
        return success(batchOrderDemandService.getAllProcessingUnit());
    }

    @GetMapping("/getListByOrderId")
    @Operation(summary = "根据订单查询需求")
    public CommonResult<List<BatchOrderDemandRespVO>> getListByOrderId(@RequestParam("orderId") String orderId) {
        List<BatchOrderDemandDO> list = batchOrderDemandService.getListByOrderId(orderId);
        List<BatchOrderDemandRespVO> result = BeanUtils.toBean(list, BatchOrderDemandRespVO.class);

        Set<Long> userIds = result.stream().filter(item -> item.getConfirmedBy() != null).map(item -> Long.parseLong(item.getConfirmedBy())).collect(Collectors.toSet());
        List<AdminUserRespDTO> userList = adminUserApi.getUserList(userIds).getData();
        Map<String, String> userMap = userList.stream().collect(Collectors.toMap(item -> item.getId() + "", AdminUserRespDTO::getNickname, (a, b) -> b));
        for (BatchOrderDemandRespVO batchOrderDemand : result) {
            batchOrderDemand.setConfirmedByName(userMap.get(batchOrderDemand.getConfirmedBy()));
        }
        return success(result);
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出批次订单需求 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-demand:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchOrderDemandExcel(@Valid BatchOrderDemandPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchOrderDemandDO> list = batchOrderDemandService.getBatchOrderDemandPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "批次订单需求.xls", "数据", BatchOrderDemandRespVO.class,
                        BeanUtils.toBean(list, BatchOrderDemandRespVO.class));
    }

    /**
     * 齐备确认
     */
    @PostMapping("/completeConfirmation")
    @Operation(summary = "齐备批次订单需求")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order-demand:update')")
    public CommonResult<Boolean> completeConfirmation(String id) {
        BatchOrderDemandDO batchOrderDemand = batchOrderDemandService.getBatchOrderDemand(id);
        Long loginUserId = WebFrameworkUtils.getLoginUserId();
        if (loginUserId != null) {
            batchOrderDemand.setConfirmedBy(loginUserId + "");
        }
        batchOrderDemand.setConfirmedTime(LocalDateTime.now());
        batchOrderDemandService.completeBatchOrderDemand(batchOrderDemand);
        return success(true);
    }

    /**
     * 根据类型获取库存
     */
    @GetMapping("/getMaterialsByConfigIds")
    @Operation(summary = "根据类型获取库存毛料")
    public CommonResult<List<MaterialStockRespDTO>> getMaterialsByConfigIds(@RequestParam("materialConfigId") String id) {
        List<MaterialStockRespDTO> list = warehouseRestService.getOutOrderMaterialsByConfigIds(Collections.singletonList(id));
        List<HashMap> result = BeanUtils.toBean(list, HashMap.class);
        result.forEach(item -> item.put("deliveryRequired", true));
        return CommonResult.success(list);
    }

    @GetMapping("/getToolMaterialsByConfigIds")
    @Operation(summary = "根据类型获取库存工装")
    public CommonResult<Map<String,Object>> getToolMaterialsByConfigIds(@RequestParam("materialConfigId") String materialConfigId, @RequestParam(value = "demandId", required = false) String demandId) {
        Map<String, Integer> locationAndCount = new HashMap<>();
        if (demandId != null) {
            List<BatchDemandRecordDO> list = batchDemandRecordService.list(new LambdaQueryWrapperX<BatchDemandRecordDO>().eq(BatchDemandRecordDO::getDemandId, demandId));
            for (BatchDemandRecordDO demandRecordDO : list) {
                String deviceId = demandRecordDO.getDeviceId();
                String location = ledgerService.getToolLocationByDevice(deviceId);
                if (location == null) continue;
                if (locationAndCount.containsKey(location)) {
                    locationAndCount.put(location, locationAndCount.get(location) + 1);
                } else {
                    locationAndCount.put(location, 1);
                }
            }
        }
        return CommonResult.success(batchOrderDemandService.getOutOrderOtherMaterialsByConfigIds(Collections.singletonList(materialConfigId), locationAndCount));
    }

    @GetMapping("/getCuttingMaterialsByConfigIds")
    @Operation(summary = "根据类型获取库存刀具")
    public CommonResult<Map<String,Object>> getCuttingMaterialsByConfigIds(@RequestParam("materialConfigId") String materialConfigId, @RequestParam(value = "demandId", required = false) String demandId) {
        Map<String,Integer> locationAndCount = new HashMap<>();
        if (demandId != null) {
            List<BatchDemandRecordDO> list = batchDemandRecordService.list(new LambdaQueryWrapperX<BatchDemandRecordDO>().eq(BatchDemandRecordDO::getDemandId, demandId));
            for (BatchDemandRecordDO demandRecordDO : list) {
                String deviceId = demandRecordDO.getDeviceId();
                String location = ledgerService.getCuttingLocationByDevice(deviceId);
                if (location == null) continue;
                if (locationAndCount.containsKey(location)) {
                    locationAndCount.put(location, locationAndCount.get(location) + 1);
                } else {
                    locationAndCount.put(location, 1);
                }
            }
        }
        return CommonResult.success(batchOrderDemandService.getOutOrderOtherMaterialsByConfigIds(Collections.singletonList(materialConfigId), locationAndCount));
    }

    /**
     * 分拣结果保存
     */
    @PostMapping("/resourceSortingSave")
    @Operation(summary = "分拣结果保存")
    public CommonResult<?> resourceSortingSave(@RequestBody BatchOrderDemandSaveReqVO createReqVO) {
        try {
            batchOrderDemandService.resourceSortingSave(createReqVO);
            return CommonResult.success("操作成功");
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
    }

    @GetMapping("/getCuttingToolDemandPage")
    @PreAuthorize("@ss.hasPermission('tms:toolneed-task:query')")
    @Operation(summary = "刀具需求查询")
    public CommonResult<PageResult<McsCuttingToolDemandDTO>> getCuttingToolDemandPage(PageParam pageReqVO) {
        try {
            PageResult<McsCuttingToolDemandDTO> data = batchOrderDemandService.getCuttingToolDemandPage(pageReqVO);
            return CommonResult.success(data);
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
    }

    @PostMapping("/updateCuttingToolDemand")
    @Operation(summary = "刀具需求条码更新")
    public CommonResult<?> updateCuttingToolDemand(@RequestBody McsCuttingToolDemandUptateDTO cuttingTool) {
        try {
            batchOrderDemandService.updateCuttingToolDemand(cuttingTool);
            return CommonResult.success("操作成功");
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
    }

    @PostMapping("/updateOrderFormDemand")
    @Operation(summary = "重新生成订单需求")
    public CommonResult<?> updateOrderFormDemand(@RequestBody List<String> orderIdList) {
        try {
            batchOrderDemandService.updateOrderFormDemand(orderIdList);
            return CommonResult.success("操作成功");
        } catch (ServiceException e) {
            return CommonResult.error(e);
        }
    }

}
