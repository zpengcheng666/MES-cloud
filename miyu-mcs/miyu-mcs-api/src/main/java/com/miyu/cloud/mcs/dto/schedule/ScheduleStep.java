package com.miyu.cloud.mcs.dto.schedule;

import com.alibaba.fastjson.annotation.JSONField;
import ilog.concert.IloIntervalVar;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class ScheduleStep {

    @Schema(description = "工步id")
    private String id;
    @Schema(description = "工步序号")
    private String stepNum;
    @Schema(description = "工步名称")
    private String stepName;
    @Schema(description = "工时(min)")
    private Integer processingTime;
    @Schema(description = "工步制造资源列表")
    private List<ScheduleResourceType> resourceList;

    @JSONField(serialize = false,deserialize = false)
    private IloIntervalVar innerInterval;
    //预计开始时间
    private Date planStartTime;
    //预计结束时间
    private Date planEndTime;
    //设备id,不是前端传来的
    List<String> ledgerIdList;
    //最终选择的设备
    List<String> selectLedgerIdList;

}
