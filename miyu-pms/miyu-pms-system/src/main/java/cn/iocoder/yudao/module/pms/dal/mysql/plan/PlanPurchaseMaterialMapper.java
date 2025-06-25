package cn.iocoder.yudao.module.pms.dal.mysql.plan;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanDemandCutterDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanPurchaseMaterialDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 项目计划，物料采购计划，工艺生产需要的物料
 */
@Mapper
public interface PlanPurchaseMaterialMapper extends BaseMapperX<PlanPurchaseMaterialDO> {
    default List<PlanPurchaseMaterialDO> selectListByProjectPlanId(String projectPlanId) {
        return selectList(PlanPurchaseMaterialDO::getProjectPlanId, projectPlanId);
    }

    default int deleteByProjectPlanId(String projectPlanId) {
        return delete(PlanPurchaseMaterialDO::getProjectPlanId, projectPlanId);
    }
    default int deleteByProjectPlanItemId(String projectPlanItemId) {
        return delete(PlanPurchaseMaterialDO::getProjectPlanItemId, projectPlanItemId);
    }

    default List<PlanPurchaseMaterialDO> selectPurchaseMaterialByMaterialId(String materialId){
        return selectList(PlanPurchaseMaterialDO::getMaterialId,materialId);
    }
}
