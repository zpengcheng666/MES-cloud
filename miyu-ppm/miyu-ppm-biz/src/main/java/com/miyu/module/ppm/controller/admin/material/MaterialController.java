package com.miyu.module.ppm.controller.admin.material;

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

import com.miyu.module.ppm.controller.admin.material.vo.*;
import com.miyu.module.ppm.dal.dataobject.material.MaterialDO;
import com.miyu.module.ppm.service.material.MaterialService;

@Tag(name = "管理后台 - 物料基本信息")
@RestController
@RequestMapping("/ppm/material")
@Validated
public class MaterialController {

    @Resource
    private MaterialService materialService;

    @PostMapping("/create")
    @Operation(summary = "创建物料基本信息")
    @PreAuthorize("@ss.hasPermission('ppm:material:create')")
    public CommonResult<String> createMaterial(@Valid @RequestBody MaterialSaveReqVO createReqVO) {
        return success(materialService.createMaterial(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料基本信息")
    @PreAuthorize("@ss.hasPermission('ppm:material:update')")
    public CommonResult<Boolean> updateMaterial(@Valid @RequestBody MaterialSaveReqVO updateReqVO) {
        materialService.updateMaterial(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料基本信息")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('ppm:material:delete')")
    public CommonResult<Boolean> deleteMaterial(@RequestParam("id") String id) {
        materialService.deleteMaterial(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料基本信息")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('ppm:material:query')")
    public CommonResult<MaterialRespVO> getMaterial(@RequestParam("id") String id) {
        MaterialDO material = materialService.getMaterial(id);
        return success(BeanUtils.toBean(material, MaterialRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料基本信息分页")
    @PreAuthorize("@ss.hasPermission('ppm:material:query')")
    public CommonResult<PageResult<MaterialRespVO>> getMaterialPage(@Valid MaterialPageReqVO pageReqVO) {
        PageResult<MaterialDO> pageResult = materialService.getMaterialPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料基本信息 Excel")
    @PreAuthorize("@ss.hasPermission('ppm:material:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialExcel(@Valid MaterialPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaterialDO> list = materialService.getMaterialPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料基本信息.xls", "数据", MaterialRespVO.class,
                        BeanUtils.toBean(list, MaterialRespVO.class));
    }

    @GetMapping("/list")
    public CommonResult<List<MaterialRespVO>> getMaterialList() {
        List<MaterialDO> list = materialService.getMaterialList();
        return success(BeanUtils.toBean(list, MaterialRespVO.class));
    }
}