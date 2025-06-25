package com.miyu.cloud.macs.controller.admin.door.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 门新增/修改 Request VO")
@Data
public class DoorSaveReqVO {

    @Schema(description = "主键id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12669")
    private String id;

    @Schema(description = "名称", example = "李四")
    private String name;

    @Schema(description = "编码")
    private String code;

    @Schema(description = "关联区域id", example = "31210")
    private String regionId;

    @Schema(description = "关联设备id", example = "31452")
    private String deviceId;

    @Schema(description = "位置", example = "赵六")
    private String locationName;

    @Schema(description = "门禁状态(0关闭,1打开,2故障)", example = "2")
    private Integer doorStatus;

    @Schema(description = "描述/备注", example = "随便")
    private String description;

    @Schema(description = "关联设备位置")
    private Integer devicePort;

}