package com.miyu.cloud.dms.dal.mysql.maintenancerecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.controller.admin.maintenancerecord.vo.MaintenanceRecordPageReqVO;
import com.miyu.cloud.dms.dal.dataobject.maintenanceplan.MaintenancePlanDO;
import com.miyu.cloud.dms.dal.dataobject.maintenancerecord.MaintenanceRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备保养维护记录 Mapper
 *
 * @author miyu
 */
@Mapper
public interface MaintenanceRecordMapper extends BaseMapperX<MaintenanceRecordDO> {

    default PageResult<MaintenanceRecordDO> selectPage(MaintenanceRecordPageReqVO reqVO) {
        System.out.println(reqVO);
        MPJLambdaWrapperX<MaintenanceRecordDO> wrapper = new MPJLambdaWrapperX<MaintenanceRecordDO>();
        wrapper
                .rightJoin(MaintenancePlanDO.class, MaintenancePlanDO::getId, MaintenanceRecordDO::getCode)
                .and(a1 -> a1
                        .and(a2 -> a2
                                .eq(MaintenanceRecordDO::getRecordStatus, 0)
                                .in(reqVO.getRoles() != null && reqVO.getRoles().length != 0, MaintenancePlanDO::getSuperintendent, reqVO.getRoles())
                        )
                        .or()
                        .eq(MaintenanceRecordDO::getRecordStatus, 1)

                )
                .likeIfExists(MaintenanceRecordDO::getCode, reqVO.getCode())
                .eqIfExists(MaintenanceRecordDO::getRecordStatus, reqVO.getRecordStatus())
                .eqIfExists(MaintenanceRecordDO::getDevice, reqVO.getDevice())
                .eqIfExists(MaintenanceRecordDO::getCriticalDevice, reqVO.getCriticalDevice())
                .eqIfExists(MaintenanceRecordDO::getExpirationShutdown, reqVO.getExpirationShutdown())
                .eqIfExists(MaintenanceRecordDO::getExpirationTime, reqVO.getExpirationTime())
                .eqIfExists(MaintenanceRecordDO::getType, reqVO.getType())
                .eqIfExists(MaintenanceRecordDO::getStatus, reqVO.getStatus())
                .likeIfExists(MaintenanceRecordDO::getRemarks, reqVO.getRemarks())
                .likeIfExists(MaintenanceRecordDO::getContent, reqVO.getContent())
                .eqIfExists(MaintenanceRecordDO::getMaintenanceBy, reqVO.getMaintenanceBy())
                .between(reqVO.getStartTime() != null, MaintenanceRecordDO::getStartTime, reqVO.getStartTime() != null ? reqVO.getStartTime()[0] : null, reqVO.getStartTime() != null ? reqVO.getStartTime()[1] : null)
                .between(reqVO.getEndTime() != null, MaintenanceRecordDO::getEndTime, reqVO.getEndTime() != null ? reqVO.getEndTime()[0] : null, reqVO.getEndTime() != null ? reqVO.getEndTime()[1] : null)
                .between(reqVO.getCreateTime() != null, MaintenanceRecordDO::getCreateTime, reqVO.getCreateTime() != null ? reqVO.getCreateTime()[0] : null, reqVO.getCreateTime() != null ? reqVO.getCreateTime()[1] : null)
                .orderByAsc(MaintenanceRecordDO::getRecordStatus)
                .orderByDesc(MaintenanceRecordDO::getId);

        return selectPage(reqVO, wrapper);
    }

    default List<MaintenanceRecordDO> selectPreExpirationShutdown() {

        LambdaQueryWrapper<MaintenanceRecordDO> wrapper = new LambdaQueryWrapper<MaintenanceRecordDO>();
        wrapper
                .eq(MaintenanceRecordDO::getRecordStatus, 0)
                .eq(MaintenanceRecordDO::getExpirationShutdown, 1);
        return selectList(wrapper);
    }

}
