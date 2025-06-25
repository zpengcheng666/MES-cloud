package com.miyu.cloud.mcs.restServer.api;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.qms.api.inspectionsheetcreatetask.dto.TaskDTO;
import com.miyu.module.qms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Component
@FeignClient(name = ApiConstants.NAME)
public interface InspectionApiMapping {

    String PREFIX = ApiConstants.PREFIX + "/inspectionsheettask";

    @PostMapping(PREFIX + "/create")
    @Operation(summary = "创建检验单任务")
    CommonResult<String> createInspectionSheetTask(@Valid @RequestBody TaskDTO reqDTO, @RequestHeader("Tenant-Id") String tenantId);

}
