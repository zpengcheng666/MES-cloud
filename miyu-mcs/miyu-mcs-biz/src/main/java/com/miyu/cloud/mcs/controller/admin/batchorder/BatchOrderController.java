package com.miyu.cloud.mcs.controller.admin.batchorder;

import cn.iocoder.yudao.framework.common.exception.ServiceException;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.miyu.cloud.dms.dal.dataobject.linestationgroup.LineStationGroupDO;
import com.miyu.cloud.dms.service.linestationgroup.LineStationGroupService;
import com.miyu.cloud.mcs.dal.dataobject.orderform.OrderFormDO;
import com.miyu.cloud.mcs.service.orderform.OrderFormService;
import com.miyu.cloud.mcs.restServer.service.technology.TechnologyRestService;
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

import com.miyu.cloud.mcs.controller.admin.batchorder.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchorder.BatchOrderDO;
import com.miyu.cloud.mcs.service.batchorder.BatchOrderService;

@Tag(name = "管理后台 - 批次级订单")
@RestController
@RequestMapping("/mcs/batch-order")
@Validated
public class BatchOrderController {

    @Resource
    private BatchOrderService batchOrderService;
    @Resource
    private LineStationGroupService lineStationGroupService;
    @Resource
    private OrderFormService orderFormService;
    @Resource
    private TechnologyRestService technologyRestService;

    @PostMapping("/create")
    @Operation(summary = "创建批次级订单")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:create')")
    public CommonResult<String> createBatchOrder(@Valid @RequestBody BatchOrderSaveReqVO createReqVO) {
        return success(batchOrderService.createBatchOrder(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新批次级订单")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:update')")
    public CommonResult<Boolean> updateBatchOrder(@Valid @RequestBody BatchOrderSaveReqVO updateReqVO) {
        batchOrderService.updateBatchOrder(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除批次级订单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:delete')")
    public CommonResult<Boolean> deleteBatchOrder(@RequestParam("id") String id) {
        batchOrderService.deleteBatchOrder(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得批次级订单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:query')")
    public CommonResult<BatchOrderRespVO> getBatchOrder(@RequestParam("id") String id) {
        BatchOrderDO batchOrder = batchOrderService.getBatchOrder(id);
        BatchOrderRespVO data = BeanUtils.toBean(batchOrder, BatchOrderRespVO.class);
        OrderFormDO orderForm = orderFormService.getOrderForm(batchOrder.getOrderId());
        data.setOrderNumber(orderForm.getOrderNumber());
        return success(data);
    }

    @GetMapping("/page")
    @Operation(summary = "获得批次级订单分页")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:query')")
    public CommonResult<PageResult<BatchOrderRespVO>> getBatchOrderPage(@Valid BatchOrderPageReqVO pageReqVO) {
        PageResult<BatchOrderDO> pageResult = batchOrderService.getBatchOrderPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BatchOrderRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出批次级订单 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchOrderExcel(@Valid BatchOrderPageReqVO pageReqVO,
                                      HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchOrderDO> list = batchOrderService.getBatchOrderPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "批次级订单.xls", "数据", BatchOrderRespVO.class,
                BeanUtils.toBean(list, BatchOrderRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得批次级订单")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:query')")
    public CommonResult<List<BatchOrderRespVO>> list(BatchOrderDO batchOrderDO) {
        QueryWrapper<BatchOrderDO> queryWrapper = new QueryWrapper<>(batchOrderDO);
        List<BatchOrderDO> batchOrderList = batchOrderService.list(queryWrapper);
        return success(BeanUtils.toBean(batchOrderList, BatchOrderRespVO.class));
    }

    @PostMapping("/batchSuspend")
    @Operation(summary = "批次订单暂停")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:update')")
    public CommonResult<?> batchSuspend(@RequestParam("id") String id) {
        try {
            batchOrderService.batchSuspend(id);
            return success("订单已暂停, 可修改订单信息");
        } catch (ServiceException e) {
            return error(e);
        }
    }

    @PostMapping("/batchContinue")
    @Operation(summary = "批次订单暂停恢复")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:update')")
    public CommonResult<?> batchContinue(@RequestParam("id") String id) {
        try {
            batchOrderService.batchContinue(id);
            return success("订单已恢复");
        } catch (ServiceException e) {
            return error(e);
        }
    }

    @GetMapping("/listByOrderId")
    @Operation(summary = "获得批次级订单分页")
    @PreAuthorize("@ss.hasPermission('mcs:batch-order:query')")
    public CommonResult<List<BatchOrderRespVO>> getListByOrderId(@RequestParam("orderId") String orderId) {
        List<BatchOrderDO> list = batchOrderService.getBatchOrderListByOrderId(orderId);
        return success(BeanUtils.toBean(list, BatchOrderRespVO.class));
    }

    @PostMapping("/batchIssuance")
    @Operation(summary = "订单下发")
    public CommonResult<?> batchIssuance(@RequestParam("batchId") String batchId) {
        try {
            batchOrderService.batchIssuance(batchId);
            return success("下发成功");
        } catch (ServiceException e) {
            return error(e);
        }
    }

}
