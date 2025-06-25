package com.miyu.module.pdm.controller.admin.toolingCategorys.vo;

import com.alibaba.excel.annotation.ExcelIgnoreUnannotated;
import com.alibaba.excel.annotation.ExcelProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "管理后台 - 产品分类信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class ToolingCategoryRespVO {

    @Schema(description = "分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "738")
    @ExcelProperty("分类编号")
    private Long id;

    @Schema(description = "父分类编号", requiredMode = Schema.RequiredMode.REQUIRED, example = "17072")
    @ExcelProperty("父分类编号")
    private Long parentId;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "王五")
    @ExcelProperty("分类名称")
    private String name;

    @Schema(description = "分类编码", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("分类编码")
    private String code;

    @Schema(description = "分类排序", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("分类排序")
    private Integer sort;

    @Schema(description = "开启状态（0正常 1停用）", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @ExcelProperty("开启状态（0正常 1停用）")
    private Integer status;

    @Schema(description = "创建时间")
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

}