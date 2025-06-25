package com.miyu.module.pdm.controller.admin.device;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.dms.api.devicetype.DeviceTypeApi;
import com.miyu.cloud.dms.api.devicetype.dto.CommonDevice;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.module.pdm.controller.admin.device.vo.DeviceListReqVO;
import com.miyu.module.pdm.controller.admin.device.vo.DeviceRespVO;
import com.miyu.module.pdm.dal.dataobject.device.DeviceDO;
import com.miyu.module.pdm.service.device.DeviceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - PDM 设备-临时")
@RestController
@RequestMapping("/pdm/device")
@Validated
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @Resource
    private DeviceTypeApi deviceTypeApi;

    @GetMapping("/getDeviceList1")
    @Operation(summary = "获得设备列表")
    public CommonResult<List<DeviceRespVO>> getDeviceList1(@Valid DeviceListReqVO listReqVO) {
        List<DeviceDO> list = deviceService.getDeviceList(listReqVO);
        return success(BeanUtils.toBean(list, DeviceRespVO.class));
    }

    @PostMapping("/getDeviceList")
    @Operation(summary = "获得设备列表")
    public CommonResult<List<DeviceTypeDataRespDTO>> getDeviceList(@Valid CommonDevice commonDevice) {
        CommonResult<List<DeviceTypeDataRespDTO>> list = deviceTypeApi.getEnableList(commonDevice);
        return list;
    }

    @GetMapping("/getDeviceListByDeviceIds1")
    @Operation(summary = "根据设备id数组获得设备列表")
    public CommonResult<List<DeviceRespVO>> getDeviceListByDeviceIds1(@RequestParam("ids") List<String> deviceIds) {
        List<DeviceDO> list = deviceService.getDeviceListByDeviceIds(deviceIds);
        return success(BeanUtils.toBean(list, DeviceRespVO.class));
    }

    @GetMapping("/getDeviceListByDeviceIds")
    @Operation(summary = "根据设备类型id,匹配设备类型列表")
    public CommonResult<List<DeviceTypeDataRespDTO>> getDeviceListByDeviceIds(@RequestParam("ids") List<String> deviceIds) {
        CommonResult<List<DeviceTypeDataRespDTO>> list = deviceTypeApi.getDeviceTypeListByIds(deviceIds);
        return list;
    }

    @GetMapping("/getProductionLineDeviceList1")
    @Operation(summary = "获得产线或单机设备")
    public CommonResult<List<DeviceRespVO>> getProductionLineDeviceList1(@Valid DeviceListReqVO listReqVO) {
        List<DeviceDO> list = deviceService.getDeviceList(listReqVO);
        return success(BeanUtils.toBean(list, DeviceRespVO.class));
    }

    @PostMapping("/getProductionLineDeviceList")
    @Operation(summary = "获得产线或单机设备")
    public CommonResult<List<CommonDevice>> getProductionLineDeviceList(@Valid CommonDevice commonDevice) {
        CommonResult<List<CommonDevice>> list = deviceTypeApi.getLineTypeList(commonDevice);
        return list;
    }

    @GetMapping("/getDeviceTypeListByLineType")
    @Operation(summary = "依据产线/单机id,获得所有设备类型")
    public CommonResult<List<DeviceTypeDataRespDTO>> getDeviceTypeListByLineType(@RequestParam("ids") List<String> deviceIds) {
        CommonResult<List<DeviceTypeDataRespDTO>> list = deviceTypeApi.getDeviceTypeListByLineType(deviceIds);
        return list;
    }
}
