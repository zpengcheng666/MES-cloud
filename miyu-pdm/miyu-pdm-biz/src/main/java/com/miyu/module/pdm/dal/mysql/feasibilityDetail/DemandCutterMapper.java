package com.miyu.module.pdm.dal.mysql.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.DemandCutterReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.DemandCutterDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemandCutterMapper extends BaseMapperX<DemandCutterDO> {

    default List<DemandCutterDO> selectDemandCutterList(DemandCutterReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DemandCutterDO>()
                .eq(DemandCutterDO::getProjectCode, reqVO.getProjectCode())
                .eq(DemandCutterDO::getPartVersionId, reqVO.getPartVersionId())
                .orderByDesc(DemandCutterDO::getId));
    }
}
