package com.miyu.cloud.mcs.dto.manufacture;

import lombok.Data;

@Data
public class McsSplitProcessingProcedure {

    //任务id
    private String batchRecordId;
    //工位id
    private String workstationId;
    //原物料条码
    private String barCode;
}
