package com.miyu.cloud.dms.dal.mysql.maintenanceplan;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.maintenanceplan.vo.MaintenancePlanPageReqVO;
import com.miyu.cloud.dms.dal.dataobject.inspectionplan.InspectionPlanDO;
import com.miyu.cloud.dms.dal.dataobject.maintenanceplan.MaintenancePlanDO;
import org.apache.ibatis.annotations.Mapper;

/**
 * 设备保养维护计划 Mapper
 *
 * @author miyu
 */
@Mapper
public interface MaintenancePlanMapper extends BaseMapperX<MaintenancePlanDO> {

    default PageResult<MaintenancePlanDO> selectPage(MaintenancePlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<MaintenancePlanDO>()
                .likeIfPresent(MaintenancePlanDO::getCode,reqVO.getCode())
                .eqIfPresent(MaintenancePlanDO::getTree,reqVO.getTree())
                .eqIfPresent(MaintenancePlanDO::getDevice, reqVO.getDevice())
                .eqIfPresent(MaintenancePlanDO::getCriticalDevice, reqVO.getCriticalDevice())
                .eqIfPresent(MaintenancePlanDO::getExpirationShutdown,reqVO.getExpirationShutdown())
                .eqIfPresent(MaintenancePlanDO::getExpirationTime,reqVO.getExpirationTime())
                .eqIfPresent(MaintenancePlanDO::getEnableStatus,reqVO.getEnableStatus())
                .eqIfPresent(MaintenancePlanDO::getType, reqVO.getType())
                .betweenIfPresent(MaintenancePlanDO::getStartTime, reqVO.getStartTime())
                .likeIfPresent(MaintenancePlanDO::getCornExpression, reqVO.getCornExpression())
                .likeIfPresent(MaintenancePlanDO::getContent, reqVO.getContent())
                .likeIfPresent(MaintenancePlanDO::getRemark, reqVO.getRemark())
                .likeIfPresent(MaintenancePlanDO::getSuperintendent, reqVO.getSuperintendent())
                .betweenIfPresent(MaintenancePlanDO::getLastTime,reqVO.getLastTime())
                .eqIfPresent(MaintenancePlanDO::getLastStatus, reqVO.getLastStatus())
                .betweenIfPresent(MaintenancePlanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(MaintenancePlanDO::getId));
    }

}
