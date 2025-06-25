package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 技术评估选择资源 Response VO")
@Data
@ExcelIgnoreUnannotated
public class FeasibilityDetailRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "制造资源类型(1设备、2刀具、3工装)")
    private Integer resourcesType;

    @Schema(description = "制造工具id(设备、刀具、工装等)")
    private String resourcesTypeId;

    @Schema(description = "评估结果 0:通过 1:不通过")
    private Integer quantity;

    @Schema(description = "预估工时(min)")
    private Integer processingTime;
}
