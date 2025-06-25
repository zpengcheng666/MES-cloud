package com.miyu.cloud.mcs.controller.admin.batchdemandrecord.vo;

import lombok.Data;

@Data
public class BatchDemandResourceReqVO {

    // 物料id
    private String id;
    // 物料条码
    private String barCode;
    // 物料批次号
    private String batchNumber;
    // 管理方式(1单件,2批量) 默认为1单件
    private Integer materialManage;
    // 数量
    private Integer total;
    // 需求详情id
    private String demandRecordId;
    //配送需求
    private Boolean deliveryRequired;
}
