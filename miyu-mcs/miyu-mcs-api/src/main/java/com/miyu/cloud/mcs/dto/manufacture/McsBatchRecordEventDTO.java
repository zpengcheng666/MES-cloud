package com.miyu.cloud.mcs.dto.manufacture;

import com.miyu.cloud.mcs.dto.productionProcess.McsStepResourceDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class McsBatchRecordEventDTO {

    //批次编码
    private String batchRecordId;
    //物料条码
    private String barCode;
    //生产单元(产线/工位)编码
    private String deviceUnitId;
    //操作者
    private String operatorId;
    //任务进度
    private Integer progress;
    //操作时间 默认当前
    private LocalDateTime operatingTime = LocalDateTime.now();
//工步事件 任务开工时 同时开工工步
    //工步id
    private String stepId;
    //资源使用
    private List<McsStepResourceDTO> resourceList;
    //数量
    private Integer totality;

    //终端ip
    private String ip;
}
