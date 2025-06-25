package com.miyu.module.pdm.service.processPlanNew;


import com.miyu.module.pdm.controller.admin.processPlanNew.vo.ProcessTaskStatisticsReqVO;
import com.miyu.module.pdm.dal.dataobject.processTask.ProcessTaskDO;
import java.util.List;

public interface ProcessTaskStatisticsService {

    //获取统计任务分页
    List<ProcessTaskDO> getProcessTaskStatistics(ProcessTaskStatisticsReqVO reqVO);



}
