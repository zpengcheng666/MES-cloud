package com.miyu.module.qms.controller.admin.inspectionsheetrecord;

import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetTempUpdateReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetUpdateAuditReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetUpdateReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheet.vo.InspectionSheetUpdateTerminalReqVO;
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

import com.miyu.module.qms.controller.admin.inspectionsheetrecord.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetrecord.InspectionSheetRecordDO;
import com.miyu.module.qms.service.inspectionsheetrecord.InspectionSheetRecordService;

@Tag(name = "管理后台 - 检验记录")
@RestController
@RequestMapping("/qms/inspection-sheet-record")
@Validated
public class InspectionSheetRecordController {

    @Resource
    private InspectionSheetRecordService inspectionSheetRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建检验记录")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-record:create')")
    public CommonResult<String> createInspectionSheetRecord(@Valid @RequestBody InspectionSheetRecordSaveReqVO createReqVO) {
        return success(inspectionSheetRecordService.createInspectionSheetRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验记录")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-record:update')")
    public CommonResult<Boolean> updateInspectionSheetRecord(@Valid @RequestBody InspectionSheetRecordSaveReqVO updateReqVO) {
        inspectionSheetRecordService.updateInspectionSheetRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检验记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-record:delete')")
    public CommonResult<Boolean> deleteInspectionSheetRecord(@RequestParam("id") String id) {
        inspectionSheetRecordService.deleteInspectionSheetRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-record:query')")
    public CommonResult<InspectionSheetRecordRespVO> getInspectionSheetRecord(@RequestParam("id") String id) {
        InspectionSheetRecordDO inspectionSheetRecord = inspectionSheetRecordService.getInspectionSheetRecord(id);
        return success(BeanUtils.toBean(inspectionSheetRecord, InspectionSheetRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检验记录分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-record:query')")
    public CommonResult<PageResult<InspectionSheetRecordRespVO>> getInspectionSheetRecordPage(@Valid InspectionSheetRecordPageReqVO pageReqVO) {
        PageResult<InspectionSheetRecordDO> pageResult = inspectionSheetRecordService.getInspectionSheetRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionSheetRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检验记录 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionSheetRecordExcel(@Valid InspectionSheetRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionSheetRecordDO> list = inspectionSheetRecordService.getInspectionSheetRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检验记录.xls", "数据", InspectionSheetRecordRespVO.class,
                        BeanUtils.toBean(list, InspectionSheetRecordRespVO.class));
    }

    @GetMapping("/list-record-by-material-id")
    @Operation(summary = "获得检测项目详情列表")
    @Parameter(name = "materialId", description = "检测单产品ID", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<List<InspectionSheetRecordRespVO>> getInspectionSheetRecordListByMaterialId(@RequestParam("id") String id) {
        return success(BeanUtils.toBean(inspectionSheetRecordService.getInspectionSheetRecordListByMaterialId(id), InspectionSheetRecordRespVO.class));
    }

    @PostMapping("/update-inspection-record")
    @Operation(summary = "产品检验")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionRecord(@Valid @RequestBody InspectionSheetUpdateReqVO updateReqVO) {
        // 设置产品检验状态
        // updateReqVO.setStatus(1);
        inspectionSheetRecordService.updateInspectionRecord(updateReqVO);
        return success(true);
    }

    @PostMapping("/update-inspection-record-temp")
    @Operation(summary = "产品检验")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionRecordTemp(@Valid @RequestBody InspectionSheetTempUpdateReqVO updateReqVO) {
        updateReqVO.setTempSave("1");
        inspectionSheetRecordService.updateInspectionRecord(BeanUtils.toBean(updateReqVO, InspectionSheetUpdateReqVO.class));
        return success(true);
    }

    /** 作废 **/
    @PostMapping("/update-inspection-record-and-audit")
    @Operation(summary = "产品自检并提交审核")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionRecordAndAudit(@Valid @RequestBody InspectionSheetUpdateAuditReqVO updateReqVO) {
        inspectionSheetRecordService.updateInspectionRecordAndAudit(updateReqVO);
        return success(true);
    }

    @GetMapping("/terminal/list")
    @Operation(summary = "获得检测项目详情列表")
    @Parameter(name = "id", description = "检测单产品ID", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:query')")
    public CommonResult<Map<String, Object>> getInspectionSheetRecordList4Terminal(InspectionSheetRecordReqVO reqVO) {
        return success(inspectionSheetRecordService.getInspectionSheetRecordList4Terminal(reqVO));
    }

    @PostMapping("/update-inspection-record-terminal")
    @Operation(summary = "产品检验")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateInspectionRecordTerminal(@Valid @RequestBody InspectionSheetUpdateTerminalReqVO updateReqVO) {
        inspectionSheetRecordService.updateInspectionRecordTerminal(updateReqVO);
        return success(true);
    }

    @PostMapping("/terminal/updateMcsRecordBegin")
    @Operation(summary = "工序检验开始")
    @PreAuthorize("@ss.hasPermission('qms:inspection-sheet:update')")
    public CommonResult<Boolean> updateMcsStepBegin(@Valid @RequestBody InspectionSheetRecordReqVO updateReqVO) {
        inspectionSheetRecordService.updateMcsRecordBegin(updateReqVO);
        return success(true);
    }
}
