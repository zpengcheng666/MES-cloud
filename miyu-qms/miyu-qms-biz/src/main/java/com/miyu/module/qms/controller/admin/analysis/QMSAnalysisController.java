package com.miyu.module.qms.controller.admin.analysis;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import com.google.common.collect.Lists;
import com.miyu.module.qms.controller.admin.analysis.vo.*;
import com.miyu.module.qms.service.analysis.AnalysisService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@Tag(name = "管理后台 - 质量分析")
@RestController
@RequestMapping("/qms/analysis")
@Validated
public class QMSAnalysisController {

    @Resource
    private AnalysisService analysisService;


    /***
     * 获取质检数量统计
     * @param reqVO
     * @return
     */
    @GetMapping("/getAnalysisNumber")
    @Operation(summary = "质检数量统计")
    public CommonResult<AnalysisNumberResp> getAnalysisNumber(@Valid AnalysisReqVO reqVO) {

        getReq(reqVO);
        AnalysisNumberResp result = analysisService.getAnalysisNumber(reqVO);
        return success(result);
    }


    /***
     * 获取不同批次产品的检验合格率
     * @param reqVO
     * @return
     */
    @GetMapping("/getBatchAnalysis")
    @Operation(summary = "获取不同批次产品的检验合格率")
    public CommonResult<List<BatchAnalysisResp>> getBatchAnalysis(@Valid AnalysisReqVO reqVO) {
        getReq(reqVO);
        List<BatchAnalysisResp> result = analysisService.getBatchAnalysis(reqVO);
        return success(result);
    }

    /***
     * 缺陷统计
     * @param reqVO
     * @return
     */
    @GetMapping("/getDefectives")
    @Operation(summary = "缺陷统计")
    public CommonResult<List<DefectiveAnalysisResp>> getDefectives(@Valid AnalysisReqVO reqVO) {
        getReq(reqVO);
        List<DefectiveAnalysisResp> results = analysisService.getDefectives(reqVO);
        return success(results);
    }

    /***
     * 获取不同检测项的检验合格率
     * @param reqVO
     * @return
     */
    @GetMapping("/getItemAnalysis")
    @Operation(summary = "获取不同检测项的检验统计")
    public CommonResult<List<ItemAnalysisResp>> getItemAnalysis(@Valid AnalysisReqVO reqVO) {
        getReq(reqVO);
        List<ItemAnalysisResp> result = analysisService.getItemAnalysis(reqVO);
        return success(result);
    }


    /***
     * 报废返修率统计
     * @param reqVO
     * @return
     */
    @GetMapping("/getScrapAndRepair")
    @Operation(summary = "报废返修率统计")
    public CommonResult<ScrapAndRepairResp> getScrapAndRepair(@Valid AnalysisReqVO reqVO) {
        getReq(reqVO);
        //TODO
        ScrapAndRepairResp resp = analysisService.getScrapAndRepair(reqVO);
        return success(resp);
    }


    /***
     * 工序质检统计
     * @param reqVO
     * @return
     */
    @GetMapping("/getProcessAnalysis")
    @Operation(summary = "工序质检统计")
    public CommonResult<List<ProcessAnalysisResp>> getProcessAnalysis(@Valid AnalysisReqVO reqVO) {
        getReq(reqVO);
        List<ProcessAnalysisResp> resp = analysisService.getProcessAnalysis(reqVO);

        return success(resp);
    }


    /***
     * 工人生产质量统计
     * @param reqVO
     * @return
     */
    @GetMapping("/getWorkerAnalysis")
    @Operation(summary = "工人生产质量统计")
    public CommonResult<List<WorkerAnalysisResp>> getWorkerAnalysis(@Valid AnalysisReqVO reqVO) {
        getReq(reqVO);
        List<WorkerAnalysisResp> resp = analysisService.getWorkerAnalysis(reqVO);
        return success(resp);
    }


    /***
     * 检验工时统计
     * @param reqVO
     * @return
     */
    @GetMapping("/getWorkerHoursAnalysis")
    @Operation(summary = "检验工时统计")
    public CommonResult<List<WorkerHoursAnalysisResp>> getWorkerHoursAnalysis(@Valid AnalysisReqVO reqVO) {
        getReq(reqVO);
        List<WorkerHoursAnalysisResp> resp = analysisService.getWorkerHoursAnalysis(reqVO);
        return success(resp);
    }

    private static void getReq(AnalysisReqVO reqVO) {

        //默认查询当月的
        if (reqVO.getQueryTime() ==null) {
            LocalDateTime now = LocalDateTime.now();
            YearMonth yearMonth = YearMonth.from(now);
            LocalDateTime firstDayOfMonthAtMidnight = yearMonth.atDay(1).atStartOfDay();

            LocalDateTime[] localDateTimes = new LocalDateTime[2];
            localDateTimes[0] = firstDayOfMonthAtMidnight;
            localDateTimes[1] = now;
            reqVO.setQueryTime(localDateTimes);
        }


    }

}
