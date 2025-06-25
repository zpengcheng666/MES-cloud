package com.miyu.module.pdm.controller.admin.part.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
@Schema(description = "管理后台 - pdm__part_instance Response VO")
@Data
@ExcelIgnoreUnannotated
public class PartInstanceRespVO {

        @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "23543")
        @ExcelProperty("主键")
        private String id;

        @Schema(description = "实例号")
        @ExcelProperty("实例号")
        private String name;

        @Schema(description = "产品根节点ID", example = "24352")
        @ExcelProperty("产品根节点ID")
        private String rootproductId;

        @Schema(description = "零部件版本ID", example = "32538")
        @ExcelProperty("零部件版本ID")
        private String partVersionId;

        @Schema(description = "父节点ID", example = "20632")
        @ExcelProperty("父节点ID")
        private String parentId;

        @Schema(description = "客户化标识")
        @ExcelProperty("客户化标识")
        private String customizedIndex;

        @Schema(description = "排序")
        @ExcelProperty("排序")
        private String serialNumber;

        @Schema(description = "位置坐标系，坐标原点X值")
        @ExcelProperty("位置坐标系，坐标原点X值")
        private String vmatrix01;

        @Schema(description = "位置坐标系，坐标原点Y值")
        @ExcelProperty("位置坐标系，坐标原点Y值")
        private String vmatrix02;

        @Schema(description = "位置坐标系，坐标原点Z值")
        @ExcelProperty("位置坐标系，坐标原点Z值")
        private String vmatrix03;

        @Schema(description = "位置坐标系，X轴方向向量X值")
        @ExcelProperty("位置坐标系，X轴方向向量X值")
        private String vmatrix04;

        @Schema(description = "位置坐标系，X轴方向向量Y值")
        @ExcelProperty("位置坐标系，X轴方向向量Y值")
        private String vmatrix05;

        @Schema(description = "位置坐标系，X轴方向向量Z值")
        @ExcelProperty("位置坐标系，X轴方向向量Z值")
        private String vmatrix06;

        @Schema(description = "位置坐标系，Y轴方向向量X值")
        @ExcelProperty("位置坐标系，Y轴方向向量X值")
        private String vmatrix07;

        @Schema(description = "位置坐标系，Y轴方向向量Y值")
        @ExcelProperty("位置坐标系，Y轴方向向量Y值")
        private String vmatrix08;

        @Schema(description = "位置坐标系，Y轴方向向量Z值")
        @ExcelProperty("位置坐标系，Y轴方向向量Z值")
        private String vmatrix09;

        @Schema(description = "位置坐标系，Z轴方向向量X值")
        @ExcelProperty("位置坐标系，Z轴方向向量X值")
        private String vmatrix10;

        @Schema(description = "位置坐标系，Z轴方向向量Y值")
        @ExcelProperty("位置坐标系，Z轴方向向量Y值")
        private String vmatrix11;

        @Schema(description = "位置坐标系，Z轴方向向量Z值")
        @ExcelProperty("位置坐标系，Z轴方向向量Z值")
        private String vmatrix12;

        @Schema(description = "节点类型，root产品根节点、comp部件、part零件", example = "1")
        @ExcelProperty("节点类型，root产品根节点、comp部件、part零件")
        private String type;

        @Schema(description = "对象id(节点类型对应部件时存部件id)", example = "14151")
        @ExcelProperty("对象id(节点类型对应部件时存部件id)")
        private String targetId;

    }

