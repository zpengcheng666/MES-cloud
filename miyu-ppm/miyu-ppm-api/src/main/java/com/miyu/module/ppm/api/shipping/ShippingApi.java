package com.miyu.module.ppm.api.shipping;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.ppm.api.purchaseConsignment.dto.ConsignmentInfoDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.ConsignmentSignDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDetailDTO;
import com.miyu.module.ppm.api.shipping.dto.*;
import com.miyu.module.ppm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Tag(name = "RPC 服务 - 销售发货")
public interface ShippingApi {


    String PREFIX = ApiConstants.PREFIX + "/shipping";


    @PostMapping(PREFIX + "/updateShippingStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "userId", description = "用户编号", required = true, example = "1")
    CommonResult<String> updateShippingStatus(@RequestParam("businessKey") String businessKey,@RequestParam("status")  Integer status);



    @PostMapping(PREFIX + "/outBoundSubmit")
    @Operation(summary = "出库反馈")
    @Parameter(name = "userId", description = "用户编号", required = true, example = "1")
    void outBoundSubmit(@RequestParam("userId")Long userId,@Valid @RequestBody ShippingOutboundReqDTO outboundReqDTO);

    @GetMapping(PREFIX + "/getShippingListByContractIds")
    @Operation(summary = "通过合同查询发货及明细")
    @Parameter(name = "ids", description = "合同id", required = true)
    CommonResult<List<ShippingDTO>> getShippingListByContractIds(@RequestParam("ids") Collection<String> ids);

    /**
     * 通过合同查询发货及明细
     * @param ids
     * @return
     */
    default Map<String,ShippingDTO> getShippingMapByContractIds(@RequestParam("ids") Collection<String> ids){
        List<ShippingDTO> checkedData = getShippingListByContractIds(ids).getCheckedData();
        return CollectionUtils.convertMap(checkedData, ShippingDTO::getId);
    }


    /***
     * 根据产品码获取销售记录
     */
    @GetMapping(PREFIX + "/getShippingListByBarcode")
    @Operation(summary = "根据产品码获取销售发货记录")
    @Parameter(name = "barCode", description = "物料条码", required = true)
    CommonResult<List<ShippingDetailRetraceDTO>> getShippingListByBarcode(@RequestParam("barCode") String barCode);



    @GetMapping(PREFIX + "/getShippingListByProjectIds")
    @Operation(summary = "通过项目id查询发货单")
    @Parameter(name = "ids", description = "项目id", required = true)
    CommonResult<List<ShippingDTO>> getShippingListByProjectIds(@RequestParam("ids") Collection<String> ids);

    @GetMapping(PREFIX + "/getShippingDetailListByProjectIds")
    @Operation(summary = "通过项目id查询发货明细")
    @Parameter(name = "ids", description = "项目id", required = true)
    CommonResult<List<ShippingDetailDTO>> getShippingDetailListByProjectIds(@RequestParam("ids") Collection<String> ids);

    @GetMapping(PREFIX + "/getShippingByConsignmentDetailIds")
    @Operation(summary = "通过收货详细id查发货,这种是收货的退货")
    @Parameter(name = "ids", description = "合同id", required = true)
    CommonResult<List<ShippingDetailDTO>> getShippingByConsignmentDetailIds(@RequestParam("ids") Collection<String> ids);


    //获取待签收收货单
    @GetMapping(PREFIX + "/getSignShippingInfo")
    @Operation(summary = "获取待发货的发货单")
    CommonResult<List<ShippingInfoDTO>> getOutboundingShippingInfo();



    @GetMapping(PREFIX + "/getShippingDetailByShippingInfoId")
    @Operation(summary = "根据发货单信息Id获取发货条码信息")
    CommonResult<List<ShippingDetailDTO>> getShippingDetailByShippingInfoId(@RequestParam("shippingInfoId") String shippingInfoId);



    @PostMapping(PREFIX + "/signMaterial")
    @Operation(summary = "出库签收条码")
    CommonResult<String> signMaterial(@RequestBody ShippingOutDTO shippingOutDTO);



    @PostMapping(PREFIX + "/generatorOutBound")
    @Operation(summary = "生成出库单")
    CommonResult<String> generatorOutBound(@RequestBody ShippingOutDTO shippingOutDTO);


}
