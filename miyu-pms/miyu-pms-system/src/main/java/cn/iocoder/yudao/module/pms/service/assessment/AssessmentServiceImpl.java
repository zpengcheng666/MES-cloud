package cn.iocoder.yudao.module.pms.service.assessment;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.module.bpm.api.task.BpmProcessInstanceApi;
import cn.iocoder.yudao.module.bpm.api.task.dto.BpmProcessInstanceCreateReqDTO;
import cn.iocoder.yudao.module.bpm.enums.task.BpmTaskStatusEnum;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentSaveReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentReplenishDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.dal.mysql.assessment.AssessmentMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.assessment.AssessmentReplenishMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.assessmentdevice.AssessmentDeviceMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.order.PmsOrderMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.pmsapproval.PmsApprovalMapper;
import cn.iocoder.yudao.module.pms.enums.AssessmentConstans;
import com.miyu.module.pdm.api.processPlan.ProcessPlanApi;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;


import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static cn.iocoder.yudao.framework.security.core.util.SecurityFrameworkUtils.getLoginUserId;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.ASSESSMENT_NOT_EXISTS;
import static cn.iocoder.yudao.module.pms.enums.ErrorCodeConstants.ORDER_NOT_EXISTS;

/**
 * 项目评审 Service 实现类
 *
 * @author 芋道源码
 */
@Service
@Validated
public class AssessmentServiceImpl implements AssessmentService {

    @Resource
    private AssessmentMapper assessmentMapper;
    @Resource
    private AssessmentReplenishMapper assessmentReplenishMapper;
    @Resource
    private AssessmentDeviceMapper assessmentDeviceMapper;

    public static final String PROCESS_KEY = "pms_assessment";

    @Resource
    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private PmsApprovalMapper approvalMapper;

    @Resource
    private PmsOrderMapper orderMapper;

//    @Resource
//    private ProcessPlanApi processPlanApi;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createAssessment(AssessmentSaveReqVO createReqVO) {
        Long userId = getLoginUserId();
        //查询项目
        PmsApprovalDO pmsApprovalDO = approvalMapper.selectById(createReqVO.getProjectId());
        // 插入
        AssessmentDO assessment = BeanUtils.toBean(createReqVO, AssessmentDO.class).setProjectCode(pmsApprovalDO.getProjectCode()).setStatus(BpmTaskStatusEnum.RUNNING.getStatus());
        //评估为真,对应评估状态就改为正在评估
        if(createReqVO.getTechnology()==1){
            assessment.setTechnologyAssessmentStatus(2);
        }
        if(createReqVO.getCapacity()==1){
            assessment.setCapacityAssessmentStatus(2);
        }
        if(createReqVO.getCost()==1){
            assessment.setCostAssessmentStatus(2);
        }
        if(createReqVO.getStrategy()==1){
            assessment.setStrategyAssessmentStatus(2);
        }
        assessmentMapper.insert(assessment);

        Map<String, Object> processInstanceVariables = new HashMap<>();
        processInstanceVariables.put("technology",createReqVO.getTechnology());
        processInstanceVariables.put("strategy",createReqVO.getStrategy());
        processInstanceVariables.put("capacity",createReqVO.getCapacity());
        processInstanceVariables.put("cost",createReqVO.getCost());
        processInstanceVariables.put("assessmentId",assessment.getId());
        String processInstanceId = processInstanceApi.createProcessInstance(userId,
                new BpmProcessInstanceCreateReqDTO().setProcessDefinitionKey(PROCESS_KEY)
                        .setVariables(processInstanceVariables).setBusinessKey(String.valueOf(assessment.getId()))
                        .setStartUserSelectAssignees(createReqVO.getStartUserSelectAssignees())).getCheckedData();

        // 将工作流的编号，更新到业务表中
        assessmentMapper.updateById(new AssessmentDO().setId(assessment.getId()).setProcessInstanceId(processInstanceId));

        // 插入子表
        createAssessmentReplenishList(assessment.getId(), createReqVO.getAssessmentReplenishs());
        // 返回流程实例id
        return assessment.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAssessment(AssessmentSaveReqVO updateReqVO) {
        // 校验存在
        validateAssessmentExists(updateReqVO.getId());
        //查询项目
        PmsApprovalDO pmsApprovalDO = approvalMapper.selectById(updateReqVO.getProjectId());
        // 更新
        AssessmentDO updateObj = BeanUtils.toBean(updateReqVO, AssessmentDO.class).setProjectCode(pmsApprovalDO.getProjectCode());
        assessmentMapper.updateById(updateObj);

        // 更新子表
        updateAssessmentReplenishList(updateReqVO.getId(), updateReqVO.getAssessmentReplenishs());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteAssessment(String id) {
        // 校验存在
        validateAssessmentExists(id);
        // 删除
        assessmentMapper.deleteById(id);

        // 删除子表
        deleteAssessmentReplenishByAssessmentId(id);
        assessmentDeviceMapper.deleteByAssessmentId(id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAssessmentStatus(String id, Integer status) {
        AssessmentDO assessmentDO = validateAssessmentExists(id);
        List<PmsOrderDO> pmsOrderList = orderMapper.selectList(PmsOrderDO::getProjectId, assessmentDO.getProjectId());
        if(pmsOrderList.size()>0){
            //更新审批状态
            assessmentMapper.updateById(new AssessmentDO().setId(id).setStatus(status));
            //改变项目状态为准备中
            approvalMapper.updateById(new PmsApprovalDO().setId(assessmentDO.getProjectId()).setProjectStatus(3L));
            for (PmsOrderDO pmsOrderDO : pmsOrderList) {
                //改变订单状态为准备中
                orderMapper.updateById(new PmsOrderDO().setId(pmsOrderDO.getId()).setOrderStatus(3));
                //发起工艺编制
//                if(pmsOrderDO.getOutsource()!=1){
//                    String checkedData = processPlanApi.pushProcess(pmsOrderDO.getProjectCode(), pmsOrderDO.getPartNumber(), pmsOrderDO.getPartName(), pmsOrderDO.getProcessCondition()).getCheckedData();
//                }
            }
        }else {
            throw exception(ORDER_NOT_EXISTS);
        }

    }

    private AssessmentDO validateAssessmentExists(String id) {
        AssessmentDO assessmentDO = assessmentMapper.selectById(id);
        if (assessmentDO == null) {
            throw exception(ASSESSMENT_NOT_EXISTS);
        }
        return assessmentDO;
    }

    @Override
    public AssessmentDO getAssessment(String id) {
        return assessmentMapper.selectById(id);
    }

    @Override
    public PageResult<AssessmentDO> getAssessmentPage(AssessmentPageReqVO pageReqVO) {
        return assessmentMapper.selectPage(pageReqVO);
    }

    @Override
    public void updateAuditor(Map<String, String> map) {
        String id = map.get("id");
        AssessmentDO updateObj = new AssessmentDO();
        updateObj.setId(id);
        //技术部长
        if(map.containsKey("Activity_0ipck9y")){
            updateObj.setTechnologyAuditor(Long.valueOf(map.get("Activity_0ipck9y")));
        }
        //制造部长
        if(map.containsKey("Activity_04bad3q")){
            updateObj.setCapacityAuditor(Long.valueOf(map.get("Activity_04bad3q")));
        }
        //财务部长
        if(map.containsKey("Activity_1u6c3vu")){
            updateObj.setCostAuditor(Long.valueOf(map.get("Activity_1u6c3vu")));
        }
        //战略部长
        if(map.containsKey("Activity_113e2mt")){
            updateObj.setStrategyAuditor(Long.valueOf(map.get("Activity_113e2mt")));
        }
        assessmentMapper.updateById(updateObj);


    }

    // ==================== 子表（评审子表，评审补充） ====================

    @Override
    public List<AssessmentReplenishDO> getAssessmentReplenishListByAssessmentId(String assessmentId) {
        return assessmentReplenishMapper.selectListByAssessmentId(assessmentId);
    }

    private void createAssessmentReplenishList(String assessmentId, List<AssessmentReplenishDO> list) {
        if(ObjectUtil.isNotNull(list)){
            list.forEach(o -> o.setAssessmentId(assessmentId));
            assessmentReplenishMapper.insertBatch(list);
        }

    }

    private void updateAssessmentReplenishList(String assessmentId, List<AssessmentReplenishDO> list) {
        deleteAssessmentReplenishByAssessmentId(assessmentId);
		list.forEach(o -> o.setId(null).setUpdater(null).setUpdateTime(null)); // 解决更新情况下：1）id 冲突；2）updateTime 不更新
        createAssessmentReplenishList(assessmentId, list);
    }

    private void deleteAssessmentReplenishByAssessmentId(String assessmentId) {
        assessmentReplenishMapper.deleteByAssessmentId(assessmentId);
    }

    //创建子评审
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createAssessmentReplenish(Map<String,String> map) {
        AssessmentReplenishDO assessmentReplenishDO = BeanUtils.toBean(map, AssessmentReplenishDO.class).setAuditDate(LocalDateTime.now());
        assessmentReplenishMapper.insert(assessmentReplenishDO);
        //策略评估还要多加一步，改变评估结论的状态
        if(map.containsKey("conclusion")) {
            Integer conclusion = Integer.valueOf(map.get("conclusion"));
            assessmentMapper.updateById(new AssessmentDO().setId(assessmentReplenishDO.getAssessmentId()).setConclusion(conclusion));
        }

        //改变评估状态
        switch (map.get("assessment_type")){
            case AssessmentConstans.CapacityAssessment:
                assessmentMapper.updateById(new AssessmentDO().setId(assessmentReplenishDO.getAssessmentId()).setCapacityAssessmentStatus(3));
                break;
            case AssessmentConstans.CostAssessment:
                assessmentMapper.updateById(new AssessmentDO().setId(assessmentReplenishDO.getAssessmentId()).setCostAssessmentStatus(3));
                break;
            case AssessmentConstans.TechnologyAssessment:
                assessmentMapper.updateById(new AssessmentDO().setId(assessmentReplenishDO.getAssessmentId()).setTechnologyAssessmentStatus(3));
                break;
            case AssessmentConstans.StrategyAssessment:
                //策略评估还要多加一步，改变评估结论的状态
                if(map.containsKey("conclusion")) {
                    Integer conclusion = Integer.valueOf(map.get("conclusion"));
                    assessmentMapper.updateById(new AssessmentDO().setId(assessmentReplenishDO.getAssessmentId()).setStrategyAssessmentStatus(3).setConclusion(conclusion));
                }else {
                    assessmentMapper.updateById(new AssessmentDO().setId(assessmentReplenishDO.getAssessmentId()).setStrategyAssessmentStatus(3));
                }

                break;
            default:
                break;
        }
        return assessmentReplenishDO.getId();
    }

}
