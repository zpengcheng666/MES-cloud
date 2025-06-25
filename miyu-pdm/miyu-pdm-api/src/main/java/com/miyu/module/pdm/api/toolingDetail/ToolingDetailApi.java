package com.miyu.module.pdm.api.toolingDetail;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.pdm.api.toolingDetail.dto.ProductDTO;
import com.miyu.module.pdm.api.toolingDetail.dto.ToolingProductDTO;
import com.miyu.module.pdm.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 工装详细设计")
public interface ToolingDetailApi {
    String PREFIX = ApiConstants.PREFIX + "/tooling-detail";

    @PostMapping(PREFIX + "/updateToolingDetailStatus")
    @Operation(summary = "更新工装详情审批状态")
    @Parameter(name = "businessKey", description = "任务id", required = true, example = "1")
    @Parameter(name = "status", description = "审批状态", required = true, example = "1")
    CommonResult<String> updateToolingDetailStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status")  Integer status);

    @GetMapping(PREFIX + "/getToolingList")
    @Operation(summary = "工装列表(供三维使用)")
    CommonResult<List<ToolingProductDTO>> getToolingList();

    @GetMapping(PREFIX+"/getToolingPartInstanceByRootProductId")
    @Operation(summary = "根据工装产品id获取工装结构树")
    @Parameter(name = "rootProductId", description = "产品id", required = true, example = "1")
    CommonResult<List<ProductDTO>> getToolingPartInstanceByRootProductId(@RequestParam("rootProductId") String rootProductId);

}
