package com.miyu.module.pdm.controller.admin.dataStatistics.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 数据包产品目录 Response VO")
@Data
@ExcelIgnoreUnannotated
public class BomRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "零件图号")
    private String partNumber;

    @Schema(description = "零件名称")
    private String partName;

    @Schema(description = "零件版次")
    private String partVersion;

    @Schema(description = "产品ID")
    private String rootProductId;

    @Schema(description = "客户化标识")
    private String customizedIndex;
}
