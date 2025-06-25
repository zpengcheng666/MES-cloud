package com.miyu.module.ppm.controller.admin.dmcontract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 项目订单")
@Data
@ToString(callSuper = true)
public class ProjectOrderRespVO {

    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "32706")
    private String id;

    @Schema(description = "项目编码(本地关联时写入)")
    private String projectCode;

    @Schema(description = "项目名称(本地关联时写入)")
    private String projectName;

    @Schema(description = "项目id(本地关联时写入)", example = "13694")
    private String projectId;

    @Schema(description = "物料牌号	产品编号(产品ID(与工艺内产品版本ID对应))		这里写的是material。在产品里是partNumber，图号。在物料里是material。")
    private String materialNumber;

    @Schema(description = "图号")
    private String partNumber;

    @Schema(description = "工件名称")
    private String partName;

    @Schema(description = "数量")
    private Integer quantity;



    private String materialConfigId;

    /**
     * 物料单位
     */
    private String materialUnit;


    private String materialName;

    /***
     * 合同可签数量
     */
    private Integer canQuantity;


    private String companyId;


}
