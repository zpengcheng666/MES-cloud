package com.miyu.cloud.dms.service.inspectionplan;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.core.carrytask.job.XxlJobInfo;
import com.miyu.cloud.core.carrytask.job.XxlJobUtil;
import com.miyu.cloud.dms.controller.admin.inspectionplan.vo.InspectionPlanPageReqVO;
import com.miyu.cloud.dms.controller.admin.inspectionplan.vo.InspectionPlanSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.inspectionplan.InspectionPlanDO;
import com.miyu.cloud.dms.dal.dataobject.inspectionrecord.InspectionRecordDO;
import com.miyu.cloud.dms.dal.mysql.inspectionplan.InspectionPlanMapper;
import com.miyu.cloud.dms.dal.mysql.inspectionrecord.InspectionRecordMapper;
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
 * 设备检查计划 Service 实现类
 *
 * @author miyu
 */
@Service
@Validated
public class InspectionPlanServiceImpl implements InspectionPlanService {

    private void check(InspectionPlanSaveReqVO data) {
        if (inspectionPlanMapper.exists(new LambdaQueryWrapperX<InspectionPlanDO>().eq(true, InspectionPlanDO::getCode, data.getCode()).ne(data.getId() != null, InspectionPlanDO::getId, data.getId()))) {
            throw exception(INSPECTION_PLAN_DUPLICATE_CODE);
        }

        if (StringUtils.isBlank(data.getCode())) {
            throw exception(INSPECTION_PLAN_CODE_EMPTY);
        }
        if (StringUtils.isBlank(data.getTree())) {
            throw exception(INSPECTION_PLAN_TREE_EMPTY);
        }
        if (StringUtils.isBlank(data.getDevice())) {
            throw exception(INSPECTION_PLAN_DEVICE_EMPTY);
        }
        if (data.getEnableStatus() == null) {
            throw exception(INSPECTION_PLAN_ENABLE_EMPTY);
        }
        if (data.getType() == null) {
            throw exception(INSPECTION_PLAN_TYPE_EMPTY);
        }
        if (StringUtils.isBlank(data.getSuperintendent())) {
            throw exception(INSPECTION_PLAN_SUPERINTENDENT_EMPTY);
        }
    }

    @Resource
    private InspectionPlanMapper inspectionPlanMapper;

    @Override
    @Transactional
    public String createInspectionPlan(InspectionPlanSaveReqVO createReqVO) {
        check(createReqVO);
        // 插入
        InspectionPlanDO inspectionPlan = BeanUtils.toBean(createReqVO, InspectionPlanDO.class);
        inspectionPlanMapper.insert(inspectionPlan);
        //添加定时任务
        String jobId = addXxlJob(inspectionPlan);
        inspectionPlan.setJobId(jobId);
        inspectionPlanMapper.updateById(inspectionPlan);
        // 返回
        return inspectionPlan.getId();
    }

    @Override
    @Transactional
    public void updateInspectionPlan(InspectionPlanSaveReqVO updateReqVO) {
        // 校验存在
        validateInspectionPlanExists(updateReqVO.getId());
        check(updateReqVO);
        // 更新
        InspectionPlanDO updateObj = BeanUtils.toBean(updateReqVO, InspectionPlanDO.class);
        inspectionPlanMapper.updateById(updateObj);
        //更新定时任务
        delXxlJob(updateObj.getId());
        InspectionPlanDO inspectionPlanDO = inspectionPlanMapper.selectById(updateObj.getId());
        String jobId = addXxlJob(inspectionPlanDO);
        inspectionPlanDO.setJobId(jobId);
        inspectionPlanMapper.updateById(inspectionPlanDO);
    }

    @Override
    @Transactional
    public void deleteInspectionPlan(String id) {
        // 校验存在
        validateInspectionPlanExists(id);
        //删除定时任务
        delXxlJob(id);
        // 删除
        inspectionPlanMapper.deleteById(id);
    }

    private void validateInspectionPlanExists(String id) {
        if (inspectionPlanMapper.selectById(id) == null) {
            throw exception(INSPECTION_PLAN_NOT_EXISTS);
        }
    }

    @Override
    public InspectionPlanDO getInspectionPlan(String id) {
        return inspectionPlanMapper.selectById(id);
    }

    @Override
    public PageResult<InspectionPlanDO> getInspectionPlanPage(InspectionPlanPageReqVO pageReqVO) {
        return inspectionPlanMapper.selectPage(pageReqVO);
    }

    @Override
    public Boolean checkTree(String treeId) {
        return inspectionPlanMapper.exists(new LambdaQueryWrapperX<InspectionPlanDO>().eq(InspectionPlanDO::getTree, treeId));
    }

    @Resource
    private InspectionRecordMapper inspectionRecordMapper;

    @Override
    @Transactional(rollbackFor = {Throwable.class})
    public void reminderInspectionPlan(String id) {
        if (StrUtil.isBlank(id)) {
            throw exception(PLAN_ID_NOT_EXISTS);
        }
        InspectionPlanDO inspectionPlan = inspectionPlanMapper.selectById(id);
        //获取检查计划
        if (inspectionPlan == null) {
            throw exception(INSPECTION_PLAN_NOT_EXISTS);
        }

        if (inspectionPlan.getEnableStatus().equals(ENABLE_STATUS_0)) {
            return;
        }
        if(LocalDateTime.now().isBefore(inspectionPlan.getStartTime())){
            return;
        }

        InspectionRecordDO inspectionRecord = new InspectionRecordDO();

        inspectionRecord.setCode(inspectionPlan.getId());
        inspectionRecord.setDevice(inspectionPlan.getDevice());
        inspectionRecord.setType(inspectionPlan.getType());
        inspectionRecord.setContent(inspectionPlan.getContent());

        inspectionRecord.setExpirationShutdown(inspectionPlan.getExpirationShutdown());
        inspectionRecord.setExpirationTime(inspectionPlan.getExpirationTime());

        inspectionRecord.setStatus(0);

        inspectionRecordMapper.insert(inspectionRecord);
    }

    @Override
    public List<InspectionPlanDO> getInspectionPlanList() {
        return inspectionPlanMapper.selectList();
    }

    /*********************************************定时任务相关************************************/


    @Resource
    private XxlJobUtil xxlJobUtil;

    /**
     * 添加并激活定时任务
     */
    private String addXxlJob(InspectionPlanDO inspectionPlan) {
        XxlJobInfo jobInfo = new XxlJobInfo();

        jobInfo.setJobDesc("设备检查计划（自动）：" + inspectionPlan.getCode());
        jobInfo.setAuthor("InspectionPlanServiceImpl.addXxlJob");

        jobInfo.setScheduleType("CRON");
        jobInfo.setScheduleConf(inspectionPlan.getCornExpression());
        jobInfo.setMisfireStrategy("DO_NOTHING");

        jobInfo.setExecutorRouteStrategy("FIRST");
        jobInfo.setExecutorHandler("mcsReminderInspectionPlanJob");
        jobInfo.setExecutorParam(inspectionPlan.getId());
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
        InspectionPlanDO inspectionPlanDO = inspectionPlanMapper.selectById(id);
        return xxlJobUtil.remove(Integer.parseInt(inspectionPlanDO.getJobId()));
    }

}
