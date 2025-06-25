package com.miyu.module.tms.controller.admin.toolgroup.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.Valid;
import javax.validation.constraints.*;

@Schema(description = "管理后台 - 刀具组装新增/修改 Request VO")
@Data
public class ToolGroupSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "31586")
    private String id;

    @Schema(description = "成品刀具类型id", requiredMode = Schema.RequiredMode.REQUIRED, example = "25222")
    @NotEmpty(message = "成品刀具类型不能为空")
    private String mainConfigId;

    @Schema(description = "物料编码", requiredMode = Schema.RequiredMode.REQUIRED, example = "25222")
    @NotEmpty(message = "物料编码不能为空")
    private String materialNumber;

    @Schema(description = "刀柄")
    @NotEmpty(message = "刀柄不能为空")
    private List<Handle> handle;

    @Schema(description = "刀具")
    @Valid
    @NotEmpty(message = "刀具不能为空")
    private List<Tool> tool;

    @Schema(description = "配件")
    @Valid
    private List<Accessories> accessories;

    @Schema(description = "刀柄列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Handle {

        @Schema(description = "成品刀具主键")
        private String mainConfigId;

        @Schema(description = "刀位")
        private Integer site;

        @Schema(description = "装配刀具类型id")
        @NotNull(message = "装配刀具类型不能为空")
        private String accessoryConfigId;

        @Schema(description = "数量")
        private Integer count;

        @Schema(description = "类码")
        private String materialCode;
    }

    @Schema(description = "刀具列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Tool {

        @Schema(description = "成品刀具主键")
        private String mainConfigId;

        @Schema(description = "刀位")
        @NotNull(message = "刀位不能为空")
        private Integer site;

        @Schema(description = "装配刀具类型id")
        @NotNull(message = "装配刀具类型不能为空")
        private String accessoryConfigId;

        @Schema(description = "数量")
        private Integer count;

        @Schema(description = "类码")
        private String materialCode;
    }

    @Schema(description = "配件列表")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Accessories {

        @Schema(description = "成品刀具主键")
        private String mainConfigId;

        @Schema(description = "刀位")
        private Integer site;

        @Schema(description = "装配刀具类型id")
        @NotNull(message = "装配刀具类型不能为空")
        private String accessoryConfigId;

        @Schema(description = "数量")
        @NotNull(message = "数量不能为空")
        private Integer count;

        @Schema(description = "类码")
        private String materialCode;
    }
}
