package com.miyu.cloud.dms.dal.mysql.calendardevice;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.mybatis.core.mapper.BaseMapperX;
import cn.iocoder.yudao.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDevicePageReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarDeviceDO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 设备日历,记录设备每天的可用时间 Mapper
 *
 * @author 上海弥彧
 */
@Mapper
public interface CalendarDeviceMapper extends BaseMapperX<CalendarDeviceDO> {

    default PageResult<CalendarDeviceDO> selectPage(CalendarDevicePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<CalendarDeviceDO>()
                .eqIfPresent(CalendarDeviceDO::getDeviceId, reqVO.getDeviceId())
                .betweenIfPresent(CalendarDeviceDO::getDate, reqVO.getDate())
                .betweenIfPresent(CalendarDeviceDO::getStartTime, reqVO.getStartTime())
                .betweenIfPresent(CalendarDeviceDO::getEndTime, reqVO.getEndTime())
                .betweenIfPresent(CalendarDeviceDO::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(CalendarDeviceDO::getId));
    }
    @Delete(
            "<script>" +
                    "delete from dms_calendar_device where device_id = #{id}" +
                    "and date in"+
                    "<foreach item='item' index='index' collection='dateList' open='(' separator=',' close=')'>"+
                    "#{item}"+
                    "</foreach>"+
                    "</script>"
    )
    public void deleteByDateAndDeviceId(@Param("id") String id, @Param("dateList") List<String> dateList);

//    @Delete("delete from dms_calendar_device where device_id = #{deviceId} and shift_id = #{shiftId}")
//    public void deleteByDeviceAndShift(@Param("deviceId") String deviceId,@Param("shiftId")String shiftId);

}
