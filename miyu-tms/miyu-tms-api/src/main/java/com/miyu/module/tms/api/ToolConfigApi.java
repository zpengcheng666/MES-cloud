package com.miyu.module.tms.api;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.tms.api.dto.ToolConfigSaveReqDTO;
import com.miyu.module.tms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;



@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 创建刀具类型")
@Validated
public interface ToolConfigApi {

    String PREFIX = ApiConstants.PREFIX + "/tool-config";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建刀具类型")
    CommonResult<Boolean> createToolConfig(@Valid @RequestBody ToolConfigSaveReqDTO reqDTO);

}
