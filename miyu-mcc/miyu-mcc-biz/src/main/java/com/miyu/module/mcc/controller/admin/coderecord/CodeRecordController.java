package com.miyu.module.mcc.controller.admin.coderecord;

import com.miyu.module.mcc.api.encodingrule.dto.GeneratorCodeReqDTO;
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

import com.miyu.module.mcc.controller.admin.coderecord.vo.*;
import com.miyu.module.mcc.dal.dataobject.coderecord.CodeRecordDO;
import com.miyu.module.mcc.service.coderecord.CodeRecordService;
@Tag(name = "管理后台 - 编码记录")
@RestController
@RequestMapping("/mcc/code-record")
@Validated
public class CodeRecordController {

    @Resource
    private CodeRecordService codeRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建编码记录")
    @PreAuthorize("@ss.hasPermission('mcc:code-record:create')")
    public CommonResult<String> createCodeRecord(@Valid @RequestBody CodeRecordSaveReqVO createReqVO) {
        return success(codeRecordService.createCodeRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新编码记录")
    @PreAuthorize("@ss.hasPermission('mcc:code-record:update')")
    public CommonResult<Boolean> updateCodeRecord(@Valid @RequestBody CodeRecordSaveReqVO updateReqVO) {
        codeRecordService.updateCodeRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除编码记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcc:code-record:delete')")
    public CommonResult<Boolean> deleteCodeRecord(@RequestParam("id") String id) {
        codeRecordService.deleteCodeRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得编码记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcc:code-record:query')")
    public CommonResult<CodeRecordRespVO> getCodeRecord(@RequestParam("id") String id) {
        CodeRecordDO codeRecord = codeRecordService.getCodeRecord(id);
        return success(BeanUtils.toBean(codeRecord, CodeRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得编码记录分页")
    @PreAuthorize("@ss.hasPermission('mcc:code-record:query')")
    public CommonResult<PageResult<CodeRecordRespVO>> getCodeRecordPage(@Valid CodeRecordPageReqVO pageReqVO) {
        PageResult<CodeRecordDO> pageResult = codeRecordService.getCodeRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, CodeRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出编码记录 Excel")
    @PreAuthorize("@ss.hasPermission('mcc:code-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportCodeRecordExcel(@Valid CodeRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<CodeRecordDO> list = codeRecordService.getCodeRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "编码记录.xls", "数据", CodeRecordRespVO.class,
                        BeanUtils.toBean(list, CodeRecordRespVO.class));
    }



}