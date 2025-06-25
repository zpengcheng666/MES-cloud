package com.miyu.module.pdm.dal.mysql.processPlanDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ResourceSelectedReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.StepDetailReqVO;
import com.miyu.module.pdm.dal.dataobject.processPlanDetail.StepDetailDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface StepDetailMapper extends BaseMapperX<StepDetailDO> {

    default List<StepDetailDO> selectResourceList(ResourceSelectedReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<StepDetailDO>()
                .eq(StepDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eq(StepDetailDO::getProcedureId, reqVO.getProcedureId())
                .eq(StepDetailDO::getStepId, reqVO.getStepId())
                .eqIfPresent(StepDetailDO::getResourcesType, reqVO.getResourcesType())
                .orderByDesc(StepDetailDO::getId));
    }

    default int deleteByResourceId(StepDetailReqVO reqVO) {
        return delete(Wrappers.lambdaUpdate(StepDetailDO.class)
                .eq(StepDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eq(StepDetailDO::getProcedureId, reqVO.getProcedureId())
                .eq(StepDetailDO::getStepId, reqVO.getStepId())
                .eq(StepDetailDO::getResourcesType, reqVO.getResourcesType())
                .eq(StepDetailDO::getResourcesTypeId,reqVO.getResourcesTypeId()));

    }

    default int deleteByProjectCode(ResourceSelectedReqVO reqVO) {
        return delete(Wrappers.lambdaUpdate(StepDetailDO.class)
                .eq(StepDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eq(StepDetailDO::getProcedureId, reqVO.getProcedureId())
                .eq(StepDetailDO::getStepId, reqVO.getStepId())
                .eq(StepDetailDO::getResourcesType,reqVO.getResourcesType()));

    }
}
