package cn.iocoder.yudao.module.pms.task;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.tenant.core.aop.TenantIgnore;
import cn.iocoder.yudao.framework.tenant.core.job.TenantJob;
import cn.iocoder.yudao.module.pms.controller.admin.notifymessage.vo.NotifyMessageSaveReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanItemDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PlanItemMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PmsPlanMapper;
import cn.iocoder.yudao.module.pms.service.notifymessage.NotifyMessageService;
import cn.iocoder.yudao.module.pms.service.plan.PmsPlanService;
import cn.iocoder.yudao.module.pms.service.pmsapproval.PmsApprovalService;
import com.miyu.module.pdm.api.projectPlan.PdmProjectPlanApi;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomReqDTO;
import com.miyu.module.pdm.api.projectPlan.dto.ProjPartBomTreeRespDTO;
import com.xxl.job.core.handler.annotation.XxlJob;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class PlanJob {
    @Resource
    private PmsApprovalService pmsApprovalService;

    @Resource
    private PdmProjectPlanApi pdmProjectPlanApi;

    @Resource
    private PmsPlanService pmsPlanService;

    @Resource
    private NotifyMessageService notifyMessageService;

//    @Resource
//    private PlanItemMapper planItemMapper;

    @Resource
    private PmsPlanMapper pmsPlanMapper;


//    //TODO 现在提醒主计划
//    // 定时任务(每天在9点和13点触发一次)
//    @XxlJob("planRemindJobHandler")
//    @TenantJob
//    @Transactional(rollbackFor = Exception.class)
//    public void planRemindJobHandler(){
//        PmsApprovalReqVO req = new PmsApprovalReqVO();
////        req.setStatus(2);
//        req.setProjectStatus(3L);
//        //先拿到通过评审的项目
//        //现在拿到准备中的项目就行,准备中的项目都是评审通过或不需要评审的
////        List<PmsApprovalDO> pmsApprovalList = pmsApprovalService.selectListWithPass2(req);
//        List<PmsApprovalDO> pmsApprovalList = pmsApprovalService.selectListWithCondition(req);
//
//        //根据项目编号查询工艺方案
//        for (PmsApprovalDO pmsApprovalDO : pmsApprovalList) {
//            List<PlanItemDO> planItemDOList = pmsPlanService.getPlanItemListByProjectId(pmsApprovalDO.getId());
//            //大于0说明项目计划生成了，没生成直接过
//            if(planItemDOList.size()==0){
//                continue;
//            }
//            //这里记录选择工艺方案后的图号,后面比较用
//            List<String> hasPartNumber = new ArrayList<>();
//            for (PlanItemDO planItemDO : planItemDOList) {
//                if(ObjectUtil.isNotEmpty(planItemDO.getProcessScheme())){
//                    hasPartNumber.add(planItemDO.getPartNumber());
//                }
//            }
//            //需要projectCode 和 partNumber
//            ProjPartBomReqDTO reqDTO = new ProjPartBomReqDTO();
//            reqDTO.setProjectCode(pmsApprovalDO.getProjectCode());
//            //查找工艺方案
//            List<ProjPartBomTreeRespDTO> projPartBomTree = pdmProjectPlanApi.getProjPartBomPlanList(pmsApprovalDO.getProjectCode(), false).getCheckedData();
//            System.out.println(projPartBomTree);
//            //这里过滤得到值,一定是可选的工艺方案
//            List<ProjPartBomTreeRespDTO> projPartBomTreeList = projPartBomTree.stream().filter(item -> item.getType() == 2).filter(item -> "5".equals(item.getStatus())).collect(Collectors.toList());
//            if(projPartBomTreeList.size()>0){
//                //图号集合
//                List<String> list = new ArrayList<>();
//                for (ProjPartBomTreeRespDTO projPartBomTreeRespDTO : projPartBomTreeList) {
//                    if(hasPartNumber.contains(projPartBomTreeRespDTO.getPartNumber())){
//                        //如果为true,说明已经选了工艺方案,就不用添加图号提醒了
//                    }else {
//                        //添加图号
//                        list.add(projPartBomTreeRespDTO.getPartNumber());
//                    }
//
//                }
//                //对没有选择工艺方案的产品零件(图号)进行通知。
//                if(list.size()>0){
//                    String partNumberStr = String.join(",", list);
//                    System.out.println(pmsApprovalDO.getProjectName()+partNumberStr);
//                    System.out.println(pmsApprovalDO.getResponsiblePerson());
//                    NotifyMessageSaveReqVO notifyReq = new NotifyMessageSaveReqVO();
//                    //type目前没有作用,是预留字段
//                    notifyReq.setReadStatus(false).setTemplateNickname("系统通知").setType(4).setUserId(pmsApprovalDO.getResponsiblePerson())
//                            .setTemplateContent("项目:"+pmsApprovalDO.getProjectName()+"所属的项目计划,图号:"+partNumberStr+"已经可以选择工艺方案了");
//                    notifyMessageService.createNotifyMessage(notifyReq);
//                }
//
//                //更新数据库,子任务提示,这个list里的图号都是可选工艺方案的
//                for (PlanItemDO planItemDO : planItemDOList) {
//                    if(list.contains(planItemDO.getPartNumber())){
////                        planItemDO.setRemindInfo("工艺方案已可选");
//                        planItemMapper.updateById(planItemDO);
//                    }
//
//                }
//
//            }
//
//        }
//        System.out.println(LocalDateTime.now());
//    }


    //TODO 现在提醒主计划
    // 定时任务(每天在9点和13点触发一次)
    @XxlJob("planRemindJobHandler")
    @TenantJob
    @Transactional(rollbackFor = Exception.class)
    public void planRemindJobHandler(){
        PmsApprovalReqVO req = new PmsApprovalReqVO();
        req.setProjectStatus(3L);
        //现在拿到准备中的项目就行,准备中的项目都是评审通过或不需要评审的
        List<PmsApprovalDO> pmsApprovalList = pmsApprovalService.selectListWithCondition(req);

        //根据项目编号查询工艺方案
        for (PmsApprovalDO pmsApprovalDO : pmsApprovalList) {
            PmsPlanDO pmsplan = pmsPlanService.getByProjectId(pmsApprovalDO.getId());
            //如果还没有主计划或者主计划选了工艺方案就跳过
            if(ObjectUtil.isNull(pmsplan)||ObjectUtil.isNotEmpty(pmsplan.getProcessScheme())){
                continue;
            }
            //需要projectCode 和 partNumber
            ProjPartBomReqDTO reqDTO = new ProjPartBomReqDTO();
            reqDTO.setProjectCode(pmsApprovalDO.getProjectCode());
            reqDTO.setPartNumber(pmsplan.getPartNumber());
            //查找工艺方案
            List<ProjPartBomTreeRespDTO> projPartBomTree = pdmProjectPlanApi.getProjPartBomPlanList(reqDTO).getCheckedData();
            System.out.println(projPartBomTree);
            //这里过滤得到值,一定是可选的工艺方案
            List<ProjPartBomTreeRespDTO> projPartBomTreeList = projPartBomTree.stream().filter(item -> item.getType() == 2).filter(item -> "5".equals(item.getStatus())).collect(Collectors.toList());

            if(projPartBomTreeList.size()>0){
                //对没有选择工艺方案的产品零件(图号)进行通知。
                NotifyMessageSaveReqVO notifyReq = new NotifyMessageSaveReqVO();
                //type目前没有作用,是预留字段
                notifyReq.setReadStatus(false).setTemplateNickname("系统通知").setType(4).setUserId(pmsApprovalDO.getResponsiblePerson())
                        .setTemplateContent("项目:"+pmsApprovalDO.getProjectName()+"所属的项目计划,图号:"+pmsplan.getPartNumber()+"已经可以选择工艺方案了");
                notifyMessageService.createNotifyMessage(notifyReq);

                //更新数据库,子任务提示
                pmsplan.setRemindInfo("工艺方案已可选");
                pmsPlanMapper.updateById(pmsplan);
            }

        }
//        System.out.println(LocalDateTime.now());
    }
}
