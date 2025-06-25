package com.miyu.module.tms.controller.admin.toolgroupdetail;

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

import com.miyu.module.tms.controller.admin.toolgroupdetail.vo.*;
import com.miyu.module.tms.dal.dataobject.toolgroupdetail.ToolGroupDetailDO;
import com.miyu.module.tms.service.toolgroupdetail.ToolGroupDetailService;

@Tag(name = "管理后台 - 刀具组装")
@RestController
@RequestMapping("/tms/tool-group-detail")
@Validated
public class ToolGroupDetailController {

    @Resource
    private ToolGroupDetailService toolGroupDetailService;

    @PostMapping("/create")
    @Operation(summary = "创建刀具组装")
    @PreAuthorize("@ss.hasPermission('tms:tool-group-detail:create')")
    public CommonResult<String> createToolGroupDetail(@Valid @RequestBody ToolGroupDetailSaveReqVO createReqVO) {
        return success(toolGroupDetailService.createToolGroupDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新刀具组装")
    @PreAuthorize("@ss.hasPermission('tms:tool-group-detail:update')")
    public CommonResult<Boolean> updateToolGroupDetail(@Valid @RequestBody ToolGroupDetailSaveReqVO updateReqVO) {
        toolGroupDetailService.updateToolGroupDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除刀具组装")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-group-detail:delete')")
    public CommonResult<Boolean> deleteToolGroupDetail(@RequestParam("id") String id) {
        toolGroupDetailService.deleteToolGroupDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得刀具组装")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:tool-group-detail:query')")
    public CommonResult<ToolGroupDetailRespVO> getToolGroupDetail(@RequestParam("id") String id) {
        ToolGroupDetailDO toolGroupDetail = toolGroupDetailService.getToolGroupDetail(id);
        return success(BeanUtils.toBean(toolGroupDetail, ToolGroupDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得刀具组装分页")
    @PreAuthorize("@ss.hasPermission('tms:tool-group-detail:query')")
    public CommonResult<PageResult<ToolGroupDetailRespVO>> getToolGroupDetailPage(@Valid ToolGroupDetailPageReqVO pageReqVO) {
        PageResult<ToolGroupDetailDO> pageResult = toolGroupDetailService.getToolGroupDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolGroupDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出刀具组装 Excel")
    @PreAuthorize("@ss.hasPermission('tms:tool-group-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportToolGroupDetailExcel(@Valid ToolGroupDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ToolGroupDetailDO> list = toolGroupDetailService.getToolGroupDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "刀具组装.xls", "数据", ToolGroupDetailRespVO.class,
                        BeanUtils.toBean(list, ToolGroupDetailRespVO.class));
    }

}
