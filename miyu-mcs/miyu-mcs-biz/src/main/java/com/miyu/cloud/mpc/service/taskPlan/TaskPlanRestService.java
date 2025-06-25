package com.miyu.cloud.mpc.service.taskPlan;

import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;

import java.util.List;
import java.util.Map;

public interface TaskPlanRestService {
    List<BatchRecordDO> checkBatchRecordNeedInspect();

}
