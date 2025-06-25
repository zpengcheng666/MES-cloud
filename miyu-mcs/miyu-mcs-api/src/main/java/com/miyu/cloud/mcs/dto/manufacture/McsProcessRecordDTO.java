package com.miyu.cloud.mcs.dto.manufacture;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
public class McsProcessRecordDTO {

    //物料条码(为空时, 在订单指定条码中任选)
    private String barCode;
    //指定设备集合
    private List<String> deviceIds;
    //计划开始时间
    private LocalDateTime planStartTime;
    //计划结束时间
    private LocalDateTime planEndTime;
    //工步任务集合
    private List<McsStepRecordDTO> stepRecordList = new ArrayList<>();

    //生产单号
    private String number;


}
