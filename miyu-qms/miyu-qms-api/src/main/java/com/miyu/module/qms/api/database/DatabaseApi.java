package com.miyu.module.qms.api.database;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.qms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 资料库信息")
public interface DatabaseApi {

    String PREFIX = ApiConstants.PREFIX + "/database";

    @PostMapping(PREFIX + "/update")
    @Operation(summary = "更新资料库审批状态")
    @Parameters({
            @Parameter(name = "businessKey", description = "流程编号", required = true, example = "1024"),
            @Parameter(name = "status", description = "流程定义标识", required = true, example = "xxx")
    })
    CommonResult<String>  updateDatabaseAuditStatus(@RequestParam("businessKey") String businessKey, @RequestParam("status") Integer status);
}
