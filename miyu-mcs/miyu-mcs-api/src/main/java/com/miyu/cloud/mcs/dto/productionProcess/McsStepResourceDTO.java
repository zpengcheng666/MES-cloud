package com.miyu.cloud.mcs.dto.productionProcess;

import lombok.Data;

/**
 * 工步资源使用
 */
@Data
public class McsStepResourceDTO {

    //资源类型 //3工装 4夹具 5刀具 6加工程序
    private Integer materialType;
    //资源条码
    private String barCode;
    //物料批次
    private String batchNumber;
    //资源类型id
    private String materialConfigId;
    //资源类型编码
    private String materialNumber;
    //数量
    private Integer totality;
    //程序名称
    private String ncName;
    //程序顺序
    private Integer index;
    //程序版本
    private String version;
}
