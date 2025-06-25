package com.miyu.cloud.dms.service.calendar;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.calendar.detailvo.CalendarDetailPageReqVO;
import com.miyu.cloud.dms.controller.admin.calendar.detailvo.CalendarDetailSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarDetailDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 基础日历的工作日 Service 接口
 *
 * @author 上海弥彧
 */
public interface CalendarDetailService {

    /**
     * 创建基础日历的工作日
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCalendarDetail(@Valid CalendarDetailSaveReqVO createReqVO);

    /**
     * 更新基础日历的工作日
     *
     * @param updateReqVO 更新信息
     */
    void updateCalendarDetail(@Valid CalendarDetailSaveReqVO updateReqVO);

    /**
     * 删除基础日历的工作日
     *
     * @param id 编号
     */
    void deleteCalendarDetail(String id);

    /**
     * 获得基础日历的工作日
     *
     * @param id 编号
     * @return 基础日历的工作日
     */
    CalendarDetailDO getCalendarDetail(String id);

    /**
     * 获得基础日历的工作日分页
     *
     * @param pageReqVO 分页查询
     * @return 基础日历的工作日分页
     */
    PageResult<CalendarDetailDO> getCalendarDetailPage(CalendarDetailPageReqVO pageReqVO);

    List<CalendarDetailDO> getListByBasicId(String id);

}
