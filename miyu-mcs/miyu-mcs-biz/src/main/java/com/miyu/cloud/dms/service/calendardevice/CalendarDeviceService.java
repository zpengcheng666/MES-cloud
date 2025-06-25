package com.miyu.cloud.dms.service.calendardevice;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDevicePageReqVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDeviceReqVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDeviceSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarDeviceDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 设备日历,记录设备每天的可用时间 Service 接口
 *
 * @author 上海弥彧
 */
public interface CalendarDeviceService {

    /**
     * 创建设备日历,记录设备每天的可用时间
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCalendarDevice(@Valid CalendarDeviceSaveReqVO createReqVO);

    /**
     * 更新设备日历,记录设备每天的可用时间
     *
     * @param updateReqVO 更新信息
     */
    void updateCalendarDevice(@Valid CalendarDeviceSaveReqVO updateReqVO);

    /**
     * 更新设备日历,主要是时间方面
     *
     * @param updateReqVO 更新信息
     */
    void updateCalendarDeviceByTime(CalendarDeviceSaveReqVO updateReqVO);

    /**
     * 删除设备日历,记录设备每天的可用时间
     *
     * @param id 编号
     */
    void deleteCalendarDevice(String id);

    void deleteByDate(CalendarDeviceSaveReqVO updateReqVO);

    /**
     * 获得设备日历,记录设备每天的可用时间
     *
     * @param id 编号
     * @return 设备日历,记录设备每天的可用时间
     */
    CalendarDeviceDO getCalendarDevice(String id);

    /**
     * 获得设备日历,记录设备每天的可用时间分页
     *
     * @param pageReqVO 分页查询
     * @return 设备日历,记录设备每天的可用时间分页
     */
    PageResult<CalendarDeviceDO> getCalendarDevicePage(CalendarDevicePageReqVO pageReqVO);

    public void bindShift(CalendarDeviceReqVO req) throws Exception;
    public void unbindShift(CalendarDeviceReqVO req) throws Exception;

    List<CalendarDeviceDO> getCalendarDeviceByDeviceId(String id);

}
