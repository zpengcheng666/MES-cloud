package com.miyu.cloud.core.carrytask.job;

import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import com.miyu.cloud.dms.service.inspectionplan.InspectionPlanService;
import com.miyu.cloud.dms.service.maintenanceplan.MaintenancePlanService;
import com.miyu.cloud.dms.service.maintenancerecord.MaintenanceRecordService;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Component
@Transactional
public class PlanCarryTaskJob {

    @Resource
    private InspectionPlanService inspectionPlanService;

    /**
     * 设备检查计划提醒
     *
     * @throws Exception
     */
    @XxlJob("mcsReminderInspectionPlanJob")
    @TenantJob
    public void mcsReminderInspectionPlanJob() throws Exception {
        String id = XxlJobHelper.getJobParam();
        inspectionPlanService.reminderInspectionPlan(id);
    }

    @Resource
    private MaintenancePlanService maintenancePlanService;

    /**
     * 保养维护计提醒
     *
     * @throws Exception
     */
    @XxlJob("mcsReminderMaintenancePlanJob")
    @TenantJob
    public void mcsReminderMaintenancePlanJob() throws Exception {
        String id = XxlJobHelper.getJobParam();
        maintenancePlanService.reminderPlan(id);
    }

    @Resource
    private MaintenanceRecordService maintenanceRecordService;

    /**
     * 设备保养维护超期检查服务,每天运行一次
     *
     * @throws Exception
     */
    @XxlJob("mcsExpirationMaintenancePlanJob")
    @TenantJob
    public void mcsExpirationMaintenancePlanJob() throws Exception {
        maintenanceRecordService.expirationShutdownService();
    }
}
