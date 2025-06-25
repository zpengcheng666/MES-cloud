package com.miyu.cloud.mcs.controller.admin.batchrecordstep;

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

import com.miyu.cloud.mcs.controller.admin.batchrecordstep.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.batchrecordstep.BatchRecordStepDO;
import com.miyu.cloud.mcs.service.batchrecordstep.BatchRecordStepService;

@Tag(name = "管理后台 - 工步计划")
@RestController
@RequestMapping("/mcs/batch-record-step")
@Validated
public class BatchRecordStepController {

    @Resource
    private BatchRecordStepService batchRecordStepService;

    @PostMapping("/create")
    @Operation(summary = "创建工步计划")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record-step:create')")
    public CommonResult<String> createBatchRecordStep(@Valid @RequestBody BatchRecordStepSaveReqVO createReqVO) {
        return success(batchRecordStepService.createBatchRecordStep(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新工步计划")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record-step:update')")
    public CommonResult<Boolean> updateBatchRecordStep(@Valid @RequestBody BatchRecordStepSaveReqVO updateReqVO) {
        batchRecordStepService.updateBatchRecordStep(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除工步计划")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:batch-record-step:delete')")
    public CommonResult<Boolean> deleteBatchRecordStep(@RequestParam("id") String id) {
        batchRecordStepService.deleteBatchRecordStep(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得工步计划")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record-step:query')")
    public CommonResult<BatchRecordStepRespVO> getBatchRecordStep(@RequestParam("id") String id) {
        BatchRecordStepDO batchRecordStep = batchRecordStepService.getBatchRecordStep(id);
        return success(BeanUtils.toBean(batchRecordStep, BatchRecordStepRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得工步计划分页")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record-step:query')")
    public CommonResult<PageResult<BatchRecordStepRespVO>> getBatchRecordStepPage(@Valid BatchRecordStepPageReqVO pageReqVO) {
        PageResult<BatchRecordStepDO> pageResult = batchRecordStepService.getBatchRecordStepPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, BatchRecordStepRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出工步计划 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:batch-record-step:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportBatchRecordStepExcel(@Valid BatchRecordStepPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<BatchRecordStepDO> list = batchRecordStepService.getBatchRecordStepPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "工步计划.xls", "数据", BatchRecordStepRespVO.class,
                        BeanUtils.toBean(list, BatchRecordStepRespVO.class));
    }

}
