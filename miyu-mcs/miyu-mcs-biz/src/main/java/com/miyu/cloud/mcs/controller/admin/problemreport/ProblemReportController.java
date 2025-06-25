package com.miyu.cloud.mcs.controller.admin.problemreport;

import cn.iocoder.yudao.framework.apilog.core.annotations.ApiAccessLog;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.excel.core.util.ExcelUtils;
import com.miyu.cloud.mcs.controller.admin.problemreport.vo.ProblemReportPageReqVO;
import com.miyu.cloud.mcs.controller.admin.problemreport.vo.ProblemReportRespVO;
import com.miyu.cloud.mcs.controller.admin.problemreport.vo.ProblemReportSaveReqVO;
import com.miyu.cloud.mcs.dal.dataobject.problemreport.ProblemReportDO;
import com.miyu.cloud.mcs.service.problemreport.ProblemReportService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

import static cn.iocoder.yudao.framework.apilog.core.enums.OperateTypeEnum.EXPORT;
import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 问题上报")
@RestController
@RequestMapping("/mcs/problem-report")
@Validated
public class ProblemReportController {

    @Resource
    private ProblemReportService problemReportService;

    @PostMapping("/create")
    @Operation(summary = "创建问题上报")
    @PreAuthorize("@ss.hasPermission('mcs:problem-report:create')")
    public CommonResult<String> createProblemReport(@Valid @RequestBody ProblemReportSaveReqVO createReqVO) {
        return success(problemReportService.createProblemReport(createReqVO));
    }

    @PutMapping("/update")
    @Operation(summary = "更新问题上报")
    @PreAuthorize("@ss.hasPermission('mcs:problem-report:update')")
    public CommonResult<Boolean> updateProblemReport(@Valid @RequestBody ProblemReportSaveReqVO updateReqVO) {
        problemReportService.updateProblemReport(updateReqVO);
        return success(true);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "删除问题上报")
    @Parameter(name = "id", description = "编号", required = true)
    @PreAuthorize("@ss.hasPermission('mcs:problem-report:delete')")
    public CommonResult<Boolean> deleteProblemReport(@RequestParam("id") String id) {
        problemReportService.deleteProblemReport(id);
        return success(true);
    }

    @GetMapping("/get")
    @Operation(summary = "获得问题上报")
    @Parameter(name = "id", description = "编号", required = true, example = "1024")
    @PreAuthorize("@ss.hasPermission('mcs:problem-report:query')")
    public CommonResult<ProblemReportRespVO> getProblemReport(@RequestParam("id") String id) {
        ProblemReportDO problemReport = problemReportService.getProblemReport(id);
        return success(BeanUtils.toBean(problemReport, ProblemReportRespVO.class));
    }

    @GetMapping("/page")
    @Operation(summary = "获得问题上报分页")
    @PreAuthorize("@ss.hasPermission('mcs:problem-report:query')")
    public CommonResult<PageResult<ProblemReportRespVO>> getProblemReportPage(@Valid ProblemReportPageReqVO pageReqVO) {
        PageResult<ProblemReportDO> pageResult = problemReportService.getProblemReportPage(pageReqVO);
        return success(BeanUtils.toBean(pageResult, ProblemReportRespVO.class));
    }

    @GetMapping("/export-excel")
    @Operation(summary = "导出问题上报 Excel")
    @PreAuthorize("@ss.hasPermission('mcs:problem-report:export')")
    @ApiAccessLog(operateType = EXPORT)
    public void exportProblemReportExcel(@Valid ProblemReportPageReqVO pageReqVO,
                                         HttpServletResponse response) throws IOException {
        pageReqVO.setPageSize(PageParam.PAGE_SIZE_NONE);
        List<ProblemReportDO> list = problemReportService.getProblemReportPage(pageReqVO).getList();
        // 导出 Excel
        ExcelUtils.write(response, "问题上报.xls", "数据", ProblemReportRespVO.class,
                BeanUtils.toBean(list, ProblemReportRespVO.class));
    }

    @GetMapping("/getList")
    @Operation(summary = "获得问题上报列表")
    public CommonResult<List<ProblemReportRespVO>> getProblemReportList(@RequestParam(value = "stationId", required = false) String stationId) {
        List<ProblemReportDO> problemReportDOList = problemReportService.getProblemReportList(stationId);
        return success(BeanUtils.toBean(problemReportDOList, ProblemReportRespVO.class));
    }

}
