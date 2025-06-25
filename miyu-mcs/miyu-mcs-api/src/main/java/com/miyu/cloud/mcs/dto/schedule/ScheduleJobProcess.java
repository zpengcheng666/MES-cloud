package com.miyu.cloud.mcs.dto.schedule;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonIgnore;
import ilog.concert.IloIntervalVar;
import lombok.Data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
public class ScheduleJobProcess {
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
    //工艺类型：0机加、1热处理、2荧光线、3三坐标测量
    private Integer processType;
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

//    @JsonIgnore
    @JSONField(serialize = false,deserialize = false)
    private IloIntervalVar innerInterval;
    //预计开始时间
    private Date planStartTime;
    //预计结束时间
    private Date planEndTime;

    //指定设备id
//    @JsonIgnore
    @JSONField(serialize = false,deserialize = false)
    private List<ScheduleLedger> ledgerList;

    //选定设备id,上边那个不是,这个才是最终选择的设备id
    private List<String> selectLedgerIdList = new ArrayList<>();
}
