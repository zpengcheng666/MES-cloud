package com.miyu.module.pdm.controller.admin.part.vo;

import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 新增零件 Request VO")
@Data
@ToString(callSuper = true)
public class AddPartRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "客户化标识")
    private String customizedIndex;

    @Schema(description = "产品根节点ID", example = "24352")
    private String rootProductId;

    @Schema(description = "数据表表名", example = "std")
    @ExcelProperty("数据表表名")
    private String tableName;

    @Schema(description = "版本", example = "-A")
    private String partVersion;

    @Schema(description = "零件名称", example = "24352")
    private String partName;

    @Schema(description = "零件图号", example = "24352")
    private String partNumber;

    @Schema(description = "加工状态", example = "24352")
    private String processCondition;

    @Schema(description = "厂家名称", example = "24352")
    private String companyName;

    @Schema(description = "厂家id", example = "24352")
    private String companyId;

    @Schema(description = "来源(0解析 1新增)", example = "24352")
    private Integer source;
}
