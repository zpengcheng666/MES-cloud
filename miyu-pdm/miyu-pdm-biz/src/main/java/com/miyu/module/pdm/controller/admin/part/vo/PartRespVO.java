package com.miyu.module.pdm.controller.admin.part.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 产品数据对象 Response VO")
@Data
@ExcelIgnoreUnannotated
public class PartRespVO {
    @Schema(description = "id", requiredMode = Schema.RequiredMode.REQUIRED, example = "12076")
    @ExcelProperty("id")
    private String id;

    @TableField(exist = false)
    @Schema(description = "产品表ID", example = "14917")
    @ExcelProperty("产品表ID")
    private String rootproductId;

    @Schema(description = "标准数据对象")
    @ExcelProperty("标准数据对象")
    private String stdDataObject;

    @Schema(description = "客户化数据对象")
    @ExcelProperty("客户化数据对象")
    private String customizedDataObject;

    @Schema(description = "客户化标识")
    @ExcelProperty("客户化标识")
    private String customizedIndex;

    @Schema(description = "客户化类型:0固有 1客户化 ", example = "2")
    @ExcelProperty("客户化类型:0固有 1客户化 ")
    private Integer customizedType;

    @Schema(description = "数据表表名", example = "王五")
    @ExcelProperty("数据表表名")
    private String tableName;

    @Schema(description = "客户化数据对象说明", example = "你说的对")
    @ExcelProperty("客户化数据对象说明")
    private String description;

    @Schema(description = "数据表实例化状态(1已实例化，不可更改；0未实例化)", example = "2")
    @ExcelProperty("数据表实例化状态(1已实例化，不可更改；0未实例化)")
    private String status;

    @Schema(description = "属性内容json(固有属性)")
    @ExcelProperty("属性内容json(固有属性)")
    private String intrinsicAttrs;

    @Schema(description = "属性内容json(客户化属性)")
    @ExcelProperty("属性内容json(客户化属性)")
    private String customizedAttrs;

    @Schema(description = "排序")
    @ExcelProperty("排序")
    private Integer serialNumber;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime upateTime;

    @TableField(exist = false)
    @Schema(description = "产品编号")
    @ExcelProperty("产品编号")
    private String productNumber;

}


