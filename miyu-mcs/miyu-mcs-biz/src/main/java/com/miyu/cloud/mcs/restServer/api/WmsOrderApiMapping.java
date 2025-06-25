package com.miyu.cloud.mcs.restServer.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockLocationTypeDTO;
import com.miyu.module.wms.api.mateiral.dto.MaterialStockRespDTO;
import com.miyu.module.wms.api.order.dto.*;
import com.miyu.module.wms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Component
@FeignClient(name = ApiConstants.NAME)
public interface WmsOrderApiMapping {

    String PREFIX1 = ApiConstants.PREFIX + "/order";

    String PREFIX2 = ApiConstants.PREFIX + "/material-stock";

    String PREFIX3 = "/admin-api/wms";

    @PostMapping(PREFIX1 + "/batch/list")
    CommonResult<List<OrderReqDTO>> orderList(@RequestBody List<OrderReqDTO> orderReqDTOList, @RequestHeader("Tenant-Id") String tenantId);

    @PostMapping(PREFIX1 + "/batch/autoProductionDispatch")
    CommonResult<List<ProductionOrderRespDTO>> autoProductionDispatch(List<ProductionOrderReqDTO> reqDTOList, @RequestHeader("Tenant-Id") String tenantId);

    @GetMapping(PREFIX2 + "/list/getMaterialsByBarCode")
    @Operation(summary = "根据物料编码获取物料库存")
    @Parameter(name = "barCode", description = "物料编码", required = true)
    CommonResult<List<MaterialStockRespDTO>> getMaterialsByBarCode(@RequestParam("barCode") String barCode, @RequestHeader("Tenant-Id") String tenantId);

    @GetMapping(PREFIX2 + "/list/getMaterialAtLocationByBarCode")
    @Operation(summary = "根据物料编码获取物料库存")
    @Parameter(name = "barCode", description = "物料编码", required = true)
    CommonResult<MaterialStockRespDTO> getMaterialAtLocationByBarCode(@RequestParam("barCode") String barCode, @RequestHeader("Tenant-Id") String tenantId);

    @PostMapping(PREFIX1 + "/batch/specifiedStorageSpaceTransportation")
    @Operation(summary = "指定库位间搬运")
    CommonResult<List<SpecifiedTransportationRespDTO>> specifiedStorageSpaceTransportation(@RequestBody List<SpecifiedTransportationReqDTO> specifiedTransportationReqDTOList, @RequestHeader("Tenant-Id") String tenantId);

    @GetMapping(PREFIX1 + "/list/getNotCompleteOrder")
    @Operation(summary = "查询所有未完成的 出库单、移库单")
    CommonResult<List<OrderReqDTO>> getNotCompleteOrder(@RequestHeader("Tenant-Id") String tenantId);

    @PostMapping(PREFIX3 + "/out-warehouse-detail/check-out")
    CommonResult<List<OrderReqDTO>> wmsCheckOut(@RequestBody Map<String,Object> map, @RequestHeader("Tenant-Id") String tenantId);
}
