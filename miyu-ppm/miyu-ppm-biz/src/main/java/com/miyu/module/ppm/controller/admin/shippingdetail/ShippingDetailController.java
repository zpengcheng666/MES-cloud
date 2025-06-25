package com.miyu.module.ppm.controller.admin.shippingdetail;

import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.PurchaseConsignmentDetailRespVO;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.dal.dataobject.contract.ContractOrderDO;
import com.miyu.module.ppm.dal.dataobject.purchaserequirement.PurchaseRequirementDetailDO;
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

import com.miyu.module.ppm.controller.admin.shippingdetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.service.shippingdetail.ShippingDetailService;

@Tag(name = "管理后台 - 销售发货明细")
@RestController
@RequestMapping("/ppm/shipping-detail")
@Validated
public class ShippingDetailController {

    @Resource
    private ShippingDetailService shippingDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建销售发货明细")
    @PreAuthorize("@ss.hasPermission('dm:shipping-detail:create')")
    public CommonResult<String> createShippingDetail(@Valid @RequestBody ShippingDetailSaveReqVO createReqVO) {
        return success(shippingDetailService.createShippingDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新销售发货明细")
    @PreAuthorize("@ss.hasPermission('dm:shipping-detail:update')")
    public CommonResult<Boolean> updateShippingDetail(@Valid @RequestBody ShippingDetailSaveReqVO updateReqVO) {
        shippingDetailService.updateShippingDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除销售发货明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dm:shipping-detail:delete')")
    public CommonResult<Boolean> deleteShippingDetail(@RequestParam("id") String id) {
        shippingDetailService.deleteShippingDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得销售发货明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dm:shipping-detail:query')")
    public CommonResult<ShippingDetailRespVO> getShippingDetail(@RequestParam("id") String id) {
        ShippingDetailDO shippingDetail = shippingDetailService.getShippingDetail(id);
        return success(BeanUtils.toBean(shippingDetail, ShippingDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得销售发货明细分页")
    @PreAuthorize("@ss.hasPermission('dm:shipping-detail:query')")
    public CommonResult<PageResult<ShippingDetailRespVO>> getShippingDetailPage(@Valid ShippingDetailPageReqVO pageReqVO) {
        PageResult<ShippingDetailDO> pageResult = shippingDetailService.getShippingDetailPage(pageReqVO);


        return success(BeanUtils.toBean(pageResult, ShippingDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出销售发货明细 Excel")
    @PreAuthorize("@ss.hasPermission('dm:shipping-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShippingDetailExcel(@Valid ShippingDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShippingDetailDO> list = shippingDetailService.getShippingDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "销售发货明细.xls", "数据", ShippingDetailRespVO.class,
                        BeanUtils.toBean(list, ShippingDetailRespVO.class));
    }

    @GetMapping("/getShippingDetailByProjectOrderId")
    @Operation(summary = "通过项目订单id获取发货明细")
    public CommonResult<List<ShippingDetailRespVO>> getShippingDetailByProjectOrderId(@RequestParam("id") String id){
        List<ShippingDetailDO> shippingDetailList = shippingDetailService.getShippingDetailByProjectOrderId(id);
        return success(BeanUtils.toBean(shippingDetailList,ShippingDetailRespVO.class));
    }

    @GetMapping("/getShippingDetailByPurchaseId")
    @Operation(summary = "通过采购订单id获取采购退货明细")
    public CommonResult<List<ShippingDetailRespVO>> getShippingDetailByPurchaseId(@RequestParam("id") String id){
        List<ShippingDetailDO> shippingDetailByPurchaseList = shippingDetailService.getShippingDetailByPurchaseId(id);
        return success(BeanUtils.toBean(shippingDetailByPurchaseList,ShippingDetailRespVO.class));
    }

}
