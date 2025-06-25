package com.miyu.module.wms.dal.mysql.agv;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.wms.dal.dataobject.agv.AGVDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.agv.vo.*;

/**
 * AGV 信息 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface AGVMapper extends BaseMapperX<AGVDO> {

    default PageResult<AGVDO> selectPage(AGVPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AGVDO>()
                .betweenIfPresent(AGVDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AGVDO::getCarNo, reqVO.getCarNo())
                .eqIfPresent(AGVDO::getMode, reqVO.getMode())
                .eqIfPresent(AGVDO::getX, reqVO.getX())
                .eqIfPresent(AGVDO::getY, reqVO.getY())
                .eqIfPresent(AGVDO::getCurrentStation, reqVO.getCurrentStation())
                .eqIfPresent(AGVDO::getLastStation, reqVO.getLastStation())
                .eqIfPresent(AGVDO::getIsStop, reqVO.getIsStop())
                .eqIfPresent(AGVDO::getBlocked, reqVO.getBlocked())
                .eqIfPresent(AGVDO::getEmergency, reqVO.getEmergency())
                .eqIfPresent(AGVDO::getTaskStatus, reqVO.getTaskStatus())
                .eqIfPresent(AGVDO::getLocationId, reqVO.getLocationId())
                .orderByDesc(AGVDO::getId));
    }

}