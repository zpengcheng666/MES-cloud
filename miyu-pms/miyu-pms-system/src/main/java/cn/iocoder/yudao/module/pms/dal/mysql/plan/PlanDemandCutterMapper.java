package cn.iocoder.yudao.module.pms.dal.mysql.plan;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.module.pms.dal.dataobject.plan.PlanDemandCutterDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 项目计划子表，物料采购计划中的设备采购 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface PlanDemandCutterMapper extends BaseMapperX<PlanDemandCutterDO> {

    default List<PlanDemandCutterDO> selectListByProjectPlanId(String projectPlanId) {
        return selectList(PlanDemandCutterDO::getProjectPlanId, projectPlanId);
    }

    default int deleteByProjectPlanId(String projectPlanId) {
        return delete(PlanDemandCutterDO::getProjectPlanId, projectPlanId);
    }

}
