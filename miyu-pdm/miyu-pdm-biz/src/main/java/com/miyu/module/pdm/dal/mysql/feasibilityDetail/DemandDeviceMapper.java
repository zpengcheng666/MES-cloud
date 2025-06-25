package com.miyu.module.pdm.dal.mysql.feasibilityDetail;

import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.module.pdm.controller.admin.feasibilityDetail.vo.DemandDeviceReqVO;
import com.miyu.module.pdm.dal.dataobject.feasibilityDetail.DemandDeviceDO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DemandDeviceMapper extends BaseMapperX<DemandDeviceDO> {

    default List<DemandDeviceDO> selectDemandDeviceList(DemandDeviceReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<DemandDeviceDO>()
                .eq(DemandDeviceDO::getProjectCode, reqVO.getProjectCode())
                .eq(DemandDeviceDO::getPartVersionId, reqVO.getPartVersionId())
                .orderByDesc(DemandDeviceDO::getId));
    }
}
