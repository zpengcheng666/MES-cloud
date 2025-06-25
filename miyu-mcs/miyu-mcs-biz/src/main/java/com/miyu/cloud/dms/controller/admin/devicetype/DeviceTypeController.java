package com.miyu.cloud.dms.controller.admin.devicetype;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.devicetype.vo.DeviceTypeListVo;
import com.miyu.cloud.dms.controller.admin.devicetype.vo.DeviceTypePageReqVO;
import com.miyu.cloud.dms.controller.admin.devicetype.vo.DeviceTypeRespVO;
import com.miyu.cloud.dms.controller.admin.devicetype.vo.DeviceTypeSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.devicetype.DeviceTypeDO;
import com.miyu.cloud.dms.service.devicetype.DeviceTypeService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 设备/工位类型")
@RestController
@RequestMapping("/dms/device-type")
@Validated
public class DeviceTypeController {

    @Resource
    private DeviceTypeService deviceTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建设备/工位类型")
    @PreAuthorize("@ss.hasPermission('dms:device-type:create')")
    public CommonResult<String> createDeviceType(@Valid @RequestBody DeviceTypeSaveReqVO createReqVO) {
        return success(deviceTypeService.createDeviceType(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备/工位类型")
    @LogRecord(type = "DMS", subType = "device-type", bizNo = "{{#updateReqVO.id}}", success = "{{#updateReqVO.id}}成功更改内容")
    @PreAuthorize("@ss.hasPermission('dms:device-type:update')")
    public CommonResult<Boolean> updateDeviceType(@Valid @RequestBody DeviceTypeSaveReqVO updateReqVO) {
        LogRecordContext.putVariable("newValue", updateReqVO);
        deviceTypeService.updateDeviceType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备/工位类型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:device-type:delete')")
    public CommonResult<Boolean> deleteDeviceType(@RequestParam("id") String id) {
        deviceTypeService.deleteDeviceType(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备/工位类型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:device-type:query')")
    public CommonResult<DeviceTypeRespVO> getDeviceType(@RequestParam("id") String id) {
        DeviceTypeDO deviceType = deviceTypeService.getDeviceType(id);
        return success(BeanUtils.toBean(deviceType, DeviceTypeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备/工位类型分页")
    @PreAuthorize("@ss.hasPermission('dms:device-type:query')")
    public CommonResult<PageResult<DeviceTypeRespVO>> getDeviceTypePage(@Valid DeviceTypePageReqVO pageReqVO) {
        PageResult<DeviceTypeDO> pageResult = deviceTypeService.getDeviceTypePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DeviceTypeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备/工位类型 Excel")
    @PreAuthorize("@ss.hasPermission('dms:device-type:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDeviceTypeExcel(@Valid DeviceTypePageReqVO pageReqVO, HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DeviceTypeDO> list = deviceTypeService.getDeviceTypePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备类型.xls", "数据", DeviceTypeRespVO.class, BeanUtils.toBean(list, DeviceTypeRespVO.class));
    }

    @GetMapping("/getList")
    @Operation(summary = "获得设备列表")
    @Parameter(name = "id", description = "设备/工位", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:device-type:query')")
    public CommonResult<List<DeviceTypeListVo>> getDeviceTypeList(@RequestParam(value = "type", required = false) Integer type) {
        List<DeviceTypeDO> deviceTypeList = deviceTypeService.listDeviceType(type);
        return success(BeanUtils.toBean(deviceTypeList, DeviceTypeListVo.class));
    }

}
