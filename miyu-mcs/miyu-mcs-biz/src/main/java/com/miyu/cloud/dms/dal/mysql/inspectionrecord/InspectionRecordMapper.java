package com.miyu.cloud.dms.dal.mysql.inspectionrecord;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.MPJLambdaWrapperX;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.controller.admin.inspectionrecord.vo.InspectionRecordPageReqVO;
import com.miyu.cloud.dms.dal.dataobject.inspectionplan.InspectionPlanDO;
import com.miyu.cloud.dms.dal.dataobject.inspectionrecord.InspectionRecordDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 设备检查记录 Mapper
 *
 * @author 王正浩
 */
@Mapper
public interface InspectionRecordMapper extends BaseMapperX<InspectionRecordDO> {

    default PageResult<InspectionRecordDO> selectPage(InspectionRecordPageReqVO reqVO) {
        MPJLambdaWrapperX<InspectionRecordDO> wrapper = new MPJLambdaWrapperX<InspectionRecordDO>();
        wrapper
                .rightJoin(InspectionPlanDO.class, InspectionPlanDO::getId, InspectionRecordDO::getCode)
                .and(a1 -> a1
                        .and(a2 -> a2
                                .eq(InspectionRecordDO::getStatus, 0)
                                .in(reqVO.getRoles() != null && reqVO.getRoles().length != 0, InspectionPlanDO::getSuperintendent, reqVO.getRoles())
                        )
                        .or()
                        .eq(InspectionRecordDO::getStatus, 1)
                )
                .eqIfExists(InspectionRecordDO::getCode, reqVO.getCode())
                .eqIfExists(InspectionRecordDO::getStatus, reqVO.getStatus())
                .eqIfExists(InspectionRecordDO::getDevice, reqVO.getDevice())
                .eqIfExists(InspectionRecordDO::getExpirationShutdown, reqVO.getExpirationShutdown())
                .eqIfExists(InspectionRecordDO::getExpirationTime, reqVO.getExpirationTime())
                .eqIfExists(InspectionRecordDO::getType, reqVO.getType())
                .likeIfExists(InspectionRecordDO::getRemark, reqVO.getRemark())
                .likeIfExists(InspectionRecordDO::getContent, reqVO.getContent())
                .likeIfExists(InspectionRecordDO::getCreateBy, reqVO.getCreateBy())
                .between(reqVO.getStartTime() != null, InspectionRecordDO::getStartTime, reqVO.getStartTime() != null ? reqVO.getStartTime()[0] : null, reqVO.getStartTime() != null ? reqVO.getStartTime()[1] : null)
                .between(reqVO.getEndTime() != null, InspectionRecordDO::getEndTime, reqVO.getEndTime() != null ? reqVO.getEndTime()[0] : null, reqVO.getEndTime() != null ? reqVO.getEndTime()[1] : null)
                .between(reqVO.getCreateTime() != null, InspectionRecordDO::getCreateTime, reqVO.getCreateTime() != null ? reqVO.getCreateTime()[0] : null, reqVO.getCreateTime() != null ? reqVO.getEndTime()[1] : null)
                .orderByAsc(InspectionRecordDO::getStatus)
                .orderByDesc(InspectionRecordDO::getId);

        return selectPage(reqVO, wrapper);
    }

    default List<InspectionRecordDO> selectPreExpirationShutdown() {

        LambdaQueryWrapper<InspectionRecordDO> wrapper = new LambdaQueryWrapper<InspectionRecordDO>();
        wrapper
                .eq(InspectionRecordDO::getStatus, 0)
                .eq(InspectionRecordDO::getExpirationShutdown, 1);
        return selectList(wrapper);
    }

}
