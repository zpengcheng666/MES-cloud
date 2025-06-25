package com.miyu.cloud.mpc.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.cloud.mcs.dal.dataobject.batchrecord.BatchRecordDO;
import com.miyu.cloud.mpc.service.EventTriggerService;
import com.miyu.cloud.mpc.service.taskPlan.TaskPlanRestService;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@Component
public class MpcScheduledJob {

    @Resource
    private EventTriggerService eventTriggerService;
    @Resource
    private TaskPlanRestService taskPlanRestService;

    //三坐标检测搬运
    @XxlJob("handleMCSTCDECarryJob")
//    @TenantIgnore
    @TenantJob
    public void handleMCSTCDECarryJob() {
        eventTriggerService.checkDetectionDeviceCarry();
    }

    //质检
    @XxlJob("handleMCSTCDEInspectionJob")
//    @TenantIgnore
    @TenantJob
    public void handleMCSInspectJob() {
        List<BatchRecordDO> list = taskPlanRestService.checkBatchRecordNeedInspect();
        for (BatchRecordDO batchRecordDO : list) {
            try {
                eventTriggerService.createInspectionSheetTask(batchRecordDO);
            } catch (Exception ignored) {}
        }
    }

}
