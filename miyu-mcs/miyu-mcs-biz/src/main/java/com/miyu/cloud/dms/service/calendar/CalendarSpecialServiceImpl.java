package com.miyu.cloud.dms.service.calendar;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.controller.admin.calendar.specialvo.CalendarSpecialPageReqVO;
import com.miyu.cloud.dms.controller.admin.calendar.specialvo.CalendarSpecialSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarSpecialDO;
import com.miyu.cloud.dms.dal.mysql.calendar.CalendarSpecialMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 基础日历的工作日（特别版，调休节假日等特殊日期） Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class CalendarSpecialServiceImpl implements CalendarSpecialService {

    @Resource
    private CalendarSpecialMapper calendarSpecialMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createCalendarSpecial(CalendarSpecialSaveReqVO createReqVO) {
        if(StrUtil.isEmpty(createReqVO.getCsdate())){
            return "0";
        }
        String[] dates = createReqVO.getCsdate().split(",");
        //先把选中的删除,这样省事一点
        List<String> dateList = Arrays.asList(dates);
        calendarSpecialMapper.deleteByDates(dateList);

        for (String date : dates) {
            // 插入
            CalendarSpecialDO calendarSpecial = BeanUtils.toBean(createReqVO, CalendarSpecialDO.class);
            calendarSpecial.setCsdate(date);
            calendarSpecialMapper.insert(calendarSpecial);
        }

        // 插入
//        CalendarSpecialDO calendarSpecial = BeanUtils.toBean(createReqVO, CalendarSpecialDO.class);
//        calendarSpecialMapper.insert(calendarSpecial);
        // 返回
        return "ok";
    }

    @Override
    public void updateCalendarSpecial(CalendarSpecialSaveReqVO updateReqVO) {
        // 校验存在
        validateCalendarSpecialExists(updateReqVO.getId());
        // 更新
        CalendarSpecialDO updateObj = BeanUtils.toBean(updateReqVO, CalendarSpecialDO.class);
        calendarSpecialMapper.updateById(updateObj);
    }

    @Override
    public void deleteCalendarSpecial(String id) {
        // 校验存在
        validateCalendarSpecialExists(id);
        // 删除
        calendarSpecialMapper.deleteById(id);
    }

    private void validateCalendarSpecialExists(String id) {
        if (calendarSpecialMapper.selectById(id) == null) {
            throw exception(new ErrorCode(500,"特殊日历不存在"));
        }
    }

    @Override
    public CalendarSpecialDO getCalendarSpecial(String id) {
        return calendarSpecialMapper.selectById(id);
    }

    @Override
    public PageResult<CalendarSpecialDO> getCalendarSpecialPage(CalendarSpecialPageReqVO pageReqVO) {
        return calendarSpecialMapper.selectPage(pageReqVO);
    }

    @Override
    public List<CalendarSpecialDO> listAll() {
        return calendarSpecialMapper.selectList();
    }

//    @Override
//    public List<CalendarSpecialDO> getListByBasicId(String id) {
//        LambdaQueryWrapper<CalendarSpecialDO> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(CalendarSpecialDO::getBcid,id);
//        return calendarSpecialMapper.selectList(wrapper);
//    }

    @Override
    public void deleteByDate(CalendarSpecialSaveReqVO createReqVO) {
        if(StrUtil.isEmpty(createReqVO.getCsdate())){
            return;
        }
        String[] dates = createReqVO.getCsdate().split(",");
        List<String> dateList = Arrays.asList(dates);
        calendarSpecialMapper.deleteByDates(dateList);
    }

}
