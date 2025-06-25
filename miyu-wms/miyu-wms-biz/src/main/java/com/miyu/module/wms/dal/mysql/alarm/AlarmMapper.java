package com.miyu.module.wms.dal.mysql.alarm;

import java.util.*;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import com.miyu.module.wms.dal.dataobject.alarm.AlarmDO;
import org.apache.ibatis.annotations.Mapper;
import com.miyu.module.wms.controller.admin.alarm.vo.*;

/**
 * 异常 Mapper
 *
 * @author QianJy
 */
@Mapper
public interface AlarmMapper extends BaseMapperX<AlarmDO> {

    default PageResult<AlarmDO> selectPage(AlarmPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<AlarmDO>()
                .betweenIfPresent(AlarmDO::getCreateTime, reqVO.getCreateTime())
                .eqIfPresent(AlarmDO::getAlarmType, reqVO.getAlarmType())
                .eqIfPresent(AlarmDO::getAlarmSource, reqVO.getAlarmSource())
                .eqIfPresent(AlarmDO::getAlarmLevel, reqVO.getAlarmLevel())
                .eqIfPresent(AlarmDO::getAlarmNum, reqVO.getAlarmNum())
                .eqIfPresent(AlarmDO::getAlarmDesc, reqVO.getAlarmDesc())
                .betweenIfPresent(AlarmDO::getAlarmTime, reqVO.getAlarmTime())
                .eqIfPresent(AlarmDO::getAlarmState, reqVO.getAlarmState())
                .orderByDesc(AlarmDO::getId));
    }

}