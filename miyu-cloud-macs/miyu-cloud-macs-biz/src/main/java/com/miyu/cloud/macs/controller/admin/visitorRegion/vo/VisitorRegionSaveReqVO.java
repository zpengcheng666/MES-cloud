package com.miyu.cloud.macs.controller.admin.visitorRegion.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 访客区域权限新增/修改 Request VO")
@Data
public class VisitorRegionSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "21735")
    private String id;

    @Schema(description = "访客id", example = "6699")
    private String visitorId;

    @Schema(description = "区域id", example = "6759")
    private String regionId;

    @Schema(description = "访客申请id", example = "6146")
    private String applicationId;

    @Schema(description = "生效日期")
    private LocalDateTime effectiveDate;

    @Schema(description = "失效日期")
    private LocalDateTime invalidDate;

}