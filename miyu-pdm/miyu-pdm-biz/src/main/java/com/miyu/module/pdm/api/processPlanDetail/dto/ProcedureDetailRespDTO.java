package com.miyu.module.pdm.api.processPlanDetail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 工序制造资源信息")
@Data
public class ProcedureDetailRespDTO {

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

    @Schema(description = "工装编号")
    private String materialNumber;

    @Schema(description = "工装名称")
    private String materialName;

    @Schema(description = "工装规格")
    private String materialSpecification;

}
