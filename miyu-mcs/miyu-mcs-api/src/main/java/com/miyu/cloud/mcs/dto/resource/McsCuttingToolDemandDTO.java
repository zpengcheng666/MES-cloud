package com.miyu.cloud.mcs.dto.resource;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsCuttingToolDemandDTO {

    //配送订单编码
    private String orderNumber;
    //需求数量
    private Integer needCount;
    //目标位置
    private String targetLocation;
    //目标位置编码
    private String targetLocationCode;
    //配送截止时间
    private LocalDateTime distributionDeadline;
    //需求时长
    private Integer minimumTime;
    //类型id
    private String materialConfigId;
    //类型编码
    private String materialNumber;
    //类型名称
    private String materialName;
}
