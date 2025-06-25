package com.miyu.cloud.mcs.dto.distribution;

import lombok.Data;

@Data
public class McsDistributionLocationDTO {

    //物料id
    private String materialId;
    //起始仓库
    private String startWarehouseId;
    //起始库位
    private String startLocation;
    //目标仓库
    private String targetWarehouseId;
    //目标库位
    private String targetLocation;
}
