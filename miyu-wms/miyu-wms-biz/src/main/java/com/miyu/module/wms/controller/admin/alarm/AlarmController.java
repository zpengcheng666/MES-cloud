package com.miyu.module.wms.controller.admin.alarm;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

import javax.validation.constraints.*;
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

import com.miyu.module.wms.controller.admin.alarm.vo.*;
import com.miyu.module.wms.dal.dataobject.alarm.AlarmDO;
import com.miyu.module.wms.service.alarm.AlarmService;

@Tag(name = "管理后台 - 异常")
@RestController
@RequestMapping("/wms/alarm")
@Validated
public class AlarmController {

    @Resource
    private AlarmService alarmService;

    @PostMapping("/create")
    @Operation(summary = "创建异常")
    @PreAuthorize("@ss.hasPermission('wms:alarm:create')")
    public CommonResult<String> createAlarm(@Valid @RequestBody AlarmSaveReqVO createReqVO) {
        return success(alarmService.createAlarm(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新异常")
    @PreAuthorize("@ss.hasPermission('wms:alarm:update')")
    public CommonResult<Boolean> updateAlarm(@Valid @RequestBody AlarmSaveReqVO updateReqVO) {
        alarmService.updateAlarm(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除异常")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:alarm:delete')")
    public CommonResult<Boolean> deleteAlarm(@RequestParam("id") String id) {
        alarmService.deleteAlarm(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得异常")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:alarm:query')")
    public CommonResult<AlarmRespVO> getAlarm(@RequestParam("id") String id) {
        AlarmDO alarm = alarmService.getAlarm(id);
        return success(BeanUtils.toBean(alarm, AlarmRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得异常分页")
    @PreAuthorize("@ss.hasAnyPermissions('wms:alarm:query','wms:alarm:info')")
    public CommonResult<PageResult<AlarmRespVO>> getAlarmPage(@Valid AlarmPageReqVO pageReqVO) {
        PageResult<AlarmDO> pageResult = alarmService.getAlarmPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, AlarmRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出异常 Excel")
    @PreAuthorize("@ss.hasPermission('wms:alarm:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportAlarmExcel(@Valid AlarmPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<AlarmDO> list = alarmService.getAlarmPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "异常.xls", "数据", AlarmRespVO.class,
                        BeanUtils.toBean(list, AlarmRespVO.class));
    }

}