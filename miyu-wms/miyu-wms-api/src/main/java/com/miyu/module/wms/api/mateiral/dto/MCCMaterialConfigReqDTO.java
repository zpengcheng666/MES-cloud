package com.miyu.module.wms.api.mateiral.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "RPC 服务 - 创建物料")
@Data
public class MCCMaterialConfigReqDTO {

    private String id;

    @Schema(description = "物料编号")
    private String materialNumber;

    @Schema(description = "物料名称", example = "王五")
    private String materialName;

    @Schema(description = "当前类别", example = "8228")
    private String materialTypeId;

    @Schema(description = "主类别（工件、托盘、工装、夹具、刀具）", example = "22245")
    private String materialParentTypeId;

    @Schema(description = "物料规格")
    private String materialSpecification;

    @Schema(description = "物料品牌")
    private String materialBrand;

    @Schema(description = "物料单位")
    private String materialUnit;

    @Schema(description = "来源物料类型", example = "31201")
    private String materialSourceId;

    @Schema(description = "类码")
    private String materialTypeCode;
    @Schema(description = "主类别类码")
    private String materialParentTypeCode;
    @Schema(description = "主类别类码名称")
    private String materialParentTypeName;

    @Schema(description = "物料管理模式")
    private Integer materialManage;
    /**
     * 审批状态
     */
    private Integer status;
    /**
     * 工作流编号
     *
     * 关联 ProcessInstance 的 id 属性
     */
    private String processInstanceId;
}
