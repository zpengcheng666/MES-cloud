package com.miyu.module.pdm.dal.mysql.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.DemandMaterialReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.DemandMaterialDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemandMaterialMapper extends BaseMapperX<DemandMaterialDO> {

    default List<DemandMaterialDO> selectDemandMaterialList(DemandMaterialReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DemandMaterialDO>()
                .eq(DemandMaterialDO::getProjectCode, reqVO.getProjectCode())
                .eq(DemandMaterialDO::getPartVersionId, reqVO.getPartVersionId())
                .orderByDesc(DemandMaterialDO::getId));
    }
}
