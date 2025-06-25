package com.miyu.module.qms.controller.admin.inspectiontoolverificationrecord;

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

import com.miyu.module.qms.controller.admin.inspectiontoolverificationrecord.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectiontoolverificationrecord.InspectionToolVerificationRecordDO;
import com.miyu.module.qms.service.inspectiontoolverificationrecord.InspectionToolVerificationRecordService;

@Tag(name = "管理后台 - 检验工具校准记录")
@RestController
@RequestMapping("/qms/inspection-tool-verification-record")
@Validated
public class InspectionToolVerificationRecordController {

    @Resource
    private InspectionToolVerificationRecordService inspectionToolVerificationRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建检验工具校准记录")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool-verification-record:create')")
    public CommonResult<String> createInspectionToolVerificationRecord(@Valid @RequestBody InspectionToolVerificationRecordSaveReqVO createReqVO) {
        return success(inspectionToolVerificationRecordService.createInspectionToolVerificationRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新检验工具校准记录")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool-verification-record:update')")
    public CommonResult<Boolean> updateInspectionToolVerificationRecord(@Valid @RequestBody InspectionToolVerificationRecordSaveReqVO updateReqVO) {
        inspectionToolVerificationRecordService.updateInspectionToolVerificationRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除检验工具校准记录")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool-verification-record:delete')")
    public CommonResult<Boolean> deleteInspectionToolVerificationRecord(@RequestParam("id") String id) {
        inspectionToolVerificationRecordService.deleteInspectionToolVerificationRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得检验工具校准记录")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool-verification-record:query')")
    public CommonResult<InspectionToolVerificationRecordRespVO> getInspectionToolVerificationRecord(@RequestParam("id") String id) {
        InspectionToolVerificationRecordDO inspectionToolVerificationRecord = inspectionToolVerificationRecordService.getInspectionToolVerificationRecord(id);
        return success(BeanUtils.toBean(inspectionToolVerificationRecord, InspectionToolVerificationRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得检验工具校准记录分页")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool-verification-record:query')")
    public CommonResult<PageResult<InspectionToolVerificationRecordRespVO>> getInspectionToolVerificationRecordPage(@Valid InspectionToolVerificationRecordPageReqVO pageReqVO) {
        PageResult<InspectionToolVerificationRecordDO> pageResult = inspectionToolVerificationRecordService.getInspectionToolVerificationRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionToolVerificationRecordRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出检验工具校准记录 Excel")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool-verification-record:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportInspectionToolVerificationRecordExcel(@Valid InspectionToolVerificationRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<InspectionToolVerificationRecordDO> list = inspectionToolVerificationRecordService.getInspectionToolVerificationRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "检验工具校准记录.xls", "数据", InspectionToolVerificationRecordRespVO.class,
                        BeanUtils.toBean(list, InspectionToolVerificationRecordRespVO.class));
    }

    /**
     * 首页获取待送检集合
     * @return
     */
    @GetMapping("/task/verification/page")
    @Operation(summary = "获得待检验工具集合")
    @PreAuthorize("@ss.hasPermission('qms:inspection-tool-verification-record:query')")
    public CommonResult<PageResult<InspectionToolVerificationRecordRespVO>> getToolVerificationTaskPage(InspectionToolVerificationRecordPageReqVO pageReqVO) {
        PageResult<InspectionToolVerificationRecordDO> pageResult = inspectionToolVerificationRecordService.getToolVerificationTaskPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, InspectionToolVerificationRecordRespVO.class));
    }
}
