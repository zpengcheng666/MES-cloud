package com.miyu.cloud.dms.controller.admin.calendar;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.dms.controller.admin.calendar.detailvo.CalendarDetailPageReqVO;
import com.miyu.cloud.dms.controller.admin.calendar.detailvo.CalendarDetailRespVO;
import com.miyu.cloud.dms.controller.admin.calendar.detailvo.CalendarDetailSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarDetailDO;
import com.miyu.cloud.dms.service.calendar.CalendarDetailService;
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


@Tag(name = "管理后台 - 基础日历的工作日")
@RestController
@RequestMapping("/dms/calendar-detail")
@Validated
public class CalendarDetailController {

    @Resource
    private CalendarDetailService calendarDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建基础日历的工作日")
    @PreAuthorize("@ss.hasPermission('dms:calendar-detail:create')")
    public CommonResult<String> createCalendarDetail(@Valid @RequestBody CalendarDetailSaveReqVO createReqVO) {
        return success(calendarDetailService.createCalendarDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新基础日历的工作日")
    @PreAuthorize("@ss.hasPermission('dms:calendar-detail:update')")
    public CommonResult<Boolean> updateCalendarDetail(@Valid @RequestBody CalendarDetailSaveReqVO updateReqVO) {
        calendarDetailService.updateCalendarDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除基础日历的工作日")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:calendar-detail:delete')")
    public CommonResult<Boolean> deleteCalendarDetail(@RequestParam("id") String id) {
        calendarDetailService.deleteCalendarDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得基础日历的工作日")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:calendar-detail:query')")
    public CommonResult<CalendarDetailRespVO> getCalendarDetail(@RequestParam("id") String id) {
        CalendarDetailDO calendarDetail = calendarDetailService.getCalendarDetail(id);
        return success(BeanUtils.toBean(calendarDetail, CalendarDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得基础日历的工作日分页")
    @PreAuthorize("@ss.hasPermission('dms:calendar-detail:query')")
    public CommonResult<PageResult<CalendarDetailRespVO>> getCalendarDetailPage(@Valid CalendarDetailPageReqVO pageReqVO) {
        PageResult<CalendarDetailDO> pageResult = calendarDetailService.getCalendarDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CalendarDetailRespVO.class));
    }
    @GetMapping("/listByBasicId")
    @Operation(summary = "获得基础日历的工作时间")
    public CommonResult<List<CalendarDetailRespVO>> listByBasicId(@RequestParam("id")String id){
        List<CalendarDetailDO> list = calendarDetailService.getListByBasicId(id);
        return success(BeanUtils.toBean(list,CalendarDetailRespVO.class));
    }

}
