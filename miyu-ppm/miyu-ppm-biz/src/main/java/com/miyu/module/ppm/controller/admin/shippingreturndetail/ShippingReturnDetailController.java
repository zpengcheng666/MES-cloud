package com.miyu.module.ppm.controller.admin.shippingreturndetail;

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

import com.miyu.module.ppm.controller.admin.shippingreturndetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippingreturndetail.ShippingReturnDetailDO;
import com.miyu.module.ppm.service.shippingreturndetail.ShippingReturnDetailService;

@Tag(name = "管理后台 - 采购退货单详情")
@RestController
@RequestMapping("/ppm/shipping-return-detail")
@Validated
public class ShippingReturnDetailController {

    @Resource
    private ShippingReturnDetailService shippingReturnDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建采购退货单详情")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return-detail:create')")
    public CommonResult<String> createShippingReturnDetail(@Valid @RequestBody ShippingReturnDetailSaveReqVO createReqVO) {
        return success(shippingReturnDetailService.createShippingReturnDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新采购退货单详情")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return-detail:update')")
    public CommonResult<Boolean> updateShippingReturnDetail(@Valid @RequestBody ShippingReturnDetailSaveReqVO updateReqVO) {
        shippingReturnDetailService.updateShippingReturnDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除采购退货单详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping-return-detail:delete')")
    public CommonResult<Boolean> deleteShippingReturnDetail(@RequestParam("id") String id) {
        shippingReturnDetailService.deleteShippingReturnDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得采购退货单详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return-detail:query')")
    public CommonResult<ShippingReturnDetailRespVO> getShippingReturnDetail(@RequestParam("id") String id) {
        ShippingReturnDetailDO shippingReturnDetail = shippingReturnDetailService.getShippingReturnDetail(id);
        return success(BeanUtils.toBean(shippingReturnDetail, ShippingReturnDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得采购退货单详情分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return-detail:query')")
    public CommonResult<PageResult<ShippingReturnDetailRespVO>> getShippingReturnDetailPage(@Valid ShippingReturnDetailPageReqVO pageReqVO) {
        PageResult<ShippingReturnDetailDO> pageResult = shippingReturnDetailService.getShippingReturnDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShippingReturnDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出采购退货单详情 Excel")
    @PreAuthorize("@ss.hasPermission('dm:shipping-return-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShippingReturnDetailExcel(@Valid ShippingReturnDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShippingReturnDetailDO> list = shippingReturnDetailService.getShippingReturnDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "采购退货单详情.xls", "数据", ShippingReturnDetailRespVO.class,
                        BeanUtils.toBean(list, ShippingReturnDetailRespVO.class));
    }

}