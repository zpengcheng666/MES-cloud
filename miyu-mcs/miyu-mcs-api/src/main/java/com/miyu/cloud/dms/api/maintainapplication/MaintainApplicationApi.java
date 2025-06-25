package com.miyu.cloud.dms.api.maintainapplication;

import com.miyu.cloud.dms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 设备维修申请")
public interface MaintainApplicationApi {
    String PREFIX = ApiConstants.PREFIX + "/maintain-application";

    public static final String PROCESS_KEY = "dms_maintain_application";

    /**
     * 更新状态
     *
     * @param id     编号
     * @param status 结果
     */
    @PostMapping(PREFIX + "/setStatus")
    @Operation(summary = "更新状态")
    void updateMaintenanceStatus(@RequestParam("id") String id, @RequestParam("status") Integer status);
}
