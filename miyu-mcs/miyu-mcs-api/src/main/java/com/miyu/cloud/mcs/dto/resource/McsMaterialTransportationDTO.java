package com.miyu.cloud.mcs.dto.resource;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class McsMaterialTransportationDTO {

    //物料条码
    private String barCode;
    //物料id
    private String materialId;

    //生产单元(产线/工位)编码
    private String unitNumber;
    //操作者
    private String operatorId;
    //操作时间
    private LocalDateTime time;
}
