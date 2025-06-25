package com.miyu.cloud.macs.dal.mysql.strategy;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.cloud.macs.dal.dataobject.strategy.StrategyDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.cloud.macs.controller.admin.strategy.vo.*;

/**
 * 策略 Mapper
 *
 * @author 芋道源码
 */
@Mapper
public interface StrategyMapper extends BaseMapperX<StrategyDO> {

    default PageResult<StrategyDO> selectPage(StrategyPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<StrategyDO>()
                .eqIfPresent(StrategyDO::getCode, reqVO.getCode())
                .likeIfPresent(StrategyDO::getName, reqVO.getName())
                .eqIfPresent(StrategyDO::getDescription, reqVO.getDescription())
                .eqIfPresent(StrategyDO::getStatus, reqVO.getStatus())
                .eqIfPresent(StrategyDO::getCreateBy, reqVO.getCreateBy())
                .betweenIfPresent(StrategyDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(StrategyDO::getUpdateBy, reqVO.getUpdateBy())
                .orderByDesc(StrategyDO::getId));
    }

}