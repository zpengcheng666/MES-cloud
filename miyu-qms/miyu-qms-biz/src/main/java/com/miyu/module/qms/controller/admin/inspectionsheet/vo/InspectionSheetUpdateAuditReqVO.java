package com.miyu.module.qms.controller.admin.inspectionsheet.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;


@Schema(description = "管理后台 - 产品检验 Request VO")
@Data
public class InspectionSheetUpdateAuditReqVO {

    @Schema(description = "检测单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "检测单ID不能为空")
    private String sheetId;

    @Schema(description = "检测任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "检测任务ID不能为空")
    private String sheetSchemeId;

    @Schema(description = "检验单产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "检验单产品ID不能为空")
    private String sheetSchemeMaterialId;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "产品ID不能为空")
    private String materialId;

    @Schema(description = "物料条码", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "物料条码不能为空")
    private String barCode;

    @Schema(description = "检验结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotNull(message = "检验结果不能为空")
    private Integer inspectionResult;

    @Schema(description = "测量结果", requiredMode = Schema.RequiredMode.REQUIRED, example = "1234")
    @NotEmpty(message = "测量结果不能为空")
    private String content;

    @Schema(description = "产品检验状态", example = "1234")
    private Integer status;

    @Schema(description = "检验记录集合")
    @Valid
    @NotEmpty(message = "检测记录不能为空")
    private List<Record> records;

    @Schema(description = "检验记录")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Record {

        @Schema(description = "主键")
        @NotNull(message = "检验记录ID不能为空")
        private String id;

        @Schema(description = "测量结果")
        @NotNull(message = "测量结果不能为空")
        private String content;

        @Schema(description = "判断")
        @NotNull(message = "测量结果不能为空")
        private Integer inspectionResult;

    }

    @Schema(description = "不合格品登记集合")
    @Valid
    @NotEmpty(message = "不合格品登记不能为空")
    private List<Registration> registrations;

    @Schema(description = "不合格品登记")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Registration {

        @Schema(description = "主键")
        private String id;

        @Schema(description = "缺陷代码", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "缺陷代码不能为空")
        private List<String> defectiveCode;

        @Schema(description = "缺陷数量", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "缺陷数量不能为空")
        private Integer quantity;

        @Schema(description = "缺陷描述", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "缺陷描述不能为空")
        private String content;

        @Schema(description = "检验单方案任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13324")
        @NotEmpty(message = "检验单方案任务ID不能为空")
        private String inspectionSheetSchemeId;

        @Schema(description = "不合格品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13324")
        @NotEmpty(message = "不合格品ID不能为空")
        private String materialId;

    }
}
