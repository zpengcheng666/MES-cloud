package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 技术评估项目零件目录 Request VO")
@Data
@ToString(callSuper = true)
public class ProjPartBomReqVO {

    @Schema(description = "项目编号", example = "20041")
    private String projectCode;

    @Schema(description = "零件图号", example = "A220")
    private String partNumber;

    @Schema(description = "展示状态，参见 CommonStatusEnum 枚举类", example = "1")
    private Integer status;

    @Schema(description = "只查看自己", example = "true")
    private Boolean viewSelf;

    @Schema(description = "零部件版本id", example = "11")
    private String partVersionId;

    @Schema(description = "任务id", example = "11")
    private String taskId;

    @Schema(description = "项目状态(0进行中 1已关闭)", example = "0")
    private Integer projectStatus;
}
