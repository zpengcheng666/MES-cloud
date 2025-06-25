package com.miyu.module.qms.controller.admin.inspectionitemtype;

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

import com.miyu.module.qms.controller.admin.inspectionitemtype.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionitemtype.InspectionItemTypeDO;
import com.miyu.module.qms.service.inspectionitemtype.InspectionItemTypeService;

@Tag(name = "管理后台 - 检测项目分类")
@RestController
@RequestMapping("/qms/inspection-item-type")
@Validated
public class InspectionItemTypeController {

    @Resource
    private InspectionItemTypeService inspectionItemTypeService;

    @PostMapping("/create")
    @Operation(summary = "创建检测项目分类")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-type:create')")
    public CommonResult<String> createInspectionItemType(@Valid @RequestBody InspectionItemTypeSaveReqVO createReqVO) {
        return success(inspectionItemTypeService.createInspectionItemType(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检测项目分类")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-type:update')")
    public CommonResult<Boolean> updateInspectionItemType(@Valid @RequestBody InspectionItemTypeSaveReqVO updateReqVO) {
        inspectionItemTypeService.updateInspectionItemType(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检测项目分类")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-type:delete')")
    public CommonResult<Boolean> deleteInspectionItemType(@RequestParam("id") String id) {
        inspectionItemTypeService.deleteInspectionItemType(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检测项目分类")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-type:query')")
    public CommonResult<InspectionItemTypeRespVO> getInspectionItemType(@RequestParam("id") String id) {
        InspectionItemTypeDO inspectionItemType = inspectionItemTypeService.getInspectionItemType(id);
        return success(BeanUtils.toBean(inspectionItemType, InspectionItemTypeRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得检测项目分类列表")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-type:query')")
    public CommonResult<List<InspectionItemTypeRespVO>> getInspectionItemTypeList(@Valid InspectionItemTypeListReqVO listReqVO) {
        List<InspectionItemTypeDO> list = inspectionItemTypeService.getInspectionItemTypeList(listReqVO);
        return success(BeanUtils.toBean(list, InspectionItemTypeRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检测项目分类 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-type:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionItemTypeExcel(@Valid InspectionItemTypeListReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<InspectionItemTypeDO> list = inspectionItemTypeService.getInspectionItemTypeList(listReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "检测项目分类.xls", "数据", InspectionItemTypeRespVO.class,
                        BeanUtils.toBean(list, InspectionItemTypeRespVO.class));
    }

}