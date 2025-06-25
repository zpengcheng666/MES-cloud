package com.miyu.module.qms.api.inspectionsheetcreatetask;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.qms.api.inspectionsheetcreatetask.dto.TaskDTO;
import com.miyu.module.qms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 检验单任务")
public interface InspectionSheetTaskApi {

    String PREFIX = ApiConstants.PREFIX + "/inspectionsheettask";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建检验单任务")
    CommonResult<String> createInspectionSheetTask(@Valid @RequestBody TaskDTO reqDTO);
}
