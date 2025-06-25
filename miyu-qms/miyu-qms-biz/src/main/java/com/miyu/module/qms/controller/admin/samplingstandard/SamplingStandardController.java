package com.miyu.module.qms.controller.admin.samplingstandard;

import com.miyu.module.qms.controller.admin.inspectionitemtype.vo.InspectionItemTypeListReqVO;
import com.miyu.module.qms.controller.admin.inspectionitemtype.vo.InspectionItemTypeRespVO;
import com.miyu.module.qms.dal.dataobject.inspectionitemtype.InspectionItemTypeDO;
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

import com.miyu.module.qms.controller.admin.samplingstandard.vo.*;
import com.miyu.module.qms.dal.dataobject.samplingstandard.SamplingStandardDO;
import com.miyu.module.qms.service.samplingstandard.SamplingStandardService;

@Tag(name = "管理后台 - 抽样标准")
@RestController
@RequestMapping("/qms/sampling-standard")
@Validated
public class SamplingStandardController {

    @Resource
    private SamplingStandardService samplingStandardService;

    @PostMapping("/create")
    @Operation(summary = "创建抽样标准")
    @PreAuthorize("@ss.hasPermission('qms:sampling-standard:create')")
    public CommonResult<String> createSamplingStandard(@Valid @RequestBody SamplingStandardSaveReqVO createReqVO) {
        return success(samplingStandardService.createSamplingStandard(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新抽样标准")
    @PreAuthorize("@ss.hasPermission('qms:sampling-standard:update')")
    public CommonResult<Boolean> updateSamplingStandard(@Valid @RequestBody SamplingStandardSaveReqVO updateReqVO) {
        samplingStandardService.updateSamplingStandard(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除抽样标准")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('qms:sampling-standard:delete')")
    public CommonResult<Boolean> deleteSamplingStandard(@RequestParam("id") String id) {
        samplingStandardService.deleteSamplingStandard(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得抽样标准")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('qms:sampling-standard:query')")
    public CommonResult<SamplingStandardRespVO> getSamplingStandard(@RequestParam("id") String id) {
        SamplingStandardDO samplingStandard = samplingStandardService.getSamplingStandard(id);
        return success(BeanUtils.toBean(samplingStandard, SamplingStandardRespVO.class));
    }


    @GetMapping("/export-excel")
    @Operation(summary = "导出抽样标准 Excel")
    @PreAuthorize("@ss.hasPermission('qms:sampling-standard:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportSamplingStandardExcel(@Valid SamplingStandardListReqVO listReqVO,
              HttpServletResponse response) throws IOException {
        List<SamplingStandardDO> list = samplingStandardService.getSamplingStandardList(listReqVO);
        // 导出 Excel
        ExcelUtils.write(response, "抽样标准.xls", "数据", SamplingStandardRespVO.class,
                        BeanUtils.toBean(list, SamplingStandardRespVO.class));
    }

    @GetMapping("/list")
    @Operation(summary = "获得抽样标准列表")
    @PreAuthorize("@ss.hasPermission('qms:sampling-standard:query')")
    public CommonResult<List<SamplingStandardRespVO>> getSamplingStandardList(@Valid SamplingStandardListReqVO listReqVO) {
        List<SamplingStandardDO> list = samplingStandardService.getSamplingStandardList(listReqVO);
        return success(BeanUtils.toBean(list, SamplingStandardRespVO.class));
    }
}