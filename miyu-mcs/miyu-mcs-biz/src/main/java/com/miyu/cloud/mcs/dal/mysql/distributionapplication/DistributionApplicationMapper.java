package com.miyu.cloud.mcs.dal.mysql.distributionapplication;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.mcs.dal.dataobject.distributionapplication.DistributionApplicationDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.distributionapplication.vo.*;

/**
 * 物料配送申请 Mapper
 *
 * @author miyu
 */
@Mapper
public interface DistributionApplicationMapper extends BaseMapperX<DistributionApplicationDO> {

    default PageResult<DistributionApplicationDO> selectPage(DistributionApplicationPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DistributionApplicationDO>()
                .eqIfPresent(DistributionApplicationDO::getId, reqVO.getId())
                .eqIfPresent(DistributionApplicationDO::getApplicationNumber, reqVO.getApplicationNumber())
                .eqIfPresent(DistributionApplicationDO::getProcessingUnitId, reqVO.getProcessingUnitId())
                .eqIfPresent(DistributionApplicationDO::getStatus, reqVO.getStatus())
                .orderByDesc(DistributionApplicationDO::getId));
    }

}
