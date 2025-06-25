package com.miyu.module.pdm.service.processDetailTask;

import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ProjPartBomReqVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskReqVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProcessDetailTaskRespVO;
import com.miyu.module.pdm.controller.admin.processDetailTask.vo.ProjPartBomTreeRespVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityTask.FeasibilityTaskDO;
import com.miyu.module.pdm.dal.dataobject.processDetailTask.ProcessDetailTaskDO;

import javax.validation.Valid;
import java.util.List;

public interface ProcessDetailTaskService {
    List<ProjPartBomTreeRespVO> getProjPartBomTreeList(ProjPartBomReqVO reqVO);

    /**
     * 通过选中项目获取评估任务列表
     */
    List<ProcessDetailTaskRespVO> getPartListByProjectCode(ProcessDetailTaskReqVO reqVO);

    /**
     * 获得项目编号及其记录
     */
    ProcessDetailTaskDO getProcessDetailTask(String id);
    /**指派工艺任务*/
    String assignProcessTask(@Valid ProcessDetailTaskReqVO addReqVO);

    void updateProcessTask(@Valid ProcessDetailTaskReqVO updateReqVO);
}
