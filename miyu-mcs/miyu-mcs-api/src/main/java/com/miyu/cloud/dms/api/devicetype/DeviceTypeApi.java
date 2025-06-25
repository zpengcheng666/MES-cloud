package com.miyu.cloud.dms.api.devicetype;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.enums.ApiConstants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.List;

@FeignClient(name = ApiConstants.NAME) // TODO 芋艿：fallbackFactory =
@Tag(name = "RPC 服务 - 设备类型")
public interface DeviceTypeApi {
    String PREFIX = ApiConstants.PREFIX + "/device-type";

    @PostMapping(PREFIX + "/getEnableList")
    @Operation(summary = "获得所有已启用的设备类型")
    CommonResult<List<DeviceTypeDataRespDTO>> getEnableList(@RequestBody CommonDevice commonDevice);

    @GetMapping(PREFIX + "/getDeviceTypeListByIds")
    @Operation(summary = "根据设备类型id,匹配设备类型列表")
    CommonResult<List<DeviceTypeDataRespDTO>> getDeviceTypeListByIds(@RequestParam("ids") Collection<String> ids);

    @PostMapping(PREFIX + "/getLineTypeList")
    @Operation(summary = "获取所有产线/单机设备类型")
    CommonResult<List<CommonDevice>> getLineTypeList(@RequestBody CommonDevice commonDevice);

    @GetMapping(PREFIX + "/getDeviceTypeListByLineType")
    @Operation(summary = "依据产线/单机id,获得所有设备类型")
    CommonResult<List<DeviceTypeDataRespDTO>> getDeviceTypeListByLineType(@RequestParam("ids") Collection<String> ids);

    @GetMapping(PREFIX + "/getDeviceType")
    @Operation(summary = "获得已启用的设备类型信息")
    CommonResult<List<DeviceTypeDataRespDTO>> getDeviceTypeList();

    @GetMapping(PREFIX + "/getDeviceListByUserId")
    @Operation(summary = "更具用户查询绑定的工位/产线")
    CommonResult<List<CommonDevice>> getDeviceListByUserId(@RequestParam("userId") String userId);
}
