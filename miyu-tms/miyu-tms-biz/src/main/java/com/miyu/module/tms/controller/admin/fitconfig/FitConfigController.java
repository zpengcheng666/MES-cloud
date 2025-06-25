package com.miyu.module.tms.controller.admin.fitconfig;

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

import com.miyu.module.tms.controller.admin.fitconfig.vo.*;
import com.miyu.module.tms.dal.dataobject.fitconfig.FitConfigDO;
import com.miyu.module.tms.service.fitconfig.FitConfigService;

@Tag(name = "管理后台 - 刀具适配")
@RestController
@RequestMapping("/tms/fit-config")
@Validated
public class FitConfigController {

    @Resource
    private FitConfigService fitConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建刀具适配")
    @PreAuthorize("@ss.hasPermission('tms:fit-config:create')")
    public CommonResult<String> createFitConfig(@Valid @RequestBody FitConfigSaveReqVO createReqVO) {
        return success(fitConfigService.createFitConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新刀具适配")
    @PreAuthorize("@ss.hasPermission('tms:fit-config:update')")
    public CommonResult<Boolean> updateFitConfig(@Valid @RequestBody FitConfigSaveReqVO updateReqVO) {
        fitConfigService.updateFitConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除刀具适配")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:fit-config:delete')")
    public CommonResult<Boolean> deleteFitConfig(@RequestParam("id") String id) {
        fitConfigService.deleteFitConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得刀具适配")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:fit-config:query')")
    public CommonResult<FitConfigRespVO> getFitConfig(@RequestParam("id") String id) {
        FitConfigDO fitConfig = fitConfigService.getFitConfig(id);
        return success(BeanUtils.toBean(fitConfig, FitConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得刀具适配分页")
    @PreAuthorize("@ss.hasPermission('tms:fit-config:query')")
    public CommonResult<PageResult<FitConfigRespVO>> getFitConfigPage(@Valid FitConfigPageReqVO pageReqVO) {
        PageResult<FitConfigDO> pageResult = fitConfigService.getFitConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, FitConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出刀具适配 Excel")
    @PreAuthorize("@ss.hasPermission('tms:fit-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFitConfigExcel(@Valid FitConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<FitConfigDO> list = fitConfigService.getFitConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "刀具适配.xls", "数据", FitConfigRespVO.class,
                        BeanUtils.toBean(list, FitConfigRespVO.class));
    }
}
