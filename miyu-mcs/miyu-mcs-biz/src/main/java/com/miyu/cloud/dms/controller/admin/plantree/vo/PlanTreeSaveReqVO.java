package com.miyu.cloud.dms.controller.admin.plantree.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 计划关联树新增/修改 Request VO")
@Data
public class PlanTreeSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "14340")
    private String id;

    @Schema(description = "父节点")
    private String parent;

    @Schema(description = "节点名", example = "李四")
    private String name;

    @Schema(description = "备注", example = "你说的对")
    private String remark;

    @Schema(description = "关联设备", example = "12631")
    private String deviceId;

    @Schema(description = "关联设备类型", example = "21029")
    private String deviceTypeId;

}