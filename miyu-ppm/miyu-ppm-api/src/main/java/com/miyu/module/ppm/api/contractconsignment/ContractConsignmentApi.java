package com.miyu.module.ppm.api.contractconsignment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import com.miyu.module.ppm.api.purchaseConsignment.dto.ConsignmentWarehouseDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.PurchaseConsignmentDTO;
import com.miyu.module.ppm.api.purchaseConsignment.dto.WarehouseRespDTO;
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
@Tag(name = "RPC 服务 - 外协发货")
public interface ContractConsignmentApi {

    String PREFIX = ApiConstants.PREFIX + "/contractConsignment";

    @PostMapping(PREFIX + "/updateContractConsignmentStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "userId", description = "用户编号", required = true, example = "1")
    CommonResult<String> updateContractConsignmentStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status")  Integer status);


}
