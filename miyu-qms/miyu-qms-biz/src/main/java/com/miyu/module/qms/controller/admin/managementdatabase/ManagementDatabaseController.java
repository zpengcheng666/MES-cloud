package com.miyu.module.qms.controller.admin.managementdatabase;

import com.miyu.module.qms.dal.dataobject.managementdatabasefile.ManagementDatabaseFileDO;
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
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;

import com.miyu.module.qms.controller.admin.managementdatabase.vo.*;
import com.miyu.module.qms.dal.dataobject.managementdatabase.ManagementDatabaseDO;
import com.miyu.module.qms.service.managementdatabase.ManagementDatabaseService;

@Tag(name = "管理后台 - 质量管理资料库")
@RestController
@RequestMapping("/qms/management-database")
@Validated
public class ManagementDatabaseController {

    @Resource
    private ManagementDatabaseService managementDatabaseService;

    @PostMapping("/create")
    @Operation(summary = "创建质量管理资料库")
    @PreAuthorize("@ss.hasPermission('qms:management-database:create')")
    public CommonResult<String> createManagementDatabase(@Valid @RequestBody ManagementDatabaseSaveReqVO createReqVO) {
        return success(managementDatabaseService.createManagementDatabase(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新质量管理资料库")
    @PreAuthorize("@ss.hasPermission('qms:management-database:update')")
    public CommonResult<Boolean> updateManagementDatabase(@Valid @RequestBody ManagementDatabaseSaveReqVO updateReqVO) {
        managementDatabaseService.updateManagementDatabase(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除质量管理资料库")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:management-database:delete')")
    public CommonResult<Boolean> deleteManagementDatabase(@RequestParam("id") String id) {
        managementDatabaseService.deleteManagementDatabase(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得质量管理资料库")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:management-database:query')")
    public CommonResult<ManagementDatabaseRespVO> getManagementDatabase(@RequestParam("id") String id) {
        ManagementDatabaseDO managementDatabase = managementDatabaseService.getManagementDatabase(id);

        List<ManagementDatabaseFileDO> fileList = managementDatabaseService.getManagementDatabaseFileByDatabaseId(id);
        return success(BeanUtils.toBean(managementDatabase, ManagementDatabaseRespVO.class, o -> {
            o.setFileUrl(fileList.stream().map(ManagementDatabaseFileDO::getFileUrl).collect(Collectors.toList()));
        }));
    }

    @GetMapping("/page")
    @Operation(summary = "获得质量管理资料库分页")
    @PreAuthorize("@ss.hasPermission('qms:management-database:query')")
    public CommonResult<PageResult<ManagementDatabaseRespVO>> getManagementDatabasePage(@Valid ManagementDatabasePageReqVO pageReqVO) {
        PageResult<ManagementDatabaseDO> pageResult = managementDatabaseService.getManagementDatabasePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ManagementDatabaseRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出质量管理资料库 Excel")
    @PreAuthorize("@ss.hasPermission('qms:management-database:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportManagementDatabaseExcel(@Valid ManagementDatabasePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ManagementDatabaseDO> list = managementDatabaseService.getManagementDatabasePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "质量管理资料库.xls", "数据", ManagementDatabaseRespVO.class,
                        BeanUtils.toBean(list, ManagementDatabaseRespVO.class));
    }

    @PutMapping("/submit")
    @Operation(summary = "提交审核")
    @PreAuthorize("@ss.hasPermission('qms:management-database:update')")
    public CommonResult<Boolean> submitManagementDatabase(@RequestParam("id") String id,  @RequestParam("processKey") String processKey) {
        managementDatabaseService.submitManagementDatabase(id, processKey, getLoginUserId());
        return success(true);
    }
}