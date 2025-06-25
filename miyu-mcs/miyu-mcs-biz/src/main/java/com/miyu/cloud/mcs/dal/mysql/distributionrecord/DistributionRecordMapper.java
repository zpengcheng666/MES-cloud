package com.miyu.cloud.mcs.dal.mysql.distributionrecord;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.mcs.dal.dataobject.distributionrecord.DistributionRecordDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.mcs.controller.admin.distributionrecord.vo.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 物料配送申请详情 Mapper
 *
 * @author miyu
 */
@Mapper
public interface DistributionRecordMapper extends BaseMapperX<DistributionRecordDO> {

    default PageResult<DistributionRecordDO> selectPage(DistributionRecordPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<DistributionRecordDO>()
                .likeIfPresent(DistributionRecordDO::getNumber, reqVO.getNumber())
                .likeIfPresent(DistributionRecordDO::getBarCode, reqVO.getBarCode())
                .eqIfPresent(DistributionRecordDO::getStatus, reqVO.getStatus())
//                .orderByAsc(DistributionRecordDO::getStatus)
                .orderByDesc(DistributionRecordDO::getId));
    }

    default int deleteByApplicationId(String applicationId) {
        return delete(DistributionRecordDO::getApplicationId, applicationId);
    }

}
