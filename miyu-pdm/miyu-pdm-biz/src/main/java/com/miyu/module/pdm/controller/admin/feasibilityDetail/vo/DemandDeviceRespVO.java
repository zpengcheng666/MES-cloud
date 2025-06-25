package com.miyu.module.pdm.controller.admin.feasibilityDetail.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 设备采购意见 Response VO")
@Data
@ExcelIgnoreUnannotated
public class DemandDeviceRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
    private String id;

    @Schema(description = "项目编号")
    private String projectCode;

    @Schema(description = "零部件版本ID")
    private String partVersionId;

    @Schema(description = "设备编号")
    private String deviceCode;

    @Schema(description = "设备名称")
    private String deviceName;

    @Schema(description = "设备规格")
    private String deviceSpecification;

    @Schema(description = "备注")
    private String description;

}
