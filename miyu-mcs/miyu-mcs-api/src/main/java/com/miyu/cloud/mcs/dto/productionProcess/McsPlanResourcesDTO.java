package com.miyu.cloud.mcs.dto.productionProcess;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 资源
 */
@Schema(description = "RPC 服务 - 工序制造资源信息")
@Data
public class McsPlanResourcesDTO {

    @Schema(description = "制造资源类型：1设备 2刀具 3工装")
    private Integer resourcesType;

    @Schema(description = "制造资源id(设备、刀具、工装等)")
    private String resourcesTypeId;

    //设备信息
    @Schema(description = "设备编号")
    private String code;

    @Schema(description = "设备名称")
    private String name;

    @Schema(description = "设备规格")
    private String specification;

    //工装信息
    @Schema(description = "工装编号")
    private String materialNumber;

    @Schema(description = "工装名称")
    private String materialName;

    @Schema(description = "工装规格")
    private String materialSpecification;

    //刀具信息
    @Schema(description = "刀简号")
    private String cutternum;

    @Schema(description = "刀组编码")
    private String cutterGroupCode;

    @Schema(description = "刀柄类型")
    private String taperTypeName;

    @Schema(description = "刀柄通用规格")
    private String hiltMark;

}
