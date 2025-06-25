package com.miyu.cloud.dms.controller.admin.inspectionplan;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.dms.controller.admin.inspectionplan.vo.InspectionPlanPageReqVO;
import com.miyu.cloud.dms.controller.admin.inspectionplan.vo.InspectionPlanRespVO;
import com.miyu.cloud.dms.controller.admin.inspectionplan.vo.InspectionPlanSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.inspectionplan.InspectionPlanDO;
import com.miyu.cloud.dms.service.inspectionplan.InspectionPlanService;
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

@Tag(name = "管理后台 - 设备检查计划")
@RestController
@RequestMapping("/dms/inspection-plan")
@Validated
public class InspectionPlanController {

    @Resource
    private InspectionPlanService inspectionPlanService;

    @PostMapping("/create")
    @Operation(summary = "创建设备检查计划")
    @PreAuthorize("@ss.hasPermission('dms:inspection-plan:create')")
    public CommonResult<String> createInspectionPlan(@Valid @RequestBody InspectionPlanSaveReqVO createReqVO) {
        return success(inspectionPlanService.createInspectionPlan(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新设备检查计划")
    @LogRecord(type = "DMS", subType = "inspection-plan", bizNo = "{{#updateReqVO.id}}", success = "设备检查计划{{#updateReqVO.id}}成功更改内容")
    @PreAuthorize("@ss.hasPermission('dms:inspection-plan:update')")
    public CommonResult<Boolean> updateInspectionPlan(@Valid @RequestBody InspectionPlanSaveReqVO updateReqVO) {
        inspectionPlanService.updateInspectionPlan(updateReqVO);
        LogRecordContext.putVariable("newValue", updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除设备检查计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('dms:inspection-plan:delete')")
    public CommonResult<Boolean> deleteInspectionPlan(@RequestParam("id") String id) {
        inspectionPlanService.deleteInspectionPlan(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得设备检查计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('dms:inspection-plan:query')")
    public CommonResult<InspectionPlanRespVO> getInspectionPlan(@RequestParam("id") String id) {
        InspectionPlanDO inspectionPlan = inspectionPlanService.getInspectionPlan(id);
        return success(BeanUtils.toBean(inspectionPlan, InspectionPlanRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得设备检查计划分页")
    @PreAuthorize("@ss.hasPermission('dms:inspection-plan:query')")
    public CommonResult<PageResult<InspectionPlanRespVO>> getInspectionPlanPage(@Valid InspectionPlanPageReqVO pageReqVO) {
        PageResult<InspectionPlanDO> pageResult = inspectionPlanService.getInspectionPlanPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionPlanRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出设备检查计划 Excel")
    @PreAuthorize("@ss.hasPermission('dms:inspection-plan:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionPlanExcel(@Valid InspectionPlanPageReqVO pageReqVO,
                                          HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionPlanDO> list = inspectionPlanService.getInspectionPlanPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "设备检查计划.xls", "数据", InspectionPlanRespVO.class,
                BeanUtils.toBean(list, InspectionPlanRespVO.class));
    }

    @GetMapping("/hasTree")
    @Operation(summary = "检查指定树是否存在计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<Boolean> hasTree(@RequestParam("id") String id) {
        return success(inspectionPlanService.checkTree(id));
    }

    @GetMapping("/getList")
    @Operation(summary = "获得设备检查计划列表")
    public CommonResult<List<InspectionPlanRespVO>> getInspectionPlanList() {
        return success(BeanUtils.toBean(inspectionPlanService.getInspectionPlanList(), InspectionPlanRespVO.class));
    }

    @GetMapping("/reminder")
    @Operation(summary = "提醒计划")
    public CommonResult<Boolean> reminderInspectionPlan(@RequestParam("id") String id) {
        inspectionPlanService.reminderInspectionPlan(id);
        return success(true);
    }

}
