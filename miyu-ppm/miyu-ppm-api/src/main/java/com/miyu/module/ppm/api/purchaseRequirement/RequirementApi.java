package com.miyu.module.ppm.api.purchaseRequirement;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.ppm.api.purchaseRequirement.dto.PurchaseRequirementDTO;
import com.miyu.module.ppm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 采购申请审批")
public interface RequirementApi {

    String PREFIX = ApiConstants.PREFIX + "/requirement";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "保存采购申请")
    CommonResult<Boolean> createPurchaseRequirement(@Valid @RequestBody PurchaseRequirementDTO requirementDTO);


    @PostMapping(PREFIX + "/update")
    @Operation(summary = "更新审批状态")
    @Parameters({
            @Parameter(name = "businessKey", description = "流程编号", required = true, example = "1024"),
            @Parameter(name = "status", description = "流程定义标识", required = true, example = "xxx")
    })
    CommonResult<String>  updateRequirementAuditStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status") Integer status);
}
