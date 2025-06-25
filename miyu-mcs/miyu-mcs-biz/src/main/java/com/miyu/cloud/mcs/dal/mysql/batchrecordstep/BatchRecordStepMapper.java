package com.miyu.cloud.mcs.dal.mysql.batchrecordstep;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.miyu.cloud.mcs.dal.dataobject.batchrecordstep.BatchRecordStepDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.batchrecordstep.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 工步计划 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface BatchRecordStepMapper extends BaseMapperX<BatchRecordStepDO> {

    default PageResult<BatchRecordStepDO> selectPage(BatchRecordStepPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<BatchRecordStepDO>()
                .eqIfPresent(BatchRecordStepDO::getBatchRecordId, reqVO.getBatchRecordId())
                .eqIfPresent(BatchRecordStepDO::getStepId, reqVO.getStepId())
                .likeIfPresent(BatchRecordStepDO::getStepName, reqVO.getStepName())
                .eqIfPresent(BatchRecordStepDO::getStepOrder, reqVO.getStepOrder())
                .eqIfPresent(BatchRecordStepDO::getDeviceTypeId, reqVO.getDeviceTypeId())
                .eqIfPresent(BatchRecordStepDO::getDefineDeviceId, reqVO.getDefineDeviceIds())
                .betweenIfPresent(BatchRecordStepDO::getPlanStartTime, reqVO.getPlanStartTime())
                .betweenIfPresent(BatchRecordStepDO::getPlanEndTime, reqVO.getPlanEndTime())
                .betweenIfPresent(BatchRecordStepDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(BatchRecordStepDO::getId));
    }

    @Select("DELETE FROM `mcs_batch_record_step` ${ew.customSqlSegment} ")
    void deleteByRecordIdsPhy(@Param("ew") Wrapper<BatchRecordStepDO> wrapper);
}
