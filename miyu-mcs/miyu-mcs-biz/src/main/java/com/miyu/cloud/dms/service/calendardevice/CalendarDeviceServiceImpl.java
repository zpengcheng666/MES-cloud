package com.miyu.cloud.dms.service.calendardevice;

import cn.hutool.core.util.StrUtil;
import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.util.collection.CollectionUtils;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDevicePageReqVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDeviceReqVO;
import com.miyu.cloud.dms.controller.admin.calendardevice.vo.CalendarDeviceSaveReqVO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarDetailDO;
import com.miyu.cloud.dms.dal.dataobject.calendar.CalendarSpecialDO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarDeviceDO;
import com.miyu.cloud.dms.dal.dataobject.calendardevice.CalendarProductionlineDO;
import com.miyu.cloud.dms.dal.dataobject.calendarshift.ShiftTimeDO;
import com.miyu.cloud.dms.dal.mysql.calendar.CalendarDetailMapper;
import com.miyu.cloud.dms.dal.mysql.calendar.CalendarSpecialMapper;
import com.miyu.cloud.dms.dal.mysql.calendardevice.CalendarDeviceMapper;
import com.miyu.cloud.dms.dal.mysql.calendardevice.CalendarProductionlineMapper;
import com.miyu.cloud.dms.dal.mysql.calendarshift.ShiftTimeMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;

/**
 * 设备日历,记录设备每天的可用时间 Service 实现类
 *
 * @author 上海弥彧
 */
@Service
@Validated
public class CalendarDeviceServiceImpl implements CalendarDeviceService {

    @Resource
    private CalendarDeviceMapper calendarDeviceMapper;

    @Resource
    private CalendarProductionlineMapper calendarProductionlineMapper;

    @Resource
    private CalendarDetailMapper calendarDetailMapper;

    @Resource
    private CalendarSpecialMapper calendarSpecialMapper;

    @Resource
    private ShiftTimeMapper shiftTimeMapper;

    @Override
    public String createCalendarDevice(CalendarDeviceSaveReqVO createReqVO) {
        // 插入
        CalendarDeviceDO calendarDevice = BeanUtils.toBean(createReqVO, CalendarDeviceDO.class);
        calendarDeviceMapper.insert(calendarDevice);
        // 返回
        return calendarDevice.getId();
    }

    @Override
    public void updateCalendarDevice(CalendarDeviceSaveReqVO updateReqVO) {
        // 校验存在
        validateCalendarDeviceExists(updateReqVO.getId());
        // 更新
        CalendarDeviceDO updateObj = BeanUtils.toBean(updateReqVO, CalendarDeviceDO.class);
        calendarDeviceMapper.updateById(updateObj);
    }

    @Override
    public void updateCalendarDeviceByTime(CalendarDeviceSaveReqVO updateReqVO) {
        System.out.println(System.currentTimeMillis());
        if(StrUtil.isEmpty(updateReqVO.getDateList())){
            return;
        }
        String[] dateSplit = updateReqVO.getDateList().split(",");
        List<String> dateList = Arrays.asList(dateSplit);
        //删除设备日历,该设备存在的日期
        calendarDeviceMapper.deleteByDateAndDeviceId(updateReqVO.getDeviceId(),dateList);

        //新增设备日历
        List<ShiftTimeDO> timeList = updateReqVO.getTimeList();
        //设备日历集合
        ArrayList<CalendarDeviceDO> calendarDeviceList = new ArrayList<>();
        System.out.println(System.currentTimeMillis());
        for (String dateStr : dateList) {
            String[] dateArray = dateStr.split("-");
            LocalDate date = LocalDate.of(Integer.valueOf(dateArray[0]), Integer.valueOf(dateArray[1]), Integer.valueOf(dateArray[2]));
            for (ShiftTimeDO shiftTimeDO : timeList) {
                String[] startArray = shiftTimeDO.getStartTime().split(":");
                LocalTime startLocalTime = LocalTime.of(Integer.valueOf(startArray[0]), Integer.valueOf(startArray[1]));
                String[] endArray = shiftTimeDO.getEndTime().split(":");
                LocalTime endLocalTime = LocalTime.of(Integer.valueOf(endArray[0]), Integer.valueOf(endArray[1]));
                //设备日历
                CalendarDeviceDO calendarDeviceDO = new CalendarDeviceDO();
                calendarDeviceDO.setDeviceId(updateReqVO.getDeviceId()).setDate(date).setStartTime(startLocalTime).setEndTime(endLocalTime);
                calendarDeviceList.add(calendarDeviceDO);
            }
        }
        System.out.println(System.currentTimeMillis());
        //添加
        calendarDeviceMapper.insertBatch(calendarDeviceList);
        System.out.println(System.currentTimeMillis());

    }

    @Override
    public void deleteCalendarDevice(String id) {
        // 校验存在
        validateCalendarDeviceExists(id);
        // 删除
        calendarDeviceMapper.deleteById(id);
    }

    @Override
    public void deleteByDate(CalendarDeviceSaveReqVO updateReqVO) {
        if(StrUtil.isEmpty(updateReqVO.getDateList())){
            return;
        }
        String[] dateSplit = updateReqVO.getDateList().split(",");
        calendarDeviceMapper.deleteByDateAndDeviceId(updateReqVO.getDeviceId(),Arrays.asList(dateSplit));

    }

    private void validateCalendarDeviceExists(String id) {
        if (calendarDeviceMapper.selectById(id) == null) {
            throw exception(new ErrorCode(500,"设备日历不存在"));
        }
    }

    @Override
    public CalendarDeviceDO getCalendarDevice(String id) {
        return calendarDeviceMapper.selectById(id);
    }

    @Override
    public PageResult<CalendarDeviceDO> getCalendarDevicePage(CalendarDevicePageReqVO pageReqVO) {
        return calendarDeviceMapper.selectPage(pageReqVO);
    }

//    /**
//     * 设备关联班次
//     * @param req
//     */
//    @Override
//    @Transactional(rollbackFor = Exception.class)
//    public void bindShift(CalendarDeviceReqVO req) throws Exception {
//        //首先生成绑定记录(作用好像只是区分绑定和解绑)
//        CalendarProductionlineDO productionlineDO = BeanUtils.toBean(req, CalendarProductionlineDO.class);
//        calendarProductionlineMapper.insert(productionlineDO);
//        //绑定
//        //查询基础日历,得出绑定日期
//        LambdaQueryWrapper<CalendarDetailDO> detailWrapper = new LambdaQueryWrapper<>();
//        detailWrapper.eq(CalendarDetailDO::getBcid, req.getBcid());
//        List<CalendarDetailDO> calendarDetailList = calendarDetailMapper.selectList(detailWrapper);
//        //查询特殊日期,更改基础信息
//        LambdaQueryWrapper<CalendarSpecialDO> specilWrapper = new LambdaQueryWrapper<>();
//        specilWrapper.eq(CalendarSpecialDO::getBcid,req.getBcid());
//        List<CalendarSpecialDO> calendarSpecialDOS = calendarSpecialMapper.selectList(specilWrapper);
//        Map<String, CalendarSpecialDO> specialDOMap = CollectionUtils.convertMap(calendarSpecialDOS, CalendarSpecialDO::getCsdate);
//
//
//        //查询班次,得到班次信息(班次的时间)
//        LambdaQueryWrapper<ShiftTimeDO> timeWrapper = new LambdaQueryWrapper<>();
//        timeWrapper.eq(ShiftTimeDO::getTypeId,req.getShiftId());
//        List<ShiftTimeDO> shiftTimeList = shiftTimeMapper.selectList(timeWrapper);
//
//        //合起来形成设备日历,在新增前把原本那天的日历删除,然后新增
//        //日期集合
//        ArrayList<String> dateList = new ArrayList<>();
//        //设备日历集合
//        ArrayList<CalendarDeviceDO> calendarDeviceList = new ArrayList<>();
//        for (CalendarDetailDO calendarDetailDO : calendarDetailList) {
//            //先判断是不是放假(优先判断特殊日历)
//            if(specialDOMap.containsKey(calendarDetailDO.getCddate())){
//                CalendarSpecialDO calendarSpecialDO = specialDOMap.get(calendarDetailDO.getCddate());
//                //1为休息
//                if(calendarSpecialDO.getCsname()==1){
//                    continue;
//                }
//            }else {
//                if(calendarDetailDO.getCdname()==1){
//                    continue;
//                }
//            }
//
//            dateList.add(calendarDetailDO.getCddate());
//            String cddate = calendarDetailDO.getCddate();
//            String[] dateArray = cddate.split("-");
//            LocalDate date = LocalDate.of(Integer.valueOf(dateArray[0]), Integer.valueOf(dateArray[1]), Integer.valueOf(dateArray[2]));
//            for (ShiftTimeDO shiftTimeDO : shiftTimeList) {
//                String[] startArray = shiftTimeDO.getStartTime().split(":");
//                LocalTime startLocalTime = LocalTime.of(Integer.valueOf(startArray[0]), Integer.valueOf(startArray[1]));
//                String[] endArray = shiftTimeDO.getEndTime().split(":");
//                LocalTime endLocalTime = LocalTime.of(Integer.valueOf(endArray[0]), Integer.valueOf(endArray[1]));
//                //设备日历
//                CalendarDeviceDO calendarDeviceDO = new CalendarDeviceDO();
//                calendarDeviceDO.setDeviceId(req.getDeviceId()).setDate(date).setStartTime(startLocalTime).setEndTime(endLocalTime);
//                calendarDeviceList.add(calendarDeviceDO);
//            }
//        }
//        //删除选中的日期
//        calendarDeviceMapper.deleteByDateAndDeviceId(req.getDeviceId(),dateList);
//        //添加
//        calendarDeviceMapper.insertBatch(calendarDeviceList);
//    }

    /**
     * 设备关联班次
     * @param req
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void bindShift(CalendarDeviceReqVO req) {
        //首先生成绑定记录(作用好像只是区分绑定和解绑)
        CalendarProductionlineDO productionlineDO = BeanUtils.toBean(req, CalendarProductionlineDO.class);
        calendarProductionlineMapper.insert(productionlineDO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void unbindShift(CalendarDeviceReqVO req) throws Exception {
        //删除productionLine
        calendarProductionlineMapper.deleteByDeviceAndShift(req.getDeviceId(),req.getShiftId());
//        LambdaQueryWrapper<CalendarDetailDO> wrapper = new LambdaQueryWrapper<>();
//        wrapper.eq(CalendarDetailDO::getBcid,req.getBcid());
//        List<CalendarDetailDO> calendarDetailDOS = calendarDetailMapper.selectList(wrapper);
//        List<String> dateList = calendarDetailDOS.stream().map(CalendarDetailDO::getCddate).collect(Collectors.toList());
        //删除设备日历
        //calendarDeviceMapper.deleteByDateAndDeviceId(req.getDeviceId(),dateList);
    }

    @Override
    public List<CalendarDeviceDO> getCalendarDeviceByDeviceId(String id) {
        LambdaQueryWrapper<CalendarDeviceDO> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(CalendarDeviceDO::getDeviceId,id);
        return calendarDeviceMapper.selectList(wrapper);
    }

}
