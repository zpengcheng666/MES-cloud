package com.miyu.cloud.Influxdb.enums;

public interface InfluxdbConstans {

    //数据采集使用============================================================================================>
    String DEVICE_ID = "device_id";
    String TOPIC_ID = "topic_id";
    String ERROR = "_error";
    String DATA = "data";
    String STATUSTYPE = "status";
    String SUCCESS = "success";
    String FAIL = "fail";

    //报警日志使用============================================================================================>
    String ALARM_DATABASE = "alarm";
    String AlARM_TYPE = "alarm_type"; //1.标准值报警 2.离线报警
    String ALARM_DATA = "alarm_data";

    //报警类型
    Integer CORM_TYPE = 1; //标准值报警
    Integer OLINE_TYPE = 2; //在线报警


    //站内信息
    String ONLINE_ALARM = "dc_oline_alarm";
    String NORM_ALARM = "dc_norm_alarm";


}
