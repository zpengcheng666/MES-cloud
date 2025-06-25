package com.miyu.cloud.mcs.dto.schedule;

import lombok.Data;

import java.util.List;

@Data
public class ScheduleProcess {

    //id
    private String id;
    //工序序号
    private String procedureNum;
    //工序名称(加工路线)
    private String procedureName;
    //是否专检
    private Integer isInspect;
    //是否外委
    private Integer isOut;
    //准备工时
    private Integer preparationTime;
    //加工工时
    private Integer processingTime;
    //工步列表
    private List<ScheduleStep> stepList;
    //制造资源列表
    private List<ScheduleResourceType> resourceList;
    //指定设备id
    private List<String> ledgerIdList;
}
