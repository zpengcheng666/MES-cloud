package com.miyu.module.dc.controller.admin.device;


import com.miyu.cloud.dms.api.devicetype.DeviceTypeApi;
import com.miyu.cloud.dms.api.devicetype.dto.DeviceTypeDataRespDTO;
import com.miyu.cloud.dms.api.ledger.dto.LedgerDataResDTO;
import com.miyu.module.dc.service.devicedate.DeviceDateServiceImpl;
import com.miyu.module.dc.service.producttype.ProductTypeService;
import com.miyu.module.dc.service.producttype.ProductTypeServiceImpl;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.*;
import javax.servlet.http.*;
import java.util.*;
import java.io.IOException;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.dc.controller.admin.device.vo.*;
import com.miyu.module.dc.dal.dataobject.device.DeviceDO;
import com.miyu.module.dc.service.device.DeviceService;

@Tag(name = "管理后台 - 设备")
@RestController
@RequestMapping("/dc/device")
@Validated
public class DeviceController {

    @Resource
    private DeviceService deviceService;

    @Resource
    private DeviceTypeApi deviceTypeApi;

    @PostMapping("/create")
    @Operation(summary = "创建设备")
    @PreAuthorize("@ss.hasPermission('dc:device:create')")
    public CommonResult<Boolean> createDevice(@Valid @RequestBody DeviceSaveReqVO createReqVO) {
        deviceService.createDevice(createReqVO);
        return success(true);
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备")
    @PreAuthorize("@ss.hasPermission('dc:device:update')")
    public CommonResult<Boolean> updateDevice(@Valid @RequestBody DeviceUpdateReqVO updateReqVO) {
        deviceService.updateDevice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dc:device:delete')")
    public CommonResult<Boolean> deleteDevice(@RequestParam("id") String id) {
        deviceService.deleteDevice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dc:device:query')")
    public CommonResult<DeviceRespVO> getDevice(@RequestParam("id") String id) {
        DeviceDO device = deviceService.getDevice(id);
        return success(BeanUtils.toBean(device, DeviceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备分页")
    @PreAuthorize("@ss.hasPermission('dc:device:query')")
    public CommonResult<PageResult<DeviceRespVO>> getDevicePage(@Valid DevicePageReqVO pageReqVO) {
        PageResult<DeviceDO> pageResult = deviceService.getDevicePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DeviceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备 Excel")
    @PreAuthorize("@ss.hasPermission('dc:device:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDeviceExcel(@Valid DevicePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DeviceDO> list = deviceService.getDevicePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备.xls", "数据", DeviceRespVO.class,
                        BeanUtils.toBean(list, DeviceRespVO.class));
    }

    @GetMapping("/getList")
    @Operation(summary = "获取设备信息")
    @PreAuthorize("@ss.hasPermission('dc:device:query')")
    public CommonResult<List<DeviceRespVO>> getList() {
        List<DeviceDO> list = deviceService.getDeviceList();
        return success(BeanUtils.toBean(list, DeviceRespVO.class));
    }

    /**
     *查询设备运行监控分页
     */
    @GetMapping("/getDeviceOfflinePage")
    @Operation(summary = "获取设备监控分页")
    @PreAuthorize("@ss.hasPermission('dc:device-offline:query')")
    public CommonResult<PageResult<DeviceOfflineRespVO>> getDeviceOfflinePage(@Valid DevicePageReqVO pageReqVO){
        PageResult<DeviceOfflineRespVO> pageResult = deviceService.getDeviceOfflinePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult,DeviceOfflineRespVO.class));
    }

    /**
     * 获取所有设备类型
     */
    @GetMapping("/getDeviceTypeList")
    @Operation(summary = "获取启用状态设备分类")
    @PreAuthorize("@ss.hasPermission('dc:device:query')")
    public CommonResult<List<DeviceTypeDataRespDTO>> getDeviceTypeList() {
        return success(deviceTypeApi.getDeviceTypeList().getData());
    }

    /**
     * 获取当前数据采集未存在设备
     */
    @GetMapping("getDeviceListByCollet")
    @Operation(summary = "获取启用状态设备分类")
    @PreAuthorize("@ss.hasPermission('dc:device:query')")
    public CommonResult<List<LedgerDataResDTO>> getDeviceListByCollet(@RequestParam("id") String id) {
        return success(deviceService.getDeviceListByCollet(id));
    }


}