package com.miyu.module.ppm.api.consignmentReturn;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.ppm.api.consignmentReturn.dto.ConsignmentReturnDTO;
import com.miyu.module.ppm.api.consignmentReturn.dto.ReturnMaterialDTO;
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

/***
 * 采购退货API
 */
@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 采购退货")
public interface ConsignmentReturnApi {

    String PREFIX = ApiConstants.PREFIX + "/consignment-return";

    @PostMapping(PREFIX + "/updateConsignmentReturnStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "userId", description = "用户编号", required = true, example = "1")
    CommonResult<String> updateConsignmentReturnStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status")  Integer status);


    @GetMapping(PREFIX + "/getConsignmentReturnDetailByContractIds")
    @Operation(summary = "通过合同查询采购退货及其明细")
    @Parameter(name = "ids", description = "合同id", required = true)
    CommonResult<List<ConsignmentReturnDTO>> getConsignmentReturnDetailByContractIds(@RequestParam("ids") Collection<String> ids);

    /**
     * 通过合同查询采购退货及其明细
     * @param ids
     * @return
     */
    default Map<String, ConsignmentReturnDTO> getConsignmentReturnMapByContractIds(Collection<String> ids) {
        List<ConsignmentReturnDTO> checkedData = getConsignmentReturnDetailByContractIds(ids).getCheckedData();
        return CollectionUtils.convertMap(checkedData, ConsignmentReturnDTO::getId);
    }



    @GetMapping(PREFIX + "/getReturnByCodes")
    @Operation(summary = "根据条码查询退货信息")
    @Parameter(name = "barCodes", description = "物料条码", required = true, example = "1")
    CommonResult<List<ReturnMaterialDTO>> getReturnByCodes(@RequestParam("barCodes") Collection<String> barCodes);


}
