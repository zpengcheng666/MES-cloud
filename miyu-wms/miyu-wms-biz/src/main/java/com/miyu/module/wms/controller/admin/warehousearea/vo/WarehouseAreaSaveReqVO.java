package com.miyu.module.wms.controller.admin.warehousearea.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Schema(description = "管理后台 - 库区新增/修改 Request VO")
@Data
public class WarehouseAreaSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4729")
    private String id;

    @Schema(description = "仓库id")
    private String warehouseId;

    @Schema(description = "库区名称")
    private String areaName;

    @Schema(description = "库区编码")
    private String areaCode;

    @Schema(description = "库区属性")
    private Integer areaProperty;

    @Schema(description = "库区长")
    private Integer areaLength;

    @Schema(description = "库区宽")
    private Integer areaWidth;

    @Schema(description = "库区高")
    private Integer areaHeight;

    @Schema(description = "库区承重")
    private Integer areaBearing;

    @Schema(description = "通道")
    private Integer areaChannels;

    @Schema(description = "组")
    private Integer areaGroup;

    @Schema(description = "层")
    private Integer areaLayer;

    @Schema(description = "位")
    private Integer areaSite;

    @Schema(description = "库区类型", example = "2")
    private Integer areaType;

}