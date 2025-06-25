package com.miyu.module.pdm.controller.admin.part.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "管理后台 - pdm__part_instance列表 Request VO")
@Data
public class PartInstanceListReqVO {

    @Schema(description = "实例号")
    private String name;

    @Schema(description = "产品根节点ID", example = "24352")
    private String rootproductId;

    @Schema(description = "零部件版本ID", example = "32538")
    private String partVersionId;

    @Schema(description = "父节点ID", example = "20632")
    private String parentId;

    @Schema(description = "客户化标识")
    private String customizedIndex;

    @Schema(description = "排序")
    private String serialNumber;

    @Schema(description = "位置坐标系，坐标原点X值")
    private String vmatrix01;

    @Schema(description = "位置坐标系，坐标原点Y值")
    private String vmatrix02;

    @Schema(description = "位置坐标系，坐标原点Z值")
    private String vmatrix03;

    @Schema(description = "位置坐标系，X轴方向向量X值")
    private String vmatrix04;

    @Schema(description = "位置坐标系，X轴方向向量Y值")
    private String vmatrix05;

    @Schema(description = "位置坐标系，X轴方向向量Z值")
    private String vmatrix06;

    @Schema(description = "位置坐标系，Y轴方向向量X值")
    private String vmatrix07;

    @Schema(description = "位置坐标系，Y轴方向向量Y值")
    private String vmatrix08;

    @Schema(description = "位置坐标系，Y轴方向向量Z值")
    private String vmatrix09;

    @Schema(description = "位置坐标系，Z轴方向向量X值")
    private String vmatrix10;

    @Schema(description = "位置坐标系，Z轴方向向量Y值")
    private String vmatrix11;

    @Schema(description = "位置坐标系，Z轴方向向量Z值")
    private String vmatrix12;

    @Schema(description = "节点类型，root产品根节点、comp部件、part零件", example = "1")
    private String type;

    @Schema(description = "对象id(节点类型对应部件时存部件id)", example = "14151")
    private String targetId;

}