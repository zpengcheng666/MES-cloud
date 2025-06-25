package com.miyu.module.wms.api.mateiral.dto;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class MaterialQualityCheckStatus {

    @NotEmpty(message = "物料条码")
    private String barCode;
    @NotNull(message = "物料状态（待质检，合格，不合格）")
    private Integer materialStatus;
}
