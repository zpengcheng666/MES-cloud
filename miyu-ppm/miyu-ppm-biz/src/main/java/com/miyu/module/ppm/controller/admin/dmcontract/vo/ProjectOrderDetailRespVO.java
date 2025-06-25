package com.miyu.module.ppm.controller.admin.dmcontract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.ToString;

@Schema(description = "管理后台 - 项目订单")
@Data
@ToString(callSuper = true)
public class ProjectOrderDetailRespVO {

    private String id;



    @Schema(description = "订单id(本地关联时写入)", example = "31536")
    private String orderId;

    @Schema(description = "物料编码，牌号，毛坯(本地关联时写入)")
    private String materialCode;

    @Schema(description = "变码，工序加工后，产生的新码")
    private String variableCode;

    @Schema(description = "图号(成品工件,不一定是图号，成品码可能是别的码)")
    private String productCode;

    @Schema(description = "项目id", example = "29810")
    private String projectId;

    @Schema(description = "计划id", example = "24514")
    private String planId;

    @Schema(description = "子计划id", example = "647")
    private String planItemId;

    @Schema(description = "物料状态", example = "647")
    private Integer materialStatus;

    @Schema(description = "计划类型", example = "647")
    private Integer planType;

    @Schema(description = "项目id", example = "29810")
    private String projectName;

    @Schema(description = "项目id", example = "29810")
    private String projectCode;


    /**
     * 物料库存ID
     */
    private String materialStockId;
    private String materialConfigId;
    /**
     * 物料批次号
     */
    private String batchNumber;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料类码")
    private String materialConfigCode;

    @Schema(description = "物料名称", example = "张三")
    private String materialName;

    @Schema(description = "物料属性（成品、毛坯、辅助材料）",requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer materialProperty;


    @Schema(description = "当前类名称")
    private String materialTypeName;
    /**
     * 主类别（工件、托盘、工装、夹具、刀具）
     */
    @Schema(description = "主类别名")
    private String materialParentTypeName;

    @Schema(description = "物料类型（零件、托盘、工装、夹具、刀具）",requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private String materialTypeId;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    private Integer quantity;
}
