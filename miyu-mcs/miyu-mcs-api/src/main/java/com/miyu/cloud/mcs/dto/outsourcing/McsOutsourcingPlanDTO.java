package com.miyu.cloud.mcs.dto.outsourcing;

import lombok.Data;

@Data
public class McsOutsourcingPlanDTO {

    //子计划id
    private String orderNumber;
    //工艺序号
    private String processNumber;
    //外协订单唯一码
    private String outsourcingId;
    //外协厂家
    private String aidMill;
    //条码集合(编码集合,逗号分割)
    private String MaterialCodeList;
}
