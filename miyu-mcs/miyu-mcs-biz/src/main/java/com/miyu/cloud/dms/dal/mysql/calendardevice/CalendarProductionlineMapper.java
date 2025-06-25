package com.miyu.cloud.dms.dal.mysql.calendardevice;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.calendardevice.provo.CalendarProductionlinePageReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarProductionlineDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备日历关联部分,记录了设备绑定了那些班次 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface CalendarProductionlineMapper extends BaseMapperX<CalendarProductionlineDO> {

    default PageResult<CalendarProductionlineDO> selectPage(CalendarProductionlinePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CalendarProductionlineDO>()
                .eqIfPresent(CalendarProductionlineDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(CalendarProductionlineDO::getDeviceCode, reqVO.getDeviceCode())
                .likeIfPresent(CalendarProductionlineDO::getDeviceName, reqVO.getDeviceName())
                .eqIfPresent(CalendarProductionlineDO::getShiftId, reqVO.getShiftId())
                .likeIfPresent(CalendarProductionlineDO::getShiftName, reqVO.getShiftName())
                .betweenIfPresent(CalendarProductionlineDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CalendarProductionlineDO::getId));
    }

    default List<CalendarProductionlineDO> selectListWith(CalendarProductionlineDO reqVO) {
        return selectList(new LambdaQueryWrapperX<CalendarProductionlineDO>()
                .eqIfPresent(CalendarProductionlineDO::getDeviceId, reqVO.getDeviceId())
                .eqIfPresent(CalendarProductionlineDO::getDeviceCode, reqVO.getDeviceCode())
                .likeIfPresent(CalendarProductionlineDO::getDeviceName, reqVO.getDeviceName())
                .eqIfPresent(CalendarProductionlineDO::getShiftId, reqVO.getShiftId())
                .likeIfPresent(CalendarProductionlineDO::getShiftName, reqVO.getShiftName())
                .orderByDesc(CalendarProductionlineDO::getId));
    }

    @Delete("delete from dms_calendar_productionline where device_id = #{deviceId} and shift_id = #{shiftId}")
    public void deleteByDeviceAndShift(@Param("deviceId") String deviceId, @Param("shiftId") String shiftId);
}
