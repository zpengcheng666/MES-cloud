package com.miyu.module.mcc.controller.admin.materialtype.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 编码类别属性表(树形结构) Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialTypeRespVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "32256")
    @ExcelProperty("ID")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "编码")
    @ExcelProperty("编码")
    private String code;

    @Schema(description = "名称", example = "王五")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "父类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "24179")
    @ExcelProperty("父类型id")
    private String parentId;

    @Schema(description = "位数", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("位数")
    private Integer bitNumber;

    /***
     * 层级
     */
    @Schema(description = "层级")
    @ExcelProperty("层级")
    private Integer level;
    /**
     * 总层级
     */
    @Schema(description = "限制层级")
    @ExcelProperty("限制层级")
    private Integer levelLimit;




    @Schema(description = "分类", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer encodingProperty;

}