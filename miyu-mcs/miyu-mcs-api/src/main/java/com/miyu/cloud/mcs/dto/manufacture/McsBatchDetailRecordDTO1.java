package com.miyu.cloud.mcs.dto.manufacture;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsBatchDetailRecordDTO1 {

    //工步id
    private String stepId;
    //工步名称
    private String stepName;
    //操作类型  //1开工 2完工
    private Integer operationType;

    //操作者
    private String operationBy;
    //时间
    private LocalDateTime operationTime;
    //数量
    private Integer totality;
}
