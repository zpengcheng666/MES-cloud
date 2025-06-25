package com.miyu.module.wms.service.alarm;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.miyu.module.wms.enums.DictConstants;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import org.springframework.validation.annotation.Validated;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import com.miyu.module.wms.controller.admin.alarm.vo.*;
import com.miyu.module.wms.dal.dataobject.alarm.AlarmDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;
import cn.iocoder.yudao.framework.common.util.object.BeanUtils;

import com.miyu.module.wms.dal.mysql.alarm.AlarmMapper;

import static cn.iocoder.yudao.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.miyu.module.wms.enums.ErrorCodeConstants.*;

/**
 * 异常 Service 实现类
 *
 * @author QianJy
 */
@Service
@Validated
public class AlarmServiceImpl implements AlarmService {

    @Resource
    private AlarmMapper alarmMapper;

    @Override
    public String createAlarm(AlarmSaveReqVO createReqVO) {
        // 插入
        AlarmDO alarm = BeanUtils.toBean(createReqVO, AlarmDO.class);
        alarmMapper.insert(alarm);
        // 返回
        return alarm.getId();
    }

    @Override
    public void updateAlarm(AlarmSaveReqVO updateReqVO) {
        // 校验存在
        validateAlarmExists(updateReqVO.getId());
        // 更新
        AlarmDO updateObj = BeanUtils.toBean(updateReqVO, AlarmDO.class);
        alarmMapper.updateById(updateObj);
    }

    @Override
    public void deleteAlarm(String id) {
        // 校验存在
        validateAlarmExists(id);
        // 删除
        alarmMapper.deleteById(id);
    }

    private void validateAlarmExists(String id) {
        if (alarmMapper.selectById(id) == null) {
            throw exception(ALARM_NOT_EXISTS);
        }
    }

    @Override
    public AlarmDO getAlarm(String id) {
        return alarmMapper.selectById(id);
    }

    @Override
    public PageResult<AlarmDO> getAlarmPage(AlarmPageReqVO pageReqVO) {
        return alarmMapper.selectPage(pageReqVO);
    }

    @Override
    public AlarmDO createInfoAlarm(String alarmNum, String alarmDesc, String alarmType) {
        AlarmDO alarmDO = new AlarmDO();
        alarmDO.setAlarmNum(alarmNum);
        alarmDO.setAlarmDesc(alarmDesc);
        alarmDO.setAlarmType(alarmType);
        alarmDO.setAlarmLevel(DictConstants.WMS_ALARM_LEVEL_INFO);
        alarmDO.setAlarmTime(LocalDateTime.now());
        alarmDO.setAlarmState(DictConstants.WMS_ALARM_STATUS_NOT_SOLVED);
        alarmMapper.insert(alarmDO);
        return alarmDO;
    }

    @Override
    public AlarmDO createWarningAlarm(String alarmNum, String alarmDesc, String alarmType) {
        AlarmDO alarmDO = new AlarmDO();
        alarmDO.setAlarmNum(alarmNum);
        alarmDO.setAlarmDesc(alarmDesc);
        alarmDO.setAlarmType(alarmType);
        alarmDO.setAlarmLevel(DictConstants.WMS_ALARM_LEVEL_WARNING);
        alarmDO.setAlarmTime(LocalDateTime.now());
        alarmDO.setAlarmState(DictConstants.WMS_ALARM_STATUS_NOT_SOLVED);
        alarmMapper.insert(alarmDO);
        return alarmDO;
    }

    @Override
    public AlarmDO createErrorAlarm(String alarmNum, String alarmDesc, String alarmType) {
        AlarmDO alarmDO = new AlarmDO();
        alarmDO.setAlarmNum(alarmNum);
        alarmDO.setAlarmDesc(alarmDesc);
        alarmDO.setAlarmType(alarmType);
        alarmDO.setAlarmLevel(DictConstants.WMS_ALARM_LEVEL_ERROR);
        alarmDO.setAlarmTime(LocalDateTime.now());
        alarmDO.setAlarmState(DictConstants.WMS_ALARM_STATUS_NOT_SOLVED);
        alarmMapper.insert(alarmDO);
        return alarmDO;
    }

    @Override
    public AlarmDO createSystemErrorAlarm(ErrorCode errorCode, String extraDesc) {
        return createErrorAlarm(errorCode.getCode().toString(),
                "[" + errorCode.getMsg()+ "] --- [" + extraDesc + " ]",
                DictConstants.WMS_ALARM_TYPE_SYSTEM);
    }

}