package com.miyu.module.ppm.api.purchaseConsignment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.ppm.api.purchaseConsignment.dto.*;
import com.miyu.module.ppm.api.shipping.dto.ShippingDTO;
import com.miyu.module.ppm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 采购收货")
public interface PurchaseConsignmentApi {

    String PREFIX = ApiConstants.PREFIX + "/purchaseConsignment";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "userId", description = "用户编号", required = true, example = "1")
    CommonResult<String> updatePurchaseConsignmentStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status")  Integer status);


    @PostMapping(PREFIX + "/InBoundSubmit")
    @Operation(summary = "入库反馈")
    void InBoundSubmit(@RequestParam("id") String id);


    @GetMapping(PREFIX + "/queryConsignmentWarehouseById")
    @Operation(summary = "根据采购单号ID筛选一物一码免检信息")
    CommonResult<ConsignmentWarehouseDTO> queryConsignmentWarehouseById(String id);

    @GetMapping(PREFIX + "/getConsignmentDetailByContractIds")
    @Operation(summary = "通过合同查询收货及其明细")
    @Parameter(name = "ids", description = "合同id", required = true, example = "1")
    CommonResult<List<PurchaseConsignmentDTO>> getConsignmentDetailByContractIds(@RequestParam("ids") Collection<String> ids);


    @PostMapping(PREFIX + "/addWarehouseDetail")
    @Operation(summary = "同步入库明细表至PPM")
    void addWarehouseDetail(@Valid @RequestBody List<WarehouseRespDTO> warehouseRespDTO);

    /**
     * 通过合同查询收货及其明细
     * @param ids
     * @return
     */
    default Map<String, PurchaseConsignmentDTO> getConsignmentMapByContractIds(Collection<String> ids) {
        List<PurchaseConsignmentDTO> checkedData = getConsignmentDetailByContractIds(ids).getCheckedData();
        return CollectionUtils.convertMap(checkedData, PurchaseConsignmentDTO::getId);
    }

    @GetMapping(PREFIX + "/getPurchaseListByProjectIds")
    @Operation(summary = "通过项目查询采购单")
    @Parameter(name = "ids", description = "项目id", required = true)
    CommonResult<List<PurchaseConsignmentDTO>> getPurchaseListByProjectIds(@RequestParam("ids") Collection<String> ids);

    @GetMapping(PREFIX + "/getPurchaseDetailListByProjectIds")
    @Operation(summary = "通过项目查询采购单明细")
    @Parameter(name = "ids", description = "项目id", required = true)
    CommonResult<List<PurchaseConsignmentDetailDTO>> getPurchaseDetailListByProjectIds(@RequestParam("ids") Collection<String> ids);

    @GetMapping(PREFIX + "/getPurchaseDetailListByShippingIds")
    @Operation(summary = "通过发货明细查询收货明细(发货的退货)")
    @Parameter(name = "ids", description = "项目id", required = true)
    CommonResult<List<PurchaseConsignmentDetailDTO>> getPurchaseDetailListByShippingIds(@RequestParam("ids") Collection<String> ids);

    @GetMapping(PREFIX + "/getPurchaseDetailListByContractOrderIds")
    @Operation(summary = "通过合同订单查采购明细(不是项目订单)")
    @Parameter(name = "ids", description = "合同订单id", required = true)
    CommonResult<List<PurchaseConsignmentDetailDTO>> getPurchaseDetailListByContractOrderIds(@RequestParam("ids") Collection<String> ids);

    /**
     * 通过采购计划id查询收货详情
     * 采购计划id是需求中的sourceId,通过这个联系
     * @param ids
     * @return
     */
    @GetMapping(PREFIX + "/getPurchaseDetailListByPurchaseIds")
    @Operation(summary = "通过采购计划id查采购明细")
    @Parameter(name = "ids", description = "采购计划id", required = true)
    CommonResult<List<PurchaseConsignmentDetailDTO>> getPurchaseDetailListByPurchaseIds(@RequestParam("ids") Collection<String> ids);


    //获取待签收收货单
    @GetMapping(PREFIX + "/getSignConsignmentInfo")
    @Operation(summary = "获取待签收的收货单")
    CommonResult<List<ConsignmentInfoDTO>> getSignConsignmentInfo();



    @GetMapping(PREFIX + "/getSignConsignmentDetail")
    @Operation(summary = "获取收货单条码信息")
    CommonResult<List<PurchaseConsignmentDetailDTO>> getSignConsignmentDetail(@RequestParam("consignmentInfoId") String consignmentInfoId);


    @PostMapping(PREFIX + "/signNumber")
    @Operation(summary = "签收数量")
    CommonResult<String> signNumber(@RequestBody ConsignmentSignDTO consignmentSignDTO);

    @PostMapping(PREFIX + "/signMaterial")
    @Operation(summary = "签收条码")
    CommonResult<String> signMaterial(@RequestBody ConsignmentSignDTO consignmentSignDTO);


    @PostMapping(PREFIX + "/returnConsignment")
    @Operation(summary = "退货")
    CommonResult<String> returnConsignment(@RequestBody ConsignmentSignDTO consignmentSignDTO);


    @GetMapping(PREFIX + "/getStockForIn")
    @Operation(summary = "获取待入库的库存信息")
    CommonResult<List<MaterialStockInRespDTO>> getStockForIn(@RequestParam("locationId") String locationId);

}
