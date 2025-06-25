package com.miyu.module.pdm.service.processTask;



import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskReqVO;
import com.miyu.module.pdm.controller.admin.processTask.vo.ProcessTaskRespVO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;

import javax.validation.Valid;
import java.util.List;

public interface ProcessTaskService {
    //为评估任务添加负责人和评估截止日期
    String addProcessTask(@Valid ProcessTaskReqVO addReqVO);

    //为评估任务修改负责人和评估截止日期
    void updateProcessTask(@Valid ProcessTaskReqVO updateReqVO);

    /**
     * 获得项目编号及其记录
     */
    ProcessTaskDO getProcessTask(String id);

    /**
     * 获得项目列表
     */
    List<ProcessTaskDO> getProcessTaskList(ProcessTaskReqVO reqVO);

    /**
     * 通过选中项目获取评估任务列表
     */
    List<ProcessTaskRespVO> getPartListByProjectCode(ProcessTaskReqVO reqVO);

    List<ProcessTaskRespVO> getPartListByProjectCodeNew(ProcessTaskRespVO reqVO);

    String addProcessTaskNew(ProcessTaskReqVO addReqVO);

    void updateProjectstatus(String projectCode);
}
