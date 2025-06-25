package com.miyu.module.qms.controller.admin.inspectionsheetrecord.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 检验记录 Request VO")
@Data
@ToString(callSuper = true)
public class InspectionSheetRecordReqVO {

    @Schema(description = "检测项目ID", example = "21030")
    private String inspectionSchemeId;

    @Schema(description = "检验单物料表ID", example = "18307")
    private String schemeMaterialId;

    @Schema(description = "物料条码", example = "1234")
    private String barCode;

    @Schema(description = "设备ID", example = "1234")
    private String deviceId;
}
