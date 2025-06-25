package com.miyu.module.qms.controller.admin.unqualifiedregistration.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;
import java.math.BigDecimal;

@Schema(description = "管理后台 - 不合格品登记新增/修改 Request VO")
@Data
public class UnqualifiedRegistrationSaveReqVO {

    @Schema(description = "主键", example = "30498")
    private String id;

    @Schema(description = "缺陷代码")
    private String defectiveCode;

    @Schema(description = "检验单方案任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13324")
    @NotEmpty(message = "检验单方案任务ID不能为空")
    private String inspectionSheetSchemeId;

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

        @Schema(description = "缺陷等级", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "缺陷等级不能为空")
        private Integer defectiveLevel;

        @Schema(description = "缺陷描述", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotEmpty(message = "缺陷描述不能为空")
        private String content;

        @Schema(description = "检验单方案任务ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13324")
        @NotEmpty(message = "检验单方案任务ID不能为空")
        private String inspectionSheetSchemeId;

        @Schema(description = "不合格品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "13324")
        @NotEmpty(message = "不合格品ID不能为空")
        private String schemeMaterialId;

    }
}