package com.miyu.module.ppm.api.contractconsignment;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 外协发货")
public interface ContractConsignmentReturnApi {

    String PREFIX = ApiConstants.PREFIX + "/contractConsignmentReturn";

    @PostMapping(PREFIX + "/updateContractConsignmentReturnStatus")
    @Operation(summary = "更新审批状态")
    @Parameter(name = "userId", description = "用户编号", required = true, example = "1")
    CommonResult<String> updateContractConsignmentReturnStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status")  Integer status);


}
