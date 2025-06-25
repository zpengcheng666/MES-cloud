package com.miyu.cloud.dms.service.calendar;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import com.miyu.cloud.dms.controller.admin.calendar.specialvo.CalendarSpecialPageReqVO;
import com.miyu.cloud.dms.controller.admin.calendar.specialvo.CalendarSpecialSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarSpecialDO;

import javax.validation.Valid;
import java.util.List;

/**
 * 基础日历的工作日（特别版，调休节假日等特殊日期） Service 接口
 *
 * @author 上海弥彧
 */
public interface CalendarSpecialService {

    /**
     * 创建基础日历的工作日（特别版，调休节假日等特殊日期）
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createCalendarSpecial(@Valid CalendarSpecialSaveReqVO createReqVO);

    /**
     * 更新基础日历的工作日（特别版，调休节假日等特殊日期）
     *
     * @param updateReqVO 更新信息
     */
    void updateCalendarSpecial(@Valid CalendarSpecialSaveReqVO updateReqVO);

    /**
     * 删除基础日历的工作日（特别版，调休节假日等特殊日期）
     *
     * @param id 编号
     */
    void deleteCalendarSpecial(String id);

    /**
     * 获得基础日历的工作日（特别版，调休节假日等特殊日期）
     *
     * @param id 编号
     * @return 基础日历的工作日（特别版，调休节假日等特殊日期）
     */
    CalendarSpecialDO getCalendarSpecial(String id);

    /**
     * 获得基础日历的工作日（特别版，调休节假日等特殊日期）分页
     *
     * @param pageReqVO 分页查询
     * @return 基础日历的工作日（特别版，调休节假日等特殊日期）分页
     */
    PageResult<CalendarSpecialDO> getCalendarSpecialPage(CalendarSpecialPageReqVO pageReqVO);

//    public List<CalendarSpecialDO> getListByBasicId(String id);

    public List<CalendarSpecialDO> listAll();

    /**
     * 通过基础日历id和日期集合删除
     * @param createReqVO
     * @return
     */
    public void deleteByDate(CalendarSpecialSaveReqVO createReqVO);

}
