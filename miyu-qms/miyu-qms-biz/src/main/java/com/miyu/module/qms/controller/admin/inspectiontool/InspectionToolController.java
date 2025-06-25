package com.miyu.module.qms.controller.admin.inspectiontool;

import cn.hutool.core.util.ObjectUtil;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
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
import java.util.stream.Collectors;

import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;

import com.miyu.module.qms.controller.admin.inspectiontool.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectiontool.InspectionToolDO;
import com.miyu.module.qms.service.inspectiontool.InspectionToolService;

@Tag(name = "管理后台 - 检测工具")
@RestController
@RequestMapping("/qms/inspection-tool")
@Validated
public class InspectionToolController {

    @Resource
    private InspectionToolService inspectionToolService;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @PostMapping("/create")
    @Operation(summary = "创建检测工具")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool:create')")
    public CommonResult<String> createInspectionTool(@Valid @RequestBody InspectionToolSaveReqVO createReqVO) {
        return success(inspectionToolService.createInspectionTool(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检测工具")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool:update')")
    public CommonResult<Boolean> updateInspectionTool(@Valid @RequestBody InspectionToolSaveReqVO updateReqVO) {
        inspectionToolService.updateInspectionTool(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检测工具")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool:delete')")
    public CommonResult<Boolean> deleteInspectionTool(@RequestParam("id") String id) {
        inspectionToolService.deleteInspectionTool(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检测工具")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool:query')")
    public CommonResult<InspectionToolRespVO> getInspectionTool(@RequestParam("id") String id) {
        InspectionToolDO inspectionTool = inspectionToolService.getInspectionTool(id);
        return success(BeanUtils.toBean(inspectionTool, InspectionToolRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检测工具分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool:query')")
    public CommonResult<PageResult<InspectionToolRespVO>> getInspectionToolPage(@Valid InspectionToolPageReqVO pageReqVO) {
        PageResult<InspectionToolDO> pageResult = inspectionToolService.getInspectionToolPage(pageReqVO);
        // 物料类型
        List<String> materialConfigIds = pageResult.getList().stream().filter(item-> ObjectUtil.isNotNull(item.getMaterialConfigId())).distinct().map(item -> item.getMaterialConfigId()).collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        return success(BeanUtils.toBean(pageResult, InspectionToolRespVO.class, vo -> {
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setMaterialConfigName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
            }
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检测工具 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionToolExcel(@Valid InspectionToolPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionToolDO> list = inspectionToolService.getInspectionToolPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检测工具.xls", "数据", InspectionToolRespVO.class,
                        BeanUtils.toBean(list, InspectionToolRespVO.class));
    }


    @GetMapping("/list")
    @Operation(summary = "获得检测工具集合")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool:query')")
    public CommonResult<List<InspectionToolRespVO>> getInspectionToolList() {
        List<InspectionToolDO> result = inspectionToolService.getInspectionToolList();
        // 物料类型
        List<String> materialConfigIds = result.stream().filter(item-> ObjectUtil.isNotNull(item.getMaterialConfigId())).distinct().map(item -> item.getMaterialConfigId()).collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        return success(BeanUtils.toBean(result, InspectionToolRespVO.class,vo -> {
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setMaterialConfigName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
            }
        }));
    }
}
