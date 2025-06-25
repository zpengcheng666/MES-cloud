package cn.iocoder.yudao.module.pms.bpmlistener;

import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.service.assessment.AssessmentService;
import cn.iocoder.yudao.module.pms.service.plan.PmsPlanService;
import cn.iocoder.yudao.module.pms.service.pmsapproval.PmsApprovalService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 负责项目部分审批状态的更新
 */
@RestController
@RequestMapping("pms/updateStatus")
public class PmsUpdateStatusController {
//    @Resource
//    private BpmProcessInstanceApi processInstanceApi;

    @Resource
    private PmsApprovalService pmsApprovalService;

    @Resource
    private AssessmentService assessmentService;

    @Resource
    private PmsPlanService planService;

    /**
     * 不仅要改项目审批状态,还要改项目状态为准备中,订单也改为准备中，并且发起工艺评估请求
     * @param id
     * @param status
     */
    @Transactional
    @PostMapping("/approval")
    public void approvalUpdateStatus(@RequestParam("id") String id, @RequestParam("status") Integer status){
        pmsApprovalService.updateStatus(id,status);
    }

    @PostMapping("/assessment")
    public void assessmentUpdateStatus(@RequestParam("id") String id, @RequestParam("status") Integer status){
        assessmentService.updateAssessmentStatus(id,status);
    }

    @PostMapping("/plan")
    public void planUpdateStatus(@RequestParam("id") String id, @RequestParam("status") Integer status){
        planService.updatePlanStatus(id,status);
    }

}

