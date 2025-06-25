package com.miyu.module.mcc.controller.admin.encodingruledetail;

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

import com.miyu.module.mcc.controller.admin.encodingruledetail.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingruledetail.EncodingRuleDetailDO;
import com.miyu.module.mcc.service.encodingruledetail.EncodingRuleDetailService;

@Tag(name = "管理后台 - 编码规则配置详情")
@RestController
@RequestMapping("/mcc/encoding-rule-detail")
@Validated
public class EncodingRuleDetailController {

    @Resource
    private EncodingRuleDetailService encodingRuleDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建编码规则配置详情")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule-detail:create')")
    public CommonResult<String> createEncodingRuleDetail(@Valid @RequestBody EncodingRuleDetailSaveReqVO createReqVO) {
        return success(encodingRuleDetailService.createEncodingRuleDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新编码规则配置详情")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule-detail:update')")
    public CommonResult<Boolean> updateEncodingRuleDetail(@Valid @RequestBody EncodingRuleDetailSaveReqVO updateReqVO) {
        encodingRuleDetailService.updateEncodingRuleDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除编码规则配置详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule-detail:delete')")
    public CommonResult<Boolean> deleteEncodingRuleDetail(@RequestParam("id") String id) {
        encodingRuleDetailService.deleteEncodingRuleDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得编码规则配置详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule-detail:query')")
    public CommonResult<EncodingRuleDetailRespVO> getEncodingRuleDetail(@RequestParam("id") String id) {
        EncodingRuleDetailDO encodingRuleDetail = encodingRuleDetailService.getEncodingRuleDetail(id);
        return success(BeanUtils.toBean(encodingRuleDetail, EncodingRuleDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得编码规则配置详情分页")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule-detail:query')")
    public CommonResult<PageResult<EncodingRuleDetailRespVO>> getEncodingRuleDetailPage(@Valid EncodingRuleDetailPageReqVO pageReqVO) {
        PageResult<EncodingRuleDetailDO> pageResult = encodingRuleDetailService.getEncodingRuleDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, EncodingRuleDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出编码规则配置详情 Excel")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-rule-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportEncodingRuleDetailExcel(@Valid EncodingRuleDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<EncodingRuleDetailDO> list = encodingRuleDetailService.getEncodingRuleDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "编码规则配置详情.xls", "数据", EncodingRuleDetailRespVO.class,
                        BeanUtils.toBean(list, EncodingRuleDetailRespVO.class));
    }

}