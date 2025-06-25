package com.miyu.module.es.controller.admin.brakeSync.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "车闸管理 - 配置管理")
@Data
public class BrakeSyncSaveReqVO {

    /**
     *
     */
    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "16009")
    private String id;

    /**
     *自动同步(1.开启 2.关闭)
     */
    @Schema(description = "自动同步")
    private Integer automatic;

    /**
     *周期(分钟)
     */
    @Schema(description = "周期")
    private String cycle;

    /**
     * 厂区同步方式(1.不互通 2.仅新厂同步旧厂 3.仅旧厂同步新厂 4.互通）
     */
    @Schema(description = "厂区同步方式")
    private Integer sync;
}
