package com.miyu.cloud.mcs.controller.admin.batchorderapsresult.vo;

import com.miyu.cloud.mcs.dto.schedule.ScheduleLedger;
import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class OrderScheduleSaveVO {

    private List<ScheduleLedger> deviceList;
    private Map<String,Map<String,List<ScheduleLedger>>> planDevice;
    private OrderScheduleStrategy strategy;
}
