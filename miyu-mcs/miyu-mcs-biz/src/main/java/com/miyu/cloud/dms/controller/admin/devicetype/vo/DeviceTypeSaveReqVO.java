package com.miyu.cloud.dms.controller.admin.devicetype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

@Schema(description = "管理后台 - 设备/工位类型新增/修改 Request VO")
@Data
public class DeviceTypeSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15312")
    private String id;

    @Schema(description = "类型编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "HSG-9387")
    @NotEmpty(message = "类型编号不能为空")
    private String code;

    @Schema(description = "类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "4号控制器")
    @NotEmpty(message = "类型名称不能为空")
    private String name;

    @Schema(description = "设备/工位")
    private Integer type;

    @Schema(description = "是否启用")
    private Integer enable;

    @Schema(description = "规格型号")
    private String specification;

    @Schema(description = "生产厂家")
    private String manufacturer;

    @Schema(description = "产地")
    private String countryRegion;

    @Schema(description = "厂家联系人")
    private String contacts;

    @Schema(description = "厂家联系电话")
    private String contactPhone;

    @Schema(description = "备注")
    private String remark;
}