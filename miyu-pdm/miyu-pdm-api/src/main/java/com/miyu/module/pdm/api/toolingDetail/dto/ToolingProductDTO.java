package com.miyu.module.pdm.api.toolingDetail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "rpc - 工装列表 Response DTO")
@Data
public class ToolingProductDTO {

    @Schema(description = "工装申请id对应产品表id")
    private String id;

    @Schema(description = "工装编号")
    private String toolingCode;

    @Schema(description = "工装图号")
    private String toolingNumber;

    @Schema(description = "工装名称")
    private String toolingName;

    @Schema(description = "设计员id")
    private String reviewedBy;

    @Schema(description = "设计员姓名")
    private String reviewer;

    @Schema(description = "状态")
    private String status;
}
