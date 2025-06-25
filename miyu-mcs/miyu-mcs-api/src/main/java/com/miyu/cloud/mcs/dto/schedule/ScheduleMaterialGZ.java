package com.miyu.cloud.mcs.dto.schedule;

import com.alibaba.fastjson.annotation.JSONField;
import com.miyu.cloud.mcs.dto.schedule.utils.IntervalVarList;
import lombok.Data;

/**
 * 工装
 */
@Data
public class ScheduleMaterialGZ {

    private int index;
    private String materialConfigId;

    public ScheduleMaterialGZ(int index,String materialConfigId) {
        this.materialConfigId = materialConfigId;
        this.index = index;
    }

    /**
     * 用于存储工装所有可选的任务
     */
    @JSONField(serialize = false,deserialize = false)
    private IntervalVarList machineIntervals = new IntervalVarList();
}
