package com.miyu.cloud.dms.api.devicetype.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "RPC 服务 - 设备类型 Response DTO")
@Data
public class DeviceTypeDataRespDTO {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "15312")
    private String id;

    @Schema(description = "类型编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "HSG-9387")
    private String code;

    @Schema(description = "类型名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "4号控制器")
    private String name;

    @Schema(description = "是否启用")
    private Integer enable;

    //0 设备, 1 工位, 2 产线
    @Schema(description = "类型")
    private Integer type;

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
