package com.miyu.cloud.mcs.dto.manufacture;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

/**
 * 工位事件
 */
@Data
public class McsWorkstationDTO {

    //批次零件id
    private String batchRecordId;
    //设备id
    private String deviceId;

    //操作类型  //3暂停 4恢复
    private Integer operationType;

    //操作者
    private String operatorId;

    //操作时间
    private LocalDateTime time;
}
