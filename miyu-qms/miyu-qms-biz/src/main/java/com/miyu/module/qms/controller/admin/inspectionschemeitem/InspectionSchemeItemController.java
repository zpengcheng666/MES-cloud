package com.miyu.module.qms.controller.admin.inspectionschemeitem;

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

import com.miyu.module.qms.controller.admin.inspectionschemeitem.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionschemeitem.InspectionSchemeItemDO;
import com.miyu.module.qms.service.inspectionschemeitem.InspectionSchemeItemService;

@Tag(name = "管理后台 - 检验方案检测项目详情")
@RestController
@RequestMapping("/qms/inspection-scheme-item")
@Validated
public class InspectionSchemeItemController {

    @Resource
    private InspectionSchemeItemService inspectionSchemeItemService;

    @PostMapping("/create")
    @Operation(summary = "创建检验方案检测项目详情")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme-item:create')")
    public CommonResult<String> createInspectionSchemeItem(@Valid @RequestBody InspectionSchemeItemSaveReqVO createReqVO) {
        return success(inspectionSchemeItemService.createInspectionSchemeItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验方案检测项目详情")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme-item:update')")
    public CommonResult<Boolean> updateInspectionSchemeItem(@Valid @RequestBody InspectionSchemeItemSaveReqVO updateReqVO) {
        inspectionSchemeItemService.updateInspectionSchemeItem(updateReqVO);
        return success(true);
    }



    @PostMapping("/create-with-detail")
    @Operation(summary = "创建检验方案检测项目详情并配置检测项")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme-item:create')")
    public CommonResult<String> createInspectionSchemeItemWithDetail(@Valid @RequestBody InspectionSchemeItemSaveReqVO createReqVO) {
        return success(inspectionSchemeItemService.createInspectionSchemeItemWithDetail(createReqVO));
    }

    @PutMapping("/update-with-detail")
    @Operation(summary = "更新检验方案检测项目详情并配置检测项")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme-item:update')")
    public CommonResult<Boolean> updateInspectionSchemeItemWithDetail(@Valid @RequestBody InspectionSchemeItemSaveReqVO updateReqVO) {
        inspectionSchemeItemService.updateInspectionSchemeItemWithDetail(updateReqVO);
        return success(true);
    }




    @DeleteMapping("/delete")
    @Operation(summary = "删除检验方案检测项目详情")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme-item:delete')")
    public CommonResult<Boolean> deleteInspectionSchemeItem(@RequestParam("id") String id) {
        inspectionSchemeItemService.deleteInspectionSchemeItem(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验方案检测项目详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme-item:query')")
    public CommonResult<InspectionSchemeItemRespVO> getInspectionSchemeItem(@RequestParam("id") String id) {
        InspectionSchemeItemDO inspectionSchemeItem = inspectionSchemeItemService.getInspectionSchemeItem(id);
        return success(BeanUtils.toBean(inspectionSchemeItem, InspectionSchemeItemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检验方案检测项目详情分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme-item:query')")
    public CommonResult<PageResult<InspectionSchemeItemRespVO>> getInspectionSchemeItemPage(@Valid InspectionSchemeItemPageReqVO pageReqVO) {
        PageResult<InspectionSchemeItemDO> pageResult = inspectionSchemeItemService.getInspectionSchemeItemPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionSchemeItemRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检验方案检测项目详情 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-scheme-item:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionSchemeItemExcel(@Valid InspectionSchemeItemPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionSchemeItemDO> list = inspectionSchemeItemService.getInspectionSchemeItemPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检验方案检测项目详情.xls", "数据", InspectionSchemeItemRespVO.class,
                        BeanUtils.toBean(list, InspectionSchemeItemRespVO.class));
    }


}