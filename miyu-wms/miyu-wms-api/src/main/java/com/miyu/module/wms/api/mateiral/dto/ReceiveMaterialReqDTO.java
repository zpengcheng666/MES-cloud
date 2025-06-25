package com.miyu.module.wms.api.mateiral.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ReceiveMaterialReqDTO {

    @NotEmpty(message = "物料类型不能为空")
    private String materialConfigId;
    @NotNull(message = "物料状态（待质检，合格，不合格）")
    private Integer materialStatus;
    @NotEmpty(message = "收货库位不能为空")
    private String locationId;

    // 物料条码 非必填
    private String barCode;

}
