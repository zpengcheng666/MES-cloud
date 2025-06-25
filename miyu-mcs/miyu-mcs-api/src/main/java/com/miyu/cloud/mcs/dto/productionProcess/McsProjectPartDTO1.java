package com.miyu.cloud.mcs.dto.productionProcess;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsProjectPartDTO1 {

    private String id;
    private String orderId;
    /**
     * 详情编码
     */
    private String detailNumber;
    /**
     * 物料条码
     */
    private String barCode;
    /**
     * 当前生产单元
     */
    private String processingUnitName;
    /**
     * 状态 DictConstants MCS_ORDER_STATUS_*
     */
    private Integer status;
    /**
     * 开始时间
     */
    private LocalDateTime startTime;
    /**
     * 结束时间
     */
    private LocalDateTime endTime;

}
