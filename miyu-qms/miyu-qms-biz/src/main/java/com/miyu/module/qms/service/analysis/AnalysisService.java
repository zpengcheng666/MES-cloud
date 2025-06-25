package com.miyu.module.qms.service.analysis;

import com.miyu.module.qms.controller.admin.analysis.vo.*;

import java.util.List;

/**
 * 质量分析 Service 接口
 *
 * @author zhp
 */
public interface AnalysisService {


    /***
     * 质检数量统计
     * @param vo
     * @return
     */
    AnalysisNumberResp getAnalysisNumber(AnalysisReqVO vo);
    /**
     * 获取不同批次产品的检验合格率
     * @return
     */
    List<BatchAnalysisResp> getBatchAnalysis(AnalysisReqVO vo);

    /***
     * 缺陷统计
     * @param vo
     * @return
     */
    List<DefectiveAnalysisResp> getDefectives(AnalysisReqVO vo);


    /**
     * 获取不同批次产品的检验合格率
     * @return
     */
    List<ItemAnalysisResp> getItemAnalysis(AnalysisReqVO vo);


    /***
     * 工序质检统计
     * @param vo
     * @return
     */
    List<ProcessAnalysisResp> getProcessAnalysis(AnalysisReqVO vo);


    /**
     * 工人加工质检统计
     * @param vo
     * @return
     */
    List<WorkerAnalysisResp> getWorkerAnalysis(AnalysisReqVO vo);


    ScrapAndRepairResp getScrapAndRepair(AnalysisReqVO vo);

    /**
     * 检验工时统计
     * @param reqVO
     * @return
     */
    List<WorkerHoursAnalysisResp> getWorkerHoursAnalysis(AnalysisReqVO reqVO);
}
