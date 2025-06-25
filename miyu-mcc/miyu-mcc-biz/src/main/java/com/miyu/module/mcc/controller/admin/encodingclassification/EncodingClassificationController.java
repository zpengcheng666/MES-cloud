package com.miyu.module.mcc.controller.admin.encodingclassification;

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

import com.miyu.module.mcc.controller.admin.encodingclassification.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingclassification.EncodingClassificationDO;
import com.miyu.module.mcc.service.encodingclassification.EncodingClassificationService;

@Tag(name = "管理后台 - 编码分类")
@RestController
@RequestMapping("/mcc/encoding-classification")
@Validated
public class EncodingClassificationController {

    @Resource
    private EncodingClassificationService encodingClassificationService;

    @PostMapping("/create")
    @Operation(summary = "创建编码分类")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-classification:create')")
    public CommonResult<String> createEncodingClassification(@Valid @RequestBody EncodingClassificationSaveReqVO createReqVO) {
        return success(encodingClassificationService.createEncodingClassification(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新编码分类")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-classification:update')")
    public CommonResult<Boolean> updateEncodingClassification(@Valid @RequestBody EncodingClassificationSaveReqVO updateReqVO) {
        encodingClassificationService.updateEncodingClassification(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除编码分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcc:encoding-classification:delete')")
    public CommonResult<Boolean> deleteEncodingClassification(@RequestParam("id") String id) {
        encodingClassificationService.deleteEncodingClassification(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得编码分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-classification:query')")
    public CommonResult<EncodingClassificationRespVO> getEncodingClassification(@RequestParam("id") String id) {
        EncodingClassificationDO encodingClassification = encodingClassificationService.getEncodingClassification(id);
        return success(BeanUtils.toBean(encodingClassification, EncodingClassificationRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得编码分类分页")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-classification:query')")
    public CommonResult<PageResult<EncodingClassificationRespVO>> getEncodingClassificationPage(@Valid EncodingClassificationPageReqVO pageReqVO) {
        PageResult<EncodingClassificationDO> pageResult = encodingClassificationService.getEncodingClassificationPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, EncodingClassificationRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出编码分类 Excel")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-classification:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportEncodingClassificationExcel(@Valid EncodingClassificationPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<EncodingClassificationDO> list = encodingClassificationService.getEncodingClassificationPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "编码分类.xls", "数据", EncodingClassificationRespVO.class,
                        BeanUtils.toBean(list, EncodingClassificationRespVO.class));
    }


    @GetMapping("/list")
    @Operation(summary = "获得编码分类列表")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-classification:query')")
    public CommonResult<List<EncodingClassificationRespVO>> getEncodingClassificationList(@Valid EncodingClassificationPageReqVO pageReqVO) {
        List<EncodingClassificationDO> list = encodingClassificationService.getEncodingClassificationList();
        return success(BeanUtils.toBean(list, EncodingClassificationRespVO.class));
    }

}