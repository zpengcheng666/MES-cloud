package com.miyu.module.qms.controller.admin.inspectionsheetgeneratetaskdetail;

import com.miyu.module.wms.api.mateiral.MaterialStockApi;
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
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.*;
import com.miyu.module.qms.controller.admin.inspectionsheetgeneratetaskdetail.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailDO;
import com.miyu.module.qms.service.inspectionsheetgeneratetaskdetail.InspectionSheetGenerateTaskDetailService;

@Tag(name = "管理后台 - 检验单生成任务明细")
@RestController
@RequestMapping("/qms/inspection-sheet-generate-task-detail")
@Validated
public class InspectionSheetGenerateTaskDetailController {

    @Resource
    private InspectionSheetGenerateTaskDetailService inspectionSheetGenerateTaskDetailService;

    @Resource
    private MaterialStockApi materialStockApi;

    @PostMapping("/create")
    @Operation(summary = "创建检验单生成任务明细")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task-detail:create')")
    public CommonResult<String> createInspectionSheetGenerateTaskDetail(@Valid @RequestBody InspectionSheetGenerateTaskDetailSaveReqVO createReqVO) {
        return success(inspectionSheetGenerateTaskDetailService.createInspectionSheetGenerateTaskDetail(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验单生成任务明细")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task-detail:update')")
    public CommonResult<Boolean> updateInspectionSheetGenerateTaskDetail(@Valid @RequestBody InspectionSheetGenerateTaskDetailSaveReqVO updateReqVO) {
        inspectionSheetGenerateTaskDetailService.updateInspectionSheetGenerateTaskDetail(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检验单生成任务明细")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task-detail:delete')")
    public CommonResult<Boolean> deleteInspectionSheetGenerateTaskDetail(@RequestParam("id") String id) {
        inspectionSheetGenerateTaskDetailService.deleteInspectionSheetGenerateTaskDetail(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验单生成任务明细")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task-detail:query')")
    public CommonResult<InspectionSheetGenerateTaskDetailRespVO> getInspectionSheetGenerateTaskDetail(@RequestParam("id") String id) {
        InspectionSheetGenerateTaskDetailDO inspectionSheetGenerateTaskDetail = inspectionSheetGenerateTaskDetailService.getInspectionSheetGenerateTaskDetail(id);
        return success(BeanUtils.toBean(inspectionSheetGenerateTaskDetail, InspectionSheetGenerateTaskDetailRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检验单生成任务明细分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task-detail:query')")
    public CommonResult<PageResult<InspectionSheetGenerateTaskDetailRespVO>> getInspectionSheetGenerateTaskDetailPage(@Valid InspectionSheetGenerateTaskDetailPageReqVO pageReqVO) {
        PageResult<InspectionSheetGenerateTaskDetailDO> pageResult = inspectionSheetGenerateTaskDetailService.getInspectionSheetGenerateTaskDetailPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionSheetGenerateTaskDetailRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检验单生成任务明细 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-generate-task-detail:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionSheetGenerateTaskDetailExcel(@Valid InspectionSheetGenerateTaskDetailPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionSheetGenerateTaskDetailDO> list = inspectionSheetGenerateTaskDetailService.getInspectionSheetGenerateTaskDetailPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检验单生成任务明细.xls", "数据", InspectionSheetGenerateTaskDetailRespVO.class,
                        BeanUtils.toBean(list, InspectionSheetGenerateTaskDetailRespVO.class));
    }

}
