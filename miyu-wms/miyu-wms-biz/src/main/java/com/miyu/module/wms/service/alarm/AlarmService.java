package com.miyu.module.wms.service.alarm;

import java.util.*;
import javax.validation.*;

import cn.iocoder.yudao.framework.common.exception.ErrorCode;
import com.miyu.module.wms.controller.admin.alarm.vo.*;
import com.miyu.module.wms.dal.dataobject.alarm.AlarmDO;
import cn.iocoder.yudao.framework.common.pojo.PageResult;
import cn.iocoder.yudao.framework.common.pojo.PageParam;

/**
 * 异常 Service 接口
 *
 * @author QianJy
 */
public interface AlarmService {

    /**
     * 创建异常
     *
     * @param createReqVO 创建信息
     * @return 编号
     */
    String createAlarm(@Valid AlarmSaveReqVO createReqVO);

    /**
     * 更新异常
     *
     * @param updateReqVO 更新信息
     */
    void updateAlarm(@Valid AlarmSaveReqVO updateReqVO);

    /**
     * 删除异常
     *
     * @param id 编号
     */
    void deleteAlarm(String id);

    /**
     * 获得异常
     *
     * @param id 编号
     * @return 异常
     */
    AlarmDO getAlarm(String id);

    /**
     * 获得异常分页
     *
     * @param pageReqVO 分页查询
     * @return 异常分页
     */
    PageResult<AlarmDO> getAlarmPage(AlarmPageReqVO pageReqVO);

    AlarmDO createInfoAlarm(String alarmNum, String alarmDesc, String alarmType);

    AlarmDO createWarningAlarm(String alarmNum, String alarmDesc, String alarmType);

    AlarmDO createErrorAlarm(String alarmNum, String alarmDesc, String alarmType);

    /**
     * 创建系统异常告警
     * @param errorCode
     * @param extraDesc  补充信息  例如请求参数等
     * @return
     */
    AlarmDO createSystemErrorAlarm(ErrorCode errorCode,String extraDesc);
}