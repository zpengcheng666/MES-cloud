package com.miyu.module.pdm.dal.mysql.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.DemandMaterialReqVO;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.DemandMeasureReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.DemandMaterialDO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.DemandMeasureDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemandMeasureMapper extends BaseMapperX<DemandMeasureDO> {
    default List<DemandMeasureDO> selectDemandMeasureList(DemandMeasureReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DemandMeasureDO>()
                .eq(DemandMeasureDO::getProjectCode, reqVO.getProjectCode())
                .eq(DemandMeasureDO::getPartVersionId, reqVO.getPartVersionId())
                .orderByDesc(DemandMeasureDO::getId));
    }
}
