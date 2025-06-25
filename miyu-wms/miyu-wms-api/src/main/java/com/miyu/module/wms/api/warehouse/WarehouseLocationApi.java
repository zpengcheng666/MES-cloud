package com.miyu.module.wms.api.warehouse;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.wms.api.warehouse.dto.WarehouseLocationRespDTO;
import com.miyu.module.wms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;


@FeignClient(name = ApiConstants.NAME)
@Tag(name = "RPC 服务 - 仓库库位模块")
public interface WarehouseLocationApi {

    String PREFIX = ApiConstants.PREFIX + "/warehouse-location";


    @GetMapping(PREFIX + "/list/getByIds")
    @Operation(summary = "获得库位")
    @Parameter(name = "ids", description = "编号", required = true)
    CommonResult<List<WarehouseLocationRespDTO>>  getWarehouseLocationByIds(@RequestParam("ids") Collection<String> ids);
}
