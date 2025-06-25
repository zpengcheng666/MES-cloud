package com.miyu.module.qms.controller.admin.managementtree;

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

import com.miyu.module.qms.controller.admin.managementtree.vo.*;
import com.miyu.module.qms.dal.dataobject.managementtree.ManagementTreeDO;
import com.miyu.module.qms.service.managementtree.ManagementTreeService;

@Tag(name = "管理后台 - 质量管理关联树")
@RestController
@RequestMapping("/qms/management-tree")
@Validated
public class ManagementTreeController {

    @Resource
    private ManagementTreeService managementTreeService;

    @PostMapping("/create")
    @Operation(summary = "创建质量管理关联树")
    @PreAuthorize("@ss.hasPermission('qms:management-tree:create')")
    public CommonResult<String> createManagementTree(@Valid @RequestBody ManagementTreeSaveReqVO createReqVO) {
        return success(managementTreeService.createManagementTree(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新质量管理关联树")
    @PreAuthorize("@ss.hasPermission('qms:management-tree:update')")
    public CommonResult<Boolean> updateManagementTree(@Valid @RequestBody ManagementTreeSaveReqVO updateReqVO) {
        managementTreeService.updateManagementTree(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除质量管理关联树")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:management-tree:delete')")
    public CommonResult<Boolean> deleteManagementTree(@RequestParam("id") String id) {
        managementTreeService.deleteManagementTree(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得质量管理关联树")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:management-tree:query')")
    public CommonResult<ManagementTreeRespVO> getManagementTree(@RequestParam("id") String id) {
        ManagementTreeDO managementTree = managementTreeService.getManagementTree(id);
        return success(BeanUtils.toBean(managementTree, ManagementTreeRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得质量管理关联树分页")
    @PreAuthorize("@ss.hasPermission('qms:management-tree:query')")
    public CommonResult<PageResult<ManagementTreeRespVO>> getManagementTreePage(@Valid ManagementTreePageReqVO pageReqVO) {
        PageResult<ManagementTreeDO> pageResult = managementTreeService.getManagementTreePage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ManagementTreeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出质量管理关联树 Excel")
    @PreAuthorize("@ss.hasPermission('qms:management-tree:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportManagementTreeExcel(@Valid ManagementTreePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ManagementTreeDO> list = managementTreeService.getManagementTreePage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "质量管理关联树.xls", "数据", ManagementTreeRespVO.class,
                        BeanUtils.toBean(list, ManagementTreeRespVO.class));
    }

    @GetMapping("/getList")
    @Operation(summary = "获得资料库关联树")
    @PreAuthorize("@ss.hasPermission('qms:management-tree:query')")
    public CommonResult<List<ManagementTreeRespVO>> getManagementTree() {
        List<ManagementTreeDO> managementTreeList = managementTreeService.getManagementTreeList();
        return success(BeanUtils.toBean(managementTreeList, ManagementTreeRespVO.class));
    }
}