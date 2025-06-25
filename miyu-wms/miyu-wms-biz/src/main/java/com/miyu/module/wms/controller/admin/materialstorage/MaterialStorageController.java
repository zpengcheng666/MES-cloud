package com.miyu.module.wms.controller.admin.materialstorage;

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

import com.miyu.module.wms.controller.admin.materialstorage.vo.*;
import com.miyu.module.wms.dal.dataobject.materialstorage.MaterialStorageDO;
import com.miyu.module.wms.service.materialstorage.MaterialStorageService;

@Tag(name = "管理后台 - 物料储位")
@RestController
@RequestMapping("/wms/material-storage")
@Validated
public class MaterialStorageController {

    @Resource
    private MaterialStorageService materialStorageService;

    @PostMapping("/create")
    @Operation(summary = "创建物料储位")
    @PreAuthorize("@ss.hasPermission('wms:material-storage:create')")
    public CommonResult<String> createMaterialStorage(@Valid @RequestBody MaterialStorageSaveReqVO createReqVO) {
        return success(materialStorageService.createMaterialStorage(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料储位")
    @PreAuthorize("@ss.hasPermission('wms:material-storage:update')")
    public CommonResult<Boolean> updateMaterialStorage(@Valid @RequestBody MaterialStorageSaveReqVO updateReqVO) {
        materialStorageService.updateMaterialStorage(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料储位")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:material-storage:delete')")
    public CommonResult<Boolean> deleteMaterialStorage(@RequestParam("id") String id) {
        materialStorageService.deleteMaterialStorage(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料储位")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:material-storage:query')")
    public CommonResult<MaterialStorageRespVO> getMaterialStorage(@RequestParam("id") String id) {
        MaterialStorageDO materialStorage = materialStorageService.getMaterialStorage(id);
        return success(BeanUtils.toBean(materialStorage, MaterialStorageRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料储位分页")
    @PreAuthorize("@ss.hasPermission('wms:material-storage:query')")
    public CommonResult<PageResult<MaterialStorageRespVO>> getMaterialStoragePage(@Valid MaterialStoragePageReqVO pageReqVO) {
        PageResult<MaterialStorageDO> pageResult = materialStorageService.getMaterialStoragePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialStorageRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料储位 Excel")
    @PreAuthorize("@ss.hasPermission('wms:material-storage:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialStorageExcel(@Valid MaterialStoragePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaterialStorageDO> list = materialStorageService.getMaterialStoragePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料储位.xls", "数据", MaterialStorageRespVO.class,
                        BeanUtils.toBean(list, MaterialStorageRespVO.class));
    }


    @GetMapping("/list")
    @Operation(summary = "list")
    @PreAuthorize("@ss.hasPermission('wms:material-storage:export')")
    public CommonResult<List<MaterialStoragePageReqVO>> getMaterialStorageList() {
        List<MaterialStorageDO> list = materialStorageService.getMaterialStorageList();
        return success(BeanUtils.toBean(list, MaterialStoragePageReqVO.class));
    }
}
