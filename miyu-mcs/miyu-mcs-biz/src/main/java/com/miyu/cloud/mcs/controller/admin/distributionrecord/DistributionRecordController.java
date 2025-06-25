package com.miyu.cloud.mcs.controller.admin.distributionrecord;

import com.miyu.cloud.dms.service.ledger.LedgerService;
import com.miyu.cloud.dms.service.linestationgroup.LineStationGroupService;
import org.apache.commons.lang3.StringUtils;
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

import com.miyu.cloud.mcs.controller.admin.distributionrecord.vo.*;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import com.miyu.cloud.mcs.service.distributionrecord.DistributionRecordService;

@Tag(name = "管理后台 - 物料配送申请详情")
@RestController
@RequestMapping("/mcs/distribution-record")
@Validated
public class DistributionRecordController {

    @Resource
    private DistributionRecordService distributionRecordService;

    @PostMapping("/create")
    @Operation(summary = "创建物料配送申请详情")
    public CommonResult<String> createDistributionRecord(@Valid @RequestBody DistributionRecordSaveReqVO createReqVO) {
        return success(distributionRecordService.createDistributionRecord(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新物料配送申请详情")
    public CommonResult<Boolean> updateDistributionRecord(@Valid @RequestBody DistributionRecordSaveReqVO updateReqVO) {
        distributionRecordService.updateDistributionRecord(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除物料配送申请详情")
    @Parameter(name = "id", description = "编号", required = true)
    public CommonResult<Boolean> deleteDistributionRecord(@RequestParam("id") String id) {
        distributionRecordService.deleteDistributionRecord(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得物料配送申请详情")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    public CommonResult<DistributionRecordRespVO> getDistributionRecord(@RequestParam("id") String id) {
        DistributionRecordDO distributionRecord = distributionRecordService.getDistributionRecord(id);
        return success(BeanUtils.toBean(distributionRecord, DistributionRecordRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得物料配送申请详情分页")
    public CommonResult<PageResult<DistributionRecordRespVO>> getDistributionRecordPage(@Valid DistributionRecordPageReqVO pageReqVO) {
        PageResult<DistributionRecordDO> pageResult = distributionRecordService.getDistributionRecordPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, DistributionRecordRespVO.class));
    }

    @GetMapping("/pageAll")
    @Operation(summary = "获得物料配送申请详情分页")
    public CommonResult<PageResult<DistributionRecordRespVO>> getDistributionRecordPageAll(@Valid DistributionRecordPageReqVO pageReqVO) {
        return success(distributionRecordService.getDistributionRecordPageAll(pageReqVO));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出物料配送申请详情 Excel")
    @ApiAccessLog(operateType = EXPORT)
    public void exportDistributionRecordExcel(@Valid DistributionRecordPageReqVO pageReqVO,
              HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<DistributionRecordDO> list = distributionRecordService.getDistributionRecordPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "物料配送申请详情.xls", "数据", DistributionRecordRespVO.class,
                        BeanUtils.toBean(list, DistributionRecordRespVO.class));
    }

    @GetMapping("/listByApplication")
    @Operation(summary = "获得物料配送申请详情列表")
    @Parameter(name = "applicationId", description = "申请id")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:query')")
    public CommonResult<List<DistributionRecordDO>> listByApplication(@RequestParam("applicationId") String applicationId) {
        return success(distributionRecordService.listByApplication(applicationId));
    }

    @PostMapping("/recordRevokeById")
    @Operation(summary = "获得物料配送申请详情列表")
    @PreAuthorize("@ss.hasPermission('mcs:distribution-application:query')")
    public CommonResult<String> recordRevokeById(@RequestParam("id") String id) {
        distributionRecordService.recordRevokeById(id);
        return success("操作成功");
    }

}
