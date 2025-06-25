package com.miyu.cloud.mcs.dto.schedule;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miyu.cloud.mcs.dto.schedule.utils.IntervalVarList;
import lombok.Data;

@Data
public class ScheduleLedger {

    //id
    private String id;
    //编码
    private String code;
    //名称
    private String name;
    //产线id
    private String lintStationGroup;
    //产线类型id
    private String lintStationGroupType;
    //设备类型id
    private String equipmentStationType;

    /**
     * 用于存储设备所有可选的任务
     */
    @JSONField(serialize = false,deserialize = false)
    private IntervalVarList machineIntervals = new IntervalVarList();

}
