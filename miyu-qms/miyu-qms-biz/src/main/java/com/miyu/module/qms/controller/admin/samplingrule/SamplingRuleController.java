package com.miyu.module.qms.controller.admin.samplingrule;

import com.google.common.collect.Lists;
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

import com.miyu.module.qms.controller.admin.samplingrule.vo.*;
import com.miyu.module.qms.dal.dataobject.samplingrule.SamplingRuleDO;
import com.miyu.module.qms.service.samplingrule.SamplingRuleService;

@Tag(name = "管理后台 - 抽样规则")
@RestController
@RequestMapping("/qms/sampling-rule")
@Validated
public class SamplingRuleController {

    @Resource
    private SamplingRuleService samplingRuleService;

    @PostMapping("/create")
    @Operation(summary = "创建抽样规则")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:create')")
    public CommonResult<String> createSamplingRule(@Valid @RequestBody SamplingRuleSaveReqVO createReqVO) {
        return success(samplingRuleService.createSamplingRule(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新抽样规则")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:update')")
    public CommonResult<Boolean> updateSamplingRule(@Valid @RequestBody SamplingRuleSaveReqVO updateReqVO) {
        samplingRuleService.updateSamplingRule(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除抽样规则")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:delete')")
    public CommonResult<Boolean> deleteSamplingRule(@RequestParam("id") String id) {
        samplingRuleService.deleteSamplingRule(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得抽样规则")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:query')")
    public CommonResult<SamplingRuleRespVO> getSamplingRule(@RequestParam("id") String id) {
        SamplingRuleDO samplingRule = samplingRuleService.getSamplingRule(id);
        return success(BeanUtils.toBean(samplingRule, SamplingRuleRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得抽样规则分页")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:query')")
    public CommonResult<PageResult<SamplingRuleRespVO>> getSamplingRulePage(@Valid SamplingRulePageReqVO pageReqVO) {
        PageResult<SamplingRuleDO> pageResult = samplingRuleService.getSamplingRulePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, SamplingRuleRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出抽样规则 Excel")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSamplingRuleExcel(@Valid SamplingRulePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<SamplingRuleDO> list = samplingRuleService.getSamplingRulePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "抽样规则.xls", "数据", SamplingRuleRespVO.class,
                        BeanUtils.toBean(list, SamplingRuleRespVO.class));
    }




    @PostMapping("/createT")
    @Operation(summary = "创建抽样规则")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:create')")
    public CommonResult<String> createT(@Valid @RequestBody SamplingRuleSaveReqVO createReqVO) {

        List<Integer> type = Lists.newArrayList(1,2,3,4,5,6,7);


        for (Integer i :type){
            SamplingRuleSaveReqVO vo = BeanUtils.toBean(createReqVO,SamplingRuleSaveReqVO.class);
            vo.setInspectionLevelType(i);

            samplingRuleService.createSamplingRule(vo);
        }
        return success("");
    }


    @GetMapping("/getInfo")
    @Operation(summary = "获得抽样规则")
    @Parameter(name = "samplingStandardId", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:sampling-rule:query')")
    public CommonResult<List<SamplingRuleInfoRespVO>> getSamplingRuleInfo(@RequestParam("samplingStandardId") String samplingStandardId) {

        return success(samplingRuleService.getSamplingRuleInfo(samplingStandardId));
    }

}