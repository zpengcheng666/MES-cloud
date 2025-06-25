package cn.iocoder.yudao.module.pms.dal.mysql.plan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanDeviceDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目计划子表，物料采购计划中的设备采购 Mapper
 *
 * @author xia
 */
@Mapper
public interface PlanDeviceMapper extends BaseMapperX<PlanDeviceDO> {

    default List<PlanDeviceDO> selectListByProjectPlanId(String projectPlanId) {
        return selectList(PlanDeviceDO::getProjectPlanId, projectPlanId);
    }
    default List<PlanDeviceDO> selectListByProjectPlanItemId(String projectPlanItemId) {
        return selectList(PlanDeviceDO::getProjectPlanItemId, projectPlanItemId);
    }
    default int deleteByProjectPlanId(String projectPlanId) {
        return delete(PlanDeviceDO::getProjectPlanId, projectPlanId);
    }
    default int deleteByProjectPlanItemId(String projectPlanItemId) {
        return delete(PlanDeviceDO::getProjectPlanItemId, projectPlanItemId);
    }

    /**
     * 物理删除
     * @param projectPlanItemId
     */
    @Delete("DELETE FROM project_plan_device where project_plan_item_id = #{projectPlanItemId}")
    public void deleteByProjectPlanId2(String projectPlanItemId);

}
