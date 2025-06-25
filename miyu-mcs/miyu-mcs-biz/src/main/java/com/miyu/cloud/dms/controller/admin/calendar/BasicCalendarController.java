package com.miyu.cloud.dms.controller.admin.calendar;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.calendar.detailvo.CalendarDetailRespVO;
import com.miyu.cloud.dms.controller.admin.calendar.specialvo.CalendarSpecialRespVO;
import com.miyu.cloud.dms.controller.admin.calendar.vo.BasicCalendarPageReqVO;
import com.miyu.cloud.dms.controller.admin.calendar.vo.BasicCalendarRespVO;
import com.miyu.cloud.dms.controller.admin.calendar.vo.BasicCalendarSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.BasicCalendarDO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarDetailDO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarSpecialDO;
import com.miyu.cloud.dms.service.calendar.BasicCalendarService;
import com.miyu.cloud.dms.service.calendar.CalendarDetailService;
import com.miyu.cloud.dms.service.calendar.CalendarSpecialService;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;


@Tag(name = "管理后台 - 基础日历")
@RestController
@RequestMapping("/dms/basic-calendar")
@Validated
public class BasicCalendarController {

    @Resource
    private BasicCalendarService basicCalendarService;

    @Resource
    private CalendarDetailService calendarDetailService;

    @Resource
    private CalendarSpecialService calendarSpecialService;

    @PostMapping("/create")
    @Operation(summary = "创建基础日历")
    @PreAuthorize("@ss.hasPermission('pms:basic-calendar:create')")
    public CommonResult<String> createBasicCalendar(@Valid @RequestBody BasicCalendarSaveReqVO createReqVO) {
        return success(basicCalendarService.createBasicCalendar(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新基础日历")
    @PreAuthorize("@ss.hasPermission('pms:basic-calendar:update')")
    public CommonResult<Boolean> updateBasicCalendar(@Valid @RequestBody BasicCalendarSaveReqVO updateReqVO) {
        basicCalendarService.updateBasicCalendar(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除基础日历")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('pms:basic-calendar:delete')")
    public CommonResult<Boolean> deleteBasicCalendar(@RequestParam("id") String id) {
        basicCalendarService.deleteBasicCalendar(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得基础日历")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:basic-calendar:query')")
    public CommonResult<BasicCalendarRespVO> getBasicCalendar(@RequestParam("id") String id) {
        BasicCalendarDO basicCalendar = basicCalendarService.getBasicCalendar(id);
        return success(BeanUtils.toBean(basicCalendar, BasicCalendarRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得基础日历分页")
    @PreAuthorize("@ss.hasPermission('pms:basic-calendar:query')")
    public CommonResult<PageResult<BasicCalendarRespVO>> getBasicCalendarPage(@Valid BasicCalendarPageReqVO pageReqVO) {
        PageResult<BasicCalendarDO> pageResult = basicCalendarService.getBasicCalendarPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BasicCalendarRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出基础日历 Excel")
    @PreAuthorize("@ss.hasPermission('pms:basic-calendar:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBasicCalendarExcel(@Valid BasicCalendarPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BasicCalendarDO> list = basicCalendarService.getBasicCalendarPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "基础日历.xls", "数据", BasicCalendarRespVO.class,
                        BeanUtils.toBean(list, BasicCalendarRespVO.class));
    }

    @GetMapping("/getTest")
    @Operation(summary = "获得基础日历")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('pms:basic-calendar:query')")
    public CommonResult<BasicCalendarRespVO> getBasicCalendarTest(@RequestParam("id") String id) {
        BasicCalendarDO basicCalendar = basicCalendarService.getBasicCalendar(id);
        LocalDateTime startDate = basicCalendar.getStartDate();
        LocalDateTime endDate = basicCalendar.getEndDate();
        String s = String.valueOf(startDate.getYear()) + "-" + String.valueOf(startDate.getMonthValue()) + "-" + String.valueOf(startDate.getDayOfMonth());
        String s2 = String.valueOf(endDate.getYear()) + "-" + String.valueOf(endDate.getMonthValue()) + "-" + String.valueOf(endDate.getDayOfMonth());
        System.out.println(s);
        System.out.println(s2);
        return success(BeanUtils.toBean(basicCalendar, BasicCalendarRespVO.class));
    }

    @GetMapping("/listByBasicId")
    @Operation(summary = "获得基础日历的工作时间")
    public CommonResult<List<CalendarSpecialRespVO>> listByBasicId(@RequestParam("id")String id){
//        List<CalendarDetailDO> listD = calendarDetailService.getListByBasicId(id);
//        List<CalendarSpecialDO> listS = calendarSpecialService.getListByBasicId(id);
//        Map<String, CalendarSpecialDO> specialDOMap = CollectionUtils.convertMap(listS, CalendarSpecialDO::getCsdate);
//        for (CalendarDetailDO calendarDetailDO : listD) {
//            if(specialDOMap.containsKey(calendarDetailDO.getCddate())){
//                calendarDetailDO.setCdname(specialDOMap.get(calendarDetailDO.getCddate()).getCsname());
//            }
//        }
//        return success(BeanUtils.toBean(listD,CalendarDetailRespVO.class));
        //现在只查特殊就行
        List<CalendarSpecialDO> listS = calendarSpecialService.listAll();
        return success(BeanUtils.toBean(listS, CalendarSpecialRespVO.class));
    }
}
