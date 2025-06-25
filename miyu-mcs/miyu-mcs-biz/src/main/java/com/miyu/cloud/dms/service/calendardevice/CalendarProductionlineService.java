package com.miyu.cloud.dms.service.calendardevice;

import cn.iocoder.yudao.framework.common.pojo.CommonResult;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.calendardevice.provo.CalendarProductionlinePageReqVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.provo.CalendarProductionlineSaveReqVO;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTimeRespVO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarProductionlineDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 设备日历关联部分,记录了设备绑定了那些班次 Service 接口
 *
 * @author 上海弥彧
 */
public interface CalendarProductionlineService {

    /**
     * 创建设备日历关联部分,记录了设备绑定了那些班次
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCalendarProductionline(@Valid CalendarProductionlineSaveReqVO createReqVO);

    /**
     * 更新设备日历关联部分,记录了设备绑定了那些班次
     *
     * @param updateReqVO 更新信息
     */
    void updateCalendarProductionline(@Valid CalendarProductionlineSaveReqVO updateReqVO);

    /**
     * 删除设备日历关联部分,记录了设备绑定了那些班次
     *
     * @param id 编号
     */
    void deleteCalendarProductionline(String id);

    /**
     * 获得设备日历关联部分,记录了设备绑定了那些班次
     *
     * @param id 编号
     * @return 设备日历关联部分,记录了设备绑定了那些班次
     */
    CalendarProductionlineDO getCalendarProductionline(String id);

    /**
     * 获得设备日历关联部分,记录了设备绑定了那些班次分页
     *
     * @param pageReqVO 分页查询
     * @return 设备日历关联部分,记录了设备绑定了那些班次分页
     */
    PageResult<CalendarProductionlineDO> getCalendarProductionlinePage(CalendarProductionlinePageReqVO pageReqVO);

    List<CalendarProductionlineDO> selectWith(CalendarProductionlineDO productionlineDO);

    /**
     * 通过设备id查询班次时间,这个是通用时间
     * @param id
     * @return
     */
    public List<ShiftTimeRespVO> getShiftByDeviceId(String id);

}
