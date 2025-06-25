package com.miyu.module.wms.controller.admin.materialconfigarea;

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

import com.miyu.module.wms.controller.admin.materialconfigarea.vo.*;
import com.miyu.module.wms.dal.dataobject.materialconfigarea.MaterialConfigAreaDO;
import com.miyu.module.wms.service.materialconfigarea.MaterialConfigAreaService;

@Tag(name = "管理后台 - 物料类型关联库区配置")
@RestController
@RequestMapping("/wms/material-config-area")
@Validated
public class MaterialConfigAreaController {

    @Resource
    private MaterialConfigAreaService materialConfigAreaService;

    @PostMapping("/create")
    @Operation(summary = "创建物料类型关联库区配置")
    @PreAuthorize("@ss.hasPermission('wms:material-config-area:create')")
    public CommonResult<String> createMaterialConfigArea(@Valid @RequestBody MaterialConfigAreaSaveReqVO createReqVO) {
        return success(materialConfigAreaService.createMaterialConfigArea(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料类型关联库区配置")
    @PreAuthorize("@ss.hasPermission('wms:material-config-area:update')")
    public CommonResult<Boolean> updateMaterialConfigArea(@Valid @RequestBody MaterialConfigAreaSaveReqVO updateReqVO) {
        materialConfigAreaService.updateMaterialConfigArea(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料类型关联库区配置")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:material-config-area:delete')")
    public CommonResult<Boolean> deleteMaterialConfigArea(@RequestParam("id") String id) {
        materialConfigAreaService.deleteMaterialConfigArea(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料类型关联库区配置")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:material-config-area:query')")
    public CommonResult<MaterialConfigAreaRespVO> getMaterialConfigArea(@RequestParam("id") String id) {
        MaterialConfigAreaDO materialConfigArea = materialConfigAreaService.getMaterialConfigArea(id);
        return success(BeanUtils.toBean(materialConfigArea, MaterialConfigAreaRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料类型关联库区配置分页")
    @PreAuthorize("@ss.hasPermission('wms:material-config-area:query')")
    public CommonResult<PageResult<MaterialConfigAreaRespVO>> getMaterialConfigAreaPage(@Valid MaterialConfigAreaPageReqVO pageReqVO) {
        PageResult<MaterialConfigAreaDO> pageResult = materialConfigAreaService.getMaterialConfigAreaPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialConfigAreaRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料类型关联库区配置 Excel")
    @PreAuthorize("@ss.hasPermission('wms:material-config-area:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialConfigAreaExcel(@Valid MaterialConfigAreaPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaterialConfigAreaDO> list = materialConfigAreaService.getMaterialConfigAreaPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料类型关联库区配置.xls", "数据", MaterialConfigAreaRespVO.class,
                        BeanUtils.toBean(list, MaterialConfigAreaRespVO.class));
    }

}