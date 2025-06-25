package com.miyu.module.ppm.controller.admin.material.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 物料基本信息 Response VO")
@Data
@ExcelIgnoreUnannotated
public class MaterialRespVO {

    @Schema(description = "名臣", requiredMode = Schema.RequiredMode.REQUIRED, example = "7279")
    @ExcelProperty("名臣")
    private String id;

    @Schema(description = "类型", example = "2")
    @ExcelProperty("类型")
    private Integer type;

    @Schema(description = "名称", example = "赵六")
    @ExcelProperty("名称")
    private String name;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "创建IP")
    @ExcelProperty("创建IP")
    private String creationIp;

    @Schema(description = "更新ip")
    @ExcelProperty("更新ip")
    private String updatedIp;

}