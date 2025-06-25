package com.miyu.module.tms.controller.admin.toolparamtemplate;

import com.miyu.module.mcc.api.materialtype.MaterialTypeApi;
import com.miyu.module.mcc.api.materialtype.dto.MaterialTypeRespDTO;
import com.miyu.module.tms.controller.admin.toolparamtemplatedetail.vo.ToolParamTemplateDetailRespVO;
import com.miyu.module.tms.dal.dataobject.toolparamtemplatedetail.ToolParamTemplateDetailDO;
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

import com.miyu.module.tms.controller.admin.toolparamtemplate.vo.*;
import com.miyu.module.tms.dal.dataobject.toolparamtemplate.ToolParamTemplateDO;
import com.miyu.module.tms.service.toolparamtemplate.ToolParamTemplateService;

@Tag(name = "管理后台 - 刀具参数模板")
@RestController
@RequestMapping("/tms/tool-param-template")
@Validated
public class ToolParamTemplateController {

    @Resource
    private ToolParamTemplateService toolParamTemplateService;

    @Resource
    private MaterialTypeApi materialTypeApi;

    @PostMapping("/create")
    @Operation(summary = "创建刀具参数模板")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template:create')")
    public CommonResult<String> createToolParamTemplate(@Valid @RequestBody ToolParamTemplateSaveReqVO createReqVO) {
        return success(toolParamTemplateService.createToolParamTemplate(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新刀具参数模板")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template:update')")
    public CommonResult<Boolean> updateToolParamTemplate(@Valid @RequestBody ToolParamTemplateSaveReqVO updateReqVO) {
        toolParamTemplateService.updateToolParamTemplate(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除刀具参数模板")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template:delete')")
    public CommonResult<Boolean> deleteToolParamTemplate(@RequestParam("id") String id) {
        toolParamTemplateService.deleteToolParamTemplate(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得刀具参数模板")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template:query')")
    public CommonResult<ToolParamTemplateRespVO> getToolParamTemplate(@RequestParam("id") String id) {
        ToolParamTemplateDO toolParamTemplate = toolParamTemplateService.getToolParamTemplate(id);
        return success(BeanUtils.toBean(toolParamTemplate, ToolParamTemplateRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得刀具参数模板分页")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template:query')")
    public CommonResult<PageResult<ToolParamTemplateRespVO>> getToolParamTemplatePage(@Valid ToolParamTemplatePageReqVO pageReqVO) {
        PageResult<ToolParamTemplateDO> pageResult = toolParamTemplateService.getToolParamTemplatePage(pageReqVO);
        HashSet<String> materialTypeIds = new HashSet<>();
        pageResult.getList().forEach(item -> {
            materialTypeIds.add(item.getMaterialTypeId());
        });

        // 获取物料类型信息
        List<MaterialTypeRespDTO> materialTypeList = materialTypeApi.getMaterialTypeList(materialTypeIds).getCheckedData();
        Map<String, String> materialTypeMap = materialTypeList.stream().collect(Collectors.toMap(MaterialTypeRespDTO::getId, MaterialTypeRespDTO::getName, (a, b) -> b));
        return success(BeanUtils.toBean(pageResult, ToolParamTemplateRespVO.class, o -> {
                if(materialTypeMap.containsKey(o.getMaterialTypeId())){
                    o.setMaterialTypeName(materialTypeMap.get(o.getMaterialTypeId()));
            }
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出刀具参数模板 Excel")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportToolParamTemplateExcel(@Valid ToolParamTemplatePageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ToolParamTemplateDO> list = toolParamTemplateService.getToolParamTemplatePage(pageReqVO).getList();
        HashSet<String> materialTypeIds = new HashSet<>();
        list.forEach(item -> {
            materialTypeIds.add(item.getMaterialTypeId());
        });
        // 获取物料类型信息
        List<MaterialTypeRespDTO> materialTypeList = materialTypeApi.getMaterialTypeList(materialTypeIds).getCheckedData();
        Map<String, String> materialTypeMap = materialTypeList.stream().collect(Collectors.toMap(MaterialTypeRespDTO::getId, MaterialTypeRespDTO::getName, (a, b) -> b));
        // 导出 Excel
        ExcelUtils.write(response, "刀具参数模板.xls", "数据", ToolParamTemplateRespVO.class,
                        BeanUtils.toBean(list, ToolParamTemplateRespVO.class, o -> {
            if(materialTypeMap.containsKey(o.getMaterialTypeId())){
                o.setMaterialTypeName(materialTypeMap.get(o.getMaterialTypeId()));
            }
        }));
    }



    @GetMapping("/getToolParamTemplateByMaterialTypeId")
    @Operation(summary = "物料类别ID获得刀具参数模板")
    @Parameter(name = "materialTypeId", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('tms:tool-param-template:query')")
    public CommonResult<List<ToolParamTemplateDetailRespVO>> getToolParamTemplateByMaterialTypeId(@RequestParam("materialTypeId") String materialTypeId) {
        List<ToolParamTemplateDetailDO> templateDetailList = toolParamTemplateService.getToolParamTemplateByMaterialTypeId(materialTypeId);
        return success(BeanUtils.toBean(templateDetailList, ToolParamTemplateDetailRespVO.class));
    }
}
