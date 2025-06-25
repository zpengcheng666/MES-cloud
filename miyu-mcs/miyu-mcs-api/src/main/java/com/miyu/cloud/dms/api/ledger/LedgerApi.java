package com.miyu.cloud.dms.api.ledger;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.cloud.dms.api.ledger.dto.LedgerDataResDTO;
import com.miyu.cloud.dms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.security.PermitAll;
import java.util.List;

@FeignClient(name = ApiConstants.NAME)
@Tag(name = "设备台账")
public interface LedgerApi {

    String PREFIX = ApiConstants.PREFIX + "/ledger";

    @GetMapping(PREFIX + "/getNameByDeviceId")
    @Operation(summary = "根据设备台账id获得设备台账信息")
    @Parameter(name = "deviceId", description = "台账id", required = true, example = "1")
    @PermitAll
    CommonResult<LedgerDataResDTO> getNameByDeviceId(@RequestParam("deviceId") String deviceId);

    @GetMapping(PREFIX + "/getLedgerListByDeviceType")
    @Operation(summary = "根据设备类型Id获取设备信息")
    @Parameter(name = "deviceTypeId", description = "设备类型id", required = true, example = "1")
    CommonResult<List<LedgerDataResDTO>> getLedgerListByDeviceType(@RequestParam("deviceTypeId") String deviceTypeId);


}
