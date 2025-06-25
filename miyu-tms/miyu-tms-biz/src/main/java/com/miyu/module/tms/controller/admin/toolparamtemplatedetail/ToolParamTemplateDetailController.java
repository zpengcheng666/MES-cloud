package com.miyu.module.tms.controller.admin.toolparamtemplatedetail;

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

import com.miyu.module.tms.controller.admin.toolparamtemplatedetail.vo.*;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
import com.miyu.module.tms.service.toolparamtemplatedetail.ToolParamTemplateDetailService;

@Tag(name = "管理后台 - 参数模版详情")
@RestController
@RequestMapping("/tms/tool-param-template-detail")
@Validated
public class ToolParamTemplateDetailController {

    @Resource
    private ToolParamTemplateDetailService toolParamTemplateDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建参数模版详情")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template-detail:create')")
    public CommonResult<String> createToolParamTemplateDetail(@Valid @RequestBody ToolParamTemplateDetailSaveReqVO createReqVO) {
        return success(toolParamTemplateDetailService.createToolParamTemplateDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新参数模版详情")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template-detail:update')")
    public CommonResult<Boolean> updateToolParamTemplateDetail(@Valid @RequestBody ToolParamTemplateDetailSaveReqVO updateReqVO) {
        toolParamTemplateDetailService.updateToolParamTemplateDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除参数模版详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template-detail:delete')")
    public CommonResult<Boolean> deleteToolParamTemplateDetail(@RequestParam("id") String id) {
        toolParamTemplateDetailService.deleteToolParamTemplateDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得参数模版详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template-detail:query')")
    public CommonResult<ToolParamTemplateDetailRespVO> getToolParamTemplateDetail(@RequestParam("id") String id) {
        ToolParamTemplateDetailDO toolParamTemplateDetail = toolParamTemplateDetailService.getToolParamTemplateDetail(id);
        return success(BeanUtils.toBean(toolParamTemplateDetail, ToolParamTemplateDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得参数模版详情分页")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template-detail:query')")
    public CommonResult<PageResult<ToolParamTemplateDetailRespVO>> getToolParamTemplateDetailPage(@Valid ToolParamTemplateDetailPageReqVO pageReqVO) {
        PageResult<ToolParamTemplateDetailDO> pageResult = toolParamTemplateDetailService.getToolParamTemplateDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolParamTemplateDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出参数模版详情 Excel")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportToolParamTemplateDetailExcel(@Valid ToolParamTemplateDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ToolParamTemplateDetailDO> list = toolParamTemplateDetailService.getToolParamTemplateDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "参数模版详情.xls", "数据", ToolParamTemplateDetailRespVO.class,
                        BeanUtils.toBean(list, ToolParamTemplateDetailRespVO.class));
    }

}