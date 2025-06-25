package com.miyu.module.qms.controller.admin.inspectionsheetgeneratetask;

import cn.hutool.core.util.ObjectUtil;
import com.miyu.module.mcc.api.materialconfig.MaterialMCCApi;
import com.miyu.module.mcc.api.materialconfig.dto.MaterialConfigRespDTO;
import com.miyu.module.qms.service.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailService;
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
import com.miyu.module.qms.controller.admin.inspectionsheetgeneratetask.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetask.InspectionSheetGenerateTaskDO;
import com.miyu.module.qms.service.inspectionsheetgeneratetask.InspectionSheetGenerateTaskService;

@Tag(name = "管理后台 - 检验单")
@RestController
@RequestMapping("/qms/inspection-sheet-generate-task")
@Validated
public class InspectionSheetGenerateTaskController {

    @Resource
    private InspectionSheetGenerateTaskService inspectionSheetGenerateTaskService;

    @Resource
    private InspectionSheetGenerateTaskDetailService inspectionSheetGenerateTaskDetailService;

    @Resource
    private MaterialMCCApi materialMCCApi;

    @PostMapping("/create")
    @Operation(summary = "创建检验单")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task:create')")
    public CommonResult<String> createInspectionSheetGenerateTask(@Valid @RequestBody InspectionSheetGenerateTaskSaveReqVO createReqVO) {
        return success(inspectionSheetGenerateTaskService.createInspectionSheetGenerateTask(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验单")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task:update')")
    public CommonResult<Boolean> updateInspectionSheetGenerateTask(@Valid @RequestBody InspectionSheetGenerateTaskSaveReqVO updateReqVO) {
        inspectionSheetGenerateTaskService.updateInspectionSheetGenerateTask(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检验单")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task:delete')")
    public CommonResult<Boolean> deleteInspectionSheetGenerateTask(@RequestParam("id") String id) {
        inspectionSheetGenerateTaskService.deleteInspectionSheetGenerateTask(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验单")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task:query')")
    public CommonResult<InspectionSheetGenerateTaskRespVO> getInspectionSheetGenerateTask(@RequestParam("id") String id) {
        InspectionSheetGenerateTaskDO inspectionSheetGenerateTask = inspectionSheetGenerateTaskService.getInspectionSheetGenerateTask(id);
        // 物料类型
        List<String> materialConfigIds = new ArrayList<>(Arrays.asList(inspectionSheetGenerateTask.getMaterialConfigId()));
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        return success(BeanUtils.toBean(inspectionSheetGenerateTask, InspectionSheetGenerateTaskRespVO.class, vo -> {
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setMaterialConfigName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
                vo.setQuantity(inspectionSheetGenerateTaskDetailService.getInspectionSheetGenerateTaskDetailListByTaskId(id).size());
                vo.setTaskId(vo.getId());
                vo.setId(null);
            }
        }));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检验单分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task:query')")
    public CommonResult<PageResult<InspectionSheetGenerateTaskRespVO>> getInspectionSheetGenerateTaskPage(@Valid InspectionSheetGenerateTaskPageReqVO pageReqVO) {
        PageResult<InspectionSheetGenerateTaskDO> pageResult = inspectionSheetGenerateTaskService.getInspectionSheetGenerateTaskPage(pageReqVO);

        List<String> materialConfigIds = pageResult.getList().stream().filter(item-> ObjectUtil.isNotNull(item.getMaterialConfigId())).distinct().map(item -> item.getMaterialConfigId()).collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        return success(BeanUtils.toBean(pageResult, InspectionSheetGenerateTaskRespVO.class, vo -> {
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setMaterialConfigName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
                vo.setMaterialNumber(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialNumber());
            }
        }));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检验单 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionSheetGenerateTaskExcel(@Valid InspectionSheetGenerateTaskPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionSheetGenerateTaskDO> list = inspectionSheetGenerateTaskService.getInspectionSheetGenerateTaskPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检验单.xls", "数据", InspectionSheetGenerateTaskRespVO.class,
                        BeanUtils.toBean(list, InspectionSheetGenerateTaskRespVO.class));
    }

    @GetMapping("/task/generate/page")
    @Operation(summary = "获得检验任务")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task:query')")
    public CommonResult<PageResult<InspectionSheetGenerateTaskRespVO>> getInspectionSheetGenerateTaskListPage(InspectionSheetGenerateTaskPageReqVO pageReqVO) {
        PageResult<InspectionSheetGenerateTaskDO> pageResult = inspectionSheetGenerateTaskService.getInspectionSheetGenerateTaskListPage(pageReqVO);
        List<String> materialConfigIds = pageResult.getList().stream().filter(item-> ObjectUtil.isNotNull(item.getMaterialConfigId())).distinct().map(item -> item.getMaterialConfigId()).collect(Collectors.toList());
        Map<String, MaterialConfigRespDTO> materialTypeMap = materialMCCApi.getMaterialConfigMap(materialConfigIds);
        return success(BeanUtils.toBean(pageResult, InspectionSheetGenerateTaskRespVO.class, vo -> {
            if(materialTypeMap.containsKey(vo.getMaterialConfigId())){
                vo.setMaterialConfigName(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialName());
                vo.setMaterialNumber(materialTypeMap.get(vo.getMaterialConfigId()).getMaterialNumber());
            }
        }));
    }
}
