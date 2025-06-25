package com.miyu.cloud.dms.controller.admin.calendardevice;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.calendardevice.provo.CalendarProductionlinePageReqVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.provo.CalendarProductionlineRespVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.provo.CalendarProductionlineSaveReqVO;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTimeRespVO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarProductionlineDO;
import com.miyu.cloud.dms.service.calendardevice.CalendarProductionlineService;
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


@Tag(name = "管理后台 - 设备日历关联部分,记录了设备绑定了那些班次")
@RestController
@RequestMapping("/dms/calendar-productionline")
@Validated
public class CalendarProductionlineController {

    @Resource
    private CalendarProductionlineService calendarProductionlineService;

    @PostMapping("/create")
    @Operation(summary = "创建设备日历关联部分,记录了设备绑定了那些班次")
    @PreAuthorize("@ss.hasPermission('pms:calendar-productionline:create')")
    public CommonResult<String> createCalendarProductionline(@Valid @RequestBody CalendarProductionlineSaveReqVO createReqVO) {
        return success(calendarProductionlineService.createCalendarProductionline(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备日历关联部分,记录了设备绑定了那些班次")
    @PreAuthorize("@ss.hasPermission('pms:calendar-productionline:update')")
    public CommonResult<Boolean> updateCalendarProductionline(@Valid @RequestBody CalendarProductionlineSaveReqVO updateReqVO) {
        calendarProductionlineService.updateCalendarProductionline(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备日历关联部分,记录了设备绑定了那些班次")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:calendar-productionline:delete')")
    public CommonResult<Boolean> deleteCalendarProductionline(@RequestParam("id") String id) {
        calendarProductionlineService.deleteCalendarProductionline(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备日历关联部分,记录了设备绑定了那些班次")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:calendar-productionline:query')")
    public CommonResult<CalendarProductionlineRespVO> getCalendarProductionline(@RequestParam("id") String id) {
        CalendarProductionlineDO calendarProductionline = calendarProductionlineService.getCalendarProductionline(id);
        return success(BeanUtils.toBean(calendarProductionline, CalendarProductionlineRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备日历关联部分,记录了设备绑定了那些班次分页")
    @PreAuthorize("@ss.hasPermission('pms:calendar-productionline:query')")
    public CommonResult<PageResult<CalendarProductionlineRespVO>> getCalendarProductionlinePage(@Valid CalendarProductionlinePageReqVO pageReqVO) {
        PageResult<CalendarProductionlineDO> pageResult = calendarProductionlineService.getCalendarProductionlinePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CalendarProductionlineRespVO.class));
    }

    @GetMapping("/getShiftByDeviceId")
    @Operation(summary = "通过绑定设备获得班次时间")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:calendar-productionline:query')")
    public CommonResult<List<ShiftTimeRespVO>> getShiftByDeviceId(@RequestParam("id") String id) {
        List<ShiftTimeRespVO> shiftTimeList = calendarProductionlineService.getShiftByDeviceId(id);
        return success(shiftTimeList);
    }

}
