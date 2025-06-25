package cn.iocoder.yudao.module.pms.dal.mysql.planPurchaseOrder;

import cn.hutool.core.util.ObjectUtil;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.module.pms.controller.admin.plan.vo.PmsPlanPageReqVO;
import cn.iocoder.yudao.module.pms.dal.dataobject.assessment.AssessmentDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.planPurchaseOrder.PlanPurchaseOrderDO;
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
public interface PlanPurchaseOrderMapper extends BaseMapperX<PlanPurchaseOrderDO> {

}
