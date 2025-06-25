package com.miyu.cloud.dms.service.calendar;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.calendar.vo.BasicCalendarPageReqVO;
import com.miyu.cloud.dms.controller.admin.calendar.vo.BasicCalendarSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.BasicCalendarDO;

import javax.validation.Valid;

/**
 * 基础日历 Service 接口
 *
 * @author 上海弥彧
 */
public interface BasicCalendarService {

    /**
     * 创建基础日历
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createBasicCalendar(@Valid BasicCalendarSaveReqVO createReqVO);

    /**
     * 更新基础日历
     *
     * @param updateReqVO 更新信息
     */
    void updateBasicCalendar(@Valid BasicCalendarSaveReqVO updateReqVO);

    /**
     * 删除基础日历
     *
     * @param id 编号
     */
    void deleteBasicCalendar(String id);

    /**
     * 获得基础日历
     *
     * @param id 编号
     * @return 基础日历
     */
    BasicCalendarDO getBasicCalendar(String id);

    /**
     * 获得基础日历分页
     *
     * @param pageReqVO 分页查询
     * @return 基础日历分页
     */
    PageResult<BasicCalendarDO> getBasicCalendarPage(BasicCalendarPageReqVO pageReqVO);

}
