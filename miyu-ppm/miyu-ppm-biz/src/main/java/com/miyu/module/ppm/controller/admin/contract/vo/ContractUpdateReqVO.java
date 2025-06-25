package com.miyu.module.ppm.controller.admin.contract.vo;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static cn.iocoder.yudao.framework.common.util.date.DateUtils.FORMAT_YEAR_MONTH_DAY;


@Schema(description = "管理后台 - 购销合同更新 Request VO")
@Data
public class ContractUpdateReqVO {

    @Schema(description = "ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "25351")
    private String id;

    @Schema(description = "合同状态", requiredMode = Schema.RequiredMode.REQUIRED, example = "1")
    private Integer contractStatus;

    @Schema(description = "审批状态", example = "1")
    private Integer status;

    @Schema(description = "操作类型1终止,2作废", example = "1")
    private String type;
}