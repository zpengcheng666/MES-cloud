package com.miyu.cloud.dms.service.maintenanceplan;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.core.carrytask.job.XxlJobInfo;
import com.miyu.cloud.core.carrytask.job.XxlJobUtil;
import com.miyu.cloud.dms.controller.admin.maintenanceplan.vo.MaintenancePlanPageReqVO;
import com.miyu.cloud.dms.controller.admin.maintenanceplan.vo.MaintenancePlanSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.maintenanceplan.MaintenancePlanDO;
import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordDO;
import com.miyu.cloud.dms.dal.mysql.maintenanceplan.MaintenancePlanMapper;
import com.miyu.cloud.dms.dal.mysql.maintenancerecord.MaintenanceRecordMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.cloud.dms.enums.DictConstants.ENABLE_STATUS_0;
import static com.miyu.cloud.dms.enums.ErrorCodeConstants.*;

/**
 * 设备保养维护计划 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class MaintenancePlanServiceImpl implements MaintenancePlanService {

    private void check(MaintenancePlanSaveReqVO data) {
        if (maintenancePlanMapper.exists(new LambdaQueryWrapperX<MaintenancePlanDO>().eq(true, MaintenancePlanDO::getCode, data.getCode()).ne(data.getId() != null, MaintenancePlanDO::getId, data.getId()))) {
            throw exception(MAINTENANCE_PLAN_DUPLICATE_CODE);
        }

        if (StringUtils.isBlank(data.getCode())) {
            throw exception(MAINTENANCE_PLAN_CODE_EMPTY);
        }
        if (StringUtils.isBlank(data.getTree())) {
            throw exception(MAINTENANCE_PLAN_TREE_EMPTY);
        }
        if (StringUtils.isBlank(data.getDevice())) {
            throw exception(MAINTENANCE_PLAN_DEVICE_EMPTY);
        }
        if (data.getCriticalDevice() == null) {
            throw exception(MAINTENANCE_PLAN_CRITICAL_DEVICE_EMPTY);
        }
        if (data.getEnableStatus() == null) {
            throw exception(MAINTENANCE_PLAN_ENABLE_STATUS_EMPTY);
        }
        if (data.getType() == null) {
            throw exception(MAINTENANCE_PLAN_TYPE_EMPTY);
        }
    }

    @Resource
    private MaintenancePlanMapper maintenancePlanMapper;

    @Override
    @Transactional
    public String createMaintenancePlan(MaintenancePlanSaveReqVO createReqVO) {
        check(createReqVO);
        // 插入
        MaintenancePlanDO maintenancePlan = BeanUtils.toBean(createReqVO, MaintenancePlanDO.class);
        maintenancePlanMapper.insert(maintenancePlan);
        //添加定时任务
        String jobId = addXxlJob(maintenancePlan);
        maintenancePlan.setJobId(jobId);
        maintenancePlanMapper.updateById(maintenancePlan);
        // 返回
        return maintenancePlan.getId();
    }

    @Override
    @Transactional
    public void updateMaintenancePlan(MaintenancePlanSaveReqVO updateReqVO) {
        // 校验存在
        validateMaintenancePlanExists(updateReqVO.getId());
        check(updateReqVO);
        // 更新
        MaintenancePlanDO updateObj = BeanUtils.toBean(updateReqVO, MaintenancePlanDO.class);
        maintenancePlanMapper.updateById(updateObj);
        //更新定时任务
        delXxlJob(updateObj.getId());
        MaintenancePlanDO maintenancePlanDO = maintenancePlanMapper.selectById(updateObj.getId());
        String jobId = addXxlJob(maintenancePlanDO);
        maintenancePlanDO.setJobId(jobId);
        maintenancePlanMapper.updateById(maintenancePlanDO);
    }

    @Override
    @Transactional
    public void deleteMaintenancePlan(String id) {
        // 校验存在
        validateMaintenancePlanExists(id);
        //删除定时任务
        delXxlJob(id);
        // 删除
        maintenancePlanMapper.deleteById(id);
    }

    private void validateMaintenancePlanExists(String id) {
        if (maintenancePlanMapper.selectById(id) == null) {
            throw exception(MAINTENANCE_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public MaintenancePlanDO getMaintenancePlan(String id) {
        return maintenancePlanMapper.selectById(id);
    }

    @Override
    public PageResult<MaintenancePlanDO> getMaintenancePlanPage(MaintenancePlanPageReqVO pageReqVO) {
        return maintenancePlanMapper.selectPage(pageReqVO);
    }

    @Override
    public Boolean checkTree(String treeId) {
        return maintenancePlanMapper.exists(new LambdaQueryWrapperX<MaintenancePlanDO>().eq(MaintenancePlanDO::getTree, treeId));
    }

    @Override
    public List<MaintenancePlanDO> getList() {
        return maintenancePlanMapper.selectList();
    }

    @Resource
    private MaintenanceRecordMapper maintenanceRecordMapper;

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void reminderPlan(String id) {
        MaintenancePlanDO plan = maintenancePlanMapper.selectById(id);
        if (plan == null) {
            throw exception(MAINTENANCE_PLAN_NOT_EXISTS);
        }

        if (plan.getEnableStatus().equals(ENABLE_STATUS_0)) {
            return;
        }
        if(LocalDateTime.now().isBefore(plan.getStartTime())){
            return;
        }

        MaintenanceRecordDO record = new MaintenanceRecordDO();

        record.setCode(plan.getCode());
        record.setDevice(plan.getDevice());
        record.setCriticalDevice(plan.getCriticalDevice());
        record.setType(plan.getType());
        record.setContent(plan.getContent());
        record.setExpirationShutdown(plan.getExpirationShutdown());
        record.setExpirationTime(plan.getExpirationTime());

        record.setRecordStatus(0);
        maintenanceRecordMapper.insert(record);

    }

    /*********************************************定时任务相关************************************/


    @Resource
    private XxlJobUtil xxlJobUtil;

    /**
     * 添加并激活定时任务
     */
    private String addXxlJob(MaintenancePlanDO maintenancePlanDO) {
        XxlJobInfo jobInfo = new XxlJobInfo();

        jobInfo.setJobDesc("保养维护计划（自动）：" + maintenancePlanDO.getCode());
        jobInfo.setAuthor("MaintenancePlanServiceImpl.addXxlJob");

        jobInfo.setScheduleType("CRON");
        jobInfo.setScheduleConf(maintenancePlanDO.getCornExpression());
        jobInfo.setMisfireStrategy("DO_NOTHING");

        jobInfo.setExecutorRouteStrategy("FIRST");
        jobInfo.setExecutorHandler("mcsReminderMaintenancePlanJob");
        jobInfo.setExecutorParam(maintenancePlanDO.getId());
        jobInfo.setExecutorBlockStrategy("SERIAL_EXECUTION");
        jobInfo.setExecutorTimeout(0);
        jobInfo.setExecutorFailRetryCount(0);

        jobInfo.setGlueType("BEAN");
        jobInfo.setGlueRemark("GLUE代码初始化");

        String jobId = xxlJobUtil.add(jobInfo);

        xxlJobUtil.start(Integer.parseInt(jobId));

        return jobId;
    }

    /**
     * 删除定时任务
     */
    private String delXxlJob(String id) {
        MaintenancePlanDO maintenancePlanDO = maintenancePlanMapper.selectById(id);
        return xxlJobUtil.remove(Integer.parseInt(maintenancePlanDO.getJobId()));
    }

}
