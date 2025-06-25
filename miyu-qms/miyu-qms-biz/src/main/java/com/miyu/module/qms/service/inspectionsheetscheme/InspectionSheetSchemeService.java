package com.miyu.module.qms.service.inspectionsheetscheme;

import java.util.*;
import javax.validation.*;

import com.miyu.module.qms.controller.admin.analysis.vo.AnalysisReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.InspectionSchemeUpdateReqVO;
import com.miyu.module.qms.controller.admin.inspectionsheetscheme.vo.*;
import com.miyu.module.qms.dal.dataobject.inspectionsheetscheme.InspectionSheetSchemeDO;

/**
 * 检验单方案任务计划 Service 接口
 *
 * @author Zhangyunfei
 */
public interface InspectionSheetSchemeService {

    /**
     * 创建检验单方案任务计划
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createInspectionSheetScheme(@Valid InspectionSheetSchemeSaveReqVO createReqVO);

    /**
     * 更新检验单方案任务计划
     *
     * @param updateReqVO 更新信息
     */
    void updateInspectionSheetScheme(@Valid InspectionSheetSchemeSaveReqVO updateReqVO);

    /**
     * 删除检验单方案任务计划
     *
     * @param id 编号
     */
    void deleteInspectionSheetScheme(String id);

    /**
     * 获得检验单方案任务计划
     *
     * @param id 编号
     * @return 检验单方案任务计划
     */
    InspectionSheetSchemeDO getInspectionSheetScheme(String id);

    /**
     * 分配检验人员
     * @param inspectionSheetScheme
     */
    void updateInspectionSheetSchemeAssign(InspectionSheetSchemeDO inspectionSheetScheme);


    /**
     * 自检分配检验人员
     * @param inspectionSheetScheme
     */
    void updateInspectionSheetSchemeSelfAssign(InspectionSheetSchemeDO inspectionSheetScheme);

    /**
     * 自检任务认领
     * @param inspectionSheetScheme
     */
    void updateInspectionSheetSchemeClaim(InspectionSheetSchemeDO inspectionSheetScheme);

    /**
     * id获取检验任务
     * @param schemeId
     * @return
     */
    List<InspectionSheetSchemeDO> getInspectionSheetSchemeInfoById(String schemeId);


    /**
     * 更新检验任务眼见结果
     * @param updateReqVO
     */
    void updateInspectionSchemeResult(InspectionSchemeUpdateReqVO updateReqVO);


    /***
     * 根据时间和物料类型获取该段时间内的生产检验单
     * @param vo
     * @return
     */
    List<InspectionSheetSchemeDO> getInspectionSheetSchemes(AnalysisReqVO vo);

    /****
     * 根据条件获取所有完成的质检单
     * @param vo
     * @return
     */
    List<InspectionSheetSchemeDO> getInspectionSheetSchemeAnalysis(AnalysisReqVO vo,Boolean process);

    /**
     * 生产操作终端获取检验任务集合
     * @param reqVO
     * @return
     */
    List<InspectionSheetSchemeDO> getInspectionSheetSchemeList4Terminal(InspectionSchemeTerminalReqVO reqVO);

    /**
     * 生产操作终端通过barCode获取检验任务
     * @param reqVO
     * @return
     */
    InspectionSheetSchemeDO getInspectionSheetSchemeListByBarCode4Terminal(InspectionSchemeTerminalReqVO reqVO);

    /**
     * 生产操作终端验证barCode
     * @param reqVO
     */
    void validInspectionSchemeBarCode(InspectionSchemeTerminalValidReqVO reqVO);

    /**
     * 根据条件获取所有d质检单
     * @param vo
     * @return
     */
    List<InspectionSheetSchemeDO> getInspectionSheetSchemeAnalysisWorker(AnalysisReqVO vo);
}
