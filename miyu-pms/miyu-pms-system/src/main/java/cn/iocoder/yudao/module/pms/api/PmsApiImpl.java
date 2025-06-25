package cn.iocoder.yudao.module.pms.api;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pms.api.pms.PmsApi;
import cn.iocoder.yudao.module.pms.api.pms.dto.PlanItemDTO;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsApprovalDto;
import cn.iocoder.yudao.module.pms.api.pms.dto.PmsPlanDTO;
import cn.iocoder.yudao.module.pms.api.pms.dto.ProductStatusRespDTO;
import cn.iocoder.yudao.module.pms.controller.admin.assessment.vo.AssessmentRespVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.orderMaterialRelation.OrderMaterialRelationDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.OrderWithPlan;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanItemDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import cn.iocoder.yudao.module.pms.dal.mysql.order.PmsOrderMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.orderMaterialRelation.OrderMaterialRelationMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PPOMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PlanItemMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.plan.PmsPlanMapper;
import cn.iocoder.yudao.module.pms.dal.mysql.pmsapproval.PmsApprovalMapper;
import cn.iocoder.yudao.module.pms.service.pmsapproval.PmsApprovalService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.pojo.CommonResult.success;

@RestController // 提供 RESTful API 接口，给 Feign 调用
public class PmsApiImpl implements PmsApi {

    @Resource
    private PmsApprovalMapper approvalMapper;

    @Resource
    private PmsOrderMapper pmsOrderMapper;

    @Resource
    private PmsPlanMapper pmsPlanMapper;

    @Resource
    private PlanItemMapper planItemMapper;

    @Resource
    private PmsApprovalService approvalService;

    @Resource
    private OrderMaterialRelationMapper relationMapper;

    @Resource
    private PPOMapper ppoMapper;


    @Override
    public CommonResult<List<PmsApprovalDto>> getApprovalList(String projectCode) {
        List<PmsApprovalDO> pmsApprovalDOS = approvalMapper.selectList(new LambdaQueryWrapperX<PmsApprovalDO>()
                .likeIfPresent(PmsApprovalDO::getProjectCode, projectCode)
                .eq(PmsApprovalDO::getStatus, 2)
                .ne(PmsApprovalDO::getProjectStatus, 1)
                .orderByDesc(PmsApprovalDO::getUpdateTime));
        return success(BeanUtils.toBean(pmsApprovalDOS, PmsApprovalDto.class));
    }

    @Override
    public CommonResult<PmsPlanDTO> getPlanByProjectId(String id) {
        //查询项目表
        PmsApprovalDO pmsApprovalDO = approvalMapper.selectById(id);
        //获得项目订单表
        PmsOrderDO pmsOrderDO = pmsOrderMapper.selectOne(PmsOrderDO::getProjectId, id);
        if(ObjectUtil.isNull(pmsOrderDO)){
            return null;
        }
        //获得项目计划表
        PmsPlanDO pmsPlanDO = pmsPlanMapper.selectOne(PmsPlanDO::getProjectOrderId, pmsOrderDO.getId());
        if(ObjectUtil.isNull(pmsPlanDO)){
            return null;
        }
        PmsPlanDTO pmsPlanDTO = BeanUtils.toBean(pmsPlanDO, PmsPlanDTO.class,vo->{
            vo.setProjectCode(pmsApprovalDO.getProjectCode());
            vo.setProjectName(pmsApprovalDO.getProjectName());
        });

        List<PlanItemDO> planItemDOS = planItemMapper.selectListByProjectPlanId(pmsPlanDO.getId());
        List<PlanItemDTO> planItemDTOS = BeanUtils.toBean(planItemDOS, PlanItemDTO.class);
        pmsPlanDTO.setItemDTOList(planItemDTOS);
        return success(pmsPlanDTO);
    }

    @Override
    public CommonResult<List<PmsApprovalDto>> getApprovalListByIds(Collection<String> ids) {
        List<PmsApprovalDO> pmsApprovalDOS = approvalMapper.selectList(PmsApprovalDO::getId, ids);
        return success(BeanUtils.toBean(pmsApprovalDOS,PmsApprovalDto.class));
    }

    @Override
    public CommonResult<String> updateProcessStatus(String businessKey, Integer status) {
        approvalService.updateStatus(businessKey,status);
        return CommonResult.success("ok");
    }

    @Override
    public CommonResult<List<ProductStatusRespDTO>> getProductStatusList(Collection<String> ids) {
        if(CollectionUtils.isAnyEmpty(ids)){
            return success(Collections.emptyList());
        }
        List<OrderWithPlan> orderWithPlans = ppoMapper.selectListPPO(ids);
        LambdaQueryWrapper<OrderMaterialRelationDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(OrderMaterialRelationDO::getProjectId,ids);
        List<OrderMaterialRelationDO> relationDOS = relationMapper.selectList(wrapper);
        List<ProductStatusRespDTO> list = new ArrayList<>();
        for (OrderWithPlan orderWithPlan : orderWithPlans) {
            List<OrderMaterialRelationDO> orderRelationList = relationDOS.stream().filter((item) -> item.getOrderId().equals(orderWithPlan.getProjectOrderId())).collect(Collectors.toList());
            ProductStatusRespDTO product = new ProductStatusRespDTO();
            BeanUtil.copyProperties(orderWithPlan,product);
            if(orderWithPlan.getOutsource()==0){
                //正常加工
                //没有项目计划id或者没选工艺,这种没有加工
                if(ObjectUtil.isNull(orderWithPlan.getPlanId())||ObjectUtil.isNull(orderWithPlan.getProcessScheme())){
                    product.setCompleted(0);
                    product.setProcessed(0);
                    product.setUnprocessed(product.getQuantity());
                }else {
                    int completed = orderRelationList.stream().filter((item) -> {
                        return item.getMaterialStatus() == 5;
                    }).collect(Collectors.toList()).size();
                    int processed = orderRelationList.stream().filter((item) -> {
                        return (item.getMaterialStatus() == 1&&item.getPlanType()!=null)||item.getMaterialStatus() == 2||item.getMaterialStatus() == 4||item.getMaterialStatus() == 6;
                    }).collect(Collectors.toList()).size();
                    product.setCompleted(completed);
                    product.setProcessed(processed);
                    product.setUnprocessed(orderWithPlan.getQuantity() - completed - processed);
                }
            }else {
                //整单外协
                int completed = orderRelationList.stream().filter((item) -> {
                    return item.getMaterialStatus() == 5;
                }).collect(Collectors.toList()).size();
                int processed = orderRelationList.stream().filter((item) -> {
                    //其实整单外协只有4和6
                    return item.getMaterialStatus() == 2||item.getMaterialStatus() == 4||item.getMaterialStatus() == 6;
                }).collect(Collectors.toList()).size();
                product.setCompleted(completed);
                product.setProcessed(processed);
                product.setUnprocessed(orderWithPlan.getQuantity() - completed - processed);
            }
            list.add(product);
        }
        return success(list);
    }
}
