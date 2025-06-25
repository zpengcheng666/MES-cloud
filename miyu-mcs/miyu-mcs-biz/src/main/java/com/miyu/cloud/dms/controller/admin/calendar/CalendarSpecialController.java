package com.miyu.cloud.dms.controller.admin.calendar;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.dms.controller.admin.calendar.specialvo.CalendarSpecialPageReqVO;
import com.miyu.cloud.dms.controller.admin.calendar.specialvo.CalendarSpecialRespVO;
import com.miyu.cloud.dms.controller.admin.calendar.specialvo.CalendarSpecialSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarSpecialDO;
import com.miyu.cloud.dms.service.calendar.CalendarSpecialService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 基础日历的工作日（特别版，调休节假日等特殊日期）")
@RestController
@RequestMapping("/dms/calendar-special")
@Validated
public class CalendarSpecialController {

    @Resource
    private CalendarSpecialService calendarSpecialService;

    @PostMapping("/create")
    @Operation(summary = "创建基础日历的工作日（特别版，调休节假日等特殊日期）")
    @PreAuthorize("@ss.hasPermission('dms:calendar-special:create')")
    public CommonResult<String> createCalendarSpecial(@Valid @RequestBody CalendarSpecialSaveReqVO createReqVO) {
        return success(calendarSpecialService.createCalendarSpecial(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新基础日历的工作日（特别版，调休节假日等特殊日期）")
    @PreAuthorize("@ss.hasPermission('dms:calendar-special:update')")
    public CommonResult<Boolean> updateCalendarSpecial(@Valid @RequestBody CalendarSpecialSaveReqVO updateReqVO) {
        calendarSpecialService.updateCalendarSpecial(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除基础日历的工作日（特别版，调休节假日等特殊日期）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:calendar-special:delete')")
    public CommonResult<Boolean> deleteCalendarSpecial(@RequestParam("id") String id) {
        calendarSpecialService.deleteCalendarSpecial(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得基础日历的工作日（特别版，调休节假日等特殊日期）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:calendar-special:query')")
    public CommonResult<CalendarSpecialRespVO> getCalendarSpecial(@RequestParam("id") String id) {
        CalendarSpecialDO calendarSpecial = calendarSpecialService.getCalendarSpecial(id);
        return success(BeanUtils.toBean(calendarSpecial, CalendarSpecialRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得基础日历的工作日（特别版，调休节假日等特殊日期）分页")
    @PreAuthorize("@ss.hasPermission('dms:calendar-special:query')")
    public CommonResult<PageResult<CalendarSpecialRespVO>> getCalendarSpecialPage(@Valid CalendarSpecialPageReqVO pageReqVO) {
        PageResult<CalendarSpecialDO> pageResult = calendarSpecialService.getCalendarSpecialPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CalendarSpecialRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得特殊日历的工作时间")
    public CommonResult<List<CalendarSpecialRespVO>> listAll(){
        List<CalendarSpecialDO> list = calendarSpecialService.listAll();
        return success(BeanUtils.toBean(list,CalendarSpecialRespVO.class));
    }

    @PostMapping("/deleteByDate")
    @Operation(summary = "删除基础日历的工作日（特别版，调休节假日等特殊日期）")
    @PreAuthorize("@ss.hasPermission('dms:calendar-special:delete')")
    public CommonResult<Boolean> deleteCalendarSpecialByDate(@Valid @RequestBody CalendarSpecialSaveReqVO req) {
        calendarSpecialService.deleteByDate(req);
        return success(true);
    }
}
