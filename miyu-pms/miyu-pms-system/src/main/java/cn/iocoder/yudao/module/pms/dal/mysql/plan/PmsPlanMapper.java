package cn.iocoder.yudao.module.pms.dal.mysql.plan;

import java.util.*;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import cn.iocoder.yudao.module.pms.controller.admin.plan.vo.PmsPlanPageReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.plan.vo.PmsPlanSaveReqVO;
import cn.iocoder.yudao.module.pms.controller.admin.pmsapproval.vo.PmsApprovalPageReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目计划 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PmsPlanMapper extends BaseMapperX<PmsPlanDO> {

    default PageResult<PmsPlanDO> selectPage2(PmsPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PmsPlanDO>()
                .likeIfPresent(PmsPlanDO::getProjectOrderId, reqVO.getProjectOrderId())
                .eqIfPresent(PmsPlanDO::getRemark, reqVO.getRemark())
                .eqIfPresent(PmsPlanDO::getStatus, reqVO.getStatus())
                .eqIfPresent(PmsPlanDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .betweenIfPresent(PmsPlanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(PmsPlanDO::getId));
    }

    default PageResult<PmsPlanDO> selectPage(PmsPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<PmsPlanDO>()
                .eqIfPresent(PmsPlanDO::getProjectOrderId, reqVO.getProjectOrderId())
                .eqIfPresent(PmsPlanDO::getProjectId, reqVO.getProjectId())
                .eqIfPresent(PmsPlanDO::getProjectCode, reqVO.getProjectCode())
                .eqIfPresent(PmsPlanDO::getRemark, reqVO.getRemark())
                .eqIfPresent(PmsPlanDO::getMaterialNumber, reqVO.getMaterialNumber())
                .eqIfPresent(PmsPlanDO::getPartNumber, reqVO.getPartNumber())
                .likeIfPresent(PmsPlanDO::getPartName, reqVO.getPartName())
                .eqIfPresent(PmsPlanDO::getOrderType, reqVO.getOrderType())
                .eqIfPresent(PmsPlanDO::getOutSourceAmount, reqVO.getOutSourceAmount())
                .eqIfPresent(PmsPlanDO::getQuantity, reqVO.getQuantity())
                .eqIfPresent(PmsPlanDO::getProcessType, reqVO.getProcessType())
                .eqIfPresent(PmsPlanDO::getProcessCondition, reqVO.getProcessCondition())
                .eqIfPresent(PmsPlanDO::getStatus, reqVO.getStatus())
                .eqIfPresent(PmsPlanDO::getProcessInstanceId, reqVO.getProcessInstanceId())
                .betweenIfPresent(PmsPlanDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(PmsPlanDO::getProcessScheme, reqVO.getProcessScheme())
                .eqIfPresent(PmsPlanDO::getRemindInfo, reqVO.getRemindInfo())
                .betweenIfPresent(PmsPlanDO::getPurchaseCompletionTime, reqVO.getPurchaseCompletionTime())
                .betweenIfPresent(PmsPlanDO::getProcessPreparationTime, reqVO.getProcessPreparationTime())
                .betweenIfPresent(PmsPlanDO::getProductionPreparationTime, reqVO.getProductionPreparationTime())
                .betweenIfPresent(PmsPlanDO::getWarehousingTime, reqVO.getWarehousingTime())
                .betweenIfPresent(PmsPlanDO::getCheckoutCompletionTime, reqVO.getCheckoutCompletionTime())
                .betweenIfPresent(PmsPlanDO::getPlanDeliveryTime, reqVO.getPlanDeliveryTime())
                .orderByDesc(PmsPlanDO::getId));
    }

    //限定条件，工艺不为空
    default List<PmsPlanDO> selectListWith(PmsPlanSaveReqVO reqVO) {
        MPJLambdaWrapper<PmsPlanDO> wrapper = JoinWrappers.lambda(PmsPlanDO.class);
        List<Long> list = new ArrayList<>();
        list.add(3L);
        list.add(4L);
        list.add(5L);
        wrapper.selectAll(PmsPlanDO.class)
                .leftJoin(PmsApprovalDO.class,PmsApprovalDO::getId,PmsPlanDO::getProjectId)
                .selectAs(PmsApprovalDO::getProjectStatus,"projectStatus")
                .eqIfExists(PmsPlanDO::getProjectOrderId, reqVO.getProjectOrderId())
                .eqIfExists(PmsPlanDO::getProjectId, reqVO.getProjectId())
                .eqIfExists(PmsPlanDO::getProjectCode, reqVO.getProjectCode())
                .eqIfExists(PmsPlanDO::getMaterialNumber, reqVO.getMaterialNumber())
                .likeIfExists(PmsPlanDO::getPartName, reqVO.getPartName())
                .eqIfExists(PmsPlanDO::getProcessType, reqVO.getProcessType())
                .isNotNull(PmsPlanDO::getProcessScheme)
                .in(PmsApprovalDO::getProjectStatus,list)
                .orderByDesc(PmsPlanDO::getId);
        return selectJoinList(PmsPlanDO.class,wrapper);
    }

    //展示未选工艺的计划
    default List<PmsPlanDO> showProcessScheme(List<String> projectIds){
        MPJLambdaWrapper<PmsPlanDO> wrapper = JoinWrappers.lambda(PmsPlanDO.class);
        wrapper.selectAll(PmsPlanDO.class)
                .leftJoin(PmsOrderDO.class,PmsOrderDO::getId,PmsPlanDO::getProjectOrderId)
                .selectAs(PmsOrderDO::getOutsource,PmsPlanDO::getOutsource)
                .in(PmsPlanDO::getProjectId,projectIds)
                .isNull(PmsPlanDO::getProcessScheme)
                .eq(PmsOrderDO::getOutsource,0);

        return selectList(wrapper);
    }

}
