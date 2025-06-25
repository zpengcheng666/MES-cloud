package com.miyu.module.qms.controller.admin.unqualifiedmaterial.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

import com.alibaba.excel.annotation.*;

@Schema(description = "管理后台 - 不合格品产品 Response VO")
@Data
@ExcelIgnoreUnannotated
public class UnqualifiedMaterialRespVO {

    @Schema(description = "主键", requiredMode = Schema.RequiredMode.REQUIRED, example = "4912")
    @ExcelProperty("主键")
    private String id;

    @Schema(description = "创建时间", requiredMode = Schema.RequiredMode.REQUIRED)
    @ExcelProperty("创建时间")
    private LocalDateTime createTime;

    @Schema(description = "检验单方案任务ID", example = "4041")
    @ExcelProperty("检验单方案任务ID")
    private String inspectionSheetSchemeId;

    @Schema(description = "检验单产品ID", example = "26336")
    @ExcelProperty("检验单产品ID")
    private String schemeMaterialId;

    @Schema(description = "不合格品登记ID", example = "13208")
    @ExcelProperty("不合格品登记ID")
    private String unqualifiedRegistrationId;

    @Schema(description = "缺陷等级")
    @ExcelProperty("缺陷等级")
    private Integer defectiveLevel;

    @Schema(description = "缺陷描述")
    @ExcelProperty("缺陷描述")
    private String content;

    @Schema(description = "缺陷代码ID", example = "13324")
    private List<String> defectiveCode;

    private String defectiveCodesStr;

}