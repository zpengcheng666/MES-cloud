package com.miyu.module.ppm.controller.admin.shippinginstoragedetail;

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

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.ppm.controller.admin.shippinginstoragedetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippinginstoragedetail.ShippingInstorageDetailDO;
import com.miyu.module.ppm.service.shippinginstoragedetail.ShippingInstorageDetailService;

@Tag(name = "管理后台 - 销售订单入库明细")
@RestController
@RequestMapping("/ppm/shipping-instorage-detail")
@Validated
public class ShippingInstorageDetailController {

    @Resource
    private ShippingInstorageDetailService shippingInstorageDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建销售订单入库明细")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-detail:create')")
    public CommonResult<String> createShippingInstorageDetail(@Valid @RequestBody ShippingInstorageDetailSaveReqVO createReqVO) {
        return success(shippingInstorageDetailService.createShippingInstorageDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售订单入库明细")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-detail:update')")
    public CommonResult<Boolean> updateShippingInstorageDetail(@Valid @RequestBody ShippingInstorageDetailSaveReqVO updateReqVO) {
        shippingInstorageDetailService.updateShippingInstorageDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除销售订单入库明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-detail:delete')")
    public CommonResult<Boolean> deleteShippingInstorageDetail(@RequestParam("id") String id) {
        shippingInstorageDetailService.deleteShippingInstorageDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得销售订单入库明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-detail:query')")
    public CommonResult<ShippingInstorageDetailRespVO> getShippingInstorageDetail(@RequestParam("id") String id) {
        ShippingInstorageDetailDO shippingInstorageDetail = shippingInstorageDetailService.getShippingInstorageDetail(id);
        return success(BeanUtils.toBean(shippingInstorageDetail, ShippingInstorageDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售订单入库明细分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-detail:query')")
    public CommonResult<PageResult<ShippingInstorageDetailRespVO>> getShippingInstorageDetailPage(@Valid ShippingInstorageDetailPageReqVO pageReqVO) {
        PageResult<ShippingInstorageDetailDO> pageResult = shippingInstorageDetailService.getShippingInstorageDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShippingInstorageDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出销售订单入库明细 Excel")
    @PreAuthorize("@ss.hasPermission('dm:shipping-instorage-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShippingInstorageDetailExcel(@Valid ShippingInstorageDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShippingInstorageDetailDO> list = shippingInstorageDetailService.getShippingInstorageDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售订单入库明细.xls", "数据", ShippingInstorageDetailRespVO.class,
                        BeanUtils.toBean(list, ShippingInstorageDetailRespVO.class));
    }

}