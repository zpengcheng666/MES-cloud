package com.miyu.module.mcc.controller.admin.encodingattribute;

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

import com.miyu.module.mcc.controller.admin.encodingattribute.vo.*;
import com.miyu.module.mcc.dal.dataobject.encodingattribute.EncodingAttributeDO;
import com.miyu.module.mcc.service.encodingattribute.EncodingAttributeService;

@Tag(name = "管理后台 - 编码自定义属性")
@RestController
@RequestMapping("/mcc/encoding-attribute")
@Validated
public class EncodingAttributeController {

    @Resource
    private EncodingAttributeService encodingAttributeService;

    @PostMapping("/create")
    @Operation(summary = "创建编码自定义属性")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-attribute:create')")
    public CommonResult<String> createEncodingAttribute(@Valid @RequestBody EncodingAttributeSaveReqVO createReqVO) {
        return success(encodingAttributeService.createEncodingAttribute(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新编码自定义属性")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-attribute:update')")
    public CommonResult<Boolean> updateEncodingAttribute(@Valid @RequestBody EncodingAttributeSaveReqVO updateReqVO) {
        encodingAttributeService.updateEncodingAttribute(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除编码自定义属性")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcc:encoding-attribute:delete')")
    public CommonResult<Boolean> deleteEncodingAttribute(@RequestParam("id") String id) {
        encodingAttributeService.deleteEncodingAttribute(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得编码自定义属性")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-attribute:query')")
    public CommonResult<EncodingAttributeRespVO> getEncodingAttribute(@RequestParam("id") String id) {
        EncodingAttributeDO encodingAttribute = encodingAttributeService.getEncodingAttribute(id);
        return success(BeanUtils.toBean(encodingAttribute, EncodingAttributeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得编码自定义属性分页")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-attribute:query')")
    public CommonResult<PageResult<EncodingAttributeRespVO>> getEncodingAttributePage(@Valid EncodingAttributePageReqVO pageReqVO) {
        PageResult<EncodingAttributeDO> pageResult = encodingAttributeService.getEncodingAttributePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, EncodingAttributeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出编码自定义属性 Excel")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-attribute:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportEncodingAttributeExcel(@Valid EncodingAttributePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<EncodingAttributeDO> list = encodingAttributeService.getEncodingAttributePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "编码自定义属性.xls", "数据", EncodingAttributeRespVO.class,
                        BeanUtils.toBean(list, EncodingAttributeRespVO.class));
    }


    @GetMapping("/list")
    @Operation(summary = "获得编码自定义属性集合")
    @PreAuthorize("@ss.hasPermission('mcc:encoding-attribute:query')")
    public CommonResult<List<EncodingAttributeRespVO>> getEncodingAttributeList(@Valid EncodingAttributePageReqVO pageReqVO) {
        List<EncodingAttributeDO> pageResult = encodingAttributeService.getEncodingAttributeList();
        return success(BeanUtils.toBean(pageResult, EncodingAttributeRespVO.class));
    }
}