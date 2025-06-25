package com.miyu.module.wms.controller.admin.materialmaintenance;

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

import com.miyu.module.wms.controller.admin.materialmaintenance.vo.*;
import com.miyu.module.wms.dal.dataobject.materialmaintenance.MaterialMaintenanceDO;
import com.miyu.module.wms.service.materialmaintenance.MaterialMaintenanceService;

@Tag(name = "管理后台 - 物料维护记录")
@RestController
@RequestMapping("/wms/material-maintenance")
@Validated
public class MaterialMaintenanceController {

    @Resource
    private MaterialMaintenanceService materialMaintenanceService;

    @PostMapping("/create")
    @Operation(summary = "创建物料维护记录")
    @PreAuthorize("@ss.hasPermission('wms:material-maintenance:create')")
    public CommonResult<String> createMaterialMaintenance(@Valid @RequestBody MaterialMaintenanceSaveReqVO createReqVO) {
        return success(materialMaintenanceService.createMaterialMaintenance(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料维护记录")
    @PreAuthorize("@ss.hasPermission('wms:material-maintenance:update')")
    public CommonResult<Boolean> updateMaterialMaintenance(@Valid @RequestBody MaterialMaintenanceSaveReqVO updateReqVO) {
        materialMaintenanceService.updateMaterialMaintenance(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料维护记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('wms:material-maintenance:delete')")
    public CommonResult<Boolean> deleteMaterialMaintenance(@RequestParam("id") String id) {
        materialMaintenanceService.deleteMaterialMaintenance(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料维护记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('wms:material-maintenance:query')")
    public CommonResult<MaterialMaintenanceRespVO> getMaterialMaintenance(@RequestParam("id") String id) {
        MaterialMaintenanceDO materialMaintenance = materialMaintenanceService.getMaterialMaintenance(id);
        return success(BeanUtils.toBean(materialMaintenance, MaterialMaintenanceRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料维护记录分页")
    @PreAuthorize("@ss.hasPermission('wms:material-maintenance:query')")
    public CommonResult<PageResult<MaterialMaintenanceRespVO>> getMaterialMaintenancePage(@Valid MaterialMaintenancePageReqVO pageReqVO) {
        PageResult<MaterialMaintenanceDO> pageResult = materialMaintenanceService.getMaterialMaintenancePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialMaintenanceRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料维护记录 Excel")
    @PreAuthorize("@ss.hasPermission('wms:material-maintenance:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialMaintenanceExcel(@Valid MaterialMaintenancePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaterialMaintenanceDO> list = materialMaintenanceService.getMaterialMaintenancePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料维护记录.xls", "数据", MaterialMaintenanceRespVO.class,
                        BeanUtils.toBean(list, MaterialMaintenanceRespVO.class));
    }

}