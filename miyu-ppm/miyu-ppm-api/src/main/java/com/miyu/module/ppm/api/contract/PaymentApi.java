package com.miyu.module.ppm.api.contract;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.ppm.api.contract.dto.ContractPaymentDTO;
import com.miyu.module.ppm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 合同支付信息")
public interface PaymentApi {

    String PREFIX = ApiConstants.PREFIX + "/payment";


    @PostMapping(PREFIX + "/update")
    @Operation(summary = "更新支付状态")
    @Parameters({
            @Parameter(name = "businessKey", description = "流程编号", required = true, example = "1024"),
            @Parameter(name = "status", description = "流程定义标识", required = true, example = "xxx")
    })
    CommonResult<String>  updatePaymentAuditStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status") Integer status);

    /**
     * 通过合同查询付款及其明细
     * @param ids
     * @return
     */
    @GetMapping(PREFIX + "/getContractPaymentByContractIds")
    @Operation(summary = "通过合同查询付款及其明细")
    @Parameter(name = "ids", description = "合同id", required = true, example = "1")
    CommonResult<List<ContractPaymentDTO>> getContractPaymentByContractIds(@RequestParam("ids") Collection<String> ids);

    /**
     * 通过合同查询收货及其明细
     * @param ids
     * @return
     */
    default Map<String, ContractPaymentDTO> getPaymentMapByContractIds(Collection<String> ids) {
        List<ContractPaymentDTO> checkedData = getContractPaymentByContractIds(ids).getCheckedData();
        return CollectionUtils.convertMap(checkedData, ContractPaymentDTO::getContractId);
    }
}
