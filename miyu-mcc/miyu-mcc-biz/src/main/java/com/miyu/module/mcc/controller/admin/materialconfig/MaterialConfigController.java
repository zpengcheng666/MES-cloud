package com.miyu.module.mcc.controller.admin.materialconfig;

import org.apache.commons.lang3.StringUtils;
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

import com.miyu.module.mcc.controller.admin.materialconfig.vo.*;
import com.miyu.module.mcc.dal.dataobject.materialconfig.MaterialConfigDO;
import com.miyu.module.mcc.service.materialconfig.MaterialConfigService;

@Tag(name = "管理后台 - 物料类型")
@RestController
@RequestMapping("/mcc/material-config")
@Validated
public class MaterialConfigController {

    @Resource
    private MaterialConfigService materialConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建物料类型")
    @PreAuthorize("@ss.hasPermission('mcc:material-config:create')")
    public CommonResult<String> createMaterialConfig(@Valid @RequestBody MaterialConfigSaveReqVO createReqVO) {
        return success(materialConfigService.createMaterialConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料类型")
    @PreAuthorize("@ss.hasPermission('mcc:material-config:update')")
    public CommonResult<Boolean> updateMaterialConfig(@Valid @RequestBody MaterialConfigSaveReqVO updateReqVO) {
        materialConfigService.updateMaterialConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料类型")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcc:material-config:delete')")
    public CommonResult<Boolean> deleteMaterialConfig(@RequestParam("id") String id) {
        materialConfigService.deleteMaterialConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料类型")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcc:material-config:query')")
    public CommonResult<MaterialConfigRespVO> getMaterialConfig(@RequestParam("id") String id) {
        MaterialConfigDO materialConfig = materialConfigService.getMaterialConfig(id);
        if (StringUtils.isNotBlank(materialConfig.getMaterialSourceId())){
            MaterialConfigDO source = materialConfigService.getMaterialConfig(materialConfig.getMaterialSourceId());
            materialConfig.setMaterialNumberSource(source.getMaterialNumber());
            materialConfig.setMaterialSourceName(source.getMaterialName()+"("+source.getMaterialNumber()+")");
        }
        return success(BeanUtils.toBean(materialConfig, MaterialConfigRespVO.class));
    }


    @GetMapping("/list")
    @Operation(summary = "获得物料类型列表")
    @PreAuthorize("@ss.hasPermission('wms:material-config:query')")
    public CommonResult<List<MaterialConfigRespVO>> getMaterialConfig() {
        List<MaterialConfigDO> list = materialConfigService.getMaterialConfigList();
        return success(BeanUtils.toBean(list, MaterialConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料类型分页")
    @PreAuthorize("@ss.hasPermission('mcc:material-config:query')")
    public CommonResult<PageResult<MaterialConfigRespVO>> getMaterialConfigPage(@Valid MaterialConfigPageReqVO pageReqVO) {
        PageResult<MaterialConfigDO> pageResult = materialConfigService.getMaterialConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MaterialConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料类型 Excel")
    @PreAuthorize("@ss.hasPermission('mcc:material-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportMaterialConfigExcel(@Valid MaterialConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MaterialConfigDO> list = materialConfigService.getMaterialConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料类型.xls", "数据", MaterialConfigRespVO.class,
                        BeanUtils.toBean(list, MaterialConfigRespVO.class));
    }

}