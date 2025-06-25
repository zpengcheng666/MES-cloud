package cn.iocoder.yudao.module.pms.dal.mysql.plan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.dal.dataobject.order.PmsOrderDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanItemDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PmsPlanDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.pmsapproval.PmsApprovalDO;
import com.github.yulichang.toolkit.JoinWrappers;
import com.github.yulichang.wrapper.MPJLambdaWrapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目计划子表，产品计划完善 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface PlanItemMapper extends BaseMapperX<PlanItemDO> {

    default List<PlanItemDO> selectListByProjectPlanId(String projectPlanId) {
        return selectList(PlanItemDO::getProjectPlanId, projectPlanId);
    }

    default List<PlanItemDO> selectListByProjectId(String projectId) {
        return selectList(PlanItemDO::getProjectId, projectId);
    }

    default int deleteByProjectPlanId(String projectPlanId) {
        return delete(PlanItemDO::getProjectPlanId, projectPlanId);
    }

    /**
     * 物料采购计划用,根据项目(编号)id联查,用编号有问题
     * @param projectIds
     * @return
     */
    default List<PlanItemDO> selectListMaterialUse(Collection<String> projectIds) {
        MPJLambdaWrapper<PlanItemDO> wrapper = JoinWrappers.lambda(PlanItemDO.class)
                .selectAll(PlanItemDO.class)
                .selectAs(PmsApprovalDO::getId,"projectId")
                .selectAs(PmsApprovalDO::getProjectName,"projectName")
                .leftJoin(PmsPlanDO.class,PmsPlanDO::getId,PlanItemDO::getProjectPlanId)
                .leftJoin(PmsApprovalDO.class,PmsApprovalDO::getId,PmsPlanDO::getProjectId)
                .in(PmsApprovalDO::getId,projectIds)
//                .in(PmsApprovalDO::getProjectCode,projectCodes)
                .orderByDesc(PmsApprovalDO::getId);

        return selectList(wrapper);
    }

    /**
     * 通过物料编码查询
     * @param materialId
     * @return
     */
    default List<PlanItemDO> selectListByMaterialId(String materialId) {
        return selectList(PlanItemDO::getMaterialNumber, materialId);
    }

}
