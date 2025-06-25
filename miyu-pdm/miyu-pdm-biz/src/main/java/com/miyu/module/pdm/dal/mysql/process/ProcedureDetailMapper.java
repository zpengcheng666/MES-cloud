package com.miyu.module.pdm.dal.mysql.process;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.ResourceSelectedReqVO;
import com.miyu.module.pdm.controller.admin.processPlanDetail.vo.ProcedureDetailReqVO;
import com.miyu.module.pdm.dal.dataobject.procedure.ProcedureDetailDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProcedureDetailMapper extends BaseMapperX<ProcedureDetailDO> {

    default List<ProcedureDetailDO> selectResourceList(ResourceSelectedReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<ProcedureDetailDO>()
                .eq(ProcedureDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eq(ProcedureDetailDO::getProcedureId, reqVO.getProcedureId())
                .eqIfPresent(ProcedureDetailDO::getResourcesType, reqVO.getResourcesType())
                .orderByDesc(ProcedureDetailDO::getId));
    }

    default int deleteByResourceId(ProcedureDetailReqVO reqVO) {
        return delete(Wrappers.lambdaUpdate(ProcedureDetailDO.class)
                .eq(ProcedureDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eq(ProcedureDetailDO::getProcedureId, reqVO.getProcedureId())
                .eq(ProcedureDetailDO::getResourcesType, reqVO.getResourcesType())
                .eq(ProcedureDetailDO::getResourcesTypeId,reqVO.getResourcesTypeId()));

    }

    default int deleteByProjectCode(ResourceSelectedReqVO reqVO) {
        return delete(Wrappers.lambdaUpdate(ProcedureDetailDO.class)
                .eq(ProcedureDetailDO::getProcessVersionId, reqVO.getProcessVersionId())
                .eq(ProcedureDetailDO::getProcedureId, reqVO.getProcedureId())
                .eq(ProcedureDetailDO::getResourcesType,reqVO.getResourcesType()));

    }
}
