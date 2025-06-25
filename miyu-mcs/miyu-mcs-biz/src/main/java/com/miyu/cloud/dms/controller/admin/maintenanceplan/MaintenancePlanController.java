package com.miyu.cloud.dms.controller.admin.maintenanceplan;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.maintenanceplan.vo.MaintenancePlanPageReqVO;
import com.miyu.cloud.dms.controller.admin.maintenanceplan.vo.MaintenancePlanRespVO;
import com.miyu.cloud.dms.controller.admin.maintenanceplan.vo.MaintenancePlanSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.maintenanceplan.MaintenancePlanDO;
import com.miyu.cloud.dms.service.maintenanceplan.MaintenancePlanService;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
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

@Tag(name = "管理后台 - 设备保养维护计划")
@RestController
@RequestMapping("/dms/maintenance-plan")
@Validated
public class MaintenancePlanController {

    @Resource
    private MaintenancePlanService maintenancePlanService;

    @PostMapping("/create")
    @Operation(summary = "创建设备保养维护计划")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-plan:create')")
    public CommonResult<String> createMaintenancePlan(@Valid @RequestBody MaintenancePlanSaveReqVO createReqVO) {
        return success(maintenancePlanService.createMaintenancePlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备保养维护计划")
    @LogRecord(type = "DMS", subType = "maintenance-plan", bizNo = "{{#updateReqVO.id}}", success = "设备保养维护计划{{#updateReqVO.id}}成功更改内容")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-plan:update')")
    public CommonResult<Boolean> updateMaintenancePlan(@Valid @RequestBody MaintenancePlanSaveReqVO updateReqVO) {
        maintenancePlanService.updateMaintenancePlan(updateReqVO);
        LogRecordContext.putVariable("newValue", updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备保养维护计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:maintenance-plan:delete')")
    public CommonResult<Boolean> deleteMaintenancePlan(@RequestParam("id") String id) {
        maintenancePlanService.deleteMaintenancePlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备保养维护计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-plan:query')")
    public CommonResult<MaintenancePlanRespVO> getMaintenancePlan(@RequestParam("id") String id) {
        MaintenancePlanDO maintenancePlan = maintenancePlanService.getMaintenancePlan(id);
        return success(BeanUtils.toBean(maintenancePlan, MaintenancePlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备保养维护计划分页")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-plan:query')")
    public CommonResult<PageResult<MaintenancePlanRespVO>> getMaintenancePlanPage(@Valid MaintenancePlanPageReqVO pageReqVO) {
        PageResult<MaintenancePlanDO> pageResult = maintenancePlanService.getMaintenancePlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaintenancePlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备保养维护计划 Excel")
    @PreAuthorize("@ss.hasPermission('dms:maintenance-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaintenancePlanExcel(@Valid MaintenancePlanPageReqVO pageReqVO,
                                           HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaintenancePlanDO> list = maintenancePlanService.getMaintenancePlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备保养维护计划.xls", "数据", MaintenancePlanRespVO.class,
                BeanUtils.toBean(list, MaintenancePlanRespVO.class));
    }

    @GetMapping("/getTree")
    @Operation(summary = "检查指定树是否存在计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> checkTree(@RequestParam("id") String id) {
        return success(maintenancePlanService.checkTree(id));
    }

    @GetMapping("/getList")
    @Operation(summary = "获得设备保养维护计划列表")
    public CommonResult<List<MaintenancePlanRespVO>> getMaintenancePlanList() {
        return success(BeanUtils.toBean(maintenancePlanService.getList(), MaintenancePlanRespVO.class));
    }

    @PostMapping("/remind")
    @Operation(summary = "执行设备保养维护计划")
    public CommonResult<Boolean> reminderMaintenancePlan(@RequestBody OnlyId onlyId) {
        maintenancePlanService.reminderPlan(onlyId.getId());
        return success(true);
    }

    @Data
    static class OnlyId {
        private String id;
    }

}
