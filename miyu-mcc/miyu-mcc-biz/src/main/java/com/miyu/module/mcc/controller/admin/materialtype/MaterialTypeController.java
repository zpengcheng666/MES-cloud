package com.miyu.module.mcc.controller.admin.materialtype;

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

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.mcc.controller.admin.materialtype.vo.*;
import com.miyu.module.mcc.dal.dataobject.materialtype.MaterialTypeDO;
import com.miyu.module.mcc.service.materialtype.MaterialTypeService;

@Tag(name = "管理后台 - 编码类别属性表(树形结构)")
@RestController
@RequestMapping("/mcc/material-type")
@Validated
public class MaterialTypeController {

    @Resource
    private MaterialTypeService materialTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建编码类别属性表(树形结构)")
    @PreAuthorize("@ss.hasPermission('mcc:material-type:create')")
    public CommonResult<String> createMaterialType(@Valid @RequestBody MaterialTypeSaveReqVO createReqVO) {
        return success(materialTypeService.createMaterialType(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新编码类别属性表(树形结构)")
    @PreAuthorize("@ss.hasPermission('mcc:material-type:update')")
    public CommonResult<Boolean> updateMaterialType(@Valid @RequestBody MaterialTypeSaveReqVO updateReqVO) {
        materialTypeService.updateMaterialType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除编码类别属性表(树形结构)")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcc:material-type:delete')")
    public CommonResult<Boolean> deleteMaterialType(@RequestParam("id") String id) {
        materialTypeService.deleteMaterialType(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得编码类别属性表(树形结构)")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcc:material-type:query')")
    public CommonResult<MaterialTypeRespVO> getMaterialType(@RequestParam("id") String id) {
        MaterialTypeDO materialType = materialTypeService.getMaterialType(id);
        return success(BeanUtils.toBean(materialType, MaterialTypeRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得编码类别属性表(树形结构)列表")
    @PreAuthorize("@ss.hasPermission('mcc:material-type:query')")
    public CommonResult<List<MaterialTypeRespVO>> getMaterialTypeList(@Valid MaterialListReqVO listReqVO) {
        List<MaterialTypeDO> list = materialTypeService.getMaterialTypeList(listReqVO);
        return success(BeanUtils.toBean(list, MaterialTypeRespVO.class));
    }

    @GetMapping("/config/list")
    @Operation(summary = "获得编码类别属性表(树形结构)列表")
    @PreAuthorize("@ss.hasPermission('mcc:material-type:query')")
    public CommonResult<List<MaterialTypeRespVO>> getMaterialTypeConfigList(@Valid MaterialListReqVO listReqVO) {
        List<MaterialTypeDO> list = materialTypeService.getMaterialTypeConfigList(listReqVO);
        return success(BeanUtils.toBean(list, MaterialTypeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出编码类别属性表(树形结构) Excel")
    @PreAuthorize("@ss.hasPermission('mcc:material-type:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialTypeExcel(@Valid MaterialListReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<MaterialTypeDO> list = materialTypeService.getMaterialTypeList(listReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "编码类别属性表(树形结构).xls", "数据", MaterialTypeRespVO.class,
                        BeanUtils.toBean(list, MaterialTypeRespVO.class));
    }

}
