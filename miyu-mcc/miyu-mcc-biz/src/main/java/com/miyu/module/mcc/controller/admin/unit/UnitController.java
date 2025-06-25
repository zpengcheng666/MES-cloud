package com.miyu.module.mcc.controller.admin.unit;

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

import com.miyu.module.mcc.controller.admin.unit.vo.*;
import com.miyu.module.mcc.dal.dataobject.unit.UnitDO;
import com.miyu.module.mcc.service.unit.UnitService;

@Tag(name = "管理后台 - 单位")
@RestController
@RequestMapping("/mcc/unit")
@Validated
public class UnitController {

    @Resource
    private UnitService unitService;

    @PostMapping("/create")
    @Operation(summary = "创建单位")
    @PreAuthorize("@ss.hasPermission('mcc:unit:create')")
    public CommonResult<String> createUnit(@Valid @RequestBody UnitSaveReqVO createReqVO) {
        return success(unitService.createUnit(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新单位")
    @PreAuthorize("@ss.hasPermission('mcc:unit:update')")
    public CommonResult<Boolean> updateUnit(@Valid @RequestBody UnitSaveReqVO updateReqVO) {
        unitService.updateUnit(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除单位")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcc:unit:delete')")
    public CommonResult<Boolean> deleteUnit(@RequestParam("id") String id) {
        unitService.deleteUnit(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得单位")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcc:unit:query')")
    public CommonResult<UnitRespVO> getUnit(@RequestParam("id") String id) {
        UnitDO unit = unitService.getUnit(id);
        return success(BeanUtils.toBean(unit, UnitRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得单位分页")
    @PreAuthorize("@ss.hasPermission('mcc:unit:query')")
    public CommonResult<PageResult<UnitRespVO>> getUnitPage(@Valid UnitPageReqVO pageReqVO) {
        PageResult<UnitDO> pageResult = unitService.getUnitPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UnitRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出单位 Excel")
    @PreAuthorize("@ss.hasPermission('mcc:unit:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUnitExcel(@Valid UnitPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UnitDO> list = unitService.getUnitPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "单位.xls", "数据", UnitRespVO.class,
                        BeanUtils.toBean(list, UnitRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得单位列表", description = "主要用于前端的下拉选项")
    public CommonResult<List<UnitRespVO>> getUnitList() {
        List<UnitDO> list = unitService.getUnitList();
        return success(BeanUtils.toBean(list, UnitRespVO.class));
    }

}