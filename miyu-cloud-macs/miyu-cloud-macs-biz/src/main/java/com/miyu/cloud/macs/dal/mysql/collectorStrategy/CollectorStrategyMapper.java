package com.miyu.cloud.macs.dal.mysql.collectorStrategy;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.macs.dal.dataobject.collectorStrategy.CollectorStrategyDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.collectorStrategy.vo.*;

/**
 * 设备策略 Mapper
 *
 * @author miyu
 */
@Mapper
public interface CollectorStrategyMapper extends BaseMapperX<CollectorStrategyDO> {

    default PageResult<CollectorStrategyDO> selectPage(CollectorStrategyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CollectorStrategyDO>()
                .eqIfPresent(CollectorStrategyDO::getCollectorId, reqVO.getCollectorId())
                .eqIfPresent(CollectorStrategyDO::getStrategyId, reqVO.getStrategyId())
                .orderByDesc(CollectorStrategyDO::getId));
    }

}