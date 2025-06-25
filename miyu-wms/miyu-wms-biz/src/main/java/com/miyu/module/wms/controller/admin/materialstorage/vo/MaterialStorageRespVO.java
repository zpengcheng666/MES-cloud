package com.miyu.module.wms.controller.admin.materialstorage.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料储位 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialStorageRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "储位编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("储位编码")
    private String storageCode;

    @Schema(description = "储位名称")
    @ExcelProperty("储位名称")
    private String storageName;

    @Schema(description = "物料id", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料id")
    private String materialStockId;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("物料条码")
    private String barCode;

    @ExcelProperty("是否有效")
    @Schema(description = "是否有效")
    private String valid;

    @Schema(description = "层")
    private Integer layer;

    @Schema(description = "排")
    private Integer row;

    @Schema(description = "列")
    private Integer col;

}