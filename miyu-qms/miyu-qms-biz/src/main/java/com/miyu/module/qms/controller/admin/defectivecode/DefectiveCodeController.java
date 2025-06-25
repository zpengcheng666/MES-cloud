package com.miyu.module.qms.controller.admin.defectivecode;

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

import com.miyu.module.qms.controller.admin.defectivecode.vo.*;
import com.miyu.module.qms.dal.dataobject.defectivecode.DefectiveCodeDO;
import com.miyu.module.qms.service.defectivecode.DefectiveCodeService;

@Tag(name = "管理后台 - 缺陷代码")
@RestController
@RequestMapping("/qms/defective-code")
@Validated
public class DefectiveCodeController {

    @Resource
    private DefectiveCodeService defectiveCodeService;

    @PostMapping("/create")
    @Operation(summary = "创建缺陷代码")
    @PreAuthorize("@ss.hasPermission('qms:defective-code:create')")
    public CommonResult<String> createDefectiveCode(@Valid @RequestBody DefectiveCodeSaveReqVO createReqVO) {
        return success(defectiveCodeService.createDefectiveCode(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新缺陷代码")
    @PreAuthorize("@ss.hasPermission('qms:defective-code:update')")
    public CommonResult<Boolean> updateDefectiveCode(@Valid @RequestBody DefectiveCodeSaveReqVO updateReqVO) {
        defectiveCodeService.updateDefectiveCode(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除缺陷代码")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:defective-code:delete')")
    public CommonResult<Boolean> deleteDefectiveCode(@RequestParam("id") String id) {
        defectiveCodeService.deleteDefectiveCode(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得缺陷代码")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:defective-code:query')")
    public CommonResult<DefectiveCodeRespVO> getDefectiveCode(@RequestParam("id") String id) {
        DefectiveCodeDO defectiveCode = defectiveCodeService.getDefectiveCode(id);
        return success(BeanUtils.toBean(defectiveCode, DefectiveCodeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得缺陷代码分页")
    @PreAuthorize("@ss.hasPermission('qms:defective-code:query')")
    public CommonResult<PageResult<DefectiveCodeRespVO>> getDefectiveCodePage(@Valid DefectiveCodePageReqVO pageReqVO) {
        PageResult<DefectiveCodeDO> pageResult = defectiveCodeService.getDefectiveCodePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DefectiveCodeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出缺陷代码 Excel")
    @PreAuthorize("@ss.hasPermission('qms:defective-code:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDefectiveCodeExcel(@Valid DefectiveCodePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DefectiveCodeDO> list = defectiveCodeService.getDefectiveCodePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "缺陷代码.xls", "数据", DefectiveCodeRespVO.class,
                        BeanUtils.toBean(list, DefectiveCodeRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得缺陷代码集合")
    @PreAuthorize("@ss.hasPermission('qms:defective-code:query')")
    public CommonResult<List<DefectiveCodeRespVO>> getDefectiveCodeList() {
        return success(BeanUtils.toBean(defectiveCodeService.getDefectiveCodeList(), DefectiveCodeRespVO.class));
    }
}