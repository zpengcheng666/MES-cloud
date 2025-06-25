package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 设备采购意见 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DemandCutterRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "刀具长度")
    private String length;

    @Schema(description = "刃长")
    private String bladeLength;

    @Schema(description = "刃数")
    private String bladeNum;

    @Schema(description = "直径")
    private String diameter;

    @Schema(description = "R角")
    private String rrAngle;

    @Schema(description = "缩径")
    private String reducingDiameter;

    @Schema(description = "备注")
    private String description;

}
