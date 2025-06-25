package com.miyu.module.pdm.dal.mysql.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.DemandHiltReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.DemandHiltDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemandHiltMapper extends BaseMapperX<DemandHiltDO> {

    default List<DemandHiltDO> selectDemandHiltList(DemandHiltReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DemandHiltDO>()
                .eq(DemandHiltDO::getProjectCode, reqVO.getProjectCode())
                .eq(DemandHiltDO::getPartVersionId, reqVO.getPartVersionId())
                .orderByDesc(DemandHiltDO::getId));
    }
}
