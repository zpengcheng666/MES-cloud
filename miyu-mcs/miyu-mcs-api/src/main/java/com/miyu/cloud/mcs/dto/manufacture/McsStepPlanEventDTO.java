package com.miyu.cloud.mcs.dto.manufacture;

import com.miyu.cloud.mcs.dto.productionProcess.McsStepResourceDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 工步任务事件
 */

@Data
public class McsStepPlanEventDTO {

    //批次工序任务id
    private String batchRecordId;
    //物料条码
    private String barCode;
    //设备id(不包含产线)
    private String deviceId;
    //任务进度
    private Integer progress;
    //操作类型  //1开工 2完工
    private Integer operationType;
    //资源使用
    private List<McsStepResourceDTO> resourceList;
    //操作者
    private String operatorId;
    private LocalDateTime operatingTime = LocalDateTime.now();

    //工步id
    private String stepId;
    //数量
    private Integer totality;
}
