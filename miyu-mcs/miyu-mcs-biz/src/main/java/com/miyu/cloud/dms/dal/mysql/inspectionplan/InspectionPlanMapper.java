package com.miyu.cloud.dms.dal.mysql.inspectionplan;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.dms.dal.dataobject.inspectionplan.InspectionPlanDO;
import com.miyu.cloud.dms.dal.dataobject.maintenanceplan.MaintenancePlanDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.dms.controller.admin.inspectionplan.vo.*;

/**
 * 设备检查计划 Mapper
 *
 * @author miyu
 */
@Mapper
public interface InspectionPlanMapper extends BaseMapperX<InspectionPlanDO> {

    default PageResult<InspectionPlanDO> selectPage(InspectionPlanPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InspectionPlanDO>()
                .eqIfPresent(InspectionPlanDO::getCode, reqVO.getCode())
                .eqIfPresent(InspectionPlanDO::getTree, reqVO.getTree())
                .eqIfPresent(InspectionPlanDO::getDevice, reqVO.getDevice())
                .eqIfPresent(InspectionPlanDO::getEnableStatus, reqVO.getEnableStatus())
                .eqIfPresent(InspectionPlanDO::getExpirationShutdown,reqVO.getExpirationShutdown())
                .eqIfPresent(InspectionPlanDO::getExpirationTime,reqVO.getExpirationTime())
                .eqIfPresent(InspectionPlanDO::getType, reqVO.getType())
                .betweenIfPresent(InspectionPlanDO::getStartTime, reqVO.getStartTime())
                .eqIfPresent(InspectionPlanDO::getCornExpression, reqVO.getCornExpression())
                .eqIfPresent(InspectionPlanDO::getContent, reqVO.getContent())
                .eqIfPresent(InspectionPlanDO::getRemark, reqVO.getRemark())
                .betweenIfPresent(InspectionPlanDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InspectionPlanDO::getId));
    }

}
