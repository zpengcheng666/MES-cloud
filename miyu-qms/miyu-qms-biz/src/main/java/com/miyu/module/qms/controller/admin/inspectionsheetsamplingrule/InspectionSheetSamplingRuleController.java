package com.miyu.module.qms.controller.admin.inspectionsheetsamplingrule;

import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.security.access.prepost.PreAuthorize;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Operation;

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

import com.miyu.module.qms.controller.admin.inspectionsheetsamplingrule.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetsamplingrule.InspectionSheetSamplingRuleDO;
import com.miyu.module.qms.service.inspectionsheetsamplingrule.InspectionSheetSamplingRuleService;

@Tag(name = "管理后台 - 检验单抽样规则（检验抽样方案）关系")
@RestController
@RequestMapping("/qms/inspection-sheet-sampling-rule")
@Validated
public class InspectionSheetSamplingRuleController {

    @Resource
    private InspectionSheetSamplingRuleService inspectionSheetSamplingRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建检验单抽样规则（检验抽样方案）关系")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-sampling-rule:create')")
    public CommonResult<String> createInspectionSheetSamplingRule(@Valid @RequestBody InspectionSheetSamplingRuleSaveReqVO createReqVO) {
        return success(inspectionSheetSamplingRuleService.createInspectionSheetSamplingRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验单抽样规则（检验抽样方案）关系")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-sampling-rule:update')")
    public CommonResult<Boolean> updateInspectionSheetSamplingRule(@Valid @RequestBody InspectionSheetSamplingRuleSaveReqVO updateReqVO) {
        inspectionSheetSamplingRuleService.updateInspectionSheetSamplingRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检验单抽样规则（检验抽样方案）关系")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-sampling-rule:delete')")
    public CommonResult<Boolean> deleteInspectionSheetSamplingRule(@RequestParam("id") String id) {
        inspectionSheetSamplingRuleService.deleteInspectionSheetSamplingRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验单抽样规则（检验抽样方案）关系")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-sampling-rule:query')")
    public CommonResult<InspectionSheetSamplingRuleRespVO> getInspectionSheetSamplingRule(@RequestParam("id") String id) {
        InspectionSheetSamplingRuleDO inspectionSheetSamplingRule = inspectionSheetSamplingRuleService.getInspectionSheetSamplingRule(id);
        return success(BeanUtils.toBean(inspectionSheetSamplingRule, InspectionSheetSamplingRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检验单抽样规则（检验抽样方案）关系分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-sampling-rule:query')")
    public CommonResult<PageResult<InspectionSheetSamplingRuleRespVO>> getInspectionSheetSamplingRulePage(@Valid InspectionSheetSamplingRulePageReqVO pageReqVO) {
        PageResult<InspectionSheetSamplingRuleDO> pageResult = inspectionSheetSamplingRuleService.getInspectionSheetSamplingRulePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionSheetSamplingRuleRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检验单抽样规则（检验抽样方案）关系 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-sampling-rule:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionSheetSamplingRuleExcel(@Valid InspectionSheetSamplingRulePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionSheetSamplingRuleDO> list = inspectionSheetSamplingRuleService.getInspectionSheetSamplingRulePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检验单抽样规则（检验抽样方案）关系.xls", "数据", InspectionSheetSamplingRuleRespVO.class,
                        BeanUtils.toBean(list, InspectionSheetSamplingRuleRespVO.class));
    }

}