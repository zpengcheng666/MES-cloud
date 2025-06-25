package com.miyu.module.qms.controller.admin.inspectionitem;

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

import com.miyu.module.qms.controller.admin.inspectionitem.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionitem.InspectionItemDO;
import com.miyu.module.qms.service.inspectionitem.InspectionItemService;

@Tag(name = "管理后台 - 检测项目")
@RestController
@RequestMapping("/qms/inspection-item")
@Validated
public class InspectionItemController {

    @Resource
    private InspectionItemService inspectionItemService;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @PostMapping("/create")
    @Operation(summary = "创建检测项目")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item:create')")
    public CommonResult<String> createInspectionItem(@Valid @RequestBody InspectionItemSaveReqVO createReqVO) {
        return success(inspectionItemService.createInspectionItem(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检测项目")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item:update')")
    public CommonResult<Boolean> updateInspectionItem(@Valid @RequestBody InspectionItemSaveReqVO updateReqVO) {
        inspectionItemService.updateInspectionItem(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检测项目")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-item:delete')")
    public CommonResult<Boolean> deleteInspectionItem(@RequestParam("id") String id) {
        inspectionItemService.deleteInspectionItem(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检测项目")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item:query')")
    public CommonResult<InspectionItemRespVO> getInspectionItem(@RequestParam("id") String id) {
        InspectionItemDO inspectionItem = inspectionItemService.getInspectionItem(id);
        return success(BeanUtils.toBean(inspectionItem, InspectionItemRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检测项目分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item:query')")
    public CommonResult<PageResult<InspectionItemRespVO>> getInspectionItemPage(@Valid InspectionItemPageReqVO pageReqVO) {
        PageResult<InspectionItemDO> pageResult = inspectionItemService.getInspectionItemPage(pageReqVO);
        // 物料类型
        List<String> materialConfigIds = pageResult.getList().stream().filter(item-> ObjectUtil.isNotNull(item.getMaterialConfigId())).distinct().map(item -> item.getMaterialConfigId()).collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        return success(BeanUtils.toBean(pageResult, InspectionItemRespVO.class,vo -> {
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setInspectionToolName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
            }
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检测项目 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-item:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionItemExcel(@Valid InspectionItemPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionItemDO> list = inspectionItemService.getInspectionItemPage(pageReqVO).getList();
        // 物料类型
        List<String> materialConfigIds = list.stream().filter(item-> ObjectUtil.isNotNull(item.getMaterialConfigId())).distinct().map(item -> item.getMaterialConfigId()).collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        // 导出 Excel
        ExcelUtils.write(response, "检测项目.xls", "数据", InspectionItemRespVO.class,
                        BeanUtils.toBean(list, InspectionItemRespVO.class,vo -> {
                            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                                vo.setInspectionToolName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
                            }
                        }));
    }



}
