package com.miyu.module.pdm.service.feasibilityTask;

import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityTask.vo.FeasibilityTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;

import javax.validation.Valid;
import java.util.List;


public interface FeasibilityTaskService {

    //为评估任务添加负责人和评估截止日期
    String addFeasibilityTask(@Valid FeasibilityTaskReqVO addReqVO);

    //为评估任务修改负责人和评估截止日期
    void updateFeasibilityTask(@Valid FeasibilityTaskReqVO updateReqVO);

    /**
     * 获得项目编号及其记录
     */
    FeasibilityTaskDO getFeasibilityTask(String id);

    /**
     * 获得项目列表
     */
    List<FeasibilityTaskDO> getFeasibilityTaskList(FeasibilityTaskReqVO reqVO);

    /**
     * 通过选中项目获取评估任务列表
     */
    List<FeasibilityTaskRespVO> getPartListByProjectCode(FeasibilityTaskReqVO reqVO);

    /**
     * 获得待分配任务的零件目录列表
     */
    List<FeasibilityTaskRespVO> getPartListByProjectCodeNew(FeasibilityTaskReqVO reqVO);

    String addFeasibilityTaskNew(@Valid FeasibilityTaskReqVO addReqVO);


}
