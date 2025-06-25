package com.miyu.module.wms.controller.admin.warehouselocation.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 库位 Response VO")
@Data
@ExcelIgnoreUnannotated
public class WarehouseLocationSimpleRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "库位编码")
    @ExcelProperty("库位编码")
    private String locationCode;

    @Schema(description = "库位名称")
    @ExcelProperty("库位名称")
    private String locationName;
}

