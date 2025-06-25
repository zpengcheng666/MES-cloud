package com.miyu.module.ppm.api.shippingreturn;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDTO;
import com.miyu.module.ppm.api.shippingreturn.dto.ShippingReturnDetailRetraceDTO;
import com.miyu.module.ppm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 销售退货")
public interface ShippingReturnApi {


    String PREFIX = ApiConstants.PREFIX + "/shipping-return";


    @PostMapping(PREFIX + "/updateShippingReturnStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "userId", description = "用户编号", required = true, example = "1")
    CommonResult<String> updateShippingReturnStatus(@RequestParam("businessKey") String businessKey,@RequestParam("status")  Integer status);

    @GetMapping(PREFIX + "/getShippingReturnListByContractIds")
    @Operation(summary = "通过合同查询退货及明细")
    @Parameter(name = "ids", description = "合同id", required = true)
    CommonResult<List<ShippingReturnDTO>> getShippingReturnListByContractIds(@RequestParam("ids") Collection<String> ids);

    /**
     * 通过合同查询退货及明细
     * @param ids
     * @return
     */
    default Map<String,ShippingReturnDTO> getShippingReturnMapByContractIds(@RequestParam("ids") Collection<String> ids){
        List<ShippingReturnDTO> checkedData = getShippingReturnListByContractIds(ids).getCheckedData();
        return CollectionUtils.convertMap(checkedData, ShippingReturnDTO::getId);
    }



    /***
     * 根据产品码获取销售记录
     */
    @GetMapping(PREFIX + "/getShippingReturnListByBarcode")
    @Operation(summary = "根据产品码获取销售退货记录")
    @Parameter(name = "barCode", description = "物料条码", required = true)
    CommonResult<List<ShippingReturnDetailRetraceDTO>> getShippingReturnListByBarcode(@RequestParam("barCode") String barCode);
}
