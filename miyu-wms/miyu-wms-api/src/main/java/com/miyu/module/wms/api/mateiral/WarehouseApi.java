package com.miyu.module.wms.api.mateiral;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.module.wms.api.mateiral.dto.WarehouseAreaRespDTO;
import com.miyu.module.wms.api.mateiral.dto.WarehouseRespDTO;
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
@Tag(name = "RPC 服务 - 仓库模块")
public interface WarehouseApi {

    String PREFIX = ApiConstants.PREFIX + "/warehouse";

    @GetMapping(PREFIX + "/getWarehouseByCode")
    @Operation(summary = "根据仓库编码获取仓库")
    @Parameter(name = "warehouseCode", description = "仓库编码", required = true)
    CommonResult<List<WarehouseRespDTO>> getWarehouseByCode(@RequestParam("warehouseCode") String warehouseCode);



    @GetMapping(PREFIX + "/getWarehouseAreaByLocationIds")
    @Operation(summary = "库位id集合查询库区集合")
    @Parameter(name = "locationIds", description = "库位数组", example = "1,2", required = true)
    CommonResult<List<WarehouseAreaRespDTO>> getWarehouseAreaByLocationIds(@RequestParam("locationIds") Collection<String> locationIds);
}
