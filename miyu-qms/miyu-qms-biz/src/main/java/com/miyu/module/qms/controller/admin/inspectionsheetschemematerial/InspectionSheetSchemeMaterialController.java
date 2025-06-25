package com.miyu.module.qms.controller.admin.inspectionsheetschemematerial;

import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.InspectionMaterialUpdateReqVO;
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
import static cn.iocoder.yudao.framework.common.util.collection.CollectionUtils.convertList;

import com.miyu.module.qms.controller.admin.inspectionsheetschemematerial.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetschemematerial.InspectionSheetSchemeMaterialDO;
import com.miyu.module.qms.service.inspectionsheetschemematerial.InspectionSheetSchemeMaterialService;

@Tag(name = "管理后台 - 检验单产品")
@RestController
@RequestMapping("/qms/inspection-sheet-scheme-material")
@Validated
public class InspectionSheetSchemeMaterialController {

    @Resource
    private InspectionSheetSchemeMaterialService inspectionSheetSchemeMaterialService;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @PostMapping("/create")
    @Operation(summary = "创建检验单产品")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme-material:create')")
    public CommonResult<String> createInspectionSheetSchemeMaterial(@Valid @RequestBody InspectionSheetSchemeMaterialSaveReqVO createReqVO) {
        return success(inspectionSheetSchemeMaterialService.createInspectionSheetSchemeMaterial(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验单产品")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme-material:update')")
    public CommonResult<Boolean> updateInspectionSheetSchemeMaterial(@Valid @RequestBody InspectionSheetSchemeMaterialSaveReqVO updateReqVO) {
        inspectionSheetSchemeMaterialService.updateInspectionSheetSchemeMaterial(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检验单产品")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme-material:delete')")
    public CommonResult<Boolean> deleteInspectionSheetSchemeMaterial(@RequestParam("id") String id) {
        inspectionSheetSchemeMaterialService.deleteInspectionSheetSchemeMaterial(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验单产品")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme-material:query')")
    public CommonResult<InspectionSheetSchemeMaterialRespVO> getInspectionSheetSchemeMaterial(@RequestParam("id") String id) {
        InspectionSheetSchemeMaterialDO inspectionSheetSchemeMaterial = inspectionSheetSchemeMaterialService.getInspectionSheetSchemeMaterial(id);
        return success(BeanUtils.toBean(inspectionSheetSchemeMaterial, InspectionSheetSchemeMaterialRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检验单产品分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme-material:query')")
    public CommonResult<PageResult<InspectionSheetSchemeMaterialRespVO>> getInspectionSheetSchemeMaterialPage(@Valid InspectionSheetSchemeMaterialPageReqVO pageReqVO) {
        PageResult<InspectionSheetSchemeMaterialDO> pageResult = inspectionSheetSchemeMaterialService.getInspectionSheetSchemeMaterialPage(pageReqVO);

        List<String> configIds = convertList(pageResult.getList(), InspectionSheetSchemeMaterialDO::getMaterialConfigId);
        configIds = configIds.stream().distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(configIds);

        return success(BeanUtils.toBean(pageResult, InspectionSheetSchemeMaterialRespVO.class, vo -> {
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setMaterialName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
                vo.setMaterialTypeName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialTypeName());
                vo.setMaterialSpecification(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialSpecification());
                vo.setMaterialUnit(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialUnit());
            }
        }));
    }

    @GetMapping("/list-by-scheme-id")
    @Operation(summary = "获得检验单产品")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme-material:query')")
    public CommonResult<List<InspectionSheetSchemeMaterialRespVO>> getInspectionSheetSchemeMaterialListBySchemeId(@RequestParam("schemeId")  String schemeId) {
        List<InspectionSheetSchemeMaterialDO> list = inspectionSheetSchemeMaterialService.getInspectionSheetSchemeMaterialListBySchemeId(schemeId);

        List<String> configIds = convertList(list, InspectionSheetSchemeMaterialDO::getMaterialConfigId);
        configIds = configIds.stream().distinct().collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(configIds);

        return success(BeanUtils.toBean(list, InspectionSheetSchemeMaterialRespVO.class, vo -> {
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setMaterialName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
                vo.setMaterialNumber(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialNumber());
                vo.setMaterialTypeName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialTypeName());
                vo.setMaterialSpecification(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialSpecification());
                vo.setMaterialUnit(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialUnit());
            }
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检验单产品 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme-material:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionSheetSchemeMaterialExcel(@Valid InspectionSheetSchemeMaterialPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionSheetSchemeMaterialDO> list = inspectionSheetSchemeMaterialService.getInspectionSheetSchemeMaterialPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检验单产品.xls", "数据", InspectionSheetSchemeMaterialRespVO.class,
                        BeanUtils.toBean(list, InspectionSheetSchemeMaterialRespVO.class));
    }

    @PostMapping("/update-inspection-material-result")
    @Operation(summary = "产品检验")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionMaterialResult(@Valid @RequestBody InspectionMaterialUpdateReqVO updateReqVO) {
        inspectionSheetSchemeMaterialService.updateInspectionMaterialResult(updateReqVO);
        return success(true);
    }

    @GetMapping("/list-unqualified-material-by-scheme-id")
    @Operation(summary = "检验任务ID获取不合格品集合")
    @Parameter(name = "id", description = "检验任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<List<InspectionSheetSchemeMaterialRespVO>> getUnqualifiedMaterialListBySchemeId(@RequestParam("id") String id) {
        return success(BeanUtils.toBean(inspectionSheetSchemeMaterialService.getUnqualifiedMaterialListBySchemeId(id), InspectionSheetSchemeMaterialRespVO.class));
    }

    @GetMapping("/list-unqualified-material-defective-by-scheme-id")
    @Operation(summary = "检验任务ID获取不合格品集合,产品维度查看缺陷代码")
    @Parameter(name = "id", description = "检验任务ID", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<List<InspectionSheetSchemeMaterialRespVO>> getUnqualifiedMaterialDefectiveListBySchemeId(@RequestParam("id") String id) {
        return success(BeanUtils.toBean(inspectionSheetSchemeMaterialService.getUnqualifiedMaterialDefectiveListBySchemeId(id), InspectionSheetSchemeMaterialRespVO.class));
    }

    @GetMapping("/task/page")
    @Operation(summary = "检验任务叫料获得检验单产品分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-scheme-material:query')")
    public CommonResult<PageResult<InspectionSheetSchemeMaterialRespVO>> getInspectionSheetSchemeMaterialTaskPage(@Valid InspectionSheetSchemeMaterialPageReqVO pageReqVO) {
        PageResult<InspectionSheetSchemeMaterialDO> pageResult = inspectionSheetSchemeMaterialService.getInspectionSheetSchemeMaterialTaskPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionSheetSchemeMaterialRespVO.class));
    }
}
