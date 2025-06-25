package com.miyu.module.tms.controller.admin.toolconfigparameter;

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

import com.miyu.module.tms.controller.admin.toolconfigparameter.vo.*;
import com.miyu.module.tms.dal.dataobject.toolconfigparameter.ToolConfigParameterDO;
import com.miyu.module.tms.service.toolconfigparameter.ToolConfigParameterService;

@Tag(name = "管理后台 - 刀具参数信息")
@RestController
@RequestMapping("/tms/tool-config-parameter")
@Validated
public class ToolConfigParameterController {

    @Resource
    private ToolConfigParameterService toolConfigParameterService;

    @PostMapping("/create")
    @Operation(summary = "创建刀具参数信息")
    @PreAuthorize("@ss.hasPermission('tms:tool-config-parameter:create')")
    public CommonResult<String> createToolConfigParameter(@Valid @RequestBody ToolConfigParameterSaveReqVO createReqVO) {
        return success(toolConfigParameterService.createToolConfigParameter(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新刀具参数信息")
    @PreAuthorize("@ss.hasPermission('tms:tool-config-parameter:update')")
    public CommonResult<Boolean> updateToolConfigParameter(@Valid @RequestBody ToolConfigParameterSaveReqVO updateReqVO) {
        toolConfigParameterService.updateToolConfigParameter(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除刀具参数信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-config-parameter:delete')")
    public CommonResult<Boolean> deleteToolConfigParameter(@RequestParam("id") Long id) {
        toolConfigParameterService.deleteToolConfigParameter(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得刀具参数信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:tool-config-parameter:query')")
    public CommonResult<ToolConfigParameterRespVO> getToolConfigParameter(@RequestParam("id") Long id) {
        ToolConfigParameterDO toolConfigParameter = toolConfigParameterService.getToolConfigParameter(id);
        return success(BeanUtils.toBean(toolConfigParameter, ToolConfigParameterRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得刀具参数信息分页")
    @PreAuthorize("@ss.hasPermission('tms:tool-config-parameter:query')")
    public CommonResult<PageResult<ToolConfigParameterRespVO>> getToolConfigParameterPage(@Valid ToolConfigParameterPageReqVO pageReqVO) {
        PageResult<ToolConfigParameterDO> pageResult = toolConfigParameterService.getToolConfigParameterPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ToolConfigParameterRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出刀具参数信息 Excel")
    @PreAuthorize("@ss.hasPermission('tms:tool-config-parameter:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportToolConfigParameterExcel(@Valid ToolConfigParameterPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ToolConfigParameterDO> list = toolConfigParameterService.getToolConfigParameterPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "刀具参数信息.xls", "数据", ToolConfigParameterRespVO.class,
                        BeanUtils.toBean(list, ToolConfigParameterRespVO.class));
    }

}
