package com.miyu.cloud.macs.controller.admin.file;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import com.miyu.cloud.macs.service.file.MacsFileService;
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

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import com.miyu.cloud.macs.controller.admin.file.vo.*;
import com.miyu.cloud.macs.dal.dataobject.file.MacsFileDO;

@Tag(name = "管理后台 - 文件")
@RestController
@RequestMapping("/macs/file")
@Validated
public class MacsFileController {

    @Resource
    private MacsFileService macsFileService;

    @PostMapping("/create")
    @Operation(summary = "创建文件")
    @PreAuthorize("@ss.hasPermission('macs:file:create')")
    public CommonResult<String> createFile(@Valid @RequestBody MacsFileSaveReqVO createReqVO) {
        return success(macsFileService.createFile(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新文件")
    @PreAuthorize("@ss.hasPermission('macs:file:update')")
    public CommonResult<Boolean> updateFile(@Valid @RequestBody MacsFileSaveReqVO updateReqVO) {
        macsFileService.updateFile(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除文件")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('macs:file:delete')")
    public CommonResult<Boolean> deleteFile(@RequestParam("id") String id) {
        macsFileService.deleteFile(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得文件")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('macs:file:query')")
    public CommonResult<MacsFileRespVO> getFile(@RequestParam("id") String id) {
        MacsFileDO file = macsFileService.getFile(id);
        return success(BeanUtils.toBean(file, MacsFileRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得文件分页")
    @PreAuthorize("@ss.hasPermission('macs:file:query')")
    public CommonResult<PageResult<MacsFileRespVO>> getFilePage(@Valid MacsFilePageReqVO pageReqVO) {
        PageResult<MacsFileDO> pageResult = macsFileService.getFilePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, MacsFileRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出文件 Excel")
    @PreAuthorize("@ss.hasPermission('macs:file:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportFileExcel(@Valid MacsFilePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<MacsFileDO> list = macsFileService.getFilePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "文件.xls", "数据", MacsFileRespVO.class,
                        BeanUtils.toBean(list, MacsFileRespVO.class));
    }

}
