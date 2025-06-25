package com.miyu.module.pdm.controller.admin.toolingApply.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "PDM - 工装申请 Request VO")
@Data
public class ToolingDetailReqVO{

    public String getRootProductId;
    @Schema(description = "id", example = "1")
    private String id;

@Schema(description = "piid", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String piid;

@Schema(description = "实例号", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String instanceNumber;

@Schema(description = "产品根节点ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String rootProductId;

@Schema(description = "零部件版本ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String partVersionId;

@Schema(description = "父节点ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String parentPiid;

@Schema(description = "客户化标识", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String customizedIndex;

@Schema(description = "排序", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String serialNumber;

@Schema(description = "位置坐标系，坐标原点X值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix01;

@Schema(description = "位置坐标系，坐标原点Y值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix02;

@Schema(description = "位置坐标系，坐标原点Z值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix03;

@Schema(description = "位置坐标系，X轴方向向量X值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix04;

@Schema(description = "位置坐标系，X轴方向向量Y值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix05;

@Schema(description = "位置坐标系，X轴方向向量Z值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix06;

@Schema(description = "位置坐标系，Y轴方向向量X值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix07;

@Schema(description = "位置坐标系，Y轴方向向量Y值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix08;

@Schema(description = "位置坐标系，Y轴方向向量Z值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix09;

@Schema(description = "位置坐标系，Z轴方向向量X值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix10;

@Schema(description = "位置坐标系，Z轴方向向量Y值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix11;

@Schema(description = "位置坐标系，Z轴方向向量Z值", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String vmatrix12;

@Schema(description = "type", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String type;

@Schema(description = "cpartNumber", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String partNumber;

@Schema(description = "partName", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String partName;

@Schema(description = "partVersion", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String partVersion;

@Schema(description = "documentVersionId", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String documentVersionId;

@Schema(description = "链接", requiredMode = Schema.RequiredMode.REQUIRED, example = "6902")
private String  vaultUrl;
}
