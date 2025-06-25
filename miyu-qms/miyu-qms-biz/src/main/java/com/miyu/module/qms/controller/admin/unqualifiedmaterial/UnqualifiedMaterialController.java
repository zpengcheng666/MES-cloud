package com.miyu.module.qms.controller.admin.unqualifiedmaterial;

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

import com.miyu.module.qms.controller.admin.unqualifiedmaterial.vo.*;
import com.miyu.module.qms.dal.dataobject.unqualifiedmaterial.UnqualifiedMaterialDO;
import com.miyu.module.qms.service.unqualifiedmaterial.UnqualifiedMaterialService;

@Tag(name = "管理后台 - 不合格品产品")
@RestController
@RequestMapping("/qms/unqualified-material")
@Validated
public class UnqualifiedMaterialController {

    @Resource
    private UnqualifiedMaterialService unqualifiedMaterialService;

    @PostMapping("/create")
    @Operation(summary = "创建不合格品产品")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-material:create')")
    public CommonResult<String> createUnqualifiedMaterial(@Valid @RequestBody UnqualifiedMaterialSaveReqVO createReqVO) {
        return success(unqualifiedMaterialService.createUnqualifiedMaterial(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新不合格品产品")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-material:update')")
    public CommonResult<Boolean> updateUnqualifiedMaterial(@Valid @RequestBody UnqualifiedMaterialSaveReqVO updateReqVO) {
        unqualifiedMaterialService.updateUnqualifiedMaterial(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除不合格品产品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:unqualified-material:delete')")
    public CommonResult<Boolean> deleteUnqualifiedMaterial(@RequestParam("id") String id) {
        unqualifiedMaterialService.deleteUnqualifiedMaterial(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得不合格品产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-material:query')")
    public CommonResult<UnqualifiedMaterialRespVO> getUnqualifiedMaterial(@RequestParam("id") String id) {
        UnqualifiedMaterialDO unqualifiedMaterial = unqualifiedMaterialService.getUnqualifiedMaterial(id);
        return success(BeanUtils.toBean(unqualifiedMaterial, UnqualifiedMaterialRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得不合格品产品分页")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-material:query')")
    public CommonResult<PageResult<UnqualifiedMaterialRespVO>> getUnqualifiedMaterialPage(@Valid UnqualifiedMaterialPageReqVO pageReqVO) {
        PageResult<UnqualifiedMaterialDO> pageResult = unqualifiedMaterialService.getUnqualifiedMaterialPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, UnqualifiedMaterialRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出不合格品产品 Excel")
    @PreAuthorize("@ss.hasPermission('qms:unqualified-material:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportUnqualifiedMaterialExcel(@Valid UnqualifiedMaterialPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<UnqualifiedMaterialDO> list = unqualifiedMaterialService.getUnqualifiedMaterialPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "不合格品产品.xls", "数据", UnqualifiedMaterialRespVO.class,
                        BeanUtils.toBean(list, UnqualifiedMaterialRespVO.class));
    }

}