package com.miyu.cloud.dms.controller.admin.linestationgroup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - 产线/工位组新增/修改 Request VO")
@Data
public class LineStationGroupSaveReqVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "17888")
    private String id;

    @Schema(description = "编号")
    private String code;

    @Schema(description = "名称", example = "李四")
    private String name;

    @Schema(description = "产线/工位组", example = "2")
    private Integer type;

    @Schema(description = "是否启用")
    private Integer enable;

    @Schema(description = "所属类型", example = "1")
    private String affiliationDeviceType;

    @Schema(description = "本机ip", example = "1")
    private String ip;

    @Schema(description = "位置")
    private String location;

    @Schema(description = "备注", example = "你猜")
    private String remark;

}