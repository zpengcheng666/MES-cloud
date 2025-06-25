package com.miyu.module.pdm.api.processPlanDetail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 工步制造资源信息")
@Data
public class StepDetailRespDTO {

    @Schema(description = "制造资源类型：1设备 2刀具 3工装")
    private Integer resourcesType;

    @Schema(description = "制造资源id(设备、刀具、工装等)")
    private String resourcesTypeId;

    @Schema(description = "设备编号")
    private String code;

    @Schema(description = "设备名称")
    private String name;

    @Schema(description = "设备规格")
    private String specification;

    @Schema(description = "刀组编码")
    private String cutterGroupCode;

    @Schema(description = "刀柄类型")
    private String taperTypeName;

    @Schema(description = "刀柄通用规格")
    private String hiltMark;

    @Schema(description = "刀简号")
    private String cutternum;

}
