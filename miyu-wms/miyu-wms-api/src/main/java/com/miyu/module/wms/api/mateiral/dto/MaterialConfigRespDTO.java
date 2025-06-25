package com.miyu.module.wms.api.mateiral.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 获取物料")
@Data
public class MaterialConfigRespDTO {

    @Schema(description = "物料类型 ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1024")
    private String id;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialProperty;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialType;

    @Schema(description = "物料管理模式")
    private Integer materialManage;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

}
