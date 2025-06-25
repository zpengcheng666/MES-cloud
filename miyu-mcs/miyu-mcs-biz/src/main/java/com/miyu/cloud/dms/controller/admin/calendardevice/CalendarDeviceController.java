package com.miyu.cloud.dms.controller.admin.calendardevice;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDevicePageReqVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDeviceReqVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDeviceRespVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDeviceSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarDeviceDO;
import com.miyu.cloud.dms.service.calendardevice.CalendarDeviceService;
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


@Tag(name = "管理后台 - 设备日历,记录设备每天的可用时间")
@RestController
@RequestMapping("/dms/calendar-device")
@Validated
public class CalendarDeviceController {

    @Resource
    private CalendarDeviceService calendarDeviceService;

    @PostMapping("/create")
    @Operation(summary = "创建设备日历,记录设备每天的可用时间")
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:create')")
    public CommonResult<String> createCalendarDevice(@Valid @RequestBody CalendarDeviceSaveReqVO createReqVO) {
        return success(calendarDeviceService.createCalendarDevice(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备日历,记录设备每天的可用时间")
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:update')")
    public CommonResult<Boolean> updateCalendarDevice(@Valid @RequestBody CalendarDeviceSaveReqVO updateReqVO) {
        calendarDeviceService.updateCalendarDevice(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备日历,记录设备每天的可用时间")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:delete')")
    public CommonResult<Boolean> deleteCalendarDevice(@RequestParam("id") String id) {
        calendarDeviceService.deleteCalendarDevice(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备日历,记录设备每天的可用时间")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:query')")
    public CommonResult<CalendarDeviceRespVO> getCalendarDevice(@RequestParam("id") String id) {
        CalendarDeviceDO calendarDevice = calendarDeviceService.getCalendarDevice(id);
        return success(BeanUtils.toBean(calendarDevice, CalendarDeviceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备日历,记录设备每天的可用时间分页")
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:query')")
    public CommonResult<PageResult<CalendarDeviceRespVO>> getCalendarDevicePage(@Valid CalendarDevicePageReqVO pageReqVO) {
        PageResult<CalendarDeviceDO> pageResult = calendarDeviceService.getCalendarDevicePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CalendarDeviceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备日历,记录设备每天的可用时间 Excel")
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCalendarDeviceExcel(@Valid CalendarDevicePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CalendarDeviceDO> list = calendarDeviceService.getCalendarDevicePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备日历,记录设备每天的可用时间.xls", "数据", CalendarDeviceRespVO.class,
                        BeanUtils.toBean(list, CalendarDeviceRespVO.class));
    }

    @PostMapping("/bindShift")
    @Operation(summary = "设备绑定班次")
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:update')")
    public CommonResult<String> bindShift(@RequestBody CalendarDeviceReqVO req) throws Exception {
        calendarDeviceService.bindShift(req);
        return success("ok");
    }

    @PostMapping("/unbindShift")
    @Operation(summary = "设备班次解绑")
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:update')")
    public CommonResult<String> unbindShift(@RequestBody CalendarDeviceReqVO req) throws Exception {
        calendarDeviceService.unbindShift(req);
        return success("ok");
    }

//    /**
//     * 查看设备日历
//     * @param id
//     * @return
//     */
//    @GetMapping("/getByDeviceId")
//    @Operation(summary = "获得设备日历")
//    @Parameter(name = "id", description = "编号", required = true, example = "1024")
//    @PreAuthorize("@ss.hasPermission('dms:calendar-device:query')")
//    public CommonResult<List<CalendarDeviceRespVO>> getCalendarDeviceByDeviceId(@RequestParam("id") String id) {
//        List<CalendarDeviceDO> calendarDeviceList = calendarDeviceService.getCalendarDeviceByDeviceId(id);
//        return success(BeanUtils.toBean(calendarDeviceList, CalendarDeviceRespVO.class,vo->{
//            String date = vo.getDate().toString();
//            String start = vo.getStartTime().toString();
//            String end = vo.getEndTime().toString();
//            vo.setDateStr(date);
//            vo.setStartStr(start);
//            vo.setEndStr(end);
//        }));
//    }
    /**
     * 查看设备日历
     * @param id
     * @return
     */
    @GetMapping("/getByDeviceId")
    @Operation(summary = "获得设备日历")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:query')")
    public CommonResult<List<CalendarDeviceRespVO>> getCalendarDeviceByDeviceId(@RequestParam("id") String id) {
        //设备日历还是要查的,不过变成特殊日期覆盖了
        List<CalendarDeviceDO> calendarDeviceList = calendarDeviceService.getCalendarDeviceByDeviceId(id);
        return success(BeanUtils.toBean(calendarDeviceList, CalendarDeviceRespVO.class,vo->{
            String date = vo.getDate().toString();
            String start = vo.getStartTime().toString();
            String end = vo.getEndTime().toString();
            vo.setDateStr(date);
            vo.setStartStr(start);
            vo.setEndStr(end);
        }));
    }

    /**
     * 更新设备日历
     * @param updateReqVO
     * @return
     */
    @PutMapping("/updateCalendarDevice")
    @Operation(summary = "更新设备日历,记录设备每天的可用时间")
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:update')")
    public CommonResult<Boolean> updateCalendarDeviceByTime(@Valid @RequestBody CalendarDeviceSaveReqVO updateReqVO) {
        calendarDeviceService.updateCalendarDeviceByTime(updateReqVO);
        return success(true);
    }

    @PostMapping("/deleteByDate")
    @Operation(summary = "删除设备日历,记录设备每天的可用时间")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:calendar-device:delete')")
    public CommonResult<Boolean> deleteByDate(@Valid @RequestBody CalendarDeviceSaveReqVO updateReqVO) {
        calendarDeviceService.deleteByDate(updateReqVO);
        return success(true);
    }

}
