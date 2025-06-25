package com.miyu.module.Influxdb.service;

import com.miyu.module.Influxdb.dal.Offline;
import com.miyu.module.dc.controller.admin.offlinealarm.vo.OfflineAlarmPageReqVO;
import com.miyu.module.dc.controller.admin.offlinecollect.vo.OfflineCollectPageReqVO;
import com.miyu.module.dc.controller.admin.offlineerror.vo.OfflineErrorPageReqVO;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

import static com.miyu.cloud.Influxdb.enums.InfluxdbConstans.*;

@Component
public class OfflineService {

    /**
     * 生成数据采集基础sql
     */
    public Offline queryOffline(String table , OfflineCollectPageReqVO reqVO) {
        //分页
        String limit = " order by time desc" + " limit " + reqVO.getPageSize() + " offset " + (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        //基础SQL
        String selectSql = " select \"device_id\", \"topic_id\",\"time\",\"data\" from \"" + table + "\" where \"" + DEVICE_ID +"\" = '" + reqVO.getDeviceId() + "'" + " and \"" + TOPIC_ID + "\" = '" + reqVO.getTopicId() + "'" + " and \"" + STATUSTYPE + "\" = '" + SUCCESS + "'";
        return new Offline().setSelectSQL(selectSql).setLimit(limit);
    }

    /**
     * 生成数据采集总量查询sql
     */
    public Offline queryCountOffline(String table , OfflineCollectPageReqVO reqVO) {
        //基础SQL
        String selectSql = " select count(\"data\") from \"" + table + "\" where \"" + DEVICE_ID +"\" = '" + reqVO.getDeviceId() + "'" + " and \"" + TOPIC_ID + "\" = '" + reqVO.getTopicId() + "'" + " and \"" + STATUSTYPE + "\" = '" + SUCCESS + "'";
        return new Offline().setSelectSQL(selectSql);
    }

    //** 当前生成数据采集和错误日志部分代码可合并 避免后续逻辑修改暂不合并 确认后修改 **=====================================================================================================================================================================================================》

    /**
     * 生成错误日志基础sql
     */
    public Offline queryOfflineError(String table , OfflineErrorPageReqVO reqVO) {
        //分页
        String limit = " order by time desc" + " limit " + reqVO.getPageSize() + " offset " + (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        //基础SQL
        String selectSql = " select \"device_id\", \"topic_id\",\"time\",\"data\" from \"" + table + "\" where \"" + DEVICE_ID +"\" = '" + reqVO.getDeviceId() + "'" + " and \"" + TOPIC_ID + "\" = '" + reqVO.getTopicId() + "'" + " and \"" + STATUSTYPE + "\" = '" + FAIL + "'";
        return new Offline().setSelectSQL(selectSql).setLimit(limit);
    }

    /**
     * 生成错误日志总量查询sql
     */
    public Offline queryCountOfflineError(String table , OfflineErrorPageReqVO reqVO) {
        //基础SQL
        String selectSql = " select count(\"data\") from \"" + table + "\" where \"" + DEVICE_ID +"\" = '" + reqVO.getDeviceId() + "'" + " and \"" + TOPIC_ID + "\" = '" + reqVO.getTopicId() + "'" + " and \"" + STATUSTYPE + "\" = '" + FAIL + "'";
        return new Offline().setSelectSQL(selectSql);
    }

    //** 以下为报警值报警表相关=====================================================================================================================================================================================================================================================》

    /**
     * 报警表数据查询
     */
    public Offline queryAlarm(OfflineAlarmPageReqVO reqVO , Integer type) {
        //分页
        String limit = " order by time desc" + " limit " + reqVO.getPageSize() + " offset " + (reqVO.getPageNo() - 1) * reqVO.getPageSize();
        String selectSql = " select \"alarm_data\", \"topic_id\",\"time\",\"device_id\" from \"" + ALARM_DATABASE + "\" where \"" + AlARM_TYPE +"\" = '" + type + "'" + " and \"" + TOPIC_ID + "\" = '" + reqVO.getTopicId() + "'";
        return new Offline().setSelectSQL(selectSql).setLimit(limit);
    }

    /**
     * 报警表数据总量查询
     */
    public Offline queryCountAlarm( OfflineAlarmPageReqVO reqVO , Integer type) {
        String selectSql = " select count(\"alarm_data\") from \"" + ALARM_DATABASE + "\" where \"" + AlARM_TYPE +"\" = '" + type + "'" + " and \"" + TOPIC_ID + "\" = '" + reqVO.getTopicId() + "'";
        return new Offline().setSelectSQL(selectSql);
    }

}
