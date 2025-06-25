package cn.iocoder.yudao.module.pms.dal.mysql.plan;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanDeviceDO;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanMaterialDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 项目计划子表，物料采购计划中的工装采购 Mapper
 *
 * @author xia
 */
@Mapper
public interface PlanMaterialMapper extends BaseMapperX<PlanMaterialDO> {

    default List<PlanMaterialDO> selectListByProjectPlanItemId(String projectPlanItemId) {
        return selectList(PlanMaterialDO::getProjectPlanItemId, projectPlanItemId);
    }

    default int deleteByProjectPlanId(String projectPlanId){
        return delete(PlanMaterialDO::getProjectPlanId, projectPlanId);
    }

    default int deleteByProjectPlanItemId(String projectPlanItemId) {
        return delete(PlanMaterialDO::getProjectPlanItemId, projectPlanItemId);
    }

    /**
     * 物理删除
     * @param projectPlanItemId
     */
    @Delete("DELETE FROM project_plan_material where project_plan_item_id = #{projectPlanItemId}")
    public void deleteByProjectPlanId2(String projectPlanItemId);

}
