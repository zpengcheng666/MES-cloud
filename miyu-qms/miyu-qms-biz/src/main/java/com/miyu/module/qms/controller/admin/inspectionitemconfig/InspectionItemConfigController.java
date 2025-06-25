package com.miyu.module.qms.controller.admin.inspectionitemconfig;

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

import com.miyu.module.qms.controller.admin.inspectionitemconfig.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionitemconfig.InspectionItemConfigDO;
import com.miyu.module.qms.service.inspectionitemconfig.InspectionItemConfigService;

@Tag(name = "管理后台 - 检测项配置表（检测内容名称）")
@RestController
@RequestMapping("/qms/inspection-item-config")
@Validated
public class InspectionItemConfigController {

    @Resource
    private InspectionItemConfigService inspectionItemConfigService;

    @PostMapping("/create")
    @Operation(summary = "创建检测项配置表（检测内容名称）")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-config:create')")
    public CommonResult<String> createInspectionItemConfig(@Valid @RequestBody InspectionItemConfigSaveReqVO createReqVO) {
        return success(inspectionItemConfigService.createInspectionItemConfig(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检测项配置表（检测内容名称）")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-config:update')")
    public CommonResult<Boolean> updateInspectionItemConfig(@Valid @RequestBody InspectionItemConfigSaveReqVO updateReqVO) {
        inspectionItemConfigService.updateInspectionItemConfig(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检测项配置表（检测内容名称）")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-config:delete')")
    public CommonResult<Boolean> deleteInspectionItemConfig(@RequestParam("id") String id) {
        inspectionItemConfigService.deleteInspectionItemConfig(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检测项配置表（检测内容名称）")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-config:query')")
    public CommonResult<InspectionItemConfigRespVO> getInspectionItemConfig(@RequestParam("id") String id) {
        InspectionItemConfigDO inspectionItemConfig = inspectionItemConfigService.getInspectionItemConfig(id);
        return success(BeanUtils.toBean(inspectionItemConfig, InspectionItemConfigRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检测项配置表（检测内容名称）分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-config:query')")
    public CommonResult<PageResult<InspectionItemConfigRespVO>> getInspectionItemConfigPage(@Valid InspectionItemConfigPageReqVO pageReqVO) {
        PageResult<InspectionItemConfigDO> pageResult = inspectionItemConfigService.getInspectionItemConfigPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionItemConfigRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检测项配置表（检测内容名称） Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-config:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionItemConfigExcel(@Valid InspectionItemConfigPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionItemConfigDO> list = inspectionItemConfigService.getInspectionItemConfigPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检测项配置表（检测内容名称）.xls", "数据", InspectionItemConfigRespVO.class,
                        BeanUtils.toBean(list, InspectionItemConfigRespVO.class));
    }




    @GetMapping("/list")
    @Operation(summary = "获得检测项配置表（检测内容名称）集合")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item-config:query')")
    public CommonResult<List<InspectionItemConfigRespVO>> getInspectionItemConfigList(@Valid InspectionItemConfigPageReqVO pageReqVO) {
        List<InspectionItemConfigDO> result = inspectionItemConfigService.getInspectionItemConfigList();
        return success(BeanUtils.toBean(result, InspectionItemConfigRespVO.class));
    }

}