package com.miyu.cloud.mcs.dto.manufacture;

import com.miyu.cloud.mcs.dto.productionProcess.McsPlanProcessDTO;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class McsOrderFormDTO {

    //订单_工序 编码
    private String processOrderNumber;
    //工序序号
    private String processNumber;
    //物料条码集合
    private List<String> barCodeList;
    //工装条码集合
    private List<String> toolCodeList;
    //工序任务
    private List<McsProcessRecordDTO> recordList;
    //工序
    private McsPlanProcessDTO processDTO;

    /**
     * 优先级
     */
    private Integer priority;
    /**
     * 接收时间
     */
    private LocalDateTime receptionTime;
    /**
     * 交付时间
     */
    private LocalDateTime deliveryTime;
}
