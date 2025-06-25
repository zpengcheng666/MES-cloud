package com.miyu.module.ppm.controller.admin.inboundexceptionhandling.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import java.util.*;
import javax.validation.constraints.*;
import java.math.BigDecimal;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Schema(description = "管理后台 - 入库异常处理新增/修改 Request VO")
@Data
public class InboundExceptionHandlingSaveReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "29034")
    private String id;

    @Schema(description = "产品表ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "19220")
    @NotEmpty(message = "产品表ID不能为空")
    private String infoId;

    @Schema(description = "入库单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "7692")
    @NotEmpty(message = "入库单ID不能为空")
    private String consignmentId;

    @Schema(description = "入库单号", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotEmpty(message = "入库单号不能为空")
    private String no;

    @Schema(description = "应收数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "应收数量不能为空")
    private BigDecimal consignedAmount;

    @Schema(description = "实收数量", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotNull(message = "实收数量不能为空")
    private BigDecimal signedAmount;

    @Schema(description = "处理人")
    private String handleBy;

    @Schema(description = "处理日期")
    private LocalDateTime handleTime;

    @Schema(description = "物料类型ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25094")
    @NotEmpty(message = "物料类型ID不能为空")
    private String materialConfigId;

    @Schema(description = "状态  0待处理  1已处理", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "状态  0待处理  1已处理不能为空")
    private Integer status;

    @Schema(description = "类型 1采购收货 2外协退货 3原材料入库 4 销售退货", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "类型 1采购收货 2外协退货 3原材料入库 4 销售退货不能为空")
    private Integer consignmentType;

    @Schema(description = "处理结果  1入库 2退货", example = "1")
    private Integer rusultType;

    @Schema(description = "异常类型 1来货不足 2收货收多 ", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    @NotNull(message = "异常类型 1来货不足 2收货收多 不能为空")
    private Integer exceptionType;

    @Schema(description = "合同ID", example = "5401")
    private String contractId;

    @Schema(description = "项目ID", example = "32229")
    private String projectId;

    @Schema(description = "公司ID", example = "18492")
    private String companyId;

}