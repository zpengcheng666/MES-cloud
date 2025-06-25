package com.miyu.cloud.dms.controller.admin.calendarshift;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTypePageReqVO;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTypeRespVO;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTypeSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarProductionlineDO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTimeDO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTypeDO;
import com.miyu.cloud.dms.service.calendardevice.CalendarProductionlineService;
import com.miyu.cloud.dms.service.calendarshift.ShiftTypeService;
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
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.CALENDAR_DEVICE_NOT_EXISTS;

@Tag(name = "管理后台 - 班次类型")
@RestController
@RequestMapping("/dms/shift-type")
@Validated
public class ShiftTypeController {

    @Resource
    private ShiftTypeService shiftTypeService;

    @Resource
    private CalendarProductionlineService calendarProductionlineService;

    @PostMapping("/create")
    @Operation(summary = "创建班次类型")
    @PreAuthorize("@ss.hasPermission('calendar:shift-type:create')")
    public CommonResult<String> createShiftType(@Valid @RequestBody ShiftTypeSaveReqVO createReqVO) {
        return success(shiftTypeService.createShiftType(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新班次类型")
    @PreAuthorize("@ss.hasPermission('calendar:shift-type:update')")
    public CommonResult<Boolean> updateShiftType(@Valid @RequestBody ShiftTypeSaveReqVO updateReqVO) {
        shiftTypeService.updateShiftType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除班次类型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('calendar:shift-type:delete')")
    public CommonResult<Boolean> deleteShiftType(@RequestParam("id") String id) {
        shiftTypeService.deleteShiftType(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得班次类型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('calendar:shift-type:query')")
    public CommonResult<ShiftTypeRespVO> getShiftType(@RequestParam("id") String id) {
        ShiftTypeDO shiftType = shiftTypeService.getShiftType(id);
        return success(BeanUtils.toBean(shiftType, ShiftTypeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得班次类型分页")
    @PreAuthorize("@ss.hasPermission('calendar:shift-type:query')")
    public CommonResult<PageResult<ShiftTypeRespVO>> getShiftTypePage(@Valid ShiftTypePageReqVO pageReqVO) {
        PageResult<ShiftTypeDO> pageResult = shiftTypeService.getShiftTypePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ShiftTypeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出班次类型 Excel")
    @PreAuthorize("@ss.hasPermission('calendar:shift-type:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportShiftTypeExcel(@Valid ShiftTypePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ShiftTypeDO> list = shiftTypeService.getShiftTypePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "班次类型.xls", "数据", ShiftTypeRespVO.class,
                        BeanUtils.toBean(list, ShiftTypeRespVO.class));
    }

    @GetMapping("/pageWithBasic")
    @Operation(summary = "获得班次类型分页")
    @PreAuthorize("@ss.hasPermission('calendar:shift-type:query')")
    public CommonResult<PageResult<ShiftTypeRespVO>> pageWithBasic(@Valid ShiftTypePageReqVO pageReqVO) {
        PageResult<ShiftTypeDO> pageResult = shiftTypeService.selectPageWithBasic(pageReqVO);
        //如果设备绑定了班次,查询班次的时候给班次赋值设备
        List<ShiftTypeDO> list = pageResult.getList();
        String deviceId = pageReqVO.getDeviceId();
        if(deviceId==null){
            System.out.println(deviceId);
            throw exception(CALENDAR_DEVICE_NOT_EXISTS);
        }
        CalendarProductionlineDO productionlineDO = new CalendarProductionlineDO();
        productionlineDO.setDeviceId(deviceId);
        List<CalendarProductionlineDO> calendarProductionlineList = calendarProductionlineService.selectWith(productionlineDO);
        List<String> shiftIds = calendarProductionlineList.stream().map(CalendarProductionlineDO::getShiftId).collect(Collectors.toList());
        for (ShiftTypeDO shiftTypeDO : list) {
            if(shiftIds.contains(shiftTypeDO.getId())){
                shiftTypeDO.setDeviceId(deviceId);
            }
        }

        return success(BeanUtils.toBean(pageResult, ShiftTypeRespVO.class));
    }

    // ==================== 子表（班次时间） ====================

    @GetMapping("/shift-time/list-by-type-id")
    @Operation(summary = "获得班次时间列表")
    @Parameter(name = "typeId", description = "类型id")
    @PreAuthorize("@ss.hasPermission('calendar:shift-type:query')")
    public CommonResult<List<ShiftTimeDO>> getShiftTimeListByTypeId(@RequestParam("typeId") String typeId) {
        return success(shiftTypeService.getShiftTimeListByTypeId(typeId));
    }

}
