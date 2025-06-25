package com.miyu.cloud.dms.service.calendardevice;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.controller.admin.calendardevice.provo.CalendarProductionlinePageReqVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.provo.CalendarProductionlineSaveReqVO;
import com.miyu.cloud.dms.controller.admin.calendarshift.vo.ShiftTimeRespVO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarProductionlineDO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTimeDO;
import com.miyu.cloud.dms.dal.mysql.calendardevice.CalendarProductionlineMapper;
import com.miyu.cloud.dms.dal.mysql.calendarshift.ShiftTimeMapper;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 设备日历关联部分,记录了设备绑定了那些班次 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class CalendarProductionlineServiceImpl implements CalendarProductionlineService {

    @Resource
    private CalendarProductionlineMapper calendarProductionlineMapper;

    @Resource
    private ShiftTimeMapper shiftTimeMapper;

    @Override
    public String createCalendarProductionline(CalendarProductionlineSaveReqVO createReqVO) {
        // 插入
        CalendarProductionlineDO calendarProductionline = BeanUtils.toBean(createReqVO, CalendarProductionlineDO.class);
        calendarProductionlineMapper.insert(calendarProductionline);
        // 返回
        return calendarProductionline.getId();
    }

    @Override
    public void updateCalendarProductionline(CalendarProductionlineSaveReqVO updateReqVO) {
        // 校验存在
        validateCalendarProductionlineExists(updateReqVO.getId());
        // 更新
        CalendarProductionlineDO updateObj = BeanUtils.toBean(updateReqVO, CalendarProductionlineDO.class);
        calendarProductionlineMapper.updateById(updateObj);
    }

    @Override
    public void deleteCalendarProductionline(String id) {
        // 校验存在
        validateCalendarProductionlineExists(id);
        // 删除
        calendarProductionlineMapper.deleteById(id);
    }

    private void validateCalendarProductionlineExists(String id) {
        if (calendarProductionlineMapper.selectById(id) == null) {
            throw exception(new ErrorCode(500,"未关联设备"));
        }
    }

    @Override
    public CalendarProductionlineDO getCalendarProductionline(String id) {
        return calendarProductionlineMapper.selectById(id);
    }

    @Override
    public PageResult<CalendarProductionlineDO> getCalendarProductionlinePage(CalendarProductionlinePageReqVO pageReqVO) {
        return calendarProductionlineMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CalendarProductionlineDO> selectWith(CalendarProductionlineDO productionlineDO) {
        List<CalendarProductionlineDO> calendarProductionlineDOS = calendarProductionlineMapper.selectListWith(productionlineDO);
        return calendarProductionlineDOS;
    }

    @Override
    public List<ShiftTimeRespVO> getShiftByDeviceId(String id) {
        LambdaQueryWrapper<CalendarProductionlineDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CalendarProductionlineDO::getDeviceId,id);
        List<CalendarProductionlineDO> calendarProductionlineDOS = calendarProductionlineMapper.selectList(wrapper);
        if (calendarProductionlineDOS.size()==0){
            return Collections.emptyList();
        }
        List<String> typeIds = calendarProductionlineDOS.stream().map(CalendarProductionlineDO::getShiftId).collect(Collectors.toList());
        LambdaQueryWrapper<ShiftTimeDO> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(ShiftTimeDO::getTypeId, typeIds);
        List<ShiftTimeDO> shiftTimeDOS = shiftTimeMapper.selectList(wrapper1);
        List<ShiftTimeRespVO> shiftTimeRespVOS = BeanUtils.toBean(shiftTimeDOS, ShiftTimeRespVO.class, vo -> {
            String start = vo.getStartTime().toString();
            String end = vo.getEndTime().toString();
            vo.setStartStr(start);
            vo.setEndStr(end);
        });
        return shiftTimeRespVOS;
    }

}
