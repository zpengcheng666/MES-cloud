package com.miyu.cloud.mcs.service.problemreport;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.mcs.controller.admin.problemreport.vo.ProblemReportPageReqVO;
import com.miyu.cloud.mcs.controller.admin.problemreport.vo.ProblemReportSaveReqVO;
import com.miyu.cloud.mcs.dal.dataobject.problemreport.ProblemReportDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 问题上报 Service 接口
 *
 * @author 王正浩
 */
public interface ProblemReportService {

    /**
     * 创建问题上报
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createProblemReport(@Valid ProblemReportSaveReqVO createReqVO);

    /**
     * 更新问题上报
     *
     * @param updateReqVO 更新信息
     */
    void updateProblemReport(@Valid ProblemReportSaveReqVO updateReqVO);

    /**
     * 删除问题上报
     *
     * @param id 编号
     */
    void deleteProblemReport(String id);

    /**
     * 获得问题上报
     *
     * @param id 编号
     * @return 问题上报
     */
    ProblemReportDO getProblemReport(String id);

    /**
     * 获得问题上报分页
     *
     * @param pageReqVO 分页查询
     * @return 问题上报分页
     */
    PageResult<ProblemReportDO> getProblemReportPage(ProblemReportPageReqVO pageReqVO);

    /**
     * 获得问题上报列表
     *
     * @param stationId 工位id,可为空
     * @return 问题上报列表
     */
    List<ProblemReportDO> getProblemReportList(String stationId);
}