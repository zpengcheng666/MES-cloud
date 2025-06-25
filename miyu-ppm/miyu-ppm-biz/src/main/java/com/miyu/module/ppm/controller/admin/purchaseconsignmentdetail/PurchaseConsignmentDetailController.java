package com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail;

import com.miyu.module.ppm.controller.admin.shippingdetail.vo.ShippingDetailRespVO;
import com.miyu.module.ppm.dal.dataobject.shippingdetail.ShippingDetailDO;
import com.miyu.module.ppm.enums.purchaseconsignment.ConsignmentTypeEnum;
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

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.ppm.controller.admin.purchaseconsignmentdetail.vo.*;
import com.miyu.module.ppm.dal.dataobject.consignmentdetail.ConsignmentDetailDO;
import com.miyu.module.ppm.service.purchaseconsignmentdetail.PurchaseConsignmentDetailService;

@Tag(name = "管理后台 - 收货明细")
@RestController
@RequestMapping("/ppm/purchase-consignment-detail")
@Validated
public class PurchaseConsignmentDetailController {

    @Resource
    private PurchaseConsignmentDetailService purchaseConsignmentDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建收货明细")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment-detail:create')")
    public CommonResult<String> createPurchaseConsignmentDetail(@Valid @RequestBody PurchaseConsignmentDetailSaveReqVO createReqVO) {
        createReqVO.setConsignmentType(createReqVO.getConsignmentType().intValue()==2? ConsignmentTypeEnum.OUT.getStatus():ConsignmentTypeEnum.PURCHASE.getStatus());
        return success(purchaseConsignmentDetailService.createPurchaseConsignmentDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新收货明细")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment-detail:update')")
    public CommonResult<Boolean> updatePurchaseConsignmentDetail(@Valid @RequestBody PurchaseConsignmentDetailSaveReqVO updateReqVO) {
        updateReqVO.setConsignmentType(updateReqVO.getConsignmentType().intValue()==2? ConsignmentTypeEnum.OUT.getStatus():ConsignmentTypeEnum.PURCHASE.getStatus());

        purchaseConsignmentDetailService.updatePurchaseConsignmentDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除收货明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment-detail:delete')")
    public CommonResult<Boolean> deletePurchaseConsignmentDetail(@RequestParam("id") String id) {
        purchaseConsignmentDetailService.deletePurchaseConsignmentDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得收货明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment-detail:query')")
    public CommonResult<PurchaseConsignmentDetailRespVO> getPurchaseConsignmentDetail(@RequestParam("id") String id) {
        ConsignmentDetailDO purchaseConsignmentDetail = purchaseConsignmentDetailService.getPurchaseConsignmentDetail(id);
        return success(BeanUtils.toBean(purchaseConsignmentDetail, PurchaseConsignmentDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得收货明细分页")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment-detail:query')")
    public CommonResult<PageResult<PurchaseConsignmentDetailRespVO>> getPurchaseConsignmentDetailPage(@Valid PurchaseConsignmentDetailPageReqVO pageReqVO) {
        PageResult<ConsignmentDetailDO> pageResult = purchaseConsignmentDetailService.getPurchaseConsignmentDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, PurchaseConsignmentDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出收货明细 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:purchase-consignment-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportPurchaseConsignmentDetailExcel(@Valid PurchaseConsignmentDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ConsignmentDetailDO> list = purchaseConsignmentDetailService.getPurchaseConsignmentDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "收货明细.xls", "数据", PurchaseConsignmentDetailRespVO.class,
                        BeanUtils.toBean(list, PurchaseConsignmentDetailRespVO.class));
    }

    @GetMapping("/getPurchaseDetailByProjectOrderId")
    @Operation(summary = "通过项目订单id获取收货明细")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getPurchaseDetailByProjectOrderId(@RequestParam("id") String id){
        List<ConsignmentDetailDO> purchaseDetailList = purchaseConsignmentDetailService.getPurchaseDetailByProjectOrderId(id);
        return success(BeanUtils.toBean(purchaseDetailList,PurchaseConsignmentDetailRespVO.class));
    }

    @GetMapping("/getPurchaseDetailByPurchaseId")
    @Operation(summary = "通过采购订单id获取收货明细")
    public CommonResult<List<PurchaseConsignmentDetailRespVO>> getPurchaseDetailByPurchaseId(@RequestParam("id") String id){
        List<ConsignmentDetailDO> purchaseDetailList = purchaseConsignmentDetailService.getPurchaseDetailByPurchaseId(id);
        return success(BeanUtils.toBean(purchaseDetailList,PurchaseConsignmentDetailRespVO.class));
    }

}
