package com.miyu.cloud.dms.service.calendar;

import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.miyu.cloud.dms.controller.admin.calendar.vo.BasicCalendarPageReqVO;
import com.miyu.cloud.dms.controller.admin.calendar.vo.BasicCalendarSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.BasicCalendarDO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarDetailDO;
import com.miyu.cloud.dms.dal.mysql.calendar.BasicCalendarMapper;
import com.miyu.cloud.dms.dal.mysql.calendar.CalendarDetailMapper;
import com.miyu.cloud.dms.dal.mysql.calendar.CalendarSpecialMapper;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


/**
 * 基础日历 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class BasicCalendarServiceImpl implements BasicCalendarService {

    @Resource
    private BasicCalendarMapper basicCalendarMapper;

    @Resource
    private CalendarDetailMapper calendarDetailMapper;

    @Resource
    private CalendarSpecialMapper calendarSpecialMapper;

    @SneakyThrows
    @Override
    @Transactional(rollbackFor = Exception.class)
    public String createBasicCalendar(BasicCalendarSaveReqVO createReqVO) {
        // 插入
        BasicCalendarDO basicCalendar = BeanUtils.toBean(createReqVO, BasicCalendarDO.class);
        basicCalendarMapper.insert(basicCalendar);

        //detail已经没用了,方法也没用了
        //createCalendarDeatail(basicCalendar);
        // 返回
        return basicCalendar.getId();
    }

    @Override
    public void updateBasicCalendar(BasicCalendarSaveReqVO updateReqVO) {
        // 校验存在
        validateBasicCalendarExists(updateReqVO.getId());
        // 更新
        BasicCalendarDO updateObj = BeanUtils.toBean(updateReqVO, BasicCalendarDO.class);
        basicCalendarMapper.updateById(updateObj);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBasicCalendar(String id) {
        // 校验存在
        validateBasicCalendarExists(id);
        // 删除
        basicCalendarMapper.deleteById(id);
        //删除日历详情
        calendarDetailMapper.deleteByBasicId(id);
        //删除特殊日历详情
        calendarSpecialMapper.deleteByBasicId(id);
    }

    private void validateBasicCalendarExists(String id) {
        if (basicCalendarMapper.selectById(id) == null) {
            return;
            //throw exception(BASIC_CALENDAR_NOT_EXISTS);
        }
    }

    @Override
    public BasicCalendarDO getBasicCalendar(String id) {
        return basicCalendarMapper.selectById(id);
    }

    @Override
    public PageResult<BasicCalendarDO> getBasicCalendarPage(BasicCalendarPageReqVO pageReqVO) {
        return basicCalendarMapper.selectPage(pageReqVO);
    }

    /**
     * 设备详情,只是设置这段时间每天工作和休假
     * @param basicCalendar
     */
    public void createCalendarDeatail(BasicCalendarDO basicCalendar) throws Exception {
        LocalDateTime startDate = basicCalendar.getStartDate();
        LocalDateTime endDate = basicCalendar.getEndDate();
        String startStr = String.valueOf(startDate.getYear()) + "-" + String.valueOf(startDate.getMonthValue()) + "-" + String.valueOf(startDate.getDayOfMonth());
        String endStr = String.valueOf(endDate.getYear()) + "-" + String.valueOf(endDate.getMonthValue()) + "-" + String.valueOf(endDate.getDayOfMonth());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        //获取开始时间毫秒值
        long startTime = sdf.parse(startStr).getTime();
        //获取结束时间毫秒值
        long endTime = sdf.parse(endStr).getTime();
        List<CalendarDetailDO> list = new ArrayList<>();
        for(long day = startTime;day<=endTime;day =addDay(day)){
            CalendarDetailDO calendarDetailDO = new CalendarDetailDO();
            //设置基础日历id
            calendarDetailDO.setBcid(basicCalendar.getId());

            String dateStr = sdf.format(day);
            //设置日期
            calendarDetailDO.setCddate(dateStr);

            Date date = sdf.parse(dateStr);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            //获取一周中的星期(1-7,周日-周六,周日是第一天)
            int weekDay = calendar.get(Calendar.DAY_OF_WEEK);
            if(weekDay==1||weekDay==7){
                //休息
                calendarDetailDO.setCdname(1);
            }else {
                //工作
                calendarDetailDO.setCdname(2);
            }
            list.add(calendarDetailDO);
        }
        calendarDetailMapper.insertBatch(list);
    }

    public long addDay(long day){
        day =   86400 * 1000 + day;
        return day;
    }

}
