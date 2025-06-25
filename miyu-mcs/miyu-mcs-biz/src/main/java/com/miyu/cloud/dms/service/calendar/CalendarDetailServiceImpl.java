package com.miyu.cloud.dms.service.calendar;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.controller.admin.calendar.detailvo.CalendarDetailPageReqVO;
import com.miyu.cloud.dms.controller.admin.calendar.detailvo.CalendarDetailSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarDetailDO;
import com.miyu.cloud.dms.dal.mysql.calendar.CalendarDetailMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 基础日历的工作日 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class CalendarDetailServiceImpl implements CalendarDetailService {

    @Resource
    private CalendarDetailMapper calendarDetailMapper;

    @Override
    public String createCalendarDetail(CalendarDetailSaveReqVO createReqVO) {
        // 插入
        CalendarDetailDO calendarDetail = BeanUtils.toBean(createReqVO, CalendarDetailDO.class);
        calendarDetailMapper.insert(calendarDetail);
        // 返回
        return calendarDetail.getId();
    }

    @Override
    public void updateCalendarDetail(CalendarDetailSaveReqVO updateReqVO) {
        // 校验存在
        validateCalendarDetailExists(updateReqVO.getId());
        // 更新
        CalendarDetailDO updateObj = BeanUtils.toBean(updateReqVO, CalendarDetailDO.class);
        calendarDetailMapper.updateById(updateObj);
    }

    @Override
    public void deleteCalendarDetail(String id) {
        // 校验存在
        validateCalendarDetailExists(id);
        // 删除
        calendarDetailMapper.deleteById(id);
    }

    private void validateCalendarDetailExists(String id) {
        if (calendarDetailMapper.selectById(id) == null) {
            throw exception(new ErrorCode(500,"日历详情不存在"));
        }
    }

    @Override
    public CalendarDetailDO getCalendarDetail(String id) {
        return calendarDetailMapper.selectById(id);
    }

    @Override
    public PageResult<CalendarDetailDO> getCalendarDetailPage(CalendarDetailPageReqVO pageReqVO) {
        return calendarDetailMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CalendarDetailDO> getListByBasicId(String id) {
        LambdaQueryWrapper<CalendarDetailDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CalendarDetailDO::getBcid,id);
        wrapper.orderByAsc(CalendarDetailDO::getId);
        return calendarDetailMapper.selectList(wrapper);
    }

}
