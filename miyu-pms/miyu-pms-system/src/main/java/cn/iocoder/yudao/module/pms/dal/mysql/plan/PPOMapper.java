package cn.iocoder.yudao.module.pms.dal.mysql.plan;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pms.controller.admin.plan.vo.PmsPlanPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalPageReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.OrderWithPlan;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * 项目计划 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PPOMapper extends BaseMapperX<OrderWithPlan> {

    //需要项目审批通过,前端已经传了,还需要评审也通过,直接在这加条件
    default PageResult<OrderWithPlan> selectPagePPO(PmsPlanPageReqVO reqVO) {
        MPJLambdaWrapper<OrderWithPlan> wrapper = JoinWrappers.lambda(OrderWithPlan.class)
                .selectAll(OrderWithPlan.class)
                .rightJoin(PmsOrderDO.class,PmsOrderDO::getProjectId,OrderWithPlan::getId)
                .leftJoin(PmsPlanDO.class,PmsPlanDO::getProjectOrderId,PmsOrderDO::getId)
//                .leftJoin(PmsApprovalDO.class,PmsApprovalDO::getId,PmsOrderDO::getProjectId)
                .selectAs(PmsPlanDO::getId,OrderWithPlan::getPlanId)
                .selectAs(PmsPlanDO::getStatus,OrderWithPlan::getPlanStatus)
                .selectAs(PmsOrderDO::getId,OrderWithPlan::getProjectOrderId)
                .select(PmsOrderDO::getQuantity,PmsOrderDO::getPartNumber,PmsOrderDO::getProcessType,PmsOrderDO::getPartName,PmsOrderDO::getOutsource,PmsOrderDO::getMaterialNumber)
                .select(PmsPlanDO::getProcessScheme,PmsPlanDO::getRemindInfo,PmsPlanDO::getProcessVersionId,PmsPlanDO::getProcessInstanceId)
                .select(PmsPlanDO::getProcessPreparationTime,PmsPlanDO::getPurchaseCompletionTime,PmsPlanDO::getProductionPreparationTime)
                .orderByDesc(OrderWithPlan::getId).orderByDesc(PmsOrderDO::getId);
//                .select(PmsApprovalDO::getProjectName,PmsApprovalDO::getProjectCode);

//                .leftJoin()

        return selectJoinPage(reqVO,OrderWithPlan.class, wrapper);
    }

    //不分页,用项目id集合查
    default List<OrderWithPlan> selectListPPO(Collection<String> ids) {
        MPJLambdaWrapper<OrderWithPlan> wrapper = JoinWrappers.lambda(OrderWithPlan.class)
                .in(OrderWithPlan::getId,ids)
                .selectAll(OrderWithPlan.class)
                .rightJoin(PmsOrderDO.class,PmsOrderDO::getProjectId,OrderWithPlan::getId)
                .leftJoin(PmsPlanDO.class,PmsPlanDO::getProjectOrderId,PmsOrderDO::getId)
//                .leftJoin(PmsApprovalDO.class,PmsApprovalDO::getId,PmsOrderDO::getProjectId)
                .selectAs(PmsPlanDO::getId,OrderWithPlan::getPlanId)
                .selectAs(PmsPlanDO::getStatus,OrderWithPlan::getPlanStatus)
                .selectAs(PmsOrderDO::getId,OrderWithPlan::getProjectOrderId)
                .select(PmsOrderDO::getQuantity,PmsOrderDO::getPartNumber,PmsOrderDO::getProcessType,PmsOrderDO::getPartName,PmsOrderDO::getOutsource,PmsOrderDO::getMaterialNumber)
                .select(PmsPlanDO::getProcessScheme,PmsPlanDO::getRemindInfo,PmsPlanDO::getProcessVersionId,PmsPlanDO::getProcessInstanceId)
                .select(PmsPlanDO::getProcessPreparationTime,PmsPlanDO::getPurchaseCompletionTime,PmsPlanDO::getProductionPreparationTime)
                .orderByDesc(OrderWithPlan::getId).orderByDesc(PmsOrderDO::getId);
//                .select(PmsApprovalDO::getProjectName,PmsApprovalDO::getProjectCode);

//                .leftJoin()
        return selectList(wrapper);
    }

    //查找待编辑的
    default List<OrderWithPlan> selectPlanToDo() {
        MPJLambdaWrapper<OrderWithPlan> wrapper = JoinWrappers.lambda(OrderWithPlan.class)
                .selectAll(OrderWithPlan.class)
                .rightJoin(PmsOrderDO.class,PmsOrderDO::getProjectId,OrderWithPlan::getId)
                .leftJoin(PmsPlanDO.class,PmsPlanDO::getProjectOrderId,PmsOrderDO::getId)
                .leftJoin(PmsApprovalDO.class,PmsApprovalDO::getId,PmsOrderDO::getProjectId)
                .in(PmsApprovalDO::getProjectStatus, Arrays.asList(3,4,5))
                .isNull(PmsPlanDO::getId)
                .selectAs(PmsPlanDO::getId,OrderWithPlan::getPlanId)
                .selectAs(PmsPlanDO::getStatus,OrderWithPlan::getPlanStatus)
                .selectAs(PmsOrderDO::getId,OrderWithPlan::getProjectOrderId)
                .select(PmsOrderDO::getQuantity,PmsOrderDO::getPartNumber,PmsOrderDO::getProcessType,PmsOrderDO::getPartName,PmsOrderDO::getOutsource,PmsOrderDO::getMaterialNumber)
                .select(PmsPlanDO::getProcessScheme,PmsPlanDO::getRemindInfo,PmsPlanDO::getProcessVersionId,PmsPlanDO::getProcessInstanceId)
                .select(PmsPlanDO::getProcessPreparationTime,PmsPlanDO::getPurchaseCompletionTime,PmsPlanDO::getProductionPreparationTime)
                .orderByDesc(OrderWithPlan::getId).orderByDesc(PmsOrderDO::getId);
        return selectList(wrapper);
    }

}
